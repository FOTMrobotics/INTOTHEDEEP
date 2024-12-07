package org.firstinspires.ftc.teamcode.Test.kotlin

fun angleWrap (angle: Double) =
    if (angle >= 0) {angle % 360}
    else {angle % 360 + 360}

/*
// Catmull-rom spline interpolation
fun slerp (t: Double, p0: Vector2D, p1: Vector2D, p2: Vector2D, p3: Vector2D)
= ((p1*2.0)+(-p0+p2)*t+(p0*2.0-p1*5.0+p2*4.0-p3)*t*t+(-p0+p1*3.0-p2*3.0+p3)*t*t*t)*0.5

// Derivative of catmull-rom spline interpolation
fun slerpDeriv (t: Double, p0: Vector2D, p1: Vector2D, p2: Vector2D, p3: Vector2D)
= ((p1*3.0+p3-p0-p2*3.0)*3.0*t*t+(p0*2.0+p2*4.0-p1*5.0-p3)*2.0*t)*0.5

// 2nd derivative of catmull-rom spline interpolation
fun slerpDeriv2 (t: Double, p0: Vector2D, p1: Vector2D, p2: Vector2D, p3: Vector2D)
= (p1*3.0+p3-p0)*3.0*t+(p2*5.0-p1*5.0+p0-p3)*2.0
*/