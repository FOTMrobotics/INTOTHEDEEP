package org.firstinspires.ftc.teamcode.Tools;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDcontrol {
    private double Kp;
    private double Ki;
    private double Kd;
    private double lastError;
    private double integralSum;

    public PIDcontrol(double p, double i, double d) {
        this.Kp = p;
        this.Ki = i;
        this.Kd = d;
        this.integralSum = 0;
        this.lastError = 0;
    }

    public double update(double p, double i, double d, double error, double timePassed) {
        this.Kp = p;
        this.Ki = i;
        this.Kd = d;
        double derivative = (error - lastError) / timePassed;
        integralSum = integralSum + (error * timePassed);
        double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);
        return out;
    }
}
