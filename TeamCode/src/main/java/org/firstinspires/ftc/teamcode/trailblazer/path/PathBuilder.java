package org.firstinspires.ftc.teamcode.trailblazer.path;


import org.firstinspires.ftc.teamcode.trailblazer.drivebase.Drive;
import org.fotmrobotics.trailblazer.Pose2D;
import org.fotmrobotics.trailblazer.Spline;
import org.fotmrobotics.trailblazer.Vector2D;

import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * @author Preston Cokis
 */
public class PathBuilder {
    private Path path;

    private Drive drive;

    Spline spline = new Spline(new ArrayList<>());
    ArrayList<Object> eventQueue = new ArrayList<>();
    int n = 0;

    public PathBuilder(Drive drive, Vector2D start) {
        this.path = new Path();
        this.drive = drive;
        spline.addPt(start);
        spline.addPt(start);
        ++n;
    }

    public PathBuilder(Drive mecanumDrive) {
        this.drive = mecanumDrive;
    }

    public PathBuilder pt(Vector2D point) {
        spline.addPt(point);
        ++n;
        return this;
    }

    public PathBuilder headingFollow() {
        event(() -> {path.headingState = Path.State.HEADING_FOLLOW;});
        return this;
    }

    public PathBuilder headingFollow(Path.EventType eventType) {
        event(() -> {path.headingState = Path.State.HEADING_FOLLOW;}, eventType);
        return this;
    }

    public PathBuilder headingFollow(double t) {
        event(t, () -> {path.headingState = Path.State.HEADING_FOLLOW;});
        return this;
    }

    public PathBuilder headingFollow(double t, Path.EventType eventType) {
        event(t, () -> {path.headingState = Path.State.HEADING_FOLLOW;}, eventType);
        return this;
    }

    public PathBuilder headingConstant(double angle) {
        event(() -> {
            path.headingState = Path.State.HEADING_CONSTANT;
            path.headingValue = angle;
        });
        return this;
    }

    public PathBuilder headingConstant(double angle, Path.EventType eventType) {
        event(() -> {
            path.headingState = Path.State.HEADING_CONSTANT;
            path.headingValue = angle;
        }, eventType);
        return this;
    }

    public PathBuilder headingConstant(double t, double angle) {
        event(t, () -> {
            path.headingState = Path.State.HEADING_CONSTANT;
            path.headingValue = angle;
        });
        return this;
    }

    public PathBuilder headingConstant(double t, double angle, Path.EventType eventType) {
        event(t, () -> {
            path.headingState = Path.State.HEADING_CONSTANT;
            path.headingValue = angle;
        }, eventType);
        return this;
    }

    public PathBuilder headingOffset(double angle) {
        event(() -> {
            path.headingState = Path.State.HEADING_OFFSET;
            path.headingValue = angle;
        });
        return this;
    }

    public PathBuilder headingOffset(double angle, Path.EventType eventType) {
        event(() -> {
            path.headingState = Path.State.HEADING_OFFSET;
            path.headingValue = angle;
        }, eventType);
        return this;
    }

    public PathBuilder headingOffset(double t, double angle) {
        event(t, () -> {
            path.headingState = Path.State.HEADING_OFFSET;
            path.headingValue = angle;
        });
        return this;
    }

    public PathBuilder headingOffset(double t, double angle, Path.EventType eventType) {
        event(t, () -> {
            path.headingState = Path.State.HEADING_OFFSET;
            path.headingValue = angle;
        }, eventType);
        return this;
    }

