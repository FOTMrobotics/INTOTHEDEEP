package org.firstinspires.ftc.teamcode.Subsystems;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.SparkFunOTOS.SparkFunOTOS;
import org.firstinspires.ftc.teamcode.Tools.PIDcontrol;
import org.firstinspires.ftc.teamcode.Tools.Point;

@Config
public class Drivebase {
    private DcMotor motorFrontLeft;
    private DcMotor motorFrontRight;
    private DcMotor motorBackLeft;
    private DcMotor motorBackRight;
    private SparkFunOTOS myOtos;
    public ElapsedTime timer = new ElapsedTime();
    private PIDcontrol positionPID;
    private PIDcontrol headingPID;
    public boolean disablePID = false;
    public double[] motorPowers;
    public Point targetPos;

    // PID values
    public static double Kp = 0.125;
    public static double Ki = 0;
    public static double Kd = 0; //0.00005
    public static double headingKp = 0.05; //0.1
    public static double headingKi = 0;
    public static double headingKd = 0;

    // Constraints
    public static double maxSpeed = 50; // in/s
    public static double speed = 30; // speed it should travel at

    private int timesChecked = 0;

    public Drivebase(HardwareMap hardwareMap) {
        motorFrontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
        motorFrontRight = hardwareMap.get(DcMotor.class, "frontRight");
        motorBackLeft = hardwareMap.get(DcMotor.class, "backLeft");
        motorBackRight = hardwareMap.get(DcMotor.class, "backRight");

        motorBackLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        motorFrontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        myOtos = hardwareMap.get(SparkFunOTOS.class, "sensor_otos");
        myOtos.setLinearUnit(SparkFunOTOS.LinearUnit.INCHES);
        myOtos.setAngularUnit(SparkFunOTOS.AngularUnit.DEGREES);
        myOtos.setOffset(new SparkFunOTOS.Pose2D(-3.75, 0, 0));
        myOtos.setLinearScalar(1.0);
        myOtos.setAngularScalar(1.0);
        myOtos.calibrateImu();
        myOtos.resetTracking();
        SparkFunOTOS.Pose2D currentPosition = new SparkFunOTOS.Pose2D(0, 0, 0);
        myOtos.setPosition(currentPosition);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        dashboard.setTelemetryTransmissionInterval(25);

        //ElapsedTime timer = new ElapsedTime();

        positionPID = new PIDcontrol(Kp, Ki, Kd);
        headingPID = new PIDcontrol(headingKp, headingKi, headingKd);
    }

    public void update() {
        timer.reset();
    }

    public double[] getPos() {
        SparkFunOTOS.Pose2D pos = myOtos.getPosition();
        return new double[] {pos.x, pos.y, pos.h};
    }

    public void runMotors(double[] motorPowers) {
        motorFrontLeft.setPower(motorPowers[0]);
        motorBackLeft.setPower(motorPowers[1]);
        motorFrontRight.setPower(motorPowers[2]);
        motorBackRight.setPower(motorPowers[3]);
    }

    public double[] calculatePowers(double[] currentPos, Point targetPos) {
        double xDist = currentPos[0] - targetPos.x;
        double yDist = currentPos[1] - targetPos.y;
        double distance = Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));

        double angle = Math.toDegrees(Math.atan2(yDist, xDist)) + 90;
        angle = angle <= 180 ? angle : angle - 360;
        angle = angle - currentPos[2];

        double headingError = targetPos.h - currentPos[2];
        headingError = headingError > 180 ? headingError - 360 : headingError < -180 ? headingError - 360 : headingError;

        double out = positionPID.update(Kp, Ki, Kd, distance, timer.time());
        double headingOut = headingPID.update(headingKp, headingKi, headingKd,targetPos.h - currentPos[2], timer.seconds());

        if (disablePID) {
            out = 1;
        }

        double power = Math.min(out,1);
        double x = Math.sin(Math.toRadians(angle)) * power;
        double y = -Math.cos(Math.toRadians(angle)) * power;
        double r = (1 + (Boolean.compare(headingError > 0, false) * -2)) * Math.min(Math.abs(headingOut),1);

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1);
        double multiplier = speed / maxSpeed;
        return new double[] {
                ((y + x + r) / denominator) * multiplier,
                ((y - x + r) / denominator) * multiplier,
                ((y - x - r) / denominator) * multiplier,
                ((y + x - r) / denominator) * multiplier
        };
    }

    public boolean reachedEnd(double[] currentPos, Point targetPos) {
        timesChecked = Math.sqrt(
                Math.pow(currentPos[0] - targetPos.x, 2) +
                Math.pow(currentPos[1] - targetPos.y, 2)
                ) < 0.5 ?
                timesChecked + 1 : 0;
        return timesChecked > 10;
    }

    public boolean toPosition(Point targetPos) {
        this.targetPos = targetPos;

        double[] currentPos = getPos();
        double[] motorPowers = calculatePowers(currentPos, targetPos);
        runMotors(motorPowers);
        return reachedEnd(currentPos, targetPos);
    }
}

