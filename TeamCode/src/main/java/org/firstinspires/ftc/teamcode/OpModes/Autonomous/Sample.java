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
import org.fotmrobotics.trailblazer.Pose2D;
import org.fotmrobotics.trailblazer.Vector2D;

import java.util.concurrent.TimeUnit;

@Autonomous(name = "Sample")
public class Sample extends LinearOpMode {
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
        bucket = new Bucket(hardwareMap);
        lift.resetEncoders();
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
                .action(0.1, () -> {
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
                .headingConstant(100)
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

        waitForStart();

        if (isStopRequested()) return;

        scorePreload.run();

        while (!lift.atTarget()) lift.toTarget(2450);
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 500) lift.toTarget(2450);

        bucket.close();

        intake();

        while (!bucket.samplePresent()) {
            if (linkage.atZero()) {
                intake.intakePower(0.8);
            }

            if (bucket.samplePresent()) {
                intake.stop();
            }
        }

        intake.stop();

        while (!lift.atTarget()) lift.toTarget(2450);
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 500) lift.toTarget(2450);

        bucket.close();

        grabSecond.run();

        intake();

        linkage.zero();

        scoreSecond.run();

        if (!intake.sampleIn()) {
            while (timer.time(TimeUnit.MILLISECONDS) < 500) intake.intakePower(0.8);
        }
        intake.stop();

        while (!lift.atTarget()) lift.toTarget(2450);
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 500) lift.toTarget(2450);

        bucket.close();

        grabThird.run();

        intake();

        linkage.zero();

        scoreThird.run();

        if (!intake.sampleIn()) {
            while (timer.time(TimeUnit.MILLISECONDS) < 500) intake.intakePower(0.8);
        }
        intake.stop();

        while (!lift.atTarget()) lift.toTarget(2450);
        while (bucket.samplePresent()) {
            bucket.open();
            lift.toTarget(2450);
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 500) lift.toTarget(2450);

        bucket.close();
    }

    public void intake() {
        while (!lift.atZero()) lift.toZero();
        lift.toZero();
        lift.setPowers(0);
        lift.resetEncoders();

        linkage.out(0.4);
        intake.setState(Intake.State.OUT);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300);

        intake.intake();

        double t = 0.4;
        double scale = 0.0075;
        while (t <= 0.85) {
            intake.intake();
            linkage.out(t);
            t += scale;
        }

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 400); // 800

        intake.stop();
        intake.setState(Intake.State.IN);

        timer.reset();
        while (timer.time(TimeUnit.MILLISECONDS) < 300);

        linkage.zero();
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
