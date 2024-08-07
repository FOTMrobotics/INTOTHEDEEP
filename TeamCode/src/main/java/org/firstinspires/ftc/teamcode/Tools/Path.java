package org.firstinspires.ftc.teamcode.Tools;

import static java.lang.Double.isNaN;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;

import java.util.Arrays;
import java.util.LinkedHashSet;

@Config
public class Path {
    public static double radius = 8;
    public Point[] points;
    public double[] targetPoint;// = {0,0};
    public int lineNum;
    public double a;
    public double b;
    public double c;
    public double discriminant;

    public Path(Point[] points) {
        this.points = points;
        this.lineNum = 0;
    }

    public boolean runPath(Drivebase drivebase) {
        drivebase.disablePID = true;
        for (int i = 0; i < points.length - 1; i++) {
            if (i != lineNum) {
                continue;
            }

            double[] currentPos = drivebase.getPos();
            double x0 = points[i].x;
            double y0 = points[i].y;
            double x1 = points[i + 1].x;
            double y1 = points[i + 1].y;

            a = Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2);
            b = 2 * (((x0 - currentPos[0]) * (x1 - x0)) + ((y0 - currentPos[1]) * (y1 - y0)));
            c = Math.pow(x0, 2) - (2 * currentPos[0] * x0) + Math.pow(currentPos[0], 2) + Math.pow(y0, 2) - (2 * currentPos[1] * y0) + Math.pow(currentPos[1], 2) - Math.pow(radius, 2);

            discriminant = Math.sqrt(Math.pow(b, 2) - 4 * a * c);

            if (isNaN(discriminant)) {
                continue;
            }

            double t2 = (-b + discriminant) / (2 * a);

            double targetX;
            double targetY;
            if (x0 < x1) {
                targetX = Math.min(x0 + (x1 - x0) * t2, x1);
            } else {
                targetX = Math.max(x0 + (x1 - x0) * t2, x1);
            }
            if (y0 < y1) {
                targetY = Math.min(y0 + (y1 - y0) * t2, y1);
            } else {
                targetY = Math.max(y0 + (y1 - y0) * t2, y1);
            }
            double[] p2 = {targetX, targetY};

            if (Arrays.equals(p2, new double[]{x1, y1})) {
                lineNum += 1;
            }

            targetPoint = p2;
        }

        if (lineNum == points.length - 1) {
            drivebase.disablePID = false;
        }

        drivebase.toPosition(new Point(targetPoint[0], targetPoint[1], 0));
        return true;
    }
}