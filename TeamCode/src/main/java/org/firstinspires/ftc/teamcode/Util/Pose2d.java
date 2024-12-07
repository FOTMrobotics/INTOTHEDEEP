package org.firstinspires.ftc.teamcode.Util;

public class Pose2d extends Vector2d {
    public double h;

    public Pose2d() {
        super(Double.NaN, Double.NaN);
        this.h = Double.NaN;
    }

    public Pose2d(double x, double y, double h) {
        super(x, y);
        this.h = h;
    }

    public Pose2d(Vector2d vector2d, double h) {
        super(vector2d.x, vector2d.y);
        this.h = h;
    }

    public void change(double x, double y, double h) {
        super.x = x;
        super.y = y;
        this.h = h;
    }

    public void change(Vector2d vector2d, double h) {
        super.x = vector2d.x;
        super.y = vector2d.y;
        this.h = h;
    }

    public void change(Pose2d pose2d) {
        super.x = pose2d.x;
        super.y = pose2d.y;
        this.h = pose2d.h;
    }
}

