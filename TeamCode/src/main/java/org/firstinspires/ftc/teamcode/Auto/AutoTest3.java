package org.firstinspires.ftc.teamcode.Auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Path.Path;
import org.firstinspires.ftc.teamcode.Path.PathBuilder;
import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Util.Vector2d;

@Config
@Autonomous
public class AutoTest3 extends LinearOpMode {
    public static double distance = 35;
    @Override
    public void runOpMode() throws InterruptedException {
        Drivebase drivebase = new Drivebase(hardwareMap);
        /*
        Path path1 = new PathBuilder(drivebase, new Vector2d(0,0))
                .pt(new Vector2d(0,0))
                .pt(new Vector2d(0,0))
                .pt(new Vector2d(0,0))
                .pt(new Vector2d(0,0))
                .build();

        path1.run();
         */
        /*Pose2d[] points = {
                new Pose2d(0,0,0),
                new Pose2d(0,50,0),
                new Pose2d(50, 50, 0),
                new Pose2d(50,0,0),
                new Pose2d(0,0,0)
        };*/
        //Pose2d[] points1 = {
        //        new Pose2d(10,5,0),
        //        new Pose2d(5,25,0)
        //};
        //Path path1 = new Path(points);
        //Path path2 = new Path(points1);
        ElapsedTime timer = new ElapsedTime();
        boolean bpress = false;

        waitForStart();

        while (opModeIsActive()) {
            //boolean reachedEnd = false;
            //Path path3 = new Path(drivebase);
            //path3.add(point);
            if (gamepad1.b) {
                //Pose2d[] points = {
                //        new Pose2d(0,0,0),
                //        new Pose2d(0,distance,0),
                //       new Pose2d(distance, distance, 0),
                //        new Pose2d(distance,0,0),
                //        new Pose2d(0,0,0)
                //};
                //Path path1 = new Path(points);
                //path1.runPath(drivebase);
                Path path1 = new PathBuilder(drivebase, new Vector2d(0,0))
                        .setSpeed(15)
                        .actionAtPoint(() -> {
                            drivebase.runMotors(new double[] {0,0,0,0});
                            sleep(10000);
                            }, new Vector2d(0,15))
                        .pt(new Vector2d(0,30))
                        .pt(new Vector2d(15, 30))
                        .pt(new Vector2d(15,0))
                        .build();

                path1.run();
                bpress = true;
            } else if (gamepad1.y) {
                //path2.runPath(drivebase);
            } else {
                double[] zeroPowers = {0,0,0,0};
                drivebase.runMotors(zeroPowers);
            }
            telemetry.addData("b pressed?", gamepad1.b);
            if (bpress) {
                //telemetry.addData("lineNum", path1.lineNum);
                //telemetry.addData("targetPtX", path1.targetPoint[0]);
                //telemetry.addData("targetPtY", path1.targetPoint[1]);
                //telemetry.addData("disablePID", drivebase.disablePID);
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
