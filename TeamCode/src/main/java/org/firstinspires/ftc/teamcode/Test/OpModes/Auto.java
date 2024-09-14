package org.firstinspires.ftc.teamcode.Test.OpModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.Test.Drivebase.MecanumDrive;
import org.firstinspires.ftc.teamcode.Test.Path.Path;
import org.firstinspires.ftc.teamcode.Test.Util.Vector2D;

@Autonomous
public class Auto extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        MecanumDrive drive = new MecanumDrive(this.hardwareMap);
        waitForStart();
        while (opModeIsActive()) {
            boolean bPressed = this.gamepad1.b;
            if (bPressed) {
                Path path1 = drive.pathBuilder(new Vector2D(0, 0))
                        .pt(0,90)
                        .pt(90,90)
                        .pt(90,0)
                        .pt(0,0)
                        .build();

                path1.runTest();
            }
        }
    }
}