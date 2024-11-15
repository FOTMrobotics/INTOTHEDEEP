package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Test.Util.PDFLcontrol;

public class HorizontalExtension {
    private Servo extension1, extension2;
    private PDFLcontrol PDFL = new PDFLcontrol(0.01, 0, 0, 0);

    public double pos = ZERO;

    public final static double ZERO = 0.46;
    public final static double OUT = 0.46; // TODO: Change placeholder
    public final static double MAX = 1;

    public final static double SCALE = 0.1;

    public HorizontalExtension(HardwareMap hardwareMap) {
        extension1 = hardwareMap.get(Servo.class, "extension1");
        extension2 = hardwareMap.get(Servo.class, "extension2");

        //extension1.scaleRange(ZERO, 1);
        //extension2.scaleRange(ZERO, 1);

        setPosition(ZERO);
    }

    public void setPosition(double pos) {
        extension1.setPosition(pos);
        extension2.setPosition(pos);
        this.pos = pos;
    }

    public void zero() {
        setPosition(ZERO);
    }

    public void out() {
        setPosition(OUT);
    }

    public void max() {
        setPosition(MAX);
    }

    public double lerp(double t) {
        return OUT + t * (MAX - OUT);
    }

    /**
     * Use for TeleOp
     */
    public void update (Gamepad gamepad) {
        double power = gamepad.right_stick_x;

        if (pos == ZERO && power > 0) {
            out();
        } else if (pos <= OUT && power < 0) {
            zero();
        } else if (pos > OUT && Math.abs(power) > 0) {
            pos += power * SCALE;
            pos = Math.max(Math.min(OUT, pos), 1);
            setPosition(pos);
        }
    }
}
