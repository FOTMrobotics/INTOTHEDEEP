package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.teamcode.Subsystems.ColorSensor;
@TeleOp
public class colorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensor colors = new ColorSensor(hardwareMap);

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {
            /*
            int[] x = colors.test();
            telemetry.addData("red", x[0]);
            telemetry.addData("blue", x[1]);
            telemetry.addData("green", x[2]);

            telemetry.update();

             */
            float[] x = colors.test();
            telemetry.addData("red", x[0]);
            telemetry.addData("blue", x[1]);
            telemetry.addData("geen", x[2]);
            telemetry.update();
        }
    }
}