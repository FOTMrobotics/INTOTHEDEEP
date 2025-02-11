package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;

@TeleOp
public class Drive extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        boolean switchControls = false;
        boolean trueNorth = false;

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            if (trueNorth) {
                drive.trueNorthDrive(gamepad1);
            } else {
                drive.mecanumDrive(gamepad1);
            }

            if (!switchControls) {
                if (gamepad1.left_stick_button && gamepad1.right_stick_button) {
                    trueNorth = !trueNorth;
                }
            }

            switchControls = gamepad1.left_stick_button && gamepad1.right_stick_button;

            telemetry.addData("True North Movement", trueNorth);
            Pose2D pos = drive.getPosition();
            telemetry.addData("X", pos.x);
            telemetry.addData("Y", pos.y);
            telemetry.addData("H", pos.h);
            telemetry.update();

            if (gamepad1.y) {
                drive.resetSparkFunOTOS();
            }
        }
    }
}
