package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.w8wjb.ftc.AdafruitNeoDriver;

import java.util.ArrayList;

public class Lights {
    private AdafruitNeoDriver LEDS;

    public Lights (HardwareMap hardwareMap){
        LEDS = hardwareMap.get(AdafruitNeoDriver.class, "neopixels");
        LEDS.setNumberOfPixels(7);
    }

    public void setLights(int R, int G, int B){
        int color = Color.rgb(R, G, B);
        boolean x = true;
        int index = 25;
        LEDS.setPixelColor(index, color);
        LEDS.show();
    }
}