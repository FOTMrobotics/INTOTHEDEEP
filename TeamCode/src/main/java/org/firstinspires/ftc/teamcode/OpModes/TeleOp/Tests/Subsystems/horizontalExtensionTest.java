package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;

@TeleOp(name = "Horizontal Extension", group = "Subsystems")
public class horizontalExtensionTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        HorizontalExtension linkage = new HorizontalExtension(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            linkage.update(gamepad1);

            telemetry.addLine("Controls:");
            telemetry.addLine("Right Stick X - Left is inwards, right is outwards\n");
            telemetry.addLine("Values:");
            telemetry.addData("State", linkage.getState());
            telemetry.addData("Position", linkage.pos);
            double[] positions = linkage.getEncoderPositions();
            telemetry.addData("Left", positions[0]);
            telemetry.addData("Right", positions[1]);
            telemetry.update();
        }
    }
}
