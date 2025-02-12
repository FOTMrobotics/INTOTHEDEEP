package org.firstinspires.ftc.teamcode.Subsystems;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    ElapsedTime timer = new ElapsedTime();
    boolean x = true;

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

    public enum State {
        BLINKING,
        SOLID,
        NONE
    }

    public void LEDState (State effect, ColorSensor.Color color) {
        int colors = Color.rgb(0, 0, 0);

        switch (color){
            case RED:
                colors = Color.rgb(255, 0, 0);
                break;
            case BLUE:
                colors = Color.rgb(0, 0, 255);
                break;
            case YELLOW:
                colors = Color.rgb(255, 60, 0);
                break;
            case NONE:
                colors = Color.rgb(0, 0, 0);
        }
        double time = timer.time(TimeUnit.MILLISECONDS);
        int blank = Color.rgb(0, 0, 0);

        if (time >= 70){
            switch (effect) {
                case BLINKING:
                    //fix blinking logic
                    LEDS.fill(colors);
                    if (x){
                        LEDS.fill(blank);
                        x = false;
                    }else{
                        x = true;
                    }
                    break;
                case SOLID:
                    LEDS.fill(colors);
                    break;
                case NONE:
                    LEDS.fill(blank);
                    break;
            }
            timer.reset();
        }
        LEDS.show();
    }
}