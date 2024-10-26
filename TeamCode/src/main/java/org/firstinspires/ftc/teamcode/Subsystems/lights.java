package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.w8wjb.ftc.AdafruitNeoDriver;

import java.util.ArrayList;

public class lights {
    private AdafruitNeoDriver LEDS;

    public lights (HardwareMap hardwareMap){
        LEDS = hardwareMap.get(AdafruitNeoDriver.class, "neopixels");
        LEDS.setNumberOfPixels(7);
    }

    public void setLights(int R, int G, int B){
        int color = Color.rgb(R, G, B);
        boolean x = true;
        int index = 25;
        LEDS.setPixelColor(index, color);
        LEDS.show();
        /*
        while (x) {
            LEDS.setPixelColor(index, color);
            LEDS.show();

            index += 1;
            if (index == 100){
                index = 0;
            }
            R+= 3;
            G+= 7;
            if (R == 255) {
                R = 0;
            }
            if (G == 255) {
                G = 0;
            }
            if (B == 255) {
                B = 0;
            }
            color = Color.rgb(R, G, B);


        }*/
    }
}
