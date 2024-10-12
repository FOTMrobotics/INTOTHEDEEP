package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Bucket {
    private Servo bucket;
    private boolean pressed;
    private boolean liftLow = false;
    public boolean open = false;

    public Bucket (HardwareMap hardwareMap) {
        bucket = hardwareMap.get(Servo.class, "bucket");
    }

    public void open () {
        bucket.setPosition(0);
    }

    public void close () {
        bucket.setPosition(1);
    }

    public void update (VerticalExtension lift, Gamepad gamepad) {
        //if (!liftLow) {
            //open = false;
        //}

        //liftLow = lift.getEncoderL() < 100;

        //if (!pressed) {
            //open = !open;
        //}

        pressed = gamepad.left_bumper || gamepad.right_bumper;

        if (pressed) {
            open();
        } else {
            close();
        }
    }
}
