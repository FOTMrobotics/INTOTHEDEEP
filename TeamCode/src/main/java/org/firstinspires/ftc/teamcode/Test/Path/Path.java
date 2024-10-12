package org.firstinspires.ftc.teamcode.Test.Path;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.PIDcontrol;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;
import org.firstinspires.ftc.teamcode.Test.Util.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Preston Cokis
 */
@Config
public class Path {
    public static double p = 3;
    public MecanumDrive mecanumDrive;

    public List<PathSegment> pathSegments = new ArrayList<>();

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

    public void run () {
        PIDcontrol PID = new PIDcontrol(p,0,0);

        FtcDashboard dashboard  = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        for (int i = 0 ; i < this.pathPoints.size() - 1 ; i++)
        {
            while (true)
            {
                Vector2D A = this.pathPoints.get(i); // Start
                Vector2D B = this.pathPoints.get(i + 1); // End
                Pose2D C = mecanumDrive.getPosition(); // Robot Position

                Vector2D AB = A.sub(B);
                Vector2D CB = C.sub(B);

                //if (CB.magnitude() < 5) {
                    //break;
                //}

                Vector2D D = CB.proj(AB).add(B); // Point on line closest to robot's position

                Vector2D BD = B.sub(D);
                // Goes to next point when in radius
                if (BD.magnitude() < 5) {
                    break;
                }

                Vector2D DC = C.sub(D);

                TelemetryPacket packet = new TelemetryPacket();
                packet.put("distFromLine", DC.magnitude());
                double output = PID.getOutput(DC.magnitude());
                packet.put("output", output);
                dashboard.sendTelemetryPacket(packet);

                DC.scale(output); // Scale for how much the robot should adjust to line.

                Vector2D targetPoint = C.sub(DC.add(CB));

                mecanumDrive.toTarget(new Pose2D(targetPoint, 0));
            }
        }
        while (!mecanumDrive.atTarget()) {mecanumDrive.toTarget();}
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