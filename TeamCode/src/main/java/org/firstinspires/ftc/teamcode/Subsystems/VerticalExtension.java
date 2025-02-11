package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.fotmrobotics.trailblazer.PIDF;

import java.util.HashMap;

public class VerticalExtension {
    private DcMotor liftL, liftR;
    private TouchSensor limitSwitch;

    private int target;
    private boolean breakLift = true;

    private PIDF pidf = new PIDF(0.025, 0, 0, 0); //0.05

    private State state;

    public enum State {
        ZERO,
        MAX,
        LOW_BUCKET,
        HIGH_BUCKET,
        LOW_BAR,
        HIGH_BAR
    }

    public HashMap<State, Integer> map = new HashMap<State, Integer>();

    public VerticalExtension(HardwareMap hardwareMap) {
        liftL = hardwareMap.get(DcMotor.class, "liftL");
        liftR = hardwareMap.get(DcMotor.class, "liftR");
        limitSwitch = hardwareMap.get(TouchSensor.class, "liftLimitSwitch");

        liftL.setDirection(DcMotorSimple.Direction.REVERSE);

        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // TODO: May need to change
        map.put(State.ZERO, 0);
        map.put(State.MAX, 2450);

        map.put(State.LOW_BUCKET, 0);
        map.put(State.HIGH_BUCKET, 0);

        map.put(State.LOW_BAR, 0);
        map.put(State.HIGH_BAR, 640);
    }

    public State getState() {
        return state;
    }

    public void resetEncoders() {
        liftL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


    }

    public void zero() {
        setPowers(0);
        resetEncoders();
    }

    public void toZero() {
        if (atZero()) zero();
        else setPowers(-1);
    }

    public void setPowers(double power) {
        if (!atZero() || power >= 0) {
            liftL.setPower(Math.max(-0.5, power));
            liftR.setPower(Math.max(-0.5, power));
        }
    }

    public void toTarget() {
        double error = target - getCurrentPosition();
        setPowers(pidf.update(error));
    }

    public void toTarget(int target) {
        setTarget(target);
        toTarget();
    }

    public void toTarget(State target) {
        setTarget(map.get(target));
        toTarget();
    }

    public void setTarget(int target) {
        this.target = target;
    }

    private static final double distance = 25;

    public double power = 0;
    public void update(Gamepad gamepad) {
        power = -gamepad.left_stick_y;

        if (atZero() && power <= 0) {
            zero();
        } else if (getCurrentPosition() >= map.get(State.MAX) - distance && power >= 0) {
            toTarget(State.MAX);
        } else if (power != 0) {
            setPowers(power);
            breakLift = false;
        } else if (!breakLift) {
            setTarget(getCurrentPosition());
            breakLift = true;
        } else {
            toTarget();
        }
    }

    public int getCurrentPosition() {
        return (getEncoderL() + getEncoderR()) / 2;
    }

    private int tolerance = 15;
    public boolean atTarget() {
        return target > getCurrentPosition() - tolerance && target < getCurrentPosition() + tolerance;
    }

    public int getTarget() {
        return target;
    }

    public int getEncoderL() {
        return -liftL.getCurrentPosition();
    }

    public int getEncoderR() {
        return -liftR.getCurrentPosition();
    }

    public boolean atZero() {
        return limitSwitch.isPressed();
    }
}