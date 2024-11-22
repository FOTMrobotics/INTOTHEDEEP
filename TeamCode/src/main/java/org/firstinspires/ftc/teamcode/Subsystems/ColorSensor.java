package org.firstinspires.ftc.teamcode.Subsystems;


import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor {
    private RevColorSensorV3 colorSensor;

    public ColorSensor (HardwareMap hardwareMap) {
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
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

        //normal color input values

        /*
        have red, green, and yellow colors as constants in RGB
        store these values in a list to iterate through with the for loop

        read input from color sensor

        subtract value from color sensor from each red, green, and yellow constant using distance formula
        use a for loop to calculate distance. set each distance from a loop equal to a temporary value. after each loop is done compare that temporary
        value to the current loop distance so that you can see if it is greater than the previous loop

        whatever distance is the shortest, that is the closest color and should be returned for the get color function
         */

        target = null;

        return target;
    }

    public float[] test (){
        /*
        int[] colorsTest = {colorSensor.red()/10, colorSensor.blue()/10, colorSensor.green()/10};
        return colorsTest;
         */
        NormalizedRGBA colorsTest = colorSensor.getNormalizedColors();
        float red = colorsTest.red;
        float blue = colorsTest.blue;
        float green = colorsTest.green;

        float[] list = {red, blue, green};
        return list;

    }

    public boolean objectDetected () {
        return colorSensor.getDistance(DistanceUnit.CM) < 5;
    }
}
