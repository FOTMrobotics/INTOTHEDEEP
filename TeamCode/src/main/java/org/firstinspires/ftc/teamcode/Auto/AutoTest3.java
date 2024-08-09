package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.util.Path;
import org.firstinspires.ftc.teamcode.util.Pose2d;

@Autonomous
public class AutoTest3 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Drivebase drivebase = new Drivebase(hardwareMap);
        Pose2d[] points = {
                new Pose2d(0,0,0),
                new Pose2d(0,50,0),
                new Pose2d(20, 50, 0),
                new Pose2d(20,25,0),
                new Pose2d(10,25,0),
                new Pose2d(10,5,0)
        };
        Pose2d[] points1 = {
                new Pose2d(10,5,0),
                new Pose2d(5,25,0)
        };
        Path path1 = new Path(points);
        Path path2 = new Path(points1);
        ElapsedTime timer = new ElapsedTime();
        boolean bpress = false;

        waitForStart();

        while (opModeIsActive()) {
            //boolean reachedEnd = false;
            if (gamepad1.b) {
                path1.runPath(drivebase);
                bpress = true;
            } else if (gamepad1.y) {
                path2.runPath(drivebase);
            } else {
                double[] zeroPowers = {0,0,0,0};
                drivebase.runMotors(zeroPowers);
            }
            telemetry.addData("b pressed?", gamepad1.b);
            if (bpress) {
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
