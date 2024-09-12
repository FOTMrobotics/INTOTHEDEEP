package org.firstinspires.ftc.teamcode.TeleOp;

import static java.lang.Double.isNaN;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Subsystems.Drivebase;
import org.firstinspires.ftc.teamcode.Subsystems.SparkFunOTOS;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;

@TeleOp
public class Test1 extends LinearOpMode {
    public static double x_offset = -3.75;
    public static double y_offset = 0;
    SparkFunOTOS myOtos;

    @Override
    public void runOpMode() throws InterruptedException {
        Drivebase drivebase = new Drivebase(hardwareMap);

        double[] motorPowers;

        /*DcMotor motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("backRight");*/

        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");

        configureOtos();

        myOtos.setOffset(new Pose2D(x_offset, y_offset, 0));

        //motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        //motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            Pose2D pos = myOtos.getPosition();

            TelemetryPacket packet = new TelemetryPacket();
            Canvas fieldOverlay = packet.fieldOverlay();

            packet.put("x", pos.x);
            packet.put("y", pos.y);
            packet.put("heading", pos.h);
            //packet.put("velocity", drivebase.velocity);

            fieldOverlay.setTranslation(-pos.y, pos.x);
            fieldOverlay.setRotation(Math.toRadians(pos.h+90));
            fieldOverlay.setStrokeWidth(1);
            fieldOverlay.setStroke("lime");
            fieldOverlay.strokeRect(-9,-8.5,18,17);
            fieldOverlay.setStroke("red");
            fieldOverlay.strokeLine(0,0,0,-8.5);

            dashboard.sendTelemetryPacket(packet);

            if (gamepad1.y) {
                myOtos.resetTracking();
            }

            if (gamepad1.x) {
                myOtos.calibrateImu();
            }

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = gamepad1.right_stick_x;
            telemetry.addData("rx", rx);

            double power;
            double power1 = Math.min(Math.abs(Math.pow(Math.cos(Math.atan2(y, x)) * (1/x), -1)), 1);
            double power2 = Math.min(Math.abs(Math.pow(Math.sin(Math.atan2(y, x)) * (1/y), -1)), 1);

            if (power1 > power2 || isNaN(power2)) {
                power = power1;
            } else {
                power = power2;
            }

            double angle = Math.toDegrees(Math.atan2(y, x)) + 90;
            angle = angle <= 180? angle: angle - 360;

            angle = angle - pos.h;

            x = Math.sin(Math.toRadians(angle)) * power;
            y = -Math.cos(Math.toRadians(angle)) * power;

            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double multiplier = (Boolean.compare(gamepad1.right_trigger > 0.1, false) * 2) + 1; // For slow mode
            //double frontLeftPower = (y + x + rx) / denominator / multiplier;
            //double backLeftPower = (y - x + rx) / denominator / multiplier;
            //double frontRightPower = (y - x - rx) / denominator / multiplier;
            //double backRightPower = (y + x - rx) / denominator / multiplier;

            motorPowers = new double[]{
                    (y + x + rx) / 1.2 / multiplier,
                    (y - x + rx) / 1.2 / multiplier,
                    (y - x - rx) / 1.2 / multiplier,
                    (y + x - rx) / 1.2 / multiplier
            };
            drivebase.runMotors(motorPowers);
            //motorFrontLeft.setPower(frontLeftPower);
            //motorBackLeft.setPower(backLeftPower);
            //motorFrontRight.setPower(frontRightPower);
            //motorBackRight.setPower(backRightPower);

            telemetry.addLine("Press Y (triangle) on Gamepad to reset tracking");
            telemetry.addLine("Press X (square) on Gamepad to calibrate the IMU");
            telemetry.addLine();

            telemetry.addData("X coordinate", pos.x);
            telemetry.addData("Y coordinate", pos.y);
            telemetry.addData("Heading angle", pos.h);

            telemetry.update();
        }
    }

    private void configureOtos() {
        telemetry.addLine("Configuring OTOS...");
        telemetry.update();

        // Set the desired units for linear and angular measurements. Can be either
        // meters or inches for linear, and radians or degrees for angular. If not
        // set, the default is inches and degrees. Note that this setting is not
        // stored in the sensor, it's part of the library, so you need to set at the
        // start of all your programs.
        // myOtos.setLinearUnit(SparkFunOTOS.LinearUnit.METERS);
        myOtos.setLinearUnit(SparkFunOTOS.LinearUnit.INCHES);
        // myOtos.setAngularUnit(SparkFunOTOS.AngularUnit.RADIANS);
        myOtos.setAngularUnit(SparkFunOTOS.AngularUnit.DEGREES);

        // Assuming you've mounted your sensor to a robot and it's not centered,
        // you can specify the offset for the sensor relative to the center of the
        // robot. The units default to inches and degrees, but if you want to use
        // different units, specify them before setting the offset! Note that as of
        // firmware version 1.0, these values will be lost after a power cycle, so
        // you will need to set them each time you power up the sensor. For example, if
        // the sensor is mounted 5 inches to the left (negative X) and 10 inches
        // forward (positive Y) of the center of the robot, and mounted 90 degrees
        // clockwise (negative rotation) from the robot's orientation, the offset
        // would be {-5, 10, -90}. These can be any value, even the angle can be
        // tweaked slightly to compensate for imperfect mounting (eg. 1.3 degrees).
        Pose2D offset = new Pose2D(0, 0, 0);
        myOtos.setOffset(offset);

        // Here we can set the linear and angular scalars, which can compensate for
        // scaling issues with the sensor measurements. Note that as of firmware
        // version 1.0, these values will be lost after a power cycle, so you will
        // need to set them each time you power up the sensor. They can be any value
        // from 0.872 to 1.127 in increments of 0.001 (0.1%). It is recommended to
        // first set both scalars to 1.0, then calibrate the angular scalar, then
        // the linear scalar. To calibrate the angular scalar, spin the robot by
        // multiple rotations (eg. 10) to get a precise error, then set the scalar
        // to the inverse of the error. Remember that the angle wraps from -180 to
        // 180 degrees, so for example, if after 10 rotations counterclockwise
        // (positive rotation), the sensor reports -15 degrees, the required scalar
        // would be 3600/3585 = 1.004. To calibrate the linear scalar, move the
        // robot a known distance and measure the error; do this multiple times at
        // multiple speeds to get an average, then set the linear scalar to the
        // inverse of the error. For example, if you move the robot 100 inches and
        // the sensor reports 103 inches, set the linear scalar to 100/103 = 0.971
        myOtos.setLinearScalar(1.0);
        myOtos.setAngularScalar(1.0);

        // The IMU on the OTOS includes a gyroscope and accelerometer, which could
        // have an offset. Note that as of firmware version 1.0, the calibration
        // will be lost after a power cycle; the OTOS performs a quick calibration
        // when it powers up, but it is recommended to perform a more thorough
        // calibration at the start of all your programs. Note that the sensor must
        // be completely stationary and flat during calibration! When calling
        // calibrateImu(), you can specify the number of samples to take and whether
        // to wait until the calibration is complete. If no parameters are provided,
        // it will take 255 samples and wait until done; each sample takes about
        // 2.4ms, so about 612ms total
        myOtos.calibrateImu();

        // Reset the tracking algorithm - this resets the position to the origin,
        // but can also be used to recover from some rare tracking errors
        myOtos.resetTracking();

        // After resetting the tracking, the OTOS will report that the robot is at
        // the origin. If your robot does not start at the origin, or you have
        // another source of location information (eg. vision odometry), you can set
        // the OTOS location to match and it will continue to track from there.
        Pose2D currentPosition = new Pose2D(0, 0, 0);
        myOtos.setPosition(currentPosition);

        // Get the hardware and firmware version
        SparkFunOTOS.Version hwVersion = new SparkFunOTOS.Version();
        SparkFunOTOS.Version fwVersion = new SparkFunOTOS.Version();
        myOtos.getVersionInfo(hwVersion, fwVersion);

        telemetry.addLine("OTOS configured! Press start to get position data!");
        telemetry.addLine();
        telemetry.addLine(String.format("OTOS Hardware Version: v%d.%d", hwVersion.major, hwVersion.minor));
        telemetry.addLine(String.format("OTOS Firmware Version: v%d.%d", fwVersion.major, fwVersion.minor));
        telemetry.update();
    }
}
