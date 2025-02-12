package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;

@TeleOp(name = "Vertical Extension", group = "Subsystems")
public class verticalExtensionTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        VerticalExtension lift = new VerticalExtension(hardwareMap);
        lift.resetEncoders();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad1.a) {
                lift.toTarget(900);
            } else {
                lift.update(gamepad1);
            }
            telemetry.addLine("Controls:");
            telemetry.addLine("Left Stick Y - Up is up, down is down\n");
            telemetry.addLine("Values:");
            telemetry.addData("State", lift.getState());
            telemetry.addData("Position", lift.getCurrentPosition());
            telemetry.addData("Left Encoder", lift.getEncoderL());
            telemetry.addData("Right Encoder", lift.getEncoderR());
            telemetry.addData("Target", lift.getTarget());
            telemetry.addData("At Zero", lift.atZero());
            telemetry.addData("Power", lift.power);
            telemetry.update();
        }
    }
}
