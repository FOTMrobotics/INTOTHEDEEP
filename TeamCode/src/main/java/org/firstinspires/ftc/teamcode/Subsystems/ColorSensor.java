package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class ColorSensor {
    private com.qualcomm.robotcore.hardware.ColorSensor colorSensor;
    private DistanceSensor distanceSensor;

    public enum Color {
        YELLOW,
        RED,
        BLUE,
        NONE
    }

    public ColorSensor(HardwareMap hardwareMap, String deviceName) {
        colorSensor = hardwareMap.get(com.qualcomm.robotcore.hardware.ColorSensor.class, deviceName);
        distanceSensor = hardwareMap.get(DistanceSensor.class, deviceName);
    }

    private float hsvValues[] = {0F, 0F, 0F};
    private final float values[] = hsvValues;

    public Color getColor() {
        final double SCALE_FACTOR = 255;

        android.graphics.Color.RGBToHSV(
                (int) (colorSensor.red() * SCALE_FACTOR),
                (int) (colorSensor.green() * SCALE_FACTOR),
                (int) (colorSensor.blue() * SCALE_FACTOR),
                hsvValues
        );

        float blueDistance = Math.abs(hsvValues[0] - 240);
        float yellowDistance = Math.abs(hsvValues[0] - 110);
        float redDistance = Math.abs(hsvValues[0] - 20);

        if (redDistance < blueDistance && redDistance < yellowDistance) {
            return Color.RED;
        }
        if (blueDistance < redDistance && blueDistance < yellowDistance) {
            return Color.BLUE;
        }
        if (yellowDistance < redDistance && yellowDistance < blueDistance) {
            return Color.YELLOW;
        }

        return Color.NONE;
    }

    public double distance(DistanceUnit distanceUnit) {
        return distanceSensor.getDistance(distanceUnit);
    }

    public boolean inRange() {
        double MAX_DISTANCE = 2; // In CM
        return distance(DistanceUnit.CM) <= MAX_DISTANCE;
    }
}
