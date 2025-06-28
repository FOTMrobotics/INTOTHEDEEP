package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Subsystems.Bucket;
import org.firstinspires.ftc.teamcode.Subsystems.ColorSensor;
import org.firstinspires.ftc.teamcode.Subsystems.Intake;
import org.firstinspires.ftc.teamcode.Subsystems.Lights;

@Autonomous(name = "Sample Detection")
public class SampleDetection extends LinearOpMode {

    ColorSensor.Color color = ColorSensor.Color.NONE;

    @Override
    public void runOpMode() throws InterruptedException {
        Intake intake = new Intake(hardwareMap);
        Lights lights = new Lights(hardwareMap);
        Bucket bucket = new Bucket(hardwareMap);

        waitForStart();

        while (opModeIsActive()) {

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
    }
}
