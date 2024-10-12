package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp
public class ContinuousServoTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        CRServo servo = hardwareMap.crservo.get("servo");

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {
            if (gamepad1.b) {
                servo.setPower(1);
            } else if (gamepad1.a) {
                servo.setPower(-1);
            } else {
                servo.setPower(0);
            }
        }
    }
}
