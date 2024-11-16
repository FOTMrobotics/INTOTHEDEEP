package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.Claw;

@TeleOp
public class clawTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Claw ftcclaw = new Claw(hardwareMap);

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {
            if (gamepad1.a) {
                ftcclaw.open();
            }
            if (gamepad1.x) {
                ftcclaw.close();
            }
        }
    }
}
