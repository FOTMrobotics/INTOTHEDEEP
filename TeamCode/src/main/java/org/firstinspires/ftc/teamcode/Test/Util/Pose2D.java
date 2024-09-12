package org.firstinspires.ftc.teamcode.Test.Util;

/**
 * Vector2D with the addition of heading
 *
 * @author Preston Cokis
 */
public class Pose2D extends Vector2D {
    public double h;

    public Pose2D(double x, double y, double h) {
        super(x, y);
        this.h = h;
    }

    public Pose2D(Vector2D vector2d, double h) {
        super(vector2d);
        this.h = h;
    }

    public Pose2D() {
        super(Double.NaN, Double.NaN);
        this.h = Double.NaN;
    }

    public void set(Pose2D pose2D) {
        this.x = pose2D.x;
        this.y = pose2D.y;
        this.h = pose2D.h;
    }
}