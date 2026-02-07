package org.firstinspires.ftc.teamcode.test.auto;


import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.teamcode.roadrunner.tests.MecanumDrive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;


@Autonomous (name="RoadRunnerTest")
public class RoadRunnerTest extends LinearOpMode {


    Intake_transfer intake = new Intake_transfer();



    /// Kicker Controls
    public class Intake implements InstantFunction {
        @Override
        public void run() {
            intake.intake(1,1);
        }
    }

    public class stopintake implements InstantFunction {
        @Override
        public void run() {
            intake.intake(0,0);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {



        /// Set coordinates as starting position and angle as direction robot is facing
        Pose2d beginPose = new Pose2d(-49.5, 49.5, Math.toRadians(305));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action path = drive.actionBuilder(beginPose)
                /// Moving to shooting position
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(315))
                /// shooting
                .waitSeconds(3)
                /// moving to intake area
                .splineToSplineHeading(new Pose2d(-12, 30, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(-12, 50, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-15, 15))
                .stopAndAdd(new stopintake())
                /// moving to gate
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(0, 53, Math.toRadians(90)), Math.toRadians(90))
                /// openning gate
                .waitSeconds(2)
                /// moving to shoot
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(45)), Math.toRadians(135))
                /// shooting
                .waitSeconds(3)
                /// moving to intake
                .splineToSplineHeading(new Pose2d(12, 30, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(12, 50, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-15, 15))
                .stopAndAdd(new stopintake())
                /// moving to shoot
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(45)), Math.toRadians(135))
                /// shooting
                .waitSeconds(3)
                /// moving to gate
                .splineToLinearHeading(new Pose2d(0, 48, Math.toRadians(90)), Math.toRadians(90))
        .build();

        Actions.runBlocking(new SequentialAction(path));

    }
}