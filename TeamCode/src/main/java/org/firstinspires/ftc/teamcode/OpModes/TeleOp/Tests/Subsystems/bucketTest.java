package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;

@TeleOp(name = "bucket", group = "Subsystems")
public class bucketTest extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Bucket bucket = new Bucket(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            bucket.update(gamepad1);

            telemetry.addLine("Controls:");
            telemetry.addLine("Left/Right Bumper - Open bucket flap\n");
            telemetry.addLine("Values:");
            telemetry.addData("State", bucket.getState());
            telemetry.addData("Beam Broke", bucket.samplePresent());
            telemetry.update();
        }
    }
}
