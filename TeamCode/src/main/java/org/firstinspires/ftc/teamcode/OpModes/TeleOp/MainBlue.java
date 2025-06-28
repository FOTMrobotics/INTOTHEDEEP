package org.firstinspires.ftc.teamcode.OpModes.TeleOp;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.Claw;
import org.firstinspires.ftc.teamcode.Subsystems.ColorSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Hang;
import org.firstinspires.ftc.teamcode.Subsystems.HorizontalExtension;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Lights;
import org.firstinspires.ftc.teamcode.Subsystems.Sweeper;
import org.firstinspires.ftc.teamcode.Subsystems.VerticalExtension;
import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TeleOp(name = "Main (Blue)")
public class MainBlue extends LinearOpMode {

    ColorSensor.Color color = ColorSensor.Color.NONE;

    @Override
    public void runOpMode() throws InterruptedException {
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

        Drive drive = new Drive(hardwareMap);

        VerticalExtension lift = new VerticalExtension(hardwareMap);
        lift.resetEncoders();

        lift.setPIDF(0.025, 0, 0.4, 0);

        Bucket bucket = new Bucket(hardwareMap);

        Hang hang = new Hang(hardwareMap);

        boolean hangActive = false;

        Claw claw = new Claw(hardwareMap);

        HorizontalExtension linkage = new HorizontalExtension(hardwareMap);
        Intake intake = new Intake(hardwareMap);
        intake.setFilter(ColorSensor.Color.RED);
        Sweeper sweeper = new Sweeper(hardwareMap);

        Lights lights = new Lights(hardwareMap);

        ElapsedTime timer = new ElapsedTime();

        ElapsedTime delay = new ElapsedTime();

        int delayTime = 3000;

        waitForStart();

        if (isStopRequested()) return;

        timer.reset();

        while (opModeIsActive()) {
            drive.mecanumDrive(gamepad1);

            lift.update(gamepad2);
            //bucket.update(gamepad1);
            if (gamepad1.right_bumper) {
                bucket.open();
            } else {
                if (bucket.getState() == Bucket.State.OPEN) bucket.close();
            }

            if (timer.time(TimeUnit.SECONDS) > 100 && !hangActive) {
                gamepad1.rumble(750);
                gamepad2.rumble(750);

                hangActive = true;
            }

            if (hangActive) {
                hang.update(gamepad1);
            }

            //claw.update(gamepad1, gamepad2);

            linkage.update(gamepad2);

            double linkagePos = linkage.pos;
            // linkage.atZero() && lift.atZero()
            boolean atZero = true;

            if (delay.time() < delayTime * 0.5) {
                if (lift.atZero()) {
                    if (linkage.atZero()) {
                        atZero = true;
                    } else {
                        atZero = false;
                    }
                } else {
                    atZero = false;
                }
            }
            lift.update(gamepad2);

            intake.update(linkagePos, atZero, gamepad2);

            if (gamepad2.right_stick_button) {
                sweeper.out();
            } else {
                sweeper.in();
            }

            if (delay.time() < delayTime) {
                boolean sampleIn = intake.sampleIn();
                if (intake.sampleIn()) color = intake.getColor();

                if (sampleIn) {
                    lights.LEDState(Lights.State.SOLID, color);
                } else if (bucket.samplePresent()) {
                    lights.LEDState(Lights.State.BLINKING, color);
                } else {
                    lights.LEDState(Lights.State.NONE, ColorSensor.Color.NONE);
                }
            }
            lift.update(gamepad2);

            if (delay.time() < delayTime) delay.reset();
        }
    }
}
