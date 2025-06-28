package org.firstinspires.ftc.teamcode.Subsystems;

import android.graphics.Color;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

// TODO: Finish after the hang is built
//  Current hardware objects need to be changed
public class Hang {
    Servo hookPinlock;
    DcMotor hooks;

    public Hang(HardwareMap hardwareMap) {
        hookPinlock = hardwareMap.get(Servo.class, "hookPinlock");
        hooks = hardwareMap.get(DcMotor.class, "hooks");

        hooks.setDirection(DcMotorSimple.Direction.REVERSE);

        resetEncoder();

        hooksHold();
    }

    public void resetEncoder() {
        hooks.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hooks.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void hooksHold() {
        hookPinlock.setPosition(0.12);
    }

    public void hooksRelease() {
        hookPinlock.setPosition(0.72);
    }

    public void update(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            hooks.setPower(1);
        } else if (gamepad.dpad_down) {
            hooks.setPower(-1);
        } else {
            hooks.setPower(0);
        }

        if (gamepad.dpad_left || gamepad.dpad_right) {
            hooksRelease();
        }
    }
}
