package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.apache.commons.math3.analysis.function.Abs;

import java.util.HashMap;

public class Sweeper {
    private Servo sweeper;

    private State state;

    public enum State {
        IN,
        OUT
    }

    public HashMap<State, Double> map = new HashMap<State, Double>();

    public Sweeper(HardwareMap hardwareMap) {
        sweeper = hardwareMap.get(Servo.class, "sweeper");

        map.put(State.IN, 0.65);
        map.put(State.OUT, 0.31);

        in();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        sweeper.setPosition(map.get(state));
        this.state = state;
    }

    public void in() {
        setState(State.IN);
    }

    public void out() {
        setState(State.OUT);
    }

    public void update(Gamepad gamepad) {
        if (gamepad.right_stick_button) {
            out();
        } else {
            in();
        }
    }
}
