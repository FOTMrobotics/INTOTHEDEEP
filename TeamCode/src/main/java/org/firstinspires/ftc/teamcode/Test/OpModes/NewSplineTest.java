package org.firstinspires.ftc.teamcode.Test.OpModes;

import static org.firstinspires.ftc.teamcode.Test.kotlin.SplineKt.curvature;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;
import org.firstinspires.ftc.teamcode.Test.kotlin.Spline;
import org.firstinspires.ftc.teamcode.Test.kotlin.Vector2D;

import java.util.ArrayList;


@TeleOp
public class NewSplineTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        ArrayList controlPoints = new ArrayList();
        controlPoints.add(new Vector2D(0,0));
        controlPoints.add(new Vector2D(48,48));
        controlPoints.add(new Vector2D(96,24));
        controlPoints.add(new Vector2D(100,100));

        Spline spline = new Spline(controlPoints);

        spline.setSegment(1);

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            drive.mecanumDrive(gamepad1);
            Pose2D pos = drive.getPosition();
            org.firstinspires.ftc.teamcode.Test.kotlin.Pose2D newPos = new org.firstinspires.ftc.teamcode.Test.kotlin.Pose2D(pos.x, pos.y, pos.h);

            ArrayList<Vector2D> testpoints = spline.getSegment();
            double t = spline.getClosestPoint(newPos);
            Double test = spline.getClosestPointTest(newPos);
            Vector2D splinePoint = spline.getPoint(t);
            Vector2D splineDeriv = spline.getDeriv(0.5);
            Vector2D splineDeriv2 = spline.getDeriv2(0.5);
            Double curvature = curvature(splineDeriv, splineDeriv2);
            Vector2D centripetal = splineDeriv.pow(2).times(curvature);

            telemetry.addLine("Current");
            telemetry.addData("X", newPos.getX());
            telemetry.addData("Y", newPos.getY());
            telemetry.addData("H", newPos.getH());

            telemetry.addLine("Test");
            telemetry.addData("Value", test);
            telemetry.addData("X", testpoints.get(3).getX());
            telemetry.addData("Y", testpoints.get(3).getY());


            telemetry.addLine("Closest Spline");
            telemetry.addData("X", splinePoint.getX());
            telemetry.addData("Y", splinePoint.getY());
            telemetry.addData("t", t);

            telemetry.addLine("Derivative");
            telemetry.addData("X", splineDeriv2.getX());
            telemetry.addData("Y", splineDeriv2.getY());

            telemetry.addLine("Centripetal");
            telemetry.addData("X", centripetal.getX());
            telemetry.addData("Y", centripetal.getY());

            telemetry.update();

            if (gamepad1.a) {

            }
        }
    }
}
