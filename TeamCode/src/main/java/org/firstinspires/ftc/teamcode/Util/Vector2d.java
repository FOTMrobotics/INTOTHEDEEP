package org.firstinspires.ftc.teamcode.Util;

public class Vector2d {
    public double x, y;

    public Vector2d(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2d() {
        this.x = Double.NaN;
        this.y = Double.NaN;
    }

    public void change(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void change(Vector2d vector2d) {
        this.x = vector2d.x;
        this.y = vector2d.y;
    }

    public double distance(Vector2d vector2d) {
        return Math.sqrt(Math.pow(this.x-vector2d.x,2)+Math.pow(this.y-vector2d.y,2));
    }

    public double distance(Pose2d pose2d) {
        return Math.sqrt(Math.pow(this.x-pose2d.x,2)+Math.pow(this.y-pose2d.y,2));
    }
}