    public PathBuilder pause(Callable<Boolean> condition) {
        event(() -> {
            path.pathState = Path.State.PAUSE;
            try {
                if (condition.call()) {
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        });
        return this;
    }

    public PathBuilder pause(Callable<Boolean> condition, Path.EventType eventType) {
        event(() -> {
            path.pathState = Path.State.PAUSE;
            try {
                if (condition.call()) {
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        }, eventType);
        return this;
    }

    public PathBuilder pause(double t, Callable<Boolean> condition) {
        event(t, () -> {
            path.pathState = Path.State.PAUSE;
            try {
                if (condition.call()) {
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        });
        return this;
    }

    public PathBuilder pause(double t, Callable<Boolean> condition, Path.EventType eventType) {
        event(t, () -> {
            path.pathState = Path.State.PAUSE;
            try {
                if (condition.call()) {
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        }, eventType);
        return this;
    }

    public PathBuilder toPose(Pose2D pose, Callable<Boolean> condition) {
        event(() -> {
            path.pathState = Path.State.PAUSE;
            path.targetPose = pose;
            try {
                if (condition.call()) {
                    path.targetPose = pose;
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        });
        return this;
    }

    public PathBuilder toPose(Pose2D pose, Callable<Boolean> condition, Path.EventType eventType) {
        event(() -> {
            path.pathState = Path.State.PAUSE;
            path.targetPose = pose;
            try {
                if (condition.call()) {
                    path.targetPose = pose;
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        }, eventType);
        return this;
    }

    public PathBuilder toPose(double t, Pose2D pose, Callable<Boolean> condition) {
        event(t, () -> {
            path.pathState = Path.State.PAUSE;
            path.targetPose = pose;
            try {
                if (condition.call()) {
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        });
        return this;
    }

    public PathBuilder toPose(double t, Pose2D pose, Callable<Boolean> condition, Path.EventType eventType) {
        event(t, () -> {
            path.pathState = Path.State.PAUSE;
            path.targetPose = pose;
            try {
                if (condition.call()) {
                    path.pathState = Path.State.CONTINUE;
                    return true;
                }
                return false;
            }
            catch (Exception e) {throw new RuntimeException(e);}
        }, eventType);
        return this;
    }

    public PathBuilder xScale(double scale) {
        event(() -> {drive.xScale = scale;});
        return this;
    }

    public PathBuilder xScale(double scale, Path.EventType eventType) {
        event(() -> {drive.xScale = scale;}, eventType);
        return this;
    }

    public PathBuilder xScale(Double t, double scale) {
        event(t, () -> {drive.xScale = scale;});
        return this;
    }

    public PathBuilder xScale(Double t, double scale, Path.EventType eventType) {
        event(t, () -> {drive.xScale = scale;}, eventType);
        return this;
    }

    public PathBuilder yScale(double scale) {
        event(() -> {drive.yScale = scale;});
        return this;
    }

    public PathBuilder yScale(double scale, Path.EventType eventType) {
        event(() -> {drive.yScale = scale;}, eventType);
        return this;
    }

    public PathBuilder yScale(Double t, double scale) {
        event(t, () -> {drive.yScale = scale;});
        return this;
    }

    public PathBuilder yScale(Double t, double scale, Path.EventType eventType) {
        event(t, () -> {drive.yScale = scale;}, eventType);
        return this;
    }

    public PathBuilder translationalScale(double scale) {
        event(() -> {
            drive.xScale = scale;
            drive.yScale = scale;
        });
        return this;
    }

    public PathBuilder translationalScale(double scale, Path.EventType eventType) {
        event(() -> {
            drive.xScale = scale;
            drive.yScale = scale;
        }, eventType);
        return this;
    }

    public PathBuilder translationalScale(Double t, double scale) {
        event(t, () -> {
            drive.xScale = scale;
            drive.yScale = scale;
        });
        return this;
    }

    public PathBuilder translationalScale(Double t, double scale, Path.EventType eventType) {
        event(t, () -> {
            drive.xScale = scale;
            drive.yScale = scale;
        }, eventType);
        return this;
    }

    public PathBuilder angularScale(double scale) {
        event(() -> {drive.angularScale = scale;});
        return this;
    }

    public PathBuilder angularScale(double scale, Path.EventType eventType) {
        event(() -> {drive.angularScale = scale;}, eventType);
        return this;
    }

    public PathBuilder angularScale(Double t, double scale) {
        event(t, () -> {drive.angularScale = scale;});
        return this;
    }

    public PathBuilder angularScale(Double t, double scale, Path.EventType eventType) {
        event(t, () -> {drive.angularScale = scale;}, eventType);
        return this;
    }

    public PathBuilder action(Callable<Boolean> action) {
        event(action);
        return this;
    }

    public PathBuilder action(Callable<Boolean> action, Path.EventType eventType) {
        event(action, eventType);
        return this;
    }

    public PathBuilder action(Runnable action) {
        event(action);
        return this;
    }

    public PathBuilder action(Runnable action, Path.EventType eventType) {
        event(action, eventType);
        return this;
    }

    public PathBuilder action(Double t, Callable<Boolean> action) {
        event(t, action);
        return this;
    }

    public PathBuilder action(Double t, Callable<Boolean> action, Path.EventType eventType) {
        event(t, action, eventType);
        return this;
    }

    public PathBuilder action(Double t, Runnable action) {
        event(t, action);
        return this;
    }

    public PathBuilder action(Double t, Runnable action, Path.EventType eventType) {
        event(t, action, eventType);
        return this;
    }

    private void event(Callable<Boolean> event) {
        eventQueue.add(new Event(Integer.MIN_VALUE, 0, event));
        eventQueue.add(n);
        eventQueue.add(Path.EventType.PARALLEL);
    }

    private void event(Callable<Boolean> event, Path.EventType eventType) {
        eventQueue.add(new Event(Integer.MIN_VALUE, 0, event));
        eventQueue.add(n);
        eventQueue.add(eventType);
    }

    private void event(Runnable event) {
        eventQueue.add(new Event(Integer.MIN_VALUE, 0, event));
        eventQueue.add(n);
        eventQueue.add(Path.EventType.PARALLEL);
    }

    private void event(Runnable event, Path.EventType eventType) {
        eventQueue.add(new Event(Integer.MIN_VALUE, 0, event));
        eventQueue.add(n);
        eventQueue.add(eventType);
    }

    public void event(Double t, Callable<Boolean> event) {
        eventQueue.add(new Event(Integer.MIN_VALUE, t, event));
        eventQueue.add(n);
        eventQueue.add(Path.EventType.PARALLEL);
    }

    public void event(Double t, Callable<Boolean> event, Path.EventType eventType) {
        eventQueue.add(new Event(Integer.MIN_VALUE, t, event));
        eventQueue.add(n);
        eventQueue.add(eventType);
    }

    public void event(Double t, Runnable event) {
        eventQueue.add(new Event(Integer.MIN_VALUE, t, event));
        eventQueue.add(n);
        eventQueue.add(Path.EventType.PARALLEL);
    }

    public void event(Double t, Runnable event, Path.EventType eventType) {
        eventQueue.add(new Event(Integer.MIN_VALUE, t, event));
        eventQueue.add(n);
        eventQueue.add(eventType);
    }

    public Path build() {
        Vector2D endPt = spline.getPt(spline.getLength()-1);
        spline.addPt(endPt);

        event(0.85, () -> {
            if (drive.atTarget()) path.pathState = Path.State.STOP;

            return false;
        });

        while (!eventQueue.isEmpty()) {
            Event event = (Event) eventQueue.get(0);

            int segment = Math.min(Math.max(0, (int) eventQueue.get(1) - 1), spline.getLength() - 4);
            event.setSegment(segment);
            path.events.add(event);

            path.eventType.put(event, (Path.EventType) eventQueue.get(2));

            eventQueue.subList(0, 3).clear();
        }

        path.finalize(drive, spline);

        return path;
    }
}