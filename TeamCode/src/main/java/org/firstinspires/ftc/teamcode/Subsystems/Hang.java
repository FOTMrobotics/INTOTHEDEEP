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
    Servo hookPivot;
    DcMotor hooks;

    private State state;

    enum State {
        HOOKS_UP,
        HOOKS_DOWN
    }

    public Hang(HardwareMap hardwareMap) {
        hookPivot = hardwareMap.get(Servo.class, "hookPivot");
        hooks = hardwareMap.get(DcMotor.class, "hooks");

        hooks.setDirection(DcMotorSimple.Direction.REVERSE);

        resetEncoder();
    }

    public void resetEncoder() {
        hooks.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hooks.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void update(Gamepad gamepad) {
        if (gamepad.dpad_up) {
            hooks.setPower(1);
        } else if (gamepad.dpad_down) {
            hooks.setPower(-1);
        } else {
            hooks.setPower(0);
        }
    }
}
