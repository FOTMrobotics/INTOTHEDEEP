package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hang {
    Servo backHookL;
    Servo backHookR;
    Servo pinLock;
    CRServo frontHookL;
    CRServo frontHookR;
    DcMotor pivot;

    private State state;

    enum State {}

    public Hang(HardwareMap hardwareMap) {
        backHookL = hardwareMap.get(Servo.class, "backHookL");
        backHookR = hardwareMap.get(Servo.class, "backHookR");
        pinLock = hardwareMap.get(Servo.class, "pinLock");
        frontHookL = hardwareMap.get(CRServo.class, "frontHookL");
        frontHookR = hardwareMap.get(CRServo.class, "frontHookR");
        pivot = hardwareMap.get(DcMotor.class, "hangPivot");

        pivot.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void update(Gamepad gamepad) {
        pivot.setPower(gamepad.right_trigger - gamepad.left_trigger);

        if (gamepad.left_bumper) {
            frontHookL.setPower(-1);
            frontHookR.setPower(-1);
        } else if (gamepad.right_bumper) {
            frontHookL.setPower(1);
            frontHookR.setPower(1);
        } else {
            frontHookL.setPower(0);
            frontHookR.setPower(0);
        }
    }
}
