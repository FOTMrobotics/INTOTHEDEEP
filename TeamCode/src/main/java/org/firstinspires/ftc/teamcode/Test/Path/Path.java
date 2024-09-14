package org.firstinspires.ftc.teamcode.Test.Path;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;
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

    public List<Vector2D> pathPoints = new ArrayList<>();
    public Path (MecanumDrive mecanumDrive, List<Vector2D> pathPoints) {
        this.mecanumDrive = mecanumDrive;
        this.pathPoints = pathPoints;

    }

    public void runTest () {
        //while (true) {
            //mecanumDrive.toTarget(new Pose2D(0, 20, 0));
        //}
        for (int i = 0 ; i < this.pathPoints.size() - 1 ; i++)
        {
            while (true)
            {
                Vector2D A = this.pathPoints.get(i);
                Vector2D B = this.pathPoints.get(i + 1);
                Pose2D C = mecanumDrive.getPosition();

                Vector2D AB = A.sub(B);
                Vector2D CB = C.sub(B);

                if (CB.magnitude() < 5) {
                    break;
                }

                Vector2D D = CB.proj(AB).add(B);

                Vector2D DC = C.sub(D).scale(1); // Scale is how much the robot should adjust to line. Maybe add PID loop to this.

                Vector2D targetPoint = C.sub(DC.add(CB));

                mecanumDrive.toTarget(new Pose2D(targetPoint, 0));
            }
        }
        while (!mecanumDrive.atTarget()) {mecanumDrive.toTarget();}
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