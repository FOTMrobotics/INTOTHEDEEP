package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;

@TeleOp
public class LiftTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        VerticalExtension lift = new VerticalExtension(hardwareMap);
        lift.resetEncoders();

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {
            lift.update(gamepad1);

            telemetry.addData("target", lift.getTarget());
            telemetry.addData("Left", lift.getEncoderL());
            telemetry.addData("Right", lift.getEncoderR());
            telemetry.update();
        }
    }
}
