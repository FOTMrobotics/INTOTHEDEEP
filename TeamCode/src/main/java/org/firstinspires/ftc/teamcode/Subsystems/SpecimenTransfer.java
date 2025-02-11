package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.HashMap;

public class SpecimenTransfer {
    private Servo pivot;
    private CRServo carriage;
    private TouchSensor pickup, place, pickupPivot, placePivot;

    private State state;

    public enum State {
        PICKUP,
        PLACE,
    }

    public HashMap<State, Double> map = new HashMap<State, Double>();

    public SpecimenTransfer(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(Servo.class, "clawPivot");
        carriage = hardwareMap.get(CRServo.class, "carriage");
        pickup = hardwareMap.get(TouchSensor.class, "pickup");
        place = hardwareMap.get(TouchSensor.class, "place");
        pickupPivot = hardwareMap.get(TouchSensor.class, "pickupPivot");
        placePivot = hardwareMap.get(TouchSensor.class, "placePivot");

        map.put(State.PICKUP, 0.94);
        map.put(State.PLACE, 0.16);

        setState(State.PICKUP);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setPivot(State state) {
        pivot.setPosition(map.get(state));
    }

    public void setPower(double power) {
        carriage.setPower(power);
    }

    public void swapDirection() {
        if (state == State.PICKUP) setState(State.PLACE);
        else setState(State.PICKUP);

        transfer = true;
    }

    private double n = 0;
    private void advance() {
        double power = 0.25 * Math.sqrt(n);

        if (state == State.PICKUP) {
            if (pickup.isPressed()) {
                power = 0;
                n = 0;
            }
            carriage.setPower(power);
        }

        if (state == State.PLACE) {
            if (place.isPressed()) {
                power = 0;
                n = 0;
            }
            carriage.setPower(-power);
        }

        n += 0.05;
    }

    private boolean transfer = true;
    public void move() {
        advance();
        if (state == State.PICKUP) if (placePivot.isPressed()) pivot.setPosition(map.get(State.PICKUP));
        if (state == State.PLACE) if (placePivot.isPressed()) pivot.setPosition(map.get(State.PLACE));
        /*
        if (transfer) {
            if (state == State.PICKUP) {
                if (!pickup.isPressed()) carriage.setPower(1);
                else {
                    carriage.setPower(0);
                    transfer = false;
                }

                if (placePivot.isPressed()) pivot.setPosition(map.get(State.PICKUP));
            } else if (state == State.PLACE) {
                if (!place.isPressed()) carriage.setPower(-1);
                else {
                    carriage.setPower(0);
                    transfer = false;
                }

                if (placePivot.isPressed()) pivot.setPosition(map.get(State.PLACE));
            }
        }
        */
    }

    public boolean isTransfer() {
        return transfer;
    }

    public void transfer() {
        transfer = true;
    }

    private boolean pressed = false;
    private boolean transfering = false;
    public void update(Gamepad gamepad) {
        if (!pressed) {
            if (gamepad.left_stick_button) {
                swapDirection();
            }
        }

        pressed = gamepad.left_stick_button;

        move();
    }
}
