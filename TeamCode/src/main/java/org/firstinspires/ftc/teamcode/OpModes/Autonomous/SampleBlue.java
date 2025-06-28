package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.ColorSensor;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;
import org.firstinspires.ftc.teamcode.trailblazer.path.Path;
import org.firstinspires.ftc.teamcode.trailblazer.path.PathBuilder;
import org.fotmrobotics.trailblazer.Pose2D;
import org.fotmrobotics.trailblazer.Vector2D;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Sample (Blue)")
public class SampleBlue extends LinearOpMode {
    ElapsedTime timer;
    Drive drive;

    VerticalExtension lift;
    Bucket bucket;

    Claw claw;

    HorizontalExtension linkage;
    Intake intake;

    Sweeper sweeper;

    @Override
    public void runOpMode() throws InterruptedException {
        timer = new ElapsedTime();

        drive = new Drive(hardwareMap);

        lift = new VerticalExtension(hardwareMap);
        lift.resetEncoders();
        bucket = new Bucket(hardwareMap);
        bucket.close();

        claw = new Claw(hardwareMap);

        linkage = new HorizontalExtension(hardwareMap);

        intake = new Intake(hardwareMap);
        sweeper = new Sweeper(hardwareMap);
        intake.setState(Intake.State.IN);
        sweeper.in();

        Path scorePreload = new PathBuilder(drive, new Vector2D(0,0))
                .translationalScale(0.5)
                .angularScale(0.8)
                .action(0.0, () -> { // 0.1
                    lift.toTarget(2450);
                    return false;
                })
                .headingConstant(0)
                .headingConstant(0.4, 67)
                .action(0.4, () -> {
                    linkage.out(0.4);
                })
                .pt(new Vector2D(-9.75,-28))
                .build();

        Path grabSecond = new PathBuilder(drive, new Vector2D(-9.75, -28))
                .translationalScale(0.5)
                .angularScale(0.8)
                .headingConstant(97) // 100
                .action(() -> {
                    linkage.out(0.35);
                })
                .action(() -> {
                    lift.toZero();

                    if (lift.atZero()) {
                        lift.setPowers(0);
                        lift.resetEncoders();
                    }

                    return lift.atZero();
                })
                .pt(new Vector2D(-9.75, -27))
                .build();

        Path scoreSecond = new PathBuilder(drive, new Vector2D(-9.75, -27))
                .translationalScale(0.5)
                .angularScale(0.8)
                .headingConstant(60)
                .action(() -> {
                    if (linkage.atZero()) {
                        intake.intakePower(0.8);
                    }

                    if (bucket.samplePresent()) {
                        intake.stop();
                    }

                    return bucket.samplePresent();
                })
                .action(() -> {
                    if (bucket.samplePresent()) {
                        lift.toTarget(2450);
                    }
                    return lift.atTarget();
                })
                .pt(new Vector2D(-9.75,-28))
                .build();

        Path grabThird = new PathBuilder(drive, new Vector2D(-9.75, -28))
                .translationalScale(0.5)
                .angularScale(0.8)
                .action(() -> {
                    linkage.out(0.4);
                })
                .action(() -> {
                    lift.toZero();

                    if (lift.atZero()) {
                        lift.setPowers(0);
                        lift.resetEncoders();
                    }

                    return lift.atZero();
                })
                .headingConstant(127)
                .pt(new Vector2D(-14, -20))
                .build();

        Path scoreThird = new PathBuilder(drive, new Vector2D(-14, -20))
                .translationalScale(0.5)
                .angularScale(0.8)
                .headingConstant(60)
                .action(() -> {
                    if (linkage.atZero()) {
                        intake.intakePower(0.8);
                    }

                    if (bucket.samplePresent()) {
                        intake.stop();
                    }

                    return bucket.samplePresent();
                })
                .action(() -> {
                    if (bucket.samplePresent()) {
                        lift.toTarget(2450);
                    }
                    return lift.atTarget();
                })
                .pt(new Vector2D(-9.75,-28))
                .build();

        Path grabForth = new PathBuilder(drive, new Vector2D(-9.75, -28))
                .headingConstant(-2)
                .translationalScale(0.5)
                .angularScale(0.8)
                .translationalScale(0.1, 1)
                .action(() -> {
                    lift.toZero();

                    if (lift.atZero()) {
                        lift.setPowers(0);
                        lift.resetEncoders();
                    }

                    return lift.atZero();
                })
                .pt(new Vector2D(-37, -24))
                .translationalScale(0.5, 0.3)
                .pt(new Vector2D(-55, 2))
                .build();

        Path scoreForth = new PathBuilder(drive, new Vector2D(-55,2))
                .translationalScale(0.8)
                .action(() -> {
                    if (linkage.atZero()) {
                        intake.intakePower(0.8);
                    }

                    if (bucket.samplePresent()) {
                        intake.stop();
                    }

                    return bucket.samplePresent();
                })
                .headingConstant(0)
                .pt(new Vector2D(-33,-20))
                .translationalScale(0.5)
                .headingConstant(45)
                .pt(new Vector2D(-8,-20))
                .build();

        Path scoreForthNoIntake = new PathBuilder(drive, new Vector2D(-55,2))
                .translationalScale(0.8)
                .headingConstant(0)
                .pt(new Vector2D(-33,-20))
                .translationalScale(0.5)
                .headingConstant(45)
                .pt(new Vector2D(-8,-20))
                .build();

        waitForStart();

        if (isStopRequested()) return;

        scorePreload.run();

        while (!lift.atTarget()) {
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300) {
            lift.toTarget(2450); // 400

            if (isStopRequested()) break;
        }

        bucket.close();

        intake(0.4,0.05);

        timer.reset();
        while (!bucket.samplePresent()) {
            if (linkage.atZero()) {
                intake.intakePower(0.8);
            }

            if (bucket.samplePresent()) {
                intake.stop();
            }

            if (timer.time(TimeUnit.MILLISECONDS) > 1500) break;

            if (isStopRequested()) break;
        }

        intake.stop();

        while (!lift.atTarget()) {
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300) {
            lift.toTarget(2450); // 400

            if (isStopRequested()) break;
        }

        bucket.close();

        grabSecond.run();

        intake(0.35, 0.05);

        linkage.zero();

        scoreSecond.run();

        if (!intake.sampleIn()) {
            while (timer.time(TimeUnit.MILLISECONDS) < 300) {
                intake.intakePower(0.8); // 400

                if (isStopRequested()) break;
            }
        }
        intake.stop();

        while (!lift.atTarget()) {
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300) {
            lift.toTarget(2450); // 400

            if (isStopRequested()) break;
        }

        bucket.close();

        grabThird.run();

        intake(0.4, 0.05);

        linkage.zero();

        scoreThird.run();

        if (!intake.sampleIn()) {
            while (timer.time(TimeUnit.MILLISECONDS) < 300) {
                intake.intakePower(0.8); // 400

                if (isStopRequested()) break;
            }
        }
        intake.stop();

        while (!lift.atTarget()) {
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300) {
            lift.toTarget(2450); // 400

            if (isStopRequested()) break;
        }

        bucket.close();

        grabForth.run();

        linkage.out(1);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 100) {
            if (isStopRequested()) break;
        }

