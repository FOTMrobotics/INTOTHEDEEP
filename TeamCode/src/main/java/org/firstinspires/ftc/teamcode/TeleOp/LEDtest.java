package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.lights;

@TeleOp
public class LEDtest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        waitForStart();

        lights LED = new lights(hardwareMap);

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            int r = 255;
            int b = 165;
            int g = 0;

            LED.setLights(r, b, g);
        }
    }
}
