package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HorizontalExtension {
    private Servo linkageL, linkageR;
    private AnalogInput linkageLEncoder, linkageREncoder;

    private State state;

    public enum State {
        ZERO,
        IN,
        OUT,
        MAX
    }

    public HashMap<State, Double> map = new HashMap<State, Double>();

    public double pos;

    private final static double SCALE = 0.05; // 0.025

    public HorizontalExtension(HardwareMap hardwareMap) {
        linkageL = hardwareMap.get(Servo.class, "linkageL");
        linkageLEncoder = hardwareMap.get(AnalogInput.class, "linkageLEncoder");
        linkageR = hardwareMap.get(Servo.class, "linkageR");
        linkageREncoder = hardwareMap.get(AnalogInput.class, "linkageREncoder");

        map.put(State.ZERO, 0.33);
        map.put(State.OUT, 0.56);
        map.put(State.MAX, 0.85);

        setPosition(map.get(State.ZERO));
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        setPosition(map.get(state));
    }

    public void setPosition(double pos) {
        this.pos = pos;

        linkageL.setPosition(pos);
        linkageR.setPosition(pos);

        if (pos < map.get(State.OUT)) {
            state = State.IN;
        } else {
            state = State.OUT;
        }
    }

    public double[] getEncoderPositions() {
        double posL = linkageLEncoder.getVoltage() / 3.3 * 360;
        double posR = linkageREncoder.getVoltage() / 3.3 * 360;

        return new double[] {posL, posR};
    }

    public void zero() {
        setState(State.ZERO);
    }

    public void out() {
        setState(State.OUT);
    }

    public void out(double t) {
        setPosition(map.get(State.OUT)+t*(map.get(State.MAX)-map.get(State.OUT)));
    }

    public void max() {
        setState(State.MAX);
    }

    public boolean atZero() {
        double[] positions = getEncoderPositions();

        double TARGETL = 207.5; // out = negative direction
        double TARGETR = 153.3; // out = positive direction
        double TOLERANCE = 10;

        return positions[0] > TARGETL - TOLERANCE && positions[1] < TARGETR + TOLERANCE;
    }

    private ElapsedTime timer = new ElapsedTime();
    private boolean resetTimer = false;
    public void update(Gamepad gamepad) {
        double power = gamepad.right_stick_x;

        if (pos == map.get(State.ZERO) && power > 0) {
            out();
        } else if (pos <= map.get(State.OUT) && power < 0) {
            resetTimer = timer.time(TimeUnit.MILLISECONDS) > 300;
            if (resetTimer) timer.reset();

            boolean toZero = timer.time(TimeUnit.MILLISECONDS) <= 300 && timer.time(TimeUnit.MILLISECONDS) >= 200;
            if (toZero) zero();
        } else if (pos >= map.get(State.OUT) && Math.abs(power) > 0) {
            setPosition(Math.min(Math.max(map.get(State.OUT), pos + power * SCALE), map.get(State.MAX)));
        }

        if (atZero()) {
            state = State.ZERO;
        }
    }
}
