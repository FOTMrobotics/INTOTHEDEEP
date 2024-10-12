package org.firstinspires.ftc.teamcode.Test.Util;

/**
 * @author Preston Cokis
 */
public class PDFLcontrol {
    private double kP, kD, kF, kL;

    public PDFLcontrol (double p, double d, double f, double l) {
        this.kP = p;
        this.kD = d;
        this.kF = f;
        this.kL = l;
    }

    public void setValues(double p, double d, double f, double l) {
        this.kP = p;
        this.kD = d;
        this.kF = f;
        this.kL = l;
    }

    private double prevTime = 0;

    public double getTimeDifference () {
        double dt = (System.currentTimeMillis() / 1000.0) - prevTime;
        prevTime = System.currentTimeMillis() / 1000.0;
        return dt;
    }

    private double lastError = 0;

    public double update (double error) {
        double dt = getTimeDifference();
        lastError = error;

        return p(error) + d(error, dt) + f() + l(error);
    }

    private double p (double error) {
        return kP * error;
    }

    private double d (double error, double dt) {
        double derivative = (error - lastError) / dt;
        return kD * derivative;
    }

    private double f () {
        return kF;
    }

    private double l (double error) {
        double direction = Math.signum(error);
        return kL * direction;
    }
}
