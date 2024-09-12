package org.firstinspires.ftc.teamcode.Test.Drivebase;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
        setSparkFunOTOS("sensor_otos");
    }

    public void setSparkFunOTOS(String deviceName) {
        SparkFunOTOS sparkFunOTOS = (SparkFunOTOS) this.hardwareMap.get(SparkFunOTOS.class, deviceName);
        this.OTOS = sparkFunOTOS;
        sparkFunOTOS.setLinearUnit(SparkFunOTOS.LinearUnit.INCHES);
        this.OTOS.setAngularUnit(SparkFunOTOS.AngularUnit.DEGREES);
        this.OTOS.setLinearScalar(1.0d);
        this.OTOS.setAngularScalar(1.0d);
        this.OTOS.setOffset(new Pose2D(-3.75d, 0.0d, 0.0d));
        this.OTOS.calibrateImu();
        this.OTOS.resetTracking();
        this.OTOS.setPosition(new Pose2D(0.0d, 0.0d, 0.0d));
    }

    public void updatePosition () {this.currentPos.set(this.OTOS.getPosition());}

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

        Vector2D difference = this.currentPos.sub(this.targetPos);

        double angle = (Math.toDegrees(Math.atan2(difference.x, difference.y)) + 90) - this.currentPos.h;
        angle = angle <= 180 ? angle : angle - 360;

        double headingError = this.targetPos.h - this.currentPos.h;
        headingError = headingError > 180 ? headingError - 360 : headingError < -180 ? headingError - 360 : headingError;

        double out;
        if (this.disablePID) {
            out = this.positionPID.getOutput(difference.magnitude());
        } else {
            out = 1;
        }
        double headingOut = this.headingPID.getOutput(headingError);

        double power = Math.min(out, 1);
        double x = Math.sin(Math.toRadians(angle)) * power;
        double y = (-Math.cos(Math.toRadians(angle))) * power;
        double r = ((Boolean.compare(headingError > 0, false) * -2) + 1) * Math.min(Math.abs(headingOut), 1);
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

    public boolean atTarget () {
        return true;
    }

    public PathBuilder pathBuilder (Vector2D startPoint) {
        return new PathBuilder(this, startPoint);
    }

    public void mecanumDrive (double[] gamepadControls) {
        double x = gamepadControls[0];
        double y = -gamepadControls[1];
        double r = gamepadControls[2];

        runMotors(new double[] {
                y + x + r,
                y - x - r,
                y - x + r,
                y + x - r
        });
    }

    public void trueNorthDrive (double[] gamepadControls) {
        updatePosition();

        double x = gamepadControls[0];
        double y = -gamepadControls[1];
        double r = gamepadControls[2];

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