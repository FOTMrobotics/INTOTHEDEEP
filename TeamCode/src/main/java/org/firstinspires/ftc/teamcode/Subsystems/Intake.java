package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.HashMap;

public class Intake {
    private Servo pivot;
    private DcMotor intakeMotor;
    private ColorSensor colorSensor;

    private State state;

    public enum State {
        OUT,
        AWAIT,
        IN
    }

    public HashMap<State, Double> map = new HashMap<State, Double>();

    public Intake(HardwareMap hardwareMap) {
        pivot = hardwareMap.get(Servo.class, "intakePivot");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        colorSensor = new ColorSensor(hardwareMap, "intakeColorSensor");

        map.put(State.OUT, 0.49);
        map.put(State.AWAIT, 0.23);
        map.put(State.IN, 0.02);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        pivot.setPosition(map.get(state));
        this.state = state;
    }

    public void intake() {
        intakeMotor.setPower(-1);
    }

    public void outtake() {
        intakeMotor.setPower(0.4);
    }

    public void intakePower(double power) {
        intakeMotor.setPower(power);
    }

    public void stop() {
        intakeMotor.setPower(0);
    }

    public void update(HorizontalExtension linkage, Gamepad gamepad) {
        if (linkage.pos <= linkage.map.get(HorizontalExtension.State.OUT)) setState(State.IN);
        else if (!gamepad.right_bumper) setState(State.AWAIT);

        if (gamepad.right_bumper) {
            if (linkage.pos >= linkage.map.get(HorizontalExtension.State.OUT)) setState(State.OUT);
            intake();
        }
        else if (gamepad.left_bumper) {
            if (linkage.pos == linkage.map.get(HorizontalExtension.State.ZERO)) intakeMotor.setPower(0.8);
            else outtake();
        }
        else stop();

        //if (linkage.getState() == HorizontalExtension.State.ZERO && lift.atZero() && gamepad.right_stick_x < 0) outtake();
    }

    public double getDistance() {
        return colorSensor.distance(DistanceUnit.CM);
    }

    public ColorSensor.Color getColor() {
        return colorSensor.getColor();
    }

    public boolean sampleIn() {
        return getDistance() < 2;
    }
}
