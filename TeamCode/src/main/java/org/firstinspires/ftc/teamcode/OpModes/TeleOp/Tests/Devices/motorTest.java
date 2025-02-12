package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Devices;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Motor", group = "Tests")
public class motorTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor motor = hardwareMap.get(DcMotor.class, "motor");

        boolean pressed = false;
        boolean flipped = false;
        boolean showControls = false;

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {

            if (!showControls) telemetry.addLine("Press Y to view controls.\n");
            else {
                telemetry.addLine("Press Y to hide controls.\n");
                telemetry.addLine("A - Hold to move motor with triggers");
                telemetry.addLine("Left Trigger - Negative direction");
                telemetry.addLine("Right Trigger - Positive direction");
                telemetry.addLine("B - Flips direction\n");
            }

            if (gamepad1.a) motor.setPower(gamepad1.right_trigger - gamepad1.left_trigger);

            if (!pressed) {
                if (gamepad1.b) {
                    motor.setDirection(DcMotorSimple.Direction.REVERSE);
                    flipped = !flipped;
                } else if (gamepad1.y) {
                    showControls = !showControls;
                }
            }

            pressed = gamepad1.b || gamepad1.y;

            telemetry.addLine("Values:");
            telemetry.addData("Position", motor.getCurrentPosition());
            telemetry.addData("Power", motor.getPower());
            telemetry.addData("Reversed", flipped);
            telemetry.update();
        }
    }
}
