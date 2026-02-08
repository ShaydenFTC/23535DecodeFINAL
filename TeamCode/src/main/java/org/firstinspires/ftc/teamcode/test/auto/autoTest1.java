package org.firstinspires.ftc.teamcode.test.auto;


import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.tests.MecanumDrive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;


@Autonomous (name="RoadRunnerTest")
public class autoTest1 extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {



        /// Set coordinates as starting position and angle as direction robot is facing
        Pose2d beginPose = new Pose2d(-49.5, 49.5, Math.toRadians(305));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action path = drive.actionBuilder(beginPose)
                .splineToSplineHeading(new Pose2d(0, 0, Math.toRadians(315)), Math.toRadians(315))
                .splineToSplineHeading(new Pose2d(-49.5, 49.5, Math.toRadians(305)), Math.toRadians(105))

                .build();

        Actions.runBlocking(new SequentialAction(path));

    }
}