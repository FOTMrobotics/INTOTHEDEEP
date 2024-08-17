package org.firstinspires.ftc.teamcode.Path;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Util.Pose2d;
import org.firstinspires.ftc.teamcode.Util.Vector2d;

import java.util.ArrayList;
import java.util.List;

// Pass this into path
// Use this to make a list of functions from path
// build method should run runpath or make new instance of path and pass list into it with methods
public class PathBuilder {
    private List<Runnable> points;
    private List<Runnable> actions;
    private Vector2d startPoint;
    private Path path;
    private Drivebase drivebase;

    public PathBuilder(Drivebase Drivebase, Vector2d startPoint) {
        drivebase = Drivebase;
        this.points = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.startPoint = startPoint;
    }

    /*
    public PathBuilder run() {
        for (Runnable point : points) {
            point.run();
        }
        return this;
    }
    */

    public void add(Runnable action) {
        this.actions.add(action);
    }

    public PathBuilder pt(Vector2d vector2d) {
        this.points.add(path.pt(vector2d));
        return this;
    }

    public PathBuilder ptOffsetHeading(Pose2d pose2d, double angleOffset) {
        this.points.add(path.ptOffsetHeading(pose2d, angleOffset));
        return this;
    }

    public PathBuilder ptConstantHeading(Pose2d pose2d) {
        this.points.add(path.ptConstantHeading(pose2d));
        return this;
    }

    public PathBuilder ptLinearHeading(Pose2d pose2d, double startAngle, double start, double end) {
        this.points.add(path.ptLinearHeading(pose2d, startAngle, start, end));
        return this;
    }

    public PathBuilder toPoint(Pose2d pt) {
        this.points.add(path.toPoint(pt));
        return this;
    }

    public PathBuilder toHeading(double angle) {
        this.points.add(path.toHeading(angle));
        return this;
    }

    public PathBuilder forward(double distance) {
        this.points.add(path.forward(distance));
        return this;
    }

    public PathBuilder backward(double distance) {
        this.points.add(path.backward(distance));
        return this;
    }

    public PathBuilder left(double distance) {
        this.points.add(path.left(distance));
        return this;
    }

    public PathBuilder right(double distance) {
        this.points.add(path.right(distance));
        return this;
    }

    public PathBuilder turn(double angle) {
        this.points.add(path.turn(angle));
        return this;
    }

    public PathBuilder setFollowRadius(double radius) {
        this.points.add(path.setFollowRadius(radius));
        //this.actions.add(setRadius(radius));
        return this;
    }

    public PathBuilder setSpeed(double speed) {
        this.points.add(path.setSpeed(speed));
        return this;
    }

    // Actions will be implemented later
    public PathBuilder action(int placeholder) {
        return this;
    }

    // Distance = 1 in
    public PathBuilder actionAtPoint(Vector2d vector2d) {
        return this;
    }

    // Specified distance from point
    public PathBuilder actionAtPoint(Vector2d vector2d, double distance) {
        return this;
    }

    public Path build() {
        return new Path(drivebase, points, actions, startPoint);
    }

    // delete and create methods on path, make them public and make a callable with them
    class PathPoint {
        private byte type;
        private  Vector2d vector2d;
        private Pose2d pose2d;

        public PathPoint(byte type, Vector2d vector2d) {
            this.type = type;
            this.vector2d = vector2d;
        }

        public PathPoint(byte type, Pose2d pose2d) {
            this.type = type;
            this.pose2d = pose2d;
        }
    }
}