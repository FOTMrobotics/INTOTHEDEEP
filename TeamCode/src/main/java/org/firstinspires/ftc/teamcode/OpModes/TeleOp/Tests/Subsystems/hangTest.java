package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Hang;

@TeleOp(name = "hang", group = "Subsystems")
public class hangTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Hang hang = new Hang(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {
            hang.update(gamepad1);
        }
    }
}
