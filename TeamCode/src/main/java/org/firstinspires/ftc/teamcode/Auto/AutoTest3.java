package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Tools.Path;
import org.firstinspires.ftc.teamcode.Tools.Point;

import java.util.Arrays;
import java.util.Iterator;

@Autonomous
public class AutoTest3 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Drivebase drivebase = new Drivebase(hardwareMap);
        Point[] points = {
                new Point(0,0,0),
                new Point(0,50,0),
                new Point(20, 50, 0),
                new Point(20,25,0),
                new Point(10,25,0),
                new Point(10,5,0)
        };
        Path path1 = new Path(points);
        ElapsedTime timer = new ElapsedTime();
        boolean bpress = false;

        waitForStart();

        while (opModeIsActive()) {
            //boolean reachedEnd = false;
            if (gamepad1.b) {
                path1.runPath(drivebase);
                //telemetry.addData("a", path1.a);
                //telemetry.addData("b", path1.b);
                ///telemetry.addData("c", path1.c);
                //telemetry.addData("discriminant", path1.discriminant);
                //telemetry.addData("targetPtX", path1.targetPoint[0]);
                //telemetry.addData("targetPtY", path1.targetPoint[1]);
                bpress = true;
                /*
                Iterator<double[]> itr = path1.possiblePoints.iterator();
                double[] point = itr.next();
                telemetry.addData("x", point[0]);
                telemetry.addData("Y", point[1]);
                telemetry.addData("x", path1.lastEle[0]);

                 */
            } else {
                double[] zeroPowers = {0,0,0,0};
                drivebase.runMotors(zeroPowers);
            }
            telemetry.addData("b pressed?", gamepad1.b);
            if (bpress) {
                //telemetry.addData("xf", path1.lastEle[0]);
                //telemetry.addData("yf", path1.lastEle[1]);
                telemetry.addData("lineNum", path1.lineNum);
                telemetry.addData("targetPtX", path1.targetPoint[0]);
                telemetry.addData("targetPtY", path1.targetPoint[1]);
                telemetry.addData("disablePID", drivebase.disablePID);
            }
            double[] pos = drivebase.getPos();
            telemetry.addData("x", pos[0]);
            telemetry.addData("y", pos[1]);
            telemetry.addData("h", pos[2]);
            //telemetry.addData("reachedEnd", reachedEnd);
            drivebase.update();
            telemetry.update();
            timer.reset();
        }
    }
}
