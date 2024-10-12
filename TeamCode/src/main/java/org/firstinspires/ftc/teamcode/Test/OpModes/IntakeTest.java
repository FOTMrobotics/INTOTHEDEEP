package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class IntakeTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        CRServo servo1 = hardwareMap.crservo.get("wheel1");
        CRServo servo2 = hardwareMap.crservo.get("wheel2");
        DcMotor motor = hardwareMap.dcMotor.get("motor");

        waitForStart();

        if (isStopRequested()) {
            return;
        }

        while (opModeIsActive()) {
            if (gamepad1.b) {
                servo1.setPower(1);
                servo2.setPower(-1);
                motor.setPower(0.5);
            } else if (gamepad1.a) {
                servo1.setPower(-1);
                servo2.setPower(1);
                motor.setPower(-0.2);
            } else {
                servo1.setPower(0);
                servo2.setPower(0);
                motor.setPower(0);
            }
        }
    }
}
