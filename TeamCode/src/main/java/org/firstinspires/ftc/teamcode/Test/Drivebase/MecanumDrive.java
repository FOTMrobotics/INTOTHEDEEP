package org.firstinspires.ftc.teamcode.Test.Drivebase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Subsystems.SparkFunOTOS;
import org.firstinspires.ftc.teamcode.Test.Path.PathBuilder;
import org.firstinspires.ftc.teamcode.Test.Util.PIDcontrol;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;
import org.firstinspires.ftc.teamcode.Test.Util.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Preston Cokis
 */
public class MecanumDrive {
    HardwareMap hardwareMap;

    private List<DcMotor> motors = new ArrayList();
    String[] motorNames = {
            "frontLeft",
            "frontRight",
            "backLeft",
            "backRight"
    };

    private SparkFunOTOS OTOS;

    private PIDcontrol positionPID = new PIDcontrol(0.125, 0, 0);
    private PIDcontrol headingPID = new PIDcontrol(0.05, 0, 0);
    public boolean disablePID = false;

    public Pose2D currentPos = new Pose2D();
    public Pose2D targetPos = new Pose2D();

    public MecanumDrive(HardwareMap hardwareMap) {
        this.hardwareMap = hardwareMap;
        setMotors(this.motorNames);
        this.motors.get(0).setDirection(DcMotorSimple.Direction.REVERSE);
        this.motors.get(2).setDirection(DcMotorSimple.Direction.REVERSE);
        setSparkFunOTOS("otos");
    }

    public void setSparkFunOTOS(String deviceName) {
        SparkFunOTOS sparkFunOTOS = (SparkFunOTOS) this.hardwareMap.get(SparkFunOTOS.class, deviceName);
        this.OTOS = sparkFunOTOS;
        sparkFunOTOS.setLinearUnit(SparkFunOTOS.LinearUnit.INCHES);
        this.OTOS.setAngularUnit(SparkFunOTOS.AngularUnit.DEGREES);
        this.OTOS.setLinearScalar(1.0d);
        this.OTOS.setAngularScalar(1.0d);
        this.OTOS.setOffset(new Pose2D(7.0d, 0.0d, 0.0d));
        this.OTOS.calibrateImu();
        this.OTOS.resetTracking();
        this.OTOS.setPosition(new Pose2D(0.0d, 0.0d, 0.0d));
    }

    public void resetSparkFunOTOS() {
        this.OTOS.calibrateImu();
        this.OTOS.resetTracking();
    }

    public void updatePosition () {this.currentPos.set(this.OTOS.getPosition());}

    public Pose2D getPosition () {
        updatePosition();
        return this.currentPos;
    }

    public void setMotors (String[] motorNames) {
        for (int i = 0; i <= 3; i++) {
            this.motors.add(this.hardwareMap.get(DcMotor.class, motorNames[i]));
        }
    }

    public void runMotors (double[] powers) {
        for (int i = 0; i <= 3; i++) {
            this.motors.get(i).setPower(powers[i]);
        }
    }

    public double[] powersToTarget () {
        updatePosition();

        Vector2D difference = currentPos.sub(targetPos);

        double angle = Math.toDegrees(Math.atan2(difference.y, difference.x)) + 90;
        angle = angle <= 180 ? angle : angle - 360;
        angle = angle - currentPos.h;

        double headingError = targetPos.h - currentPos.h;
        headingError = headingError > 180 ? headingError - 360 : headingError < -180 ? headingError - 360 : headingError;

        double out;
        if (!disablePID) {
            out = positionPID.getOutput(difference.magnitude());
        } else {
            out = 1;
        }
        double headingOut = headingPID.getOutput(headingError);

        double power = Math.min(out, 1);
        double x = Math.sin(Math.toRadians(angle)) * power;
        double y = -Math.cos(Math.toRadians(angle)) * power;
        double r = (1 + (Boolean.compare(headingError > 0, false) * -2)) * Math.min(Math.abs(headingOut),1);
        return new double[] {
                (y + x + r) * 1,
                (y - x - r) * 1,
                (y - x + r) * 1,
                (y + x - r) * 1
        };
    }

    public double[] powersToTarget (Pose2D target) {
        setTarget(target);
        return powersToTarget();
    }

    public void setTarget (Pose2D target) {this.targetPos.set(target);}

    public void toTarget () {runMotors(powersToTarget());}

    public void toTarget (Pose2D pose2d) {
        setTarget(pose2d);
        toTarget();
    }

    public int timesChecked;
    public Pose2D lastPos = new Pose2D();

    public boolean atTarget () {
        timesChecked = Math.sqrt(
                Math.pow(currentPos.x - lastPos.x, 2) +
                Math.pow(currentPos.y - lastPos.y, 2)
                ) < 0.01 && /*Math.sqrt(
                Math.pow(currentPos[0] - targetPos.x, 2) +
                Math.pow(currentPos[1] - targetPos.y, 2)
                ) < 1 &&*/
                Math.abs(currentPos.h - lastPos.h) < 0.01 ?
                timesChecked + 1 : 0;
        lastPos.set(currentPos);
        return timesChecked > 5;
    }

    public PathBuilder pathBuilder (Vector2D startPoint) {
        return new PathBuilder(this, startPoint);
    }

    public void mecanumDrive (Gamepad gamepad) {
        double x = gamepad.left_stick_x;
        double y = -gamepad.left_stick_y;
        double r = gamepad.right_stick_x;

        // kaden's funky control scheme
        //double x = -gamepad.left_stick_x;
        //double y = gamepad.right_stick_y;
        //double r = gamepad.right_stick_x;

        runMotors(new double[] {
                y + x + r,
                y - x - r,
                y - x + r,
                y + x - r
        });
    }

    public void trueNorthDrive (Gamepad gamepad) {
        updatePosition();

        double x = gamepad.left_stick_x;
        double y = -gamepad.left_stick_y;
        double r = gamepad.right_stick_x;

        double power = Math.sqrt(Math.pow(x, 2.0d) + Math.pow(y, 2.0d));

        double angle = Math.toDegrees(Math.atan2(y, x)) + 90.0d;
        angle = (angle <= 180 ? angle : angle - 360) - this.currentPos.h;
        x = Math.sin(Math.toRadians(angle)) * power;
        y = (-Math.cos(Math.toRadians(angle))) * power;

        runMotors(new double[] {
                y + x + r,
                y - x - r,
                y - x + r,
                y + x - r
        });
    }
}