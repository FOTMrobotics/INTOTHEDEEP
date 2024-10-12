package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;

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
        color output;
        if (false) {output = color.YELLOW;}
        else if (false) {output = color.RED;}
        else if (false) {output = color.BLUE;}
        else {output = color.NONE;}

        return output;
    }

    public boolean objectDetected () {
        return colorSensor.getDistance(DistanceUnit.CM) < 5;
    }
}
