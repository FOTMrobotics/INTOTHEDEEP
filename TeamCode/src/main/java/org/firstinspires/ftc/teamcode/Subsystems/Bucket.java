package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.HashMap;

// TODO: Add sample filtering
//  Have list that includes all of the samples to be filtered
//  Preset tele/auto with the filters already selected
//  Options to disable filter
public class Bucket {
    private Servo bucketFlap;

    private DigitalChannel beamBreak;

    private State state;

    public enum State {
        OPEN,
        CLOSE
    }

    public HashMap<State, Double> map = new HashMap<State, Double>();

    public Bucket (HardwareMap hardwareMap) {
        bucketFlap = hardwareMap.get(Servo.class, "bucketFlap");
        beamBreak = hardwareMap.get(DigitalChannel.class, "beamBreak");

        // TODO: Test
        map.put(State.OPEN, 0.0);
        map.put(State.CLOSE, 1.0);

        close();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        bucketFlap.setPosition(map.get(state));
        this.state = state;
    }

    public boolean samplePresent() {
        return !beamBreak.getState();
    }

    public void open() {
        setState(State.OPEN);
    }

    public void close() {
        setState(State.CLOSE);
    }

    private boolean pressed;
    public void update(Gamepad gamepad) {
        pressed = gamepad.right_bumper;

        if (pressed) {
            open();
        } else {
            close();
        }
    }
}
