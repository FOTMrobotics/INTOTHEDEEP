package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;
import org.firstinspires.ftc.teamcode.Util.PIDcontrol;
import org.firstinspires.ftc.teamcode.Util.Pose2d;

@Config
public class Drivebase {
    FtcDashboard dashboard = FtcDashboard.getInstance();
    private final DcMotor motorFrontLeft;
    private final DcMotor motorFrontRight;
    private final DcMotor motorBackLeft;
    private final DcMotor motorBackRight;
    private final SparkFunOTOS myOtos;
    public ElapsedTime timer = new ElapsedTime();
    public double velocity;
    private int iterations;
    public boolean disablePID = false;
    public double[] motorPowers;
    public Pose2d currentPos = new Pose2d(0,0,0);
    public Pose2d targetPos = new Pose2d();
    private int timesChecked = 0;
    private final Pose2d lastPos = new Pose2d(0,0,0);

    // PID values
    private final PIDcontrol positionPID;
    public static double Kp = 0.125;
    public static double Ki = 0;
    public static double Kd = 0; //0.00005
    private final PIDcontrol headingPID;
    public static double headingKp = 0.05; //0.1
    public static double headingKi = 0;
    public static double headingKd = 0;

    // Constraints
    public static double maxSpeed = 50; // in/s (Placeholder speed)
    public static double speed = 20; // Speed it should travel at

    public double t;

    public Drivebase(HardwareMap hardwareMap) {
        motorFrontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        motorFrontRight = hardwareMap.get(DcMotor.class, "frontRight");
        motorBackLeft = hardwareMap.get(DcMotor.class, "backLeft");
        motorBackRight = hardwareMap.get(DcMotor.class, "backRight");

        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        // Configuring sparkfunOTOS
        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        myOtos.setLinearUnit(SparkFunOTOS.LinearUnit.INCHES);
        myOtos.setAngularUnit(SparkFunOTOS.AngularUnit.DEGREES);
        myOtos.setOffset(new Pose2D(-3.75, 0, 0));
        myOtos.setLinearScalar(1.0);
        myOtos.setAngularScalar(1.0);
        myOtos.calibrateImu();
        myOtos.resetTracking();
        Pose2D currentPosition = new Pose2D(0, 0, 0);
        myOtos.setPosition(currentPosition);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        positionPID = new PIDcontrol(Kp, Ki, Kd);
        headingPID = new PIDcontrol(headingKp, headingKi, headingKd);
    }

    public void update() {
        Pose2D pos = myOtos.getPosition();
        currentPos.change(pos.x, pos.y, pos.h);
        velocity = Math.sqrt(Math.pow(currentPos.x - lastPos.x, 2) + Math.pow(currentPos.y - lastPos.y, 2)) / ((double) Math.round(timer.seconds() * 100) / 100);
        TelemetryPacket packet = new TelemetryPacket();
        iterations += 1;
        if (iterations == 15) {
            packet.put("velocity", velocity);
            packet.put("time", timer.seconds());
            packet.put("t", t);
            iterations = 0;
        }
        dashboard.sendTelemetryPacket(packet);
        timer.reset();
    }

    public double[] getPos() {
        Pose2D pos = myOtos.getPosition();
        return new double[] {pos.x, pos.y, pos.h};
    }

    public void runMotors(double[] motorPowers) {
        motorFrontLeft.setPower(motorPowers[0]);
        motorBackLeft.setPower(motorPowers[1]);
        motorFrontRight.setPower(motorPowers[2]);
        motorBackRight.setPower(motorPowers[3]);
    }

    public double[] calculatePowers(Pose2d currentPos, Pose2d targetPos) {
        double xDist = currentPos.x - targetPos.x;
        double yDist = currentPos.y - targetPos.y;

        double angle = Math.toDegrees(Math.atan2(yDist, xDist)) + 90;
        angle = angle <= 180 ? angle : angle - 360;
        angle = angle - currentPos.h;

        double headingError = targetPos.h - currentPos.h;
        headingError = headingError > 180 ? headingError - 360 : headingError < -180 ? headingError - 360 : headingError;

        double out = positionPID.update(Kp, Ki, Kd, Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2)), timer.time());
        double headingOut = headingPID.update(headingKp, headingKi, headingKd,targetPos.h - currentPos.h, timer.seconds());

        if (disablePID) {
            out = 1;
        }

        update();

        double power = Math.min(out,1);
        double x = Math.sin(Math.toRadians(angle)) * power;
        double y = -Math.cos(Math.toRadians(angle)) * power;
        double r = (1 + (Boolean.compare(headingError > 0, false) * -2)) * Math.min(Math.abs(headingOut),1);

        double multiplier = speed / maxSpeed;
        return new double[] {
                (y + x + r) * multiplier,
                (y - x + r) * multiplier,
                (y - x - r) * multiplier,
                (y + x - r) * multiplier
        };
    }

    //update so they dont need to be called
    public boolean reachedEnd(Pose2d currentPos, Pose2d targetPos) {
        timesChecked = Math.sqrt(
                Math.pow(currentPos.x - lastPos.x, 2) +
                Math.pow(currentPos.y - lastPos.y, 2)
                ) < 0.01 && /*Math.sqrt(
                Math.pow(currentPos[0] - targetPos.x, 2) +
                Math.pow(currentPos[1] - targetPos.y, 2)
                ) < 1 &&*/
                Math.abs(currentPos.h - lastPos.h) < 0.01 ?
                timesChecked + 1 : 0;
        lastPos.change(currentPos);
        return timesChecked > 5;
    }

    public boolean reachedEnd() {
        timesChecked = currentPos.distance(lastPos) < 0.01 &&
                //Math.sqrt(Math.pow(currentPos.x - lastPos.x, 2) + Math.pow(currentPos.y - lastPos.y, 2)) < 0.01 &&
                /*Math.sqrt(
                Math.pow(currentPos[0] - targetPos.x, 2) +
                Math.pow(currentPos[1] - targetPos.y, 2)
                ) < 1 &&*/
                Math.abs(currentPos.h - lastPos.h) < 0.01 ?
                timesChecked + 1 : 0;
        lastPos.change(currentPos);
        return timesChecked > 5;
    }

    public boolean toPosition(Pose2d targetPos) {
        setTarget(targetPos);

        return toPosition();
        //double[] motorPowers = calculatePowers(currentPos, targetPos);

        //runMotors(motorPowers);

        //return reachedEnd(currentPos, targetPos);
    }

    public boolean toPosition() {
        double[] motorPowers = calculatePowers(currentPos, targetPos);

        runMotors(motorPowers);

        return reachedEnd(currentPos, targetPos);
    }

    public void setTarget(Pose2d target) {
        targetPos.change(target);
    }
}

