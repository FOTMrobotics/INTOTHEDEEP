package org.firstinspires.ftc.teamcode.Test.Util;

/**
 * 2-Dimension Vector
 *
 * @author Preston Cokis
 */
public class Vector2D {
    public double x;
    public double y;

    public Vector2D (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D (Vector2D vector2d) {
        this.x = vector2d.x;
        this.y = vector2d.y;
    }

    public Vector2D () {
        this.x = Double.NaN;
        this.y = Double.NaN;
    }

    public void set (double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void set (Vector2D vector2d) {
        this.x = vector2d.x;
        this.y = vector2d.y;
    }

    public void setX (double x) {this.x = x;}

    public void setY (double y) {this.y = y;}

    /**
     * Use for finding the magnitude of a vector
     *
     * @return The magnitude of the vector
     */
    public double magnitude () {return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2));}

    /**
     * Use for adding vectors
     *
     * @param vector2d Vector addend
     * @return The summation between vectors
     */
    public Vector2D add (Vector2D vector2d) {return new Vector2D(this.x + vector2d.x, this.y + vector2d.y);}

    /**
     * Use for subtracting vectors
     *
     * @param vector2d Vector minuend
     * @return The difference between vectors
     */
    public Vector2D sub(Vector2D vector2d) {return new Vector2D(this.x - vector2d.x, this.y - vector2d.y);}

    /**
     * Use for scaling the vector
     *
     * @param scalar
     * @return Scaled vector
     */
    public Vector2D scale (double scalar) {return new Vector2D(this.x * scalar, this.y * scalar);}

    /**
     * Use for the dot product between vectors
     *
     * @param vector2d
     * @return The dot product
     */
    public double dot (Vector2D vector2d) {return (this.x * vector2d.x) + (this.y * vector2d.y);}

    /**
     * Use for getting the projected vector
     *
     * @param vector2d
     * @return The projected vector
     */
    public Vector2D proj (Vector2D vector2d) {
        double k = this.dot(vector2d) / vector2d.dot(vector2d);
        return new Vector2D(vector2d.x, vector2d.y).scale(k);
    }
}