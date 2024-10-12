package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;

@TeleOp
public class Scrimmage extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        VerticalExtension lift = new VerticalExtension(hardwareMap);
        Bucket bucket = new Bucket(hardwareMap);
        HorizontalExtension extension = new HorizontalExtension(hardwareMap);
        Intake intake = new Intake(hardwareMap);

        lift.resetEncoders();
        extension.resetEncoders();

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            drive.mecanumDrive(gamepad1);
            lift.update(gamepad2);
            bucket.update(lift, gamepad1);
            extension.update(gamepad2);
            intake.update(extension, lift, gamepad2);
        }
    }
}
