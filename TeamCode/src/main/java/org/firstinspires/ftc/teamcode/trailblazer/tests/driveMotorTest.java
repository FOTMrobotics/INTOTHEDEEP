package org.firstinspires.ftc.teamcode.trailblazer.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name = "driveMotor", group = "Tests")
public class driveMotorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor frontRight = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor backLeft = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor backRight = hardwareMap.get(DcMotor.class, "backRight");

        waitForStart();

        while (isStopRequested()) return;

        while (opModeIsActive()) {
            double power = gamepad1.right_trigger - gamepad1.left_trigger;

            if (gamepad1.x) {
                frontLeft.setPower(power);
            }

            if (gamepad1.y) {
                frontRight.setPower(power);
            }

            if (gamepad1.a) {
                backLeft.setPower(power);
            }

            if (gamepad1.b) {
                backRight.setPower(power);
            }
        }
    }
}
