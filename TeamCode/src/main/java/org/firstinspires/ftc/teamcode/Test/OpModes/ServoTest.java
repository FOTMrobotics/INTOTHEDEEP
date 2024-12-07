package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        Servo servo1 = hardwareMap.servo.get("servo1");
        Servo servo2 = hardwareMap.servo.get("servo2");

        double setPosition = 0.46;
        double increment = 0.005;
        boolean pressed = false;

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            if (!pressed) {
                if (gamepad1.dpad_up) {
                    setPosition += increment;
                } else if (gamepad1.dpad_down) {
                    setPosition -= increment;
                } else if (gamepad1.dpad_right) {
                    increment += 0.01;
                } else if (gamepad1.dpad_left) {
                    increment -= 0.01;
                } else if (gamepad1.a) {
                    setPosition = 0;
                } else if (gamepad1.b) {
                    setPosition = 0.5;
                } else if (gamepad1.y) {
                    setPosition = 1;
                }
            }

            pressed = gamepad1.dpad_up || gamepad1.dpad_down || gamepad1.dpad_left || gamepad1.dpad_right;

            servo1.setPosition(setPosition);
            servo2.setPosition(setPosition);

            telemetry.addData("Position", setPosition);
            telemetry.addData("Increment", increment);

            telemetry.update();
        }
    }
}
