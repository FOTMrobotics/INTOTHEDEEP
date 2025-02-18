package org.firstinspires.ftc.teamcode.OpModes.TeleOp;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;

// TODO: Make a red and blue alliance preset so samples can be filtered
@TeleOp(name = "Main")
public class Main extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Drive drive = new Drive(hardwareMap);

        VerticalExtension lift = new VerticalExtension(hardwareMap);
        lift.resetEncoders();
        Bucket bucket = new Bucket(hardwareMap);

        Hang hang = new Hang(hardwareMap);

        Claw claw = new Claw(hardwareMap);

        HorizontalExtension linkage = new HorizontalExtension(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        Sweeper sweeper = new Sweeper(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            drive.mecanumDrive(gamepad1);

            lift.update(gamepad2);
            bucket.update(gamepad1);

            hang.update(gamepad1);

            claw.update(gamepad1, gamepad2);

            linkage.update(gamepad2);
            intake.update(linkage,linkage.atZero() && lift.atZero(), gamepad2);

            sweeper.update(gamepad2);
        }
    }
}
