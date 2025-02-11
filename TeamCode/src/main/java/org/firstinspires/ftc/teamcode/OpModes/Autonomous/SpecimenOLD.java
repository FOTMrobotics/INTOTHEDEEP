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

@Autonomous(name = "Specimen (OUTDATED)", group = "outdated")
public class SpecimenOLD extends LinearOpMode {

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

        int belowBar = 600;
        int aboveBar = 1010;

        timer = new ElapsedTime();

        drive = new Drive(hardwareMap);

        lift = new VerticalExtension(hardwareMap);
        bucket = new Bucket(hardwareMap);
        lift.resetEncoders();
        bucket.close();

        //specimenTransfer = new SpecimenTransfer(hardwareMap);
        claw = new Claw(hardwareMap);
        //specimenTransfer.setState(SpecimenTransfer.State.PLACE);
        //specimenTransfer.setPivot(SpecimenTransfer.State.PICKUP);
        claw.close();

        linkage = new HorizontalExtension(hardwareMap);

        intake = new Intake(hardwareMap);
        sweeper = new Sweeper(hardwareMap);
        intake.setState(Intake.State.IN);
        sweeper.in();

        Path initialPath = new PathBuilder(drive, new Vector2D(0,0))
                .headingConstant(0)
                .translationalScale(0.4)
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .action(() -> {
                    lift.toTarget(belowBar);
                    return false;
                })
                .pt(new Vector2D(-16,24.5))
                .build();

        Path grabSpot1 = new PathBuilder(drive, new Vector2D(-16,24.5))
                .headingConstant(0)
                .translationalScale(1)
                .action(() -> {
                    lift.toZero();
                    return false;
                })
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .pt(new Vector2D(-12, 18))
                .pt(new Vector2D(37,12.5))
                .build();


        Path grabSpot2 = new PathBuilder(drive, new Vector2D(37,12.5))
                .headingConstant(0)
                .translationalScale(0.4)
                .pt(new Vector2D(48.5, 12.5))
                .build();

        Path grabSpot3 = new PathBuilder(drive, new Vector2D(48.5,12.5))
                .headingConstant(-45)
                .translationalScale(0.4)
                .pt(new Vector2D(45,13))
                .build();

        Path dropSample = new PathBuilder(drive, new Vector2D(44,12))
                .headingConstant(0)
                .translationalScale(0.4)
                .action(() -> {
                    //specimenTransfer.transfer();
                    return true;
                })
                .action(0.1, () -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .action(0.2, () -> {
                    bucket.open();
                    return true;
                })
                .pt(new Vector2D(44, 14))
                .build();

        Path pickupSpecimen1 = new PathBuilder(drive, new Vector2D(44,12))
                .headingConstant(0)
                .translationalScale(0.4)
                .action(() -> {
                    claw.open();
                    return true;
                })
                .pt(new Vector2D(42,4.5)) // 3
                .build();

        Path scoreSpecimen1 = new PathBuilder(drive, new Vector2D(42,4.5))
                .headingConstant(0)
                .translationalScale(1)
                .action(() -> {
                    bucket.close();
                    return false;
                })
                .action(() -> {
                    lift.toTarget(belowBar);
                    return false;
                })
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .pt(new Vector2D(0,12))
                .pt(new Vector2D(-2,26))
                .build();

        Path pickupSpecimen2 = new PathBuilder(drive, new Vector2D(-2,26))
                .headingConstant(0)
                .translationalScale(1)
                .action(() -> {
                    lift.toZero();
                    return false;
                })
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .pt(new Vector2D(21, 12))
                .action(0.2, () -> {
                    claw.open();
                    return true;
                })
                .translationalScale(0.5)
                .pt(new Vector2D(42,3.5)) // 2
                .build();

        Path scoreSpecimen2 = new PathBuilder(drive, new Vector2D(42,3.5))
                .headingConstant(0)
                .translationalScale(1)
                .action(() -> {
                    lift.toTarget(belowBar);
                    return false;
                })
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .pt(new Vector2D(12,12))
                .pt(new Vector2D(0,24))
                .build();

        Path park = new PathBuilder(drive, new Vector2D(0, 24))
                .headingConstant(0)
                .translationalScale(1)
                .action(() -> {
                    lift.toZero();
                    return false;
                })
                .action(() -> {
                    //specimenTransfer.move();
                    //return !specimenTransfer.isTransfer();
                })
                .pt(new Vector2D(42,6))
                .build();

        waitForStart();

        if (isStopRequested()) return;

        initialPath.run();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200) lift.toTarget(belowBar);

        lift.toTarget(aboveBar);
        while (!lift.atTarget()) lift.toTarget();
        claw.open();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200) lift.toTarget(aboveBar);

        claw.close();

        //specimenTransfer.swapDirection();

        grabSpot1.run();

        bucket.open();

        intake();

        grabSpot2.run();

        intake();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200);

        bucket.close();

        dropSample.run();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 500);

        pickupSpecimen1.run();

        claw.close();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 400); // pick up wait

        //specimenTransfer.swapDirection();

        scoreSpecimen1.run();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200) lift.toTarget(belowBar);

        lift.toTarget(aboveBar);
        while (!lift.atTarget()) lift.toTarget();
        claw.open();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200) lift.toTarget(aboveBar);

        claw.close();

        //specimenTransfer.swapDirection();

        pickupSpecimen2.run();

        claw.close();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 400); // pick up wait

        //specimenTransfer.swapDirection();

        scoreSpecimen2.run();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200) lift.toTarget(belowBar);

        lift.toTarget(aboveBar);
        while (!lift.atTarget()) lift.toTarget();
        claw.open();

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 200) lift.toTarget(aboveBar);

        claw.close();

        //specimenTransfer.swapDirection();

        park.run();
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
        while (t <= 0.8) {
            intake.intake();
            linkage.out(t);
            t += scale;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300);

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
        while (timer.time(TimeUnit.MILLISECONDS) < 500); //550

        intake.stop();
    }
}
