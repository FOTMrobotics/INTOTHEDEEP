package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Devices;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;

@TeleOp(name = "Continuous Servo", group = "Tests")
public class continuousServoTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        CRServo servo = hardwareMap.get(CRServo.class, "servo");

        boolean pressed = false;
        boolean flipped = false;
        boolean showControls = false;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if (!showControls) telemetry.addLine("Press Y to view controls.\n");
            else {
                telemetry.addLine("Press Y to hide controls.\n");
                telemetry.addLine("A - Hold to move servo with triggers");
                telemetry.addLine("Left Trigger - Negative direction");
                telemetry.addLine("Right Trigger - Positive direction");
                telemetry.addLine("B - Flips direction\n");
            }

            if (gamepad1.a) servo.setPower(gamepad1.right_trigger - gamepad1.left_trigger);

            if (!pressed) {
                if (gamepad1.b) {
                    servo.setDirection(CRServo.Direction.REVERSE);
                    flipped = !flipped;
                } else if (gamepad1.y) {
                showControls = !showControls;
                }
            }

            pressed = gamepad1.b || gamepad1.y;

            telemetry.addLine("Values:");
            telemetry.addData("Power", servo.getPower());
            telemetry.addData("Reversed", flipped);
            telemetry.update();
        }
    }
}
