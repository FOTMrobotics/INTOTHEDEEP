package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;

@TeleOp
public class Teleop extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        MecanumDrive mecanumDrive = new MecanumDrive(hardwareMap);
        waitForStart();
        if (isStopRequested()) {
            return;
        }
        while (opModeIsActive()) {
            if (gamepad1.left_trigger >= 0.5) {
                mecanumDrive.mecanumDrive(gamepad1);
            } else {
                mecanumDrive.trueNorthDrive(gamepad1);
            }
        }
    }
}