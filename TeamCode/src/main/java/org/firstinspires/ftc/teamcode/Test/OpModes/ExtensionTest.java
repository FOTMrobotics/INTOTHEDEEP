package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;

@TeleOp
public class ExtensionTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        HorizontalExtension extension = new HorizontalExtension(hardwareMap);
        extension.resetEncoders();

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {
            extension.update(gamepad1);

            telemetry.addData("target", extension.getTarget());
            telemetry.addData("position", extension.getEncoder());
            telemetry.update();
        }
    }
}
