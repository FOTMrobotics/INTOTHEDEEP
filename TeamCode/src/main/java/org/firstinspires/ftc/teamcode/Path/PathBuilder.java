package org.firstinspires.ftc.teamcode.Path;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Util.Pose2d;
import org.firstinspires.ftc.teamcode.Util.Vector2d;

public class PathBuilder {
    private Vector2d startPoint;
    private Path path;
    private Drivebase drivebase;

    public PathBuilder(Drivebase Drivebase, Vector2d startPoint) {
        drivebase = Drivebase;
        this.path = new Path(drivebase);
        this.startPoint = startPoint;
    }

    public PathBuilder pt(Vector2d vector2d) {
        this.path.points.add(() -> this.path.pt(vector2d));
        return this;
    }

    public PathBuilder ptOffsetHeading(Pose2d pose2d, double angleOffset) {
        this.path.points.add(() -> this.path.ptOffsetHeading(pose2d, angleOffset));
        return this;
    }

    public PathBuilder ptConstantHeading(Pose2d pose2d) {
        this.path.points.add(() -> this.path.ptConstantHeading(pose2d));
        return this;
    }

    public PathBuilder ptLinearHeading(Pose2d pose2d, double startAngle, double start, double end) {
        this.path.points.add(() -> this.path.ptLinearHeading(pose2d, startAngle, start, end));
        return this;
    }

    public PathBuilder toPoint(Pose2d pt) {
        this.path.points.add(() -> this.path.toPoint(pt));
        return this;
    }

    public PathBuilder toHeading(double angle) {
        this.path.points.add(() -> this.path.toHeading(angle));
        return this;
    }

    public PathBuilder forward(double distance) {
        this.path.points.add(() -> this.path.forward(distance));
        return this;
    }

    public PathBuilder backward(double distance) {
        this.path.points.add(() -> this.path.backward(distance));
        return this;
    }

    public PathBuilder left(double distance) {
        this.path.points.add(() -> this.path.left(distance));
        return this;
    }

    public PathBuilder right(double distance) {
        this.path.points.add(() -> this.path.right(distance));
        return this;
    }

    public PathBuilder turn(double angle) {
        this.path.points.add(() -> this.path.turn(angle));
        return this;
    }

    public PathBuilder setFollowRadius(double radius) {
        this.path.points.add(() -> this.path.setFollowRadius(radius));
        //this.actions.add(setRadius(radius));
        return this;
    }

    public PathBuilder setSpeed(double speed) {
        this.path.points.add(() -> this.path.setSpeed(speed));
        return this;
    }

    /*
    public PathBuilder action(Runnable action) {
        this.path.points.add(action);
        return this;
    }
    */

    // Distance = 1 in
    public PathBuilder actionAtPoint(Runnable action, Vector2d vector2d) {
        this.path.actions.add(vector2d);
        this.path.actions.add(1.0);
        this.path.actions.add(action);
        return this;
    }

    // Specified distance from point
    public PathBuilder actionAtPoint(Runnable action, Vector2d vector2d, double distance) {
        this.path.actions.add(vector2d);
        this.path.actions.add(distance);
        this.path.actions.add(action);
        return this;
    }

    public PathBuilder wait(int milliseconds) {
        //this.path.points.add(() -> );
        return this;
    }

    public Path build() {
        return path;
    }
}