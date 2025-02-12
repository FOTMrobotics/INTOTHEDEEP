package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Sweeper;

@TeleOp(name = "Sweeper", group = "Subsystems")
public class sweeperTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Sweeper sweeper = new Sweeper(hardwareMap);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            sweeper.update(gamepad1);
        }
    }
}
