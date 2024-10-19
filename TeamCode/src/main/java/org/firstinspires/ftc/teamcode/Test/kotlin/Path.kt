package org.firstinspires.ftc.teamcode.Test.kotlin

import org.firstinspires.ftc.teamcode.Test.kotlin.Pose2D
import org.firstinspires.ftc.teamcode.Test.kotlin.Spline
import org.firstinspires.ftc.teamcode.Test.kotlin.curvature

fun driveVector (spline: Spline, pos: Pose2D): Pose2D {
    val t = spline.getClosestPoint(pos)

    val splinePoint = spline.getPoint(t)
    val splineDeriv = spline.getDeriv(t)
    val splineDeriv2 = spline.getDeriv2(t)

    val translation = splinePoint - pos

    val forward = splineDeriv

    val curvature = curvature(splineDeriv, splineDeriv2)
    val centripetal = forward.pow(2) * curvature

    val drive = translation + forward + centripetal

    return Pose2D(drive.x, drive.y, 0.0)
}

fun drivePowers () {}