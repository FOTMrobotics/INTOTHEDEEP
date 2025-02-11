package org.firstinspires.ftc.teamcode.Test.Util;

// Change to pdfl control?

/**
 * @author Preston Cokis
 */
public class PIDcontrol {
    private double Kd, Ki, Kp;

    public PIDcontrol (double p, double i, double d) {
        this.Kp = p;
        this.Ki = i;
        this.Kd = d;
    }

    public void setPID (double p, double i, double d) {
        this.Kp = p;
        this.Ki = i;
        this.Kd = d;
    }

    private double prevTime = 0;

    public double getTimeDifference () {
        double dt = (System.currentTimeMillis() / 1000.0) - prevTime;
        prevTime = System.currentTimeMillis() / 1000.0;
        return dt;
    }

    private double integralSum = 0;
    private double lastError = 0;

    public double getOutput (double error) {
        double dt = getTimeDifference();
        double derivative = (error - lastError) / dt;
        integralSum += error * dt;
        lastError = error;
        return (this.Kp * error) + (this.Ki * integralSum) + (this.Kd * derivative);
    }
}