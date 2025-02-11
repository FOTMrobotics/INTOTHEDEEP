package org.firstinspires.ftc.teamcode.trailblazer.path;

import static org.fotmrobotics.trailblazer.PathKt.driveVector;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;
import org.fotmrobotics.trailblazer.MathKt;
import org.fotmrobotics.trailblazer.PIDF;
import org.fotmrobotics.trailblazer.Pose2D;
import org.fotmrobotics.trailblazer.Spline;
import org.fotmrobotics.trailblazer.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

public class Path {
    private LinearOpMode opMode = new LinearOpMode() {
        @Override
        public void runOpMode() throws InterruptedException {

        }
    };

    Drive drive;
    Spline spline = new Spline();

    PIDF translationPIDF = new PIDF(0.5, 0, 0, 0);

    enum State {
        CONTINUE,
        PAUSE,
        STOP,
        HEADING_FOLLOW,
        HEADING_OFFSET,
        HEADING_CONSTANT
    }

    ArrayList<Event> events = new ArrayList<>();
    HashMap<Event, EventType> eventType = new HashMap<>();

    enum EventType {
        SEQUENTIAL,
        PARALLEL
    }

    State headingState = State.HEADING_FOLLOW;
    double headingValue = 0;
    double headingTarget;

    State pathState = State.CONTINUE;
    Pose2D targetPose = new Pose2D(0,0,0);

    public Path() {}

    public void finalize(Drive drive, Spline spline) {
        this.drive = drive;
        this.spline = spline;
    }

    public void run() {
        pathState = State.CONTINUE;

        ArrayList<Event> tempEvents = new ArrayList<>(events);

        ArrayList<Event> sequentialQueue = new ArrayList<>();
        ArrayList<Event> parallelQueue = new ArrayList<>();

        while (pathState != State.STOP) {
            if (opMode.isStopRequested()) pathState = State.STOP;

            Pose2D pos = drive.odometry.getPosition();

            Pose2D driveVector = driveVector(spline, pos, translationPIDF);

            double t = spline.getClosestPoint(pos);

            ArrayList<Event> removeEvents = new ArrayList<>();
            for (Event event : tempEvents) {
                if (event.getnterpolation() <= t && event.getSegment() == spline.getSegment()) {
                    switch(eventType.get(event)) {
                        case PARALLEL:
                            parallelQueue.add(event);
                        case SEQUENTIAL:
                            sequentialQueue.add(event);
                    }
                    removeEvents.add(event);
                }
            }
            tempEvents.removeAll(removeEvents);

            ArrayList<Event> removeQueue = new ArrayList<>();
            for (Event event : parallelQueue) {
                try {
                    boolean condition = event.call();

                    if (condition) removeQueue.add(event);
                }

                catch (Exception e) {throw new RuntimeException(e);}
            }
            parallelQueue.removeAll(removeQueue);

            if (!sequentialQueue.isEmpty()) {
                try {
                    if (sequentialQueue.get(0).call()) {
                        sequentialQueue.remove(0);
                    }
                }

                catch (Exception e) {throw new RuntimeException(e);}
            }

            switch (headingState) {
                case HEADING_FOLLOW:
                    headingTarget = MathKt.angleWrap(Math.toDegrees(Math.atan2(driveVector.getY(), driveVector.getX())) - 90);
                    break;
                case HEADING_OFFSET:
                    headingTarget = MathKt.angleWrap(Math.toDegrees(Math.atan2(driveVector.getY(), driveVector.getX())) - 90 + headingValue);
                    break;
                case HEADING_CONSTANT:
                    headingTarget = headingValue;
                    break;
            }

            switch (pathState) {
                case CONTINUE:
                    drive.moveVector(new Pose2D(driveVector.getX(), driveVector.getY(), headingTarget), true);

                    targetPose.set(new Pose2D(pos.getX(), pos.getY(), headingTarget));

                    if (t > 0.85 && spline.getLength() - 4 == spline.getSegment()) {
                        pathState = State.PAUSE;

                        Vector2D endPt = spline.getPt(spline.getLength() - 1);
                        targetPose.set(new Pose2D(endPt.getX(), endPt.getY(), headingTarget));
                    }

                    if (t >= 1) {
                        spline.incSegment();
                    }

                    break;
                case PAUSE:
                    drive.movePoint(targetPose);

                    break;
            }
        }
    }
}
