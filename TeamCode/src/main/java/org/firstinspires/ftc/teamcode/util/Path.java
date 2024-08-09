package org.firstinspires.ftc.teamcode.util;

import static java.lang.Double.isNaN;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;

import java.util.Arrays;

@Config
public class Path {
    public static double radius = 8; // 8
    public Pose2d[] points;
    public double[] targetPoint;
    public int lineNum;

    public Path(Pose2d[] points) {
        this.points = points;
        this.lineNum = 0;
    }

    public void runPath(Drivebase drivebase) {
        boolean reachedEnd = false;
        while (!reachedEnd) {
            drivebase.disablePID = true;
            /*
            for (double[] x : possiblePoints)
            {
                lastEle = x;
            }
            */
            for (int i = 0; i < points.length - 1; i++) {
                if (i != lineNum) {
                    continue;
                }

                double[] currentPos = drivebase.getPos();
                double x0 = points[i].x;
                double y0 = points[i].y;
                double x1 = points[i + 1].x;
                double y1 = points[i + 1].y;

                double a = Math.pow(x1 - x0, 2) + Math.pow(y1 - y0, 2);
                double b = 2 * (((x0 - currentPos[0]) * (x1 - x0)) + ((y0 - currentPos[1]) * (y1 - y0)));
                double c = Math.pow(x0, 2) - (2 * currentPos[0] * x0) + Math.pow(currentPos[0], 2) + Math.pow(y0, 2) - (2 * currentPos[1] * y0) + Math.pow(currentPos[1], 2) - Math.pow(radius, 2);

                double discriminant = Math.sqrt(Math.pow(b, 2) - 4 * a * c);

                if (isNaN(discriminant)) {
                    continue;
                }

                double t = (-b + discriminant) / (2 * a);

                double targetX;
                double targetY;
                if (x0 < x1) {
                    targetX = Math.min(x0 + (x1 - x0) * t, x1);
                } else {
                    targetX = Math.max(x0 + (x1 - x0) * t, x1);
                }
                if (y0 < y1) {
                    targetY = Math.min(y0 + (y1 - y0) * t, y1);
                } else {
                    targetY = Math.max(y0 + (y1 - y0) * t, y1);
                }
                double[] p = {targetX, targetY};

                if (Arrays.equals(p, new double[]{x1, y1})) {
                    lineNum += 1;
                }

                double targetHeading = Math.toDegrees(Math.atan2(currentPos[1] - p[1], currentPos[0] - p[0])) + 90;
                targetHeading = targetHeading <= 180 ? targetHeading : targetHeading - 360;

                targetPoint = new double[]{p[0], p[1], targetHeading};
            }

            if (lineNum == points.length - 1) {
                drivebase.disablePID = false;
                targetPoint[2] = points[lineNum].h;
            }

            reachedEnd = drivebase.toPosition(new Pose2d(targetPoint[0], targetPoint[1], targetPoint[2]));
        }
    }
}