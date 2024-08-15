package org.firstinspires.ftc.teamcode.Path;

import static java.lang.Double.isNaN;
import static java.lang.Double.max;

import android.util.Pair;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.R;
import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Util.Pose2d;
import org.firstinspires.ftc.teamcode.Util.Vector2d;

import java.util.ArrayList;
import java.util.List;

@Config
public class Path {
    private List<Runnable> points;
    private List<Runnable> actions;
    private Drivebase drivebase;
    public double radius = 8;
    public Vector2d startPoint;
    public double[] targetPoint;
    public double t;
    public int lineNum;

    public Path(Drivebase Drivebase, List<Runnable> points, List<Runnable> actions, Vector2d startPoint) {
        drivebase = Drivebase;
        this.points = points;
        this.actions = actions;
        this.startPoint = startPoint;
    }

    public Runnable pt(Vector2d vector2d) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, vector2d);

        double targetHeading = Math.toDegrees(Math.atan2(drivebase.currentPos.y - p.y, drivebase.currentPos.x - p.x)) + 90;
        targetHeading = targetHeading <= 180 ? targetHeading : targetHeading - 360;

        drivebase.toPosition(new Pose2d(p.x, p.y, targetHeading));
        return null;
    }

    public Runnable ptOffsetHeading(Pose2d pose2d) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, pose2d);

        double targetHeading = Math.toDegrees(Math.atan2(drivebase.currentPos.y - p.y, drivebase.currentPos.x - p.x)) + 90 + pose2d.h;
        targetHeading = targetHeading <= 180 ? targetHeading : targetHeading - 360;

        drivebase.toPosition(new Pose2d(p.x, p.y, targetHeading));
        return null;
    }

    public Runnable ptConstantHeading(Pose2d pose2d) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, pose2d);

        drivebase.toPosition(new Pose2d(p.x, p.y, pose2d.h));
        return null;
    }

    // Unfinished, do not use
    public Runnable ptLinearHeading(Pose2d pose2d, double startAngle, double start, double end) {
        Vector2d p = circleLineInterception(drivebase.currentPos, startPoint, pose2d);

        double headingDifference = Math.abs(pose2d.h - startAngle);

        double targetHeading = startAngle + max(0, pose2d.h - startAngle);

        drivebase.toPosition(new Pose2d(p.x, p.y, targetHeading));
        return null;
    }

    public Runnable toPoint(Pose2d pt) {
        t = drivebase.toPosition(pt) ? 1 : 0;
        return null;
    }

    public Runnable toHeading(double angle) {
        t = drivebase.toPosition(new Pose2d(drivebase.currentPos.x, drivebase.currentPos.y, angle)) ? 1 : 0;
        return null;
    }

    public Runnable forward(double distance) {
        double xPos = drivebase.currentPos.x + distance * Math.cos(Math.toRadians(drivebase.currentPos.h + 90));
        double yPos = drivebase.currentPos.y + distance * Math.sin(Math.toRadians(drivebase.currentPos.h + 90));

        t = drivebase.toPosition(new Pose2d(xPos, yPos, drivebase.currentPos.h)) ? 1 : 0;
        return null;
    }

    public Runnable backward(double distance) {
        double xPos = drivebase.currentPos.x + distance * Math.cos(Math.toRadians(drivebase.currentPos.h + 90));
        double yPos = drivebase.currentPos.y + distance * Math.sin(Math.toRadians(drivebase.currentPos.h + 90));

        t = drivebase.toPosition(new Pose2d(-xPos, -yPos, drivebase.currentPos.h)) ? 1 : 0;
        return null;
    }

    public Runnable left(double distance) {
        return null;
    }

    public Runnable right(double distance) {
        return null;
    }

    public Runnable turn(double angle) {
        return null;
    }

    public Runnable setFollowRadius(double radius) {
        this.radius = radius;
        return null;
    }

    public Runnable setSpeed(double speed) {
        this.drivebase.speed = speed;
        return null;
    }

    // Maybe return true when reached end?
    public void run() {
        for (Runnable point : points) {
            drivebase.disablePID = true;
            while (t < 1) {
                point.run();
            }
            t = 0;
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

    // Maybe
    private void runSegment() {

    }

    /*
    public void runPath(Drivebase drivebase) {
        boolean reachedEnd = false;
        while (!reachedEnd) {
            drivebase.disablePID = true;
            for (double[] x : possiblePoints)
            {
                lastEle = x;
            }
            for (int i = 0; i < points.length - 1; i++) {
                if (i != lineNum) {
                    continue;
                }

                //double[] currentPos = drivebase.getPos();
                //double x0 = points[i].x;
                //double y0 = points[i].y;
                //double x1 = points[i + 1].x;
                //double y1 = points[i + 1].y;

                Vector2d p = circleLineInterception(drivebase.currentPos, points[i], points[i + 1]);

                //double a = Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2);
                //double b = 2 * (((x0 - currentPos[0]) * (x1 - x0)) + ((y0 - currentPos[1]) * (y1 - y0)));
                //double c = Math.pow(x0, 2) - (2 * currentPos[0] * x0) + Math.pow(currentPos[0], 2) + Math.pow(y0, 2) - (2 * currentPos[1] * y0) + Math.pow(currentPos[1], 2) - Math.pow(radius, 2);

                //double discriminant = Math.sqrt(Math.pow(b, 2) - 4 * a * c);

                //if (isNaN(t)) {
                //    continue;
                //}
                if (p == null) {
                    continue;
                }

                //double t = (-b + discriminant) / (2 * a);

                if (t > 1) {
                    lineNum += 1;
                }

                //double[] p = {x0 + (x1 - x0) * t, y0 + (y1 - y0) * t};

                double targetHeading = Math.toDegrees(Math.atan2(drivebase.currentPos.y - p.y, drivebase.currentPos.x - p.x)) + 90;
                targetHeading = targetHeading <= 180 ? targetHeading : targetHeading - 360;

                drivebase.t = t;

                targetPoint = new double[]{p.x, p.y, targetHeading};
            }

            if (lineNum == points.length - 1) {
                drivebase.disablePID = false;
                targetPoint[2] = points[lineNum].h;
            }

            reachedEnd = drivebase.toPosition(new Pose2d(targetPoint[0], targetPoint[1], targetPoint[2]));
        }
    }
    */
}