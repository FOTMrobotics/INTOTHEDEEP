package org.firstinspires.ftc.teamcode.Path;

import static java.lang.Double.isNaN;
import static java.lang.Double.max;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Util.Pose2d;
import org.firstinspires.ftc.teamcode.Util.Vector2d;

import java.util.ArrayList;
import java.util.List;

@Config
public class Path {
    private final Drivebase drivebase;
    public List<Runnable> points = new ArrayList<>();
    public List<Object> actions = new ArrayList<>();
    public double radius = 8;
    public Vector2d startPoint = new Vector2d(0,0);
    public Vector2d tempPoint = new Vector2d(0,0);
    public double t; // How far the point is along the line
    public int lineNum = 0;

    public Path(Drivebase Drivebase) {
        drivebase = Drivebase;
    }

    public void pt(Vector2d vector2d) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, vector2d);

        double targetHeading = Math.toDegrees(Math.atan2(drivebase.currentPos.y - p.y, drivebase.currentPos.x - p.x)) + 90;
        targetHeading = targetHeading <= 180 ? targetHeading : targetHeading - 360;

        drivebase.toPosition(new Pose2d(p.x, p.y, targetHeading));

        this.tempPoint.change(vector2d);
    }

    public void ptOffsetHeading(Vector2d vector2d, double angleOffset) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, vector2d);

        double targetHeading = Math.toDegrees(Math.atan2(drivebase.currentPos.y - p.y, drivebase.currentPos.x - p.x)) + 90 + angleOffset;
        targetHeading = targetHeading <= 180 ? targetHeading : targetHeading - 360;

        drivebase.toPosition(new Pose2d(p.x, p.y, targetHeading));

        this.tempPoint.change(vector2d);
    }

    public void ptConstantHeading(Pose2d pose2d) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, pose2d);

        drivebase.toPosition(new Pose2d(p.x, p.y, pose2d.h));

        this.tempPoint.change(pose2d);
    }

    // Unfinished, do not use
    public void ptLinearHeading(Pose2d pose2d, double startAngle, double start, double end) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, pose2d);

        double headingDifference = Math.abs(pose2d.h - startAngle);

        double targetHeading = startAngle + max(0, pose2d.h - startAngle);

        drivebase.toPosition(new Pose2d(p.x, p.y, targetHeading));

        this.tempPoint.change(pose2d);
    }

    public void toPoint(Pose2d pt) {
        drivebase.disablePID = false;
        while (t != 1) {
            t = drivebase.toPosition(pt) ? 1 : 0;
        }

        this.tempPoint.change(pt);
    }

    public void toHeading(double angle) {
        drivebase.disablePID = false;
        while (t != 1) {
            t = drivebase.toPosition(new Pose2d(drivebase.currentPos.x, drivebase.currentPos.y, angle)) ? 1 : 0;
        }

        this.tempPoint.change(drivebase.currentPos);
    }

    public void forward(double distance) {
        double xPos = drivebase.currentPos.x + distance * Math.cos(Math.toRadians(drivebase.currentPos.h + 90));
        double yPos = drivebase.currentPos.y + distance * Math.sin(Math.toRadians(drivebase.currentPos.h + 90));

        drivebase.disablePID = false;
        while (t != 1) {
            t = drivebase.toPosition(new Pose2d(xPos, yPos, drivebase.currentPos.h)) ? 1 : 0;
        }

        this.tempPoint.change(drivebase.currentPos);
    }

    public void backward(double distance) {
        double xPos = drivebase.currentPos.x - distance * Math.cos(Math.toRadians(drivebase.currentPos.h + 90));
        double yPos = drivebase.currentPos.y - distance * Math.sin(Math.toRadians(drivebase.currentPos.h + 90));

        drivebase.disablePID = false;
        while (t != 1) {
            t = drivebase.toPosition(new Pose2d(xPos, yPos, drivebase.currentPos.h)) ? 1 : 0;
        }

        this.tempPoint.change(drivebase.currentPos);
    }

    // Unfinished, do not use
    public void left(double distance) {

    }

    // Unfinished, do not use
    public void right(double distance) {

    }

    public void turn(double angle) {
        double targetAngle = drivebase.currentPos.h + angle;
        targetAngle = targetAngle > 180 ? targetAngle - 360 : targetAngle < -180 ? targetAngle - 360 : targetAngle;

        drivebase.disablePID = false;
        while (t != 1) {
            t = drivebase.toPosition(new Pose2d(drivebase.currentPos.x, drivebase.currentPos.y, targetAngle)) ? 1 : 0;
        }

        this.tempPoint.change(drivebase.currentPos);
    }

    public void setFollowRadius(double radius) {
        this.radius = radius;
        t = 1;
    }

    public void setSpeed(double speed) {
        drivebase.speed = speed;
        t = 1;
    }

    public void run() {
        drivebase.update();
        this.startPoint.change(drivebase.currentPos);
        for (Runnable point : points) {
            drivebase.disablePID = true;
            while (t < 1) {
                point.run();

                boolean removeAction = false;
                byte actionToRemove = 0;
                for (int i = 0; i < actions.size(); i++) {
                    if (actions.get(i).getClass() == Vector2d.class) {
                        if (drivebase.currentPos.distance((Vector2d) actions.get(i)) < (double) actions.get(i+1)) {
                            Runnable action = (Runnable) actions.get(i+2);
                            action.run();
                            removeAction = true;
                            actionToRemove = (byte) i;
                        }
                    }
                }
                if (removeAction) {
                    for (int i = 0; i < 2; i++) {
                        actions.remove(actionToRemove);
                    }
                }
            }

            t = 0;
            this.startPoint.change(this.tempPoint);
        }
        drivebase.disablePID = false;
        while (true) {
            if (drivebase.toPosition()) {
                break;
            }
        }
    }

    public Vector2d circleLineInterception(Vector2d circlePos, Vector2d p1, Vector2d p2) {
        double a = Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2);
        double b = 2 * (((p1.x - circlePos.x) * (p2.x - p1.x)) + ((p1.y - circlePos.y) * (p2.y - p1.y)));
        double c = Math.pow(p1.x, 2) - (2 * circlePos.x * p1.x) + Math.pow(circlePos.x, 2) + Math.pow(p1.y, 2) - (2 * circlePos.y * p1.y) + Math.pow(circlePos.y, 2) - Math.pow(radius, 2);

        double discriminant = Math.sqrt(Math.pow(b, 2) - 4 * a * c);

        if (isNaN(discriminant)) {
            return null;
        }

        t = (-b + discriminant) / (2 * a);

        return new Vector2d(p1.x + (p2.x - p1.x) * t, p1.y + (p2.y - p1.y) * t);
    }
}