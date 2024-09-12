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
            boolean z = this.gamepad1.b;
            Path path1 = drive.pathBuilder(new Vector2D(0,0))
                    .build();
        }
    }
}