        drive.xScale = 1;
        drive.yScale = 1;
        drive.angularScale = 1;

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 775) {
            drive.moveVector(new Pose2D(-0.7,0, 0)); // 0.6
            intake.intake();
            intake.setState(Intake.State.OUT);

            if (intake.sampleIn()) break;

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 775) {
            drive.moveVector(new Pose2D(0.7,0, 0));
            intake.intake();
            intake.setState(Intake.State.OUT);

            if (intake.sampleIn()) break;

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 400) {
            if (isStopRequested()) break;
        }

        // possibly grab

        intake.readColor();
        if (intake.getColor() == ColorSensor.Color.RED) {
            intake.setState(Intake.State.AWAIT);
            intake.outtake();

            while (timer.time(TimeUnit.MILLISECONDS) < 1000) {
                if (isStopRequested()) break;
            }

            intake.setState(Intake.State.IN);
            intake.stop();

            timer.reset();
            while (timer.time(TimeUnit.MILLISECONDS) < 400) {
                if (isStopRequested()) break;
            }

            linkage.zero();

            scoreForthNoIntake.run();
        } else {
            intake.setState(Intake.State.IN);

            timer.reset();
            while (timer.time(TimeUnit.MILLISECONDS) < 100) {
                if (isStopRequested()) break;
            }

            linkage.zero();
            intake.stop();

            scoreForth.run();

            /*
            while (!lift.atTarget()) {
                lift.toTarget(2450);

                if (isStopRequested()) break;
            }
            while (bucket.samplePresent()) {
                bucket.open();
                lift.toTarget(2450);

                if (isStopRequested()) break;
            }

            timer.reset();
            while (timer.time(TimeUnit.MILLISECONDS) < 300) {
                lift.toTarget(2450); // 400

                if (isStopRequested()) break;
            }

            bucket.close();
            */
        }
    }

    public void intake(double start, double increment) { // 0.0075
        while (!lift.atZero()) {
            lift.toZero();

            if (isStopRequested()) break;
        }
        lift.toZero();
        lift.setPowers(0);
        lift.resetEncoders();

        linkage.out(start);
        intake.setState(Intake.State.OUT);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300) {
            if (isStopRequested()) break;
        }

        intake.intake();

        double t = start;
        while (t <= 0.85) {
            intake.intake();
            linkage.out(t);
            t += increment;

            if (isStopRequested()) break;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 400) {
            if (isStopRequested()) break;
        } // 800

        intake.stop();
        intake.setState(Intake.State.IN);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300) {
            if (isStopRequested()) break;
        }

        linkage.zero();
    }
}
