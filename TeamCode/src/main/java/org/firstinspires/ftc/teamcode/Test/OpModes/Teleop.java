package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;

@TeleOp
public class Teleop extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        MecanumDrive mecanumDrive = new MecanumDrive(this.hardwareMap);
        waitForStart();
        if (isStopRequested()) {
            return;
        }
        while (opModeIsActive()) {
            double[] controls = {this.gamepad1.left_stick_x, this.gamepad1.left_stick_y, this.gamepad1.right_stick_x};
            this.telemetry.addData("x", Double.valueOf(controls[0]));
            this.telemetry.addData("y", Double.valueOf(controls[1]));
            this.telemetry.addData("r", Double.valueOf(controls[2]));
            this.telemetry.update();
            if (this.gamepad1.left_trigger >= 0.5) {
                mecanumDrive.mecanumDrive(controls);
            } else {
                mecanumDrive.trueNorthDrive(controls);
            }
        }
    }
}