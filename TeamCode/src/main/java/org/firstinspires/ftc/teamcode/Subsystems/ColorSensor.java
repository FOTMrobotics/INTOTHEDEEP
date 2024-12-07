package org.firstinspires.ftc.teamcode.Subsystems;


import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Arrays;

public class ColorSensor {
    NormalizedColorSensor colorSensor;
    DistanceSensor distanceSensor;
    float gain = 17;

    public ColorSensor (HardwareMap hardwareMap) {
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "colorSensor");
        distanceSensor = hardwareMap.get(DistanceSensor.class, "colorSensor");
    }

    public enum color {
        YELLOW,
        RED,
        BLUE,
        NONE
    }

    public color getColor () {
        color target;
        int[] red = {255, 0, 0};
        int[] blue = {0, 0, 255};
        int[] yellow = {255, 255, 0};

        /*
        have red, green, and yellow colors as constants in RGB
        store these values in a list to iterate through with the for loop

        read input from color sensor

        subtract value from color sensor from each red, green, and yellow constant using distance formula
        use a for loop to calculate distance. set each distance from a loop equal to a temporary value. after each loop is done compare that temporary
        value to the current loop distance so that you can see if it is greater than the previous loop

        whatever distance is the shortest, that is the closest color and should be returned for the get color function

        implement distance sensor to only calculate color distance within a certain distance of the sensor
         */

        target = null;

        return target;
    }

    public double test (Gamepad gamepad1, Telemetry telemetry){
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        int[] redTarget = {255, 0, 0};
        int[] blueTarget = {0, 0, 255};
        int[] yellowTarget = {255, 255, 0};

        if (gamepad1.a) {
            // Only increase the gain by a small amount, since this loop will occur multiple times per second.
            gain += 0.005;
        } else if (gamepad1.b && gain > 1) { // A gain of less than 1 will make the values smaller, which is not helpful.
            gain -= 0.005;
        }
        colorSensor.setGain(gain);
        telemetry.addData("Gain", gain);



        float red = colors.red;
        float blue = colors.blue;
        float green = colors.green;

        red *= 1000;
        blue *= 1000;
        green *= 1000;

        float[] measured = {red, green, blue};

        double redDistance = color_distance(redTarget, measured);
        double blueDistance = color_distance(blueTarget, measured);
        double yellowDistance = color_distance(yellowTarget, measured);
        double x = 0;

        /*
        telemetry.addData("red ", red);
        telemetry.addData("blue ", blue);
        telemetry.addData("green ", green);

        telemetry.addData("red distance", redDistance);
        telemetry.addData("blue distance", blueDistance);
        telemetry.addData("yellow distance", yellowDistance);

         */
        if (redDistance < blueDistance && redDistance < yellowDistance) {
            x = redDistance;
            telemetry.addData("red", x);
        }
        if (blueDistance < redDistance && blueDistance < yellowDistance) {
            x = blueDistance;
            telemetry.addData("blue", x);
        }
        if (yellowDistance < redDistance && yellowDistance < blueDistance){
            x = yellowDistance;
            telemetry.addData("yellow", x);
        }

        telemetry.update();
        return x;
    }

    public double color_distance(int[] targetVal, float[] measured) {
        double colorDistance = Math.sqrt(Math.pow(targetVal[2]-measured[2],2)+Math.pow(targetVal[1]-measured[1],2)+Math.pow(targetVal[0]-measured[0],2));

        return colorDistance;

    }

    public double distance(){
        double sample_distance = distanceSensor.getDistance(DistanceUnit.MM);
        return  sample_distance;
    }
}
