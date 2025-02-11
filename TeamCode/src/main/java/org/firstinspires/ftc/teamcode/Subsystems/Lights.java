package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.ArrayList;

// TODO: Change this class to include LED effects for driver control
//  -----------------------------------------------------
//  Requirements:
//  Include different effects for telling whether the sample is in the intake or bucket
//  Feel free to make these effects whatever, but make sure they are customizable
//  Easiest effects to do would be solid and blinking
//  LED lights must have a delay for showing pixels
//  Delay must not include any sleep or while loops
//  (ElapsedTime is a good class to use for this)
//  No while loops should be used since whatever is used will be updating within the main loop
//  -----------------------------------------------------
//  Suggestion: Have switch statement and an enum value for each effect
//  Each enum corresponds to an effect and it all shares one delay time for showing pixels
//  Include inside an update method with the enum as a condition
//  This would make it easy to call each effect as only one method is needed
//  Also, it would be helpful if each part of it is customizable with constants

public class Lights {
    private AdafruitNeoDriver LEDS;

    public Lights (HardwareMap hardwareMap){
        LEDS = hardwareMap.get(AdafruitNeoDriver.class, "neopixels");
        LEDS.setNumberOfPixels(60);
    }

    public void setLights(int R, int G, int B){
        int color = Color.rgb(R, G, B);
        boolean x = true;
        int index = 25;
        LEDS.setPixelColor(index, color);
        LEDS.show();
    }
}