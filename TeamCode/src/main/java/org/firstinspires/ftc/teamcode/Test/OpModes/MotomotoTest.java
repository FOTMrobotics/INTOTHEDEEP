package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class MotomotoTest extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        DcMotor motor = hardwareMap.dcMotor.get("motor");

        boolean pressed = false;
        boolean flipped = false;

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            if (gamepad1.a) {motor.setPower(gamepad1.right_trigger - gamepad1.left_trigger);}

            if (!pressed) {
                if (gamepad1.b) {
                    motor.setDirection(DcMotorSimple.Direction.REVERSE);
                    flipped = !flipped;
                }
            }

            pressed = gamepad1.b;

            telemetry.addData("Position", motor.getCurrentPosition());
            telemetry.addData("Power", motor.getPower());
            telemetry.addData("Direction", flipped);

            telemetry.update();
        }
    }
}
