package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;
import org.firstinspires.ftc.teamcode.trailblazer.path.Path;
import org.firstinspires.ftc.teamcode.trailblazer.path.PathBuilder;
import org.fotmrobotics.trailblazer.Vector2D;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Sample (OUTDATED)", group = "outdated")
public class SampleOLD extends LinearOpMode {
    ElapsedTime timer;
    Drive drive;

    VerticalExtension lift;
    Bucket bucket;

    //SpecimenTransfer specimenTransfer;
    Claw claw;

    HorizontalExtension linkage;
    Intake intake;

    Sweeper sweeper;

    @Override
    public void runOpMode() throws InterruptedException {
        timer = new ElapsedTime();

        drive = new Drive(hardwareMap);

        lift = new VerticalExtension(hardwareMap);
        bucket = new Bucket(hardwareMap);
        lift.resetEncoders();
        bucket.close();

        //specimenTransfer = new SpecimenTransfer(hardwareMap);
        claw = new Claw(hardwareMap);
        //specimenTransfer.setState(SpecimenTransfer.State.PLACE);
        //specimenTransfer.setPivot(SpecimenTransfer.State.PLACE);
        claw.close();

        linkage = new HorizontalExtension(hardwareMap);

        intake = new Intake(hardwareMap);
        sweeper = new Sweeper(hardwareMap);
        intake.setState(Intake.State.IN);
        sweeper.in();

        Path preloadScore = new PathBuilder(drive, new Vector2D(0,0))
                .translationalScale(0.5)
                .headingConstant(45)
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .action(() -> {
                    //specimenTransfer.move();
                    return false;
                })
                .action(() -> {
                    lift.toTarget(2450);
                    return false;
                })
                .pt(new Vector2D(-10,-22))
                .build();

        Path pickupSample1 = new PathBuilder(drive, new Vector2D(-10, -22))
                .headingConstant(85)
                .action(() -> {
                    lift.toZero();
                    return false;
                })
                .pt(new Vector2D(-9, -21))
                .build();

        Path pickupSample2 = new PathBuilder(drive, new Vector2D(-9, -22))
                .headingConstant(110)
                .action(() -> {
                    lift.toZero();
                    return false;
                })
                .pt(new Vector2D(-11, -21))
                .build();

        Path pickupSample3 = new PathBuilder(drive, new Vector2D(-10, -22))
                .headingConstant(126) //130
                .action(() -> {
                    lift.toZero();
                    return false;
                })
                .pt(new Vector2D(-11, -21))
                .build();

        Path scoreSample1 = new PathBuilder(drive, new Vector2D(-11,-21))
                .translationalScale(0.5)
                .headingConstant(45)
                .action(() -> {
                    lift.toTarget(2450);
                    return false;
                })
                .pt(new Vector2D(-10,-22))
                .build();

        Path scoreSample2 = new PathBuilder(drive, new Vector2D(-11,-21))
                .translationalScale(0.5)
                .headingConstant(45)
                .action(() -> {
                    lift.toTarget(2450);
                    return false;
                })
                .pt(new Vector2D(-10,-22))
                .build();

        Path scoreSample3 = new PathBuilder(drive, new Vector2D(-11,-21))
                .translationalScale(0.5)
                .headingConstant(45)
                .action(() -> {
                    lift.toTarget(2450);
                    return false;
                })
                .pt(new Vector2D(-10,-22))
                .build();

        Path park = new PathBuilder(drive, new Vector2D(-10, -22))
                .translationalScale(0.8)
                .headingConstant(0)
                .action(() -> {
                    lift.toTarget(1100);
                    return false;
                })
                .headingConstant(0.75, 180)
                .pt(new Vector2D(-36, -16))
                .pt(new Vector2D(-46, 6))
                .build();

        waitForStart();

        if (isStopRequested()) return;

        preloadScore.run();

        //specimenTransfer.setPower(0);

        score();

        pickupSample1.run();

        intake();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 100);

        scoreSample1.run();

        score();

        pickupSample2.run();

        intake();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 100);

        scoreSample2.run();

        score();

        pickupSample3.run();

        intake();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 100);

        scoreSample3.run();

        score();

        park.run();

        while (true) {lift.toTarget(1080);}
    }

    public void intake() {
        while (!lift.atZero()) lift.toZero();
        lift.toZero();
        lift.setPowers(0);
        lift.resetEncoders();

        linkage.out(0);
        intake.setState(Intake.State.OUT);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300);

        intake.intake();

        double t = 0;
        double scale = 0.0075;
        while (t <= 0.76) {
            intake.intake();
            linkage.out(t);
            t += scale;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 800);

        intake.stop();
        intake.setState(Intake.State.IN);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 400);

        linkage.zero();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 500);

        intake.intakePower(0.8);

        while (intake.sampleIn()) {
            timer.reset();
            while (timer.time(TimeUnit.MILLISECONDS) < 300);
        }
        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 550);

        intake.stop();
    }

    public void score() {
        while (true) {
            lift.toTarget(2450);
            if (lift.atTarget()) {
                timer.reset();
                bucket.open();
                while (timer.time(TimeUnit.MILLISECONDS) < 600) lift.toTarget(2450);
                bucket.close();
                break;
            }
        }
    }
}
