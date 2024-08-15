package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Util.Pose2d;

@Autonomous
public class AutoTest2 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Drivebase drivebase = new Drivebase(hardwareMap);
        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {
            boolean reachedEnd = false;
            if (gamepad1.b) {
                reachedEnd = drivebase.toPosition(new Pose2d(10,25,0));
            } else {
                double[] zeroPowers = {0,0,0,0};
                drivebase.runMotors(zeroPowers);
            }
            telemetry.addData("b pressed?", gamepad1.b);
            //telemetry.addData("x", pos.x);
            //telemetry.addData("y", pos.y);
            //telemetry.addData("h", pos.h);
            telemetry.addData("reachedEnd", reachedEnd);
            telemetry.addData("motorPowers", drivebase.motorPowers);
            drivebase.update();
            telemetry.update();
            timer.reset();
        }
    }
}
