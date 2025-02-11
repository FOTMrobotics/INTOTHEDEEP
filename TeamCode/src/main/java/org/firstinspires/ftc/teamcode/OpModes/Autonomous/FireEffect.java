package org.firstinspires.ftc.teamcode.OpModes.Autonomous;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Subsystems.AdafruitNeoDriver;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Autonomous(name = "Fire Effect", group = "Showcase")
public class FireEffect extends LinearOpMode {
    ElapsedTime timer = new ElapsedTime();

    AdafruitNeoDriver neopixels;

    static int numPixels = 60;
    static int numPoints = 3; // 3

    static double increment = (double) numPoints / numPixels;

    static double brightness = 0.5; // 0.5

    static double delay = 10; // in milliseconds

    ArrayList<Double> points;
    Random rng;

    @Override
    public void runOpMode() throws InterruptedException {
        neopixels = hardwareMap.get(AdafruitNeoDriver.class, "neopixels");
        neopixels.setNumberOfPixels(numPixels*2);

        points = new ArrayList<>();

        rng = new Random();

        for (int i = 0; i < numPixels+5; i++) points.add(rng.nextDouble());

        int i = 0;
        int pointNum = 0;
        double shift = 0;

        int[] colors = new int[numPixels*2];

        waitForStart();

        while (opModeIsActive()) {
            double t = shift;
            while (t <= 1) {
                double min = points.get(pointNum);
                double max = points.get(pointNum+1);

                double m = (1 - Math.cos(t * 3.14)) / 2;

                int r = (int) (255 * brightness);
                int g = (int) (((min * (1 - m) + max * m) * 60) * brightness); // 180
                int b = 0;

                int color = Color.rgb(r,g,b);
                colors[i] = color;

                ++i;
                t += increment;
            }

            ++pointNum;

            if (i > numPixels) {
                neopixels.setPixelColors(colors);
                neopixels.show();
                timer.reset();
                while (timer.time(TimeUnit.MILLISECONDS) < delay);

                i = 0;
                shift += increment;
                pointNum = (int) shift;
            }

            if (pointNum > numPoints) {
                points.remove(0);
                points.add(rng.nextDouble());
                shift = 0;
                pointNum = 0;
            }
        }
    }
}
