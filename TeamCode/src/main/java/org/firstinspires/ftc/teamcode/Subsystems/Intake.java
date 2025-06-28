package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.HashMap;

// TODO: Add sample filtering
//  Have list that includes all of the samples to be filtered
//  Preset tele/auto with the filters already selected
//  Options to disable filter
public class Intake {
    private Servo pivot;
    private DcMotor intakeMotor;
    private ColorSensor colorSensor;

    private ColorSensor.Color color = ColorSensor.Color.NONE;

    private ColorSensor.Color filterColor = ColorSensor.Color.NONE;

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

        map.put(State.OUT, 0.46);
        map.put(State.AWAIT, 0.2);
        map.put(State.IN, 0.0);
    }

    public void setFilter(ColorSensor.Color color) {
        filterColor = color;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        pivot.setPosition(map.get(state));
        this.state = state;
    }

    public void intake() {
        color = colorSensor.getColor();

        if (color == filterColor) {
            outtake();
            setState(State.AWAIT);
        } else {
            intakeMotor.setPower(-1);
            setState(State.OUT);
        }
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

    public void update(double linkagePos, boolean atZero, Gamepad gamepad) {
        if (linkagePos <= 0.56) setState(State.IN); // out position
        else if (!(gamepad.right_trigger > 0)) setState(State.AWAIT);

        if (gamepad.right_trigger > 0) {
            if (linkagePos >= 0.56) setState(State.OUT); // out position
            intake();
        }

        else if (gamepad.left_trigger > 0) {
            if (linkagePos == 0.33) intakeMotor.setPower(0.8); // zero position
            else outtake();
        }

        else stop();

        if (atZero && gamepad.right_stick_x < 0) intakeMotor.setPower(0.8);
    }

    public double getDistance() {
        return colorSensor.distance(DistanceUnit.CM);
    }

    public ColorSensor.Color getColor() {
        return color;
    }

    public void readColor() {
        color = colorSensor.getColor();
    }

    public boolean sampleIn() {
        return getDistance() < 4;
    }
}
