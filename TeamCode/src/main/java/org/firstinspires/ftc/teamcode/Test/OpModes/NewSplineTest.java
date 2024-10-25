package org.firstinspires.ftc.teamcode.Test.OpModes;

import static org.firstinspires.ftc.teamcode.Test.kotlin.PathKt.driveVector;
import static org.firstinspires.ftc.teamcode.Test.kotlin.SplineKt.curvature;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Util.Pose2D;
import org.firstinspires.ftc.teamcode.Test.kotlin.PIDF;
import org.firstinspires.ftc.teamcode.Test.kotlin.Spline;
import org.firstinspires.ftc.teamcode.Test.kotlin.Vector2D;

import java.util.ArrayList;


@TeleOp
public class NewSplineTest extends LinearOpMode {

    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        ArrayList controlPoints = new ArrayList();
        controlPoints.add(new Vector2D(0,0));
        controlPoints.add(new Vector2D(-48,48));
        controlPoints.add(new Vector2D(-96,24));
        controlPoints.add(new Vector2D(100,100));

        Spline spline = new Spline(controlPoints);

        PIDF pidf = new PIDF (0.5,0, 0, 0);

        spline.setSegment(1);

        waitForStart();

        if (isStopRequested()) {return;}

        while (opModeIsActive()) {
            Pose2D pos = drive.getPosition();
            org.firstinspires.ftc.teamcode.Test.kotlin.Pose2D newPos = new org.firstinspires.ftc.teamcode.Test.kotlin.Pose2D(pos.x, pos.y, pos.h);

            ArrayList<Vector2D> testpoints = spline.getSegment();
            double t = spline.getClosestPoint(newPos);
            Double test = spline.getClosestPointTest(newPos);
            Vector2D splinePoint = spline.getPoint(t);
            Vector2D splineDeriv = spline.getDeriv(t);
            Vector2D splineDeriv2 = spline.getDeriv2(t);

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
            telemetry.addData("X", splineDeriv.getX());
            telemetry.addData("Y", splineDeriv.getY());

            telemetry.update();

            if (gamepad1.a) {
                org.firstinspires.ftc.teamcode.Test.kotlin.Pose2D driveVector = driveVector(spline, newPos, pidf);
                drive.toTarget(new Pose2D(pos.x + driveVector.getX(), pos.y + driveVector.getY(), 0));
            } else {
                drive.mecanumDrive(gamepad1);
            }
        }
    }
}
