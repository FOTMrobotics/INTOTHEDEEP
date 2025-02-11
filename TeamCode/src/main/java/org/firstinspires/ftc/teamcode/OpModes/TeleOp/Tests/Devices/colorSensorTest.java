package org.firstinspires.ftc.teamcode.OpModes.TeleOp.Tests.Devices;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.Subsystems.ColorSensor;

@TeleOp(name = "colorSensor", group = "Tests")
public class colorSensorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ColorSensor colorSensor = new ColorSensor(hardwareMap, "colorSensor");

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            telemetry.addData("color", colorSensor.getColor());
            telemetry.addData("distance", colorSensor.distance(DistanceUnit.CM));
            telemetry.update();
        }
    }
}
