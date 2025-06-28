package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.fotmrobotics.trailblazer.PIDF;

import java.util.HashMap;
import java.util.List;

public class VerticalExtension {
    private DcMotor liftL, liftR;
    private TouchSensor limitSwitch;

    private int target;
    private boolean breakLift = true;

    private double p = 0.025;
    private double i = 0;
    private double d = 0;
    private double f = 0;

    private PIDF pidf = new PIDF(p, i, d, f);

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
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);

        for (LynxModule hub : allHubs) {
            hub.setBulkCachingMode(LynxModule.BulkCachingMode.AUTO);
        }

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

    public void setPIDF(double p, double i, double d, double f) {
        this.p = p;
        this.i = i;
        this.d = d;
        this.f = f;
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
            if (getCurrentPosition() < 150) {
                liftL.setPower(Math.max(-0.75, power)); // -0.75
                liftR.setPower(Math.max(-0.75, power)); // -0.75
            } else {
                liftL.setPower(power);
                liftR.setPower(power);
            }
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

    private static final double distance = 100;

    public void update(Gamepad gamepad) {
        double power = -gamepad.left_stick_y;

        int position = getCurrentPosition();

        if (atZero() && power <= 0) {
            zero();
        } else if (position >= map.get(State.MAX) - distance && power >= 0) {
            toTarget(State.MAX);
        } else if (power != 0) {
            setPowers(power);
            breakLift = false;
        } else if (!breakLift) {
            setTarget(position);
            breakLift = true;
        } else {
            toTarget();
        }
    }

    public int getCurrentPosition() {
        return (getEncoderL() + getEncoderR()) / 2;
    }

    private int tolerance = 20; // 10
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