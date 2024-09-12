package org.firstinspires.ftc.teamcode.Test.Path;

import org.firstinspires.ftc.teamcode.Test.Util.Vector2D;

/**
 * Used for storing information about portions of the path.
 *
 * @author Preston Cokis
 */
public class PathSegment {
    enum segmentType {
        point,
        headingFollow,
        headingConstant,
        action,
        actionAtPoint
    }

    public segmentType type;
    public Vector2D point;
    public double heading;
    public Runnable runnable;

    public PathSegment (Vector2D point) {
        this.type = segmentType.point;
        this.point = point;
    }

    public PathSegment (segmentType type) {
        this.type = type;
    }

    public PathSegment (double heading) {
        this.type = segmentType.headingConstant;
        this.heading = heading;
    }

    public PathSegment (Runnable runnable) {
        this.type = segmentType.action;
        this.runnable = runnable;
    }

    public PathSegment (Runnable runnable, Vector2D point) {
        this.type = segmentType.actionAtPoint;
        this.runnable = runnable;
        this.point = point;
    }
}