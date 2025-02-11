package org.firstinspires.ftc.teamcode.Test.Path;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Preston Cokis
 */
public class PathBuilder {
    private MecanumDrive mecanumDrive;
    public List<PathSegment> pathSegments = new ArrayList<>();

    public PathBuilder(MecanumDrive mecanumDrive, Vector2D startPoint) {
        this.mecanumDrive = mecanumDrive;
        pathSegments.add(new PathSegment(startPoint));
    }

    public PathBuilder pt(Vector2D point) {
        pathSegments.add(new PathSegment(point));
        return this;
    }

    public PathBuilder pt(double x, double y) {
        pathSegments.add(new PathSegment(new Vector2D(x, y)));
        return this;
    }

    public PathBuilder headingFollow() {
        pathSegments.add(new PathSegment(PathSegment.segmentType.headingFollow));
        return this;
    }

    public PathBuilder headingConstant(double heading) {
        pathSegments.add(new PathSegment(heading));
        return this;
    }

    public PathBuilder headingLinear(double heading, double t1, double t2) {
        return this;
    }

    public PathBuilder action(Runnable action) {
        pathSegments.add(new PathSegment(action));
        return this;
    }

    public PathBuilder actionBetweenPoints(Runnable action, double t) {
        return this;
    }

    public PathBuilder actionAtPt(Runnable action, Vector2D point) {
        return this;
    }

    public PathBuilder actionAtPt(Runnable action, Vector2D point, double radius) {
        return this;
    }

    public PathBuilder setFollowRadius(double radius) {
        return this;
    }

    public PathBuilder setSpeed(double speed) {
        return this;
    }

    public PathBuilder wait(int milliseconds) {
        return this;
    }

    /**
     * Organizes the information to be used in the Path class.
     *
     * @return An instance of the path class.
     *
     * @author Preston Cokis
     */
    public Path build() {
        List<Vector2D> controlPoints = new ArrayList<>();

        for (PathSegment pathSegment : pathSegments) {
            switch (pathSegment.type) {
                case point:
                    controlPoints.add(pathSegment.point);
                    break;

                case headingFollow:
                    break;
            }
        }
        controlPoints.add(controlPoints.get(controlPoints.size() - 1));

        List<Vector2D> pathPoints = catmullRomChain(controlPoints);

        return new Path(mecanumDrive, pathPoints);
    }

    public List<Vector2D> catmullRomSpline (Vector2D p0, Vector2D p1, Vector2D p2, Vector2D p3, double numPoints) {
        double increment = 1 / numPoints;
        List<Vector2D> points = new ArrayList<>();
        for (double t = 0; t <= 1; t += increment)
        {
            double t2 = t*t;
            double t3 = t2*t;
            Vector2D a = p1.scale(2);
            Vector2D b = (p0.scale(-1).add(p2)).scale(t);
            Vector2D c = (p0.scale(2).sub(p1.scale(5)).add(p2.scale(4)).sub(p3)).scale(t2);
            Vector2D d = (p0.scale(-1).add(p1.scale(3)).sub(p2.scale(3)).add(p3)).scale(t3);
            Vector2D point = (a.add(b).add(c).add(d)).scale(0.5);
            points.add(point);
        }

        return points;
    }

    public List<Vector2D> catmullRomChain (List<Vector2D> points) {
        double pointsPerSegment = 10;

        List<Vector2D> splinePoints = new ArrayList<>();
        for (int i = 0 ; i <= points.size() - 4; i++)
        {
            Vector2D p0 = points.get(i);
            Vector2D p1 = points.get(i+1);
            Vector2D p2 = points.get(i+2);
            Vector2D p3 = points.get(i+3);
            splinePoints.addAll(catmullRomSpline(p0,p1,p2,p3,pointsPerSegment));
        }

        return splinePoints;
    }
}

