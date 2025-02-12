package org.firstinspires.ftc.teamcode.trailblazer.drivebase;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.fotmrobotics.trailblazer.PIDF;
import org.fotmrobotics.trailblazer.Pose2D;

@Config
public class DriveValues {
    /**
     * 0. Front Left
     * 1. Front Right
     * 2. Back Left
     * 3. Back Right
     */
    String[] motorNames = {
            "frontLeft",
            "frontRight",
            "backLeft",
            "backRight"
    };

    int[] reverseMotors = {1,3};

    PIDF positionPID = new PIDF(0.005, 0, 0,0);
    PIDF headingPID = new PIDF(0.01, 0,0,0);

    String SparkFunOTOS = "otos";

    DistanceUnit linearUnit = DistanceUnit.INCH;
    double linearScalar = 1;

    AngleUnit angularUnit = AngleUnit.DEGREES;
    double angularScalar = 1;

    Pose2D offset = new Pose2D(0,3.1, 0);

    double xScale = 1;
    double yScale = 1;
    double angularScale = 1;
}
