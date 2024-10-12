package org.firstinspires.ftc.teamcode.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Claw {
    private Servo pivot, claw;

    public Claw (HardwareMap hardwareMap) {
        pivot = hardwareMap.get(Servo.class, "clawPivot");
        claw = hardwareMap.get(Servo.class, "claw");
    }
}
