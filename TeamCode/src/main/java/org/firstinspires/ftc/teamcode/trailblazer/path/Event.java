package org.firstinspires.ftc.teamcode.trailblazer.path;

import org.fotmrobotics.trailblazer.Vector2D;

import java.util.concurrent.Callable;

public class Event {

    private int segment;
    private double t;

    private Callable<Boolean> callable;

    public Event(Integer segment, double t, Callable<Boolean> action) {
        this.segment = segment;
        this.t = t;
        this.callable = action;
    }

    public Event(Integer segment, double t, Runnable action) {
        this.segment = segment;
        this.t = t;
        this.callable = () -> {
            action.run();
            return true;
        };
    }

    public boolean call() throws Exception {
        return callable.call();
    }

    public int getSegment() {
        return segment;
    }

    public void setSegment(int segment) {
        this.segment = segment;
    }

    public double getnterpolation() {
        return t;
    }

    public void setInterpolation(double t) {
        this.t = t;
    }
}
