package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.HashMap;

public class Claw {
    private Servo claw;
    private Servo pivot;

    private State clawState;
    private State pivotState;

    public enum State {
        OPEN,
        CLOSE,
        PIVOT_OUT,
        PIVOT_IN
    }

    public HashMap<State, Double> map = new HashMap<State, Double>();

    public Claw(HardwareMap hardwareMap) {
        claw = hardwareMap.get(Servo.class, "claw");
        pivot = hardwareMap.get(Servo.class, "clawPivot");

        map.put(State.OPEN, 0.45); // TODO: Change value
        map.put(State.CLOSE, 0.67); // TODO: Change value
        map.put(State.PIVOT_OUT, 0.0); // TODO: Change value
        map.put(State.PIVOT_IN, 0.0); // TODO: Change value

        setState(State.CLOSE);
    }

    public State getClawState() {
        return clawState;
    }

    public State getPivotState() {
        return pivotState;
    }

    public void setState(State state) {
        if (state == State.OPEN || state == State.CLOSE) {
            claw.setPosition(map.get(state));
            this.clawState = state;
        }

        else if (state == State.PIVOT_OUT || state == State.PIVOT_IN) {
            pivot.setPosition(map.get(state));
            this.pivotState = state;
        }
    }

    public void open() {
        setState(State.OPEN);
    }

    public void close() {
        setState(State.CLOSE);
    }

    public void out() {
        setState(State.PIVOT_OUT);
    }

    public void in() {
        setState(State.PIVOT_IN);
    }

    public void update(Gamepad gamepad) {
        if (gamepad.left_bumper) setState(State.OPEN);
        else setState(State.CLOSE);
    }
}
