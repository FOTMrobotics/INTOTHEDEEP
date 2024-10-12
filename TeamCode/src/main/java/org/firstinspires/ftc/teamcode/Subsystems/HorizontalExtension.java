package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Test.Util.PDFLcontrol;

public class HorizontalExtension {
    private DcMotor extension;
    private PDFLcontrol PDFL = new PDFLcontrol(0.01, 0, 0, 0);
    private boolean breakExtension = true;
    public int target;

    public final static int MAX = 1500;

    public HorizontalExtension (HardwareMap hardwareMap) {
        extension = hardwareMap.get(DcMotor.class, "extension");

        extension.setDirection(DcMotorSimple.Direction.REVERSE);

        extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void resetEncoders () {
        extension.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        extension.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void zero () {
        toTarget(0);
        if (extension.getCurrentPosition() < 0) {
            resetEncoders();
        }
    }

    public void max () {
        toTarget(MAX);
    }

    public void setPower (double power) {
        extension.setPower(power);
    }

    public void toTarget () {
        double error = target - extension.getCurrentPosition();
        setPower(PDFL.update(error));
    }

    public void toTarget (int target) {
        setTarget(target);
        toTarget();
    }

    public void setTarget (int target) {
        this.target = target;
    }

    /**
     * Use for TeleOp
     */
    public void update (Gamepad gamepad) {
        //double power = out - in;
        double power = gamepad.right_stick_x;

        if (extension.getCurrentPosition() <= 50 && power <= 0) {
            zero();
        } else if (extension.getCurrentPosition() >= MAX - 50 && power >= 0) {
            max();
        } else if (power != 0) {
            setPower(power);
            breakExtension = false;
        } else if (!breakExtension) {
            setTarget(extension.getCurrentPosition());
            breakExtension = true;
        } else {
            toTarget();
        }
    }

    public double getTarget () {
        return target;
    }

    public double getEncoder () {
        return extension.getCurrentPosition();
    }
}
