package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.Test.Util.PDFLcontrol;

import java.util.HashMap;

public class VerticalExtension {
    private DcMotor liftL, liftR;
    private TouchSensor touchSensor;
    private PDFLcontrol PDFL = new PDFLcontrol(0.01, 0, 0, 0);
    private boolean breakLift = true;
    public int target;
    public state currentState;
    public enum state {
        ZERO,
        LOW,
        HIGH
    }

    public HashMap<state, Integer> map = new HashMap<>();

    public VerticalExtension (HardwareMap hardwareMap) {
        touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");

        liftL = hardwareMap.get(DcMotor.class, "liftL");
        liftR = hardwareMap.get(DcMotor.class, "liftR");

        liftR.setDirection(DcMotorSimple.Direction.REVERSE);

        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        map.put(state.ZERO, 50);
        map.put(state.LOW, 300);
        map.put(state.HIGH, 3750);
    }

    public void resetEncoders () {
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void zero () {
        setPowers(0);
        resetEncoders();
    }

    public void max () {
        toTarget(3750);
    }

    public void setPowers (double power) {
        liftL.setPower(power);
        liftR.setPower(power);
    }

    public void toTarget () {
        double error = target - liftL.getCurrentPosition();
        setPowers(PDFL.update(error));
    }

    public void toTarget (int target) {
        setTarget(target);
        toTarget();
    }

    public void setTarget (int target) {
        this.target = target;
    }

    /**
     * Use for TeleOp
     */
    public void update (Gamepad gamepad) {
        //double power = up - down;
        double power = -gamepad.left_stick_y;

        if (liftL.getCurrentPosition() <= 50 && power <= 0 || touchSensor.isPressed()) {
            zero();
        } else if (liftL.getCurrentPosition() >= 3700 && power >= 0) {
            max();
        } else if (power != 0) {
            setPowers(power);
            breakLift = false;
        } else if (!breakLift) {
            setTarget(liftL.getCurrentPosition());
            breakLift = true;
        } else {
            toTarget();
        }
    }

    /*
    public void updateState () {
        int position = liftL.getCurrentPosition();

        if (position <= map.get(state.LOW)) {
            currentState = state.ZERO;
        } else if (position <= map.get(state.LOW)) {
            currentState = state.LOW;
        } else {
            currentState = state.HIGH;
        }
    }
    */

    public int getTarget () {
        return target;
    }

    public int getEncoderL () {
        return liftL.getCurrentPosition();
    }

    public int getEncoderR () {
        return liftR.getCurrentPosition();
    }
}
