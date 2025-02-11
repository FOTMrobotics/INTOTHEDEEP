package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;

@TeleOp(name = "intake", group = "Subsystems")
public class intakeTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        HorizontalExtension linkage = new HorizontalExtension(hardwareMap);
        VerticalExtension lift = new VerticalExtension(hardwareMap);

        Intake intake = new Intake(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            linkage.update(gamepad1);
            lift.update(gamepad1);

            intake.update(linkage, gamepad1);

            telemetry.addLine("Values:");
            telemetry.addData("State", linkage.getState());
            telemetry.addData("Position", linkage.pos);
            telemetry.addData("At Zero", linkage.atZero());
            telemetry.update();
        }
    }
}
