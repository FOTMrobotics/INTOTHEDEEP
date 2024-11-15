package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class Intake {
    private HorizontalExtension extension;
    private DcMotor roller;
    private CRServo wheel1, wheel2;
    private Servo pivot;
    private ColorSensor colorSensor;
    private TouchSensor touchSensor;

    public Intake (HardwareMap hardwareMap) {
        roller = hardwareMap.get(DcMotor.class, "roller");
        roller.setDirection(DcMotorSimple.Direction.REVERSE);

        wheel1 = hardwareMap.get(CRServo.class, "wheel1");
        wheel2 = hardwareMap.get(CRServo.class, "wheel2");

        pivot = hardwareMap.get(Servo.class, "bucketPivot");

        //colorSensor = new ColorSensor(hardwareMap);
        //touchSensor = hardwareMap.get(TouchSensor.class, "touchSensor");
    }

    public void in () {
        setWheels(1);
        roller.setPower(1);
    }

    public void out () {
        setWheels(-1);
        roller.setPower(-0.3);
    }

    public void stop () {
        setWheels(0);
        roller.setPower(0);
    }

    public void setWheels (double power) {
        wheel1.setPower(-power);
        wheel2.setPower(power);
    }

    public void pivotOut () {
        pivot.setPosition(1); // TODO: Change
    }

    public void pivotAwait () {
        pivot.setPosition(0.75); // TODO: Change
    }

    public void pivotIn () {
        pivot.setPosition(0);
    }

    public void update (HorizontalExtension extension, VerticalExtension lift, Gamepad gamepad) {
        if (extension.pos <= HorizontalExtension.OUT) {
            pivotIn();
        } else if (!gamepad.right_bumper) {
            pivotAwait();
        }

        if (gamepad.right_bumper) {
            in();
            if (extension.pos >= HorizontalExtension.OUT) {
                pivotOut();
            }
        } else if (gamepad.left_bumper) {
            out();
        } else {
            stop();
        }

        /*
        if (extension.getEncoder() < 20 && gamepad.left_trigger > 0) {
            //lift.zero();
            if (lift.getEncoderL() < 20) {
                out();
            }
        }
        */
    }
}
