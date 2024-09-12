package org.firstinspires.ftc.teamcode.Test.Path;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Preston Cokis
 */
public class Path {
    public MecanumDrive mecanumDrive;
    public List<PathSegment> pathSegments = new ArrayList();

    enum type {
        point,
        stop
    }

    public Path (MecanumDrive mecanumDrive) {
        this.mecanumDrive = mecanumDrive;
    }

    public void run () {

    }

    public Vector2D lerp (Vector2D p1, Vector2D p2, double t) {
        Vector2D d = p2.sub(p1);
        return p1.add(d.scale(t));
    }

    public double t1;
    public double t2;

    public boolean circleLineIntersection (Vector2D p1, Vector2D p2, Vector2D circlePos, double radius) {
        Vector2D d = p2.sub(p1);
        Vector2D f = p1.sub(circlePos);

        double a = d.dot(d);
        double b = f.dot(d) * 2;
        double c = f.dot(f) - radius*radius;

        double discriminant = b*b - 4*a*c;
        if (discriminant > 0) {
            discriminant = Math.sqrt(discriminant);
            this.t1 = (-b - discriminant) / (2*a);
            this.t2 = (-b + discriminant) / (2*a);
            return true;
        }
        return false;
    }
}