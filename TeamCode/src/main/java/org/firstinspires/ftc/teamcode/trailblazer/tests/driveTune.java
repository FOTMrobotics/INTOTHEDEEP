package org.firstinspires.ftc.teamcode.trailblazer.tests;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;
import org.fotmrobotics.trailblazer.Pose2D;
import org.fotmrobotics.trailblazer.Vector2D;

@Config
@TeleOp(name = "Drive Tune", group = "Tests")
public class driveTune extends LinearOpMode {
    public static int position = 500;
    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry dashboardTelemetry = dashboard.getTelemetry();
        Drive drive = new Drive(hardwareMap);

        waitForStart();

        while (isStopRequested()) return;

        while (opModeIsActive()) {
            Pose2D position = drive.odometry.getPosition();
            telemetry.addData("x", position.getX());
            telemetry.addData("y", position.getY());
            telemetry.addData("h", position.getH());
            telemetry.addData("target h", drive.targetDriveHeading);
            if (gamepad1.a) {
                drive.movePoint(new Vector2D(0,0), 0);
                telemetry.addData("At Target", drive.atTarget());
                Vector2D error = (position.minus(drive.target));
                telemetry.addData("error", error.norm());
                dashboardTelemetry.addData("error", error.norm());
            } else if (gamepad1.b) {
                drive.moveVector(new Pose2D(1,1,0), false);
            } else {
                drive.mecanumDrive(gamepad1);
            }

            dashboardTelemetry.update();
            telemetry.update();
        }
    }
}
