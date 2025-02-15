package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.fotmrobotics.trailblazer.Vector2D;

@Config
@TeleOp(name = "Vertical Extension", group = "Subsystems")
public class verticalExtensionTest extends LinearOpMode {
    public static int target = 2000;

    @Override
    public void runOpMode() throws InterruptedException {
        VerticalExtension lift = new VerticalExtension(hardwareMap);
        lift.resetEncoders();

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            if (gamepad1.a) {
                lift.toTarget(target);

                double error = lift.getTarget() - lift.getCurrentPosition();
                telemetry.addData("error", error);
                dashboardTelemetry.addData("error", error);
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
            dashboardTelemetry.update();
        }
    }
}
