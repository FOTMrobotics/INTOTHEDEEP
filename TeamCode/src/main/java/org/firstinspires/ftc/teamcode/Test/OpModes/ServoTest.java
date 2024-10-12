package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ServoTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        Servo servo = hardwareMap.servo.get("servo");

        double setPosition = 1;
        boolean pressed = false;

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            if (!pressed) {
                if (gamepad1.dpad_up) {
                    setPosition += 0.01;
                } else if (gamepad1.dpad_down) {
                    setPosition -= 0.01;
                } else if (gamepad1.a) {
                    setPosition = 1;
                }
            }

            pressed = gamepad1.dpad_up || gamepad1.dpad_down;

            servo.setPosition(setPosition);

            telemetry.addData("Position", setPosition);

            telemetry.update();
        }
    }
}
