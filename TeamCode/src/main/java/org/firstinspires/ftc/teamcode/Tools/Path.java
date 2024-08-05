package org.firstinspires.ftc.teamcode.Tools;

import static java.lang.Double.isNaN;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;

import java.util.LinkedHashSet;

@Config
public class Path {
    public static double radius = 4;
    public Point[] points;
    public LinkedHashSet<double[]> possiblePoints;
    public double[] lastEle;
    public double a;
    public double b;
    public double c;
    public double discriminant;

    public Path(Point[] points) {
        this.points = points;
    }

    public boolean runPath(Drivebase drivebase) {
        possiblePoints = new LinkedHashSet<double[]>();
        for (int i = 0; i < points.length-1; i++) {
            double[] currentPos = drivebase.getPos();
            double x0 = points[i].x;
            double y0 = points[i].y;
            double x1 = points[i + 1].x;
            double y1 = points[i + 1].y;

            a = Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2);
            b = 2 * ((x0 - currentPos[0]) * (x1 - x0) + (y0 - currentPos[1]) * (y1 - y0));
            c = Math.pow(x0, 2) - 2 * currentPos[0] * x0 + Math.pow(currentPos[0], 2) + Math.pow(y0, 2) - 2 * currentPos[1] * y0 + Math.pow(currentPos[2], 2) - radius;

            discriminant = Math.sqrt(Math.pow(b, 2) - 4 * a * c);

            if (isNaN(discriminant)) {
                continue;
            }

            //double t1 = (-b - discriminant) / (2 * a);
            double t2 = (-b + discriminant) / (2 * a);

            //double[] p1 = {x0 + (x1 - x0) * t1, y0 + (y1 - y0) * t1};
            double[] p2 = {x0 + (x1 - x0) * t2, y0 + (y1 - y0) * t2};

            //possiblePoints.add(p1);
            possiblePoints.add(p2);
        }
            // Iterate LinkedHashSet
        for (double[] x : possiblePoints)
        {
            lastEle = x;
        }
        double[] targetPoint = lastEle;

            /*
            double dist1 = Math.sqrt(Math.pow(p1[0]-currentPos[0],2)+Math.pow(p1[1]-currentPos[1],2));
            double dist2 = Math.sqrt(Math.pow(p2[0]-currentPos[0],2)+Math.pow(p2[1]-currentPos[1],2));

            double[] targetPoint = dist1 < dist2 ? p1 : p2;
            */
        drivebase.toPosition(new Point(targetPoint[0], targetPoint[1], 0));
        return true;
    }
}