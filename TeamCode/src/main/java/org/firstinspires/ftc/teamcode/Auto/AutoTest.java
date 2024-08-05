package org.firstinspires.ftc.teamcode.Auto;

import static java.lang.Double.isNaN;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.canvas.Canvas;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SparkFunOTOS.SparkFunOTOS;

@Config
@Autonomous
public class AutoTest extends LinearOpMode {
    public static double x_offset = -3.75;
    public static double y_offset = 0;
    public static double multiplier = 1;
    public static boolean turning = true;
    public static double Kp = 0.3;
    public static double Ki = 0;
    public static double Kd = 0.01;
    public static double headingKp = 0.1;
    public static double headingKi = 0;
    public static double headingKd = 0;
    public static double targetPosX = 10;
    public static double targetPosY = 25;
    public static double targetHeading = 0;
    SparkFunOTOS myOtos;

    @Override
    public void runOpMode() throws InterruptedException {
        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");

        configureOtos();

        myOtos.setOffset(new SparkFunOTOS.Pose2D(x_offset, y_offset, 0));

        DcMotor motorFrontLeft = hardwareMap.dcMotor.get("frontLeft");
        DcMotor motorFrontRight = hardwareMap.dcMotor.get("frontRight");
        DcMotor motorBackLeft = hardwareMap.dcMotor.get("backLeft");
        DcMotor motorBackRight = hardwareMap.dcMotor.get("backRight");

        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        double count = 0;
        double sumTime = 0;

        double integralSum = 0;
        double lastError = 0;

        double headingIntegralSum = 0;
        double headingLastError = 0;

        ElapsedTime timer = new ElapsedTime();

        waitForStart();

        while (opModeIsActive()) {
            SparkFunOTOS.Pose2D pos = myOtos.getPosition();

            TelemetryPacket packet = new TelemetryPacket();
            Canvas fieldOverlay = packet.fieldOverlay();

            double x = pos.x;
            double y = pos.y;

            packet.put("x", x);
            packet.put("y", y);
            packet.put("heading", pos.h);

            telemetry.addData("x", x);
            telemetry.addData("y", y);
            telemetry.addData("heading", pos.h);

            fieldOverlay.setTranslation(x, y);
            fieldOverlay.setRotation(Math.toRadians(pos.h + 90));
            fieldOverlay.setStrokeWidth(1);
            fieldOverlay.setStroke("lime");
            fieldOverlay.strokeRect(-9, -8.5, 18, 17);
            fieldOverlay.setStroke("red");
            fieldOverlay.strokeLine(0, 0, 0, -8.5);

            double xDist = x - targetPosX;
            double yDist = y - targetPosY;

            double angle = Math.toDegrees(Math.atan2(yDist, xDist)) + 90;
            angle = angle <= 180 ? angle : angle - 360;

            telemetry.addData("angle1", angle);

            angle = angle - pos.h;

            telemetry.addData("angle2", angle);

            double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
            double error = distance;
            double derivative = (error - lastError) / timer.seconds();
            integralSum = integralSum + (error * timer.seconds());
            double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);
            lastError = error;

            double headingError = targetHeading - pos.h;
            headingError = headingError > 180 ? headingError - 360 : headingError < -180 ? headingError - 360 : headingError;
            /*if (headingError > 180) {
                headingError = headingError - 360;
            }
            if (headingError < -180) {
                headingError = headingError + 360;
            }

             */
            double headingDerivative = (headingError - headingLastError) / timer.seconds();
            headingIntegralSum = headingIntegralSum + (headingError * timer.seconds());
            double headingOut = (headingKp * headingError) + (headingKi * headingIntegralSum) + (headingKd * headingDerivative);
            headingLastError = headingError;
            telemetry.addData("distance", distance);
            telemetry.addData("output", out);
            packet.put("distance", distance);
            packet.put("output", out);
            telemetry.addData("headingError", headingError);
            telemetry.addData("headingOut", headingOut);
            packet.put("headingError", headingError);
            packet.put("headingOut", headingOut);
            dashboard.sendTelemetryPacket(packet);

            count += 1;
            sumTime += timer.seconds();

            telemetry.addData("time", sumTime / count);
            timer.reset();

            double power = Math.max(-1 / (out + 1) + 1, 0);
            power = Math.min(out,1);
            double x1 = Math.sin(Math.toRadians(angle)) * power;
            double y1 = -Math.cos(Math.toRadians(angle)) * power;
            double rx;
            if (turning) {
                rx = (1 + (Boolean.compare(headingError > 0, false) * -2)) * Math.min(Math.abs(headingOut),1); //-Math.sin(Math.toRadians(headingError)); //* Math.min(Math.abs(headingOut),1);
            } else {
                rx = 0;
            }
            telemetry.addData("x", x1);
            telemetry.addData("y", y1);
            telemetry.addData("rx", rx);

            double frontLeftPower;
            double backLeftPower;
            double frontRightPower;
            double backRightPower;
            telemetry.addData("fl", (y1 + x1 + rx) / multiplier);
            if (gamepad1.b) {
                frontLeftPower = (y1 + x1 + rx) / multiplier;
                telemetry.addData("fl", frontLeftPower);
                backLeftPower = (y1 - x1 + rx) / multiplier;
                frontRightPower = (y1 - x1 - rx) / multiplier;
                backRightPower = (y1 + x1 - rx) / multiplier;
            } else {
                frontLeftPower = 0;
                backLeftPower = 0;
                frontRightPower = 0;
                backRightPower = 0;
            }

            motorFrontLeft.setPower(frontLeftPower);
            motorBackLeft.setPower(backLeftPower);
            motorFrontRight.setPower(frontRightPower);
            motorBackRight.setPower(backRightPower);

            telemetry.update();
        }
    }

    private void configureOtos() {
        telemetry.addLine("Configuring OTOS...");
        telemetry.update();

        myOtos.setLinearUnit(SparkFunOTOS.LinearUnit.INCHES);
        myOtos.setAngularUnit(SparkFunOTOS.AngularUnit.DEGREES);
        SparkFunOTOS.Pose2D offset = new SparkFunOTOS.Pose2D(0, 0, 0);
        myOtos.setOffset(offset);
        myOtos.setLinearScalar(1.0);
        myOtos.setAngularScalar(1.0);
        myOtos.calibrateImu();
        myOtos.resetTracking();
        SparkFunOTOS.Pose2D currentPosition = new SparkFunOTOS.Pose2D(0, 0, 0);
        myOtos.setPosition(currentPosition);

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