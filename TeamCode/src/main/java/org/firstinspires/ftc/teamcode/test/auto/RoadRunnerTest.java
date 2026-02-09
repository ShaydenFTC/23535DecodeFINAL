package org.firstinspires.ftc.teamcode.test.auto;


import androidx.annotation.NonNull;

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
import com.acmerobotics.roadrunner.ParallelAction;

import org.firstinspires.ftc.teamcode.roadrunner.tests.MecanumDrive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDTurret;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDshooter;


@Autonomous (name="RoadRunnerTest")
public class RoadRunnerTest extends LinearOpMode {


    Intake_transfer intake = new Intake_transfer();

    PIDshooter pidShooter = new PIDshooter();

    PIDTurret PIDTurret = new PIDTurret();
    /// turret controls
    public class Turret implements Action {
        @Override
        public boolean run(@NonNull com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
            PIDTurret.TurretPID(0, 0.008, 0, 0.0002, 0.01);

            return true;
        }
    }
    /// Intake Controls
    public class Intake implements InstantFunction {
        @Override
        public void run() {
            intake.intake(1,1);
        }
    }

    public class stopIntake implements InstantFunction {
        @Override
        public void run() {
            intake.intake(0,0);
        }
    }

    public class IntakeShoot implements InstantFunction {
        @Override
        public void run() {
            intake.intake(0.5,0.5);
        }
    }
    /// kicker
    public class kickerUp implements InstantFunction {
        @Override
        public void run() {
            intake.setKicker(true);
        }
    }
    public class kickerDown implements InstantFunction {
        @Override
        public void run() {
            intake.setKicker(false);
        }
    }

    /// Shooter controls

    public class Shoot implements InstantFunction {
        @Override
        public void run() {
            pidShooter.LauncherPID(-2445, 0.005, 0, 0);
        }
    }

    public class stopShoot implements InstantFunction {
        @Override
        public void run() {
            pidShooter.LauncherPID(0, 0.005, 0, 0);
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
                .stopAndAdd(new Shoot())
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(315))
                /// shooting
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(1.5)
                .stopAndAdd(new stopIntake())
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())
                .stopAndAdd(new stopShoot())
                /// moving to intake area
                .splineToSplineHeading(new Pose2d(-12, 30, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(-12, 50, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-15, 15))
                .stopAndAdd(new stopIntake())
                /// moving to gate
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(0, 53, Math.toRadians(90)), Math.toRadians(90))
                /// openning gate
                .waitSeconds(2)
                /// moving to shoot
                .stopAndAdd(new Shoot())
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(135))
                /// shooting
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(1.5)
                .stopAndAdd(new stopIntake())
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())
                .stopAndAdd(new stopShoot())
                /// moving to intake
                .splineToSplineHeading(new Pose2d(12, 30, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(12, 50, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-15, 15))
                .stopAndAdd(new stopIntake())
                /// moving to shoot
                .stopAndAdd(new Shoot())
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(135))
                /// shooting
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(1.5)
                .stopAndAdd(new stopIntake())
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())
                .stopAndAdd(new stopShoot())
                /// moving to gate
                .splineToLinearHeading(new Pose2d(0, 35, Math.toRadians(90)), Math.toRadians(90))
        .build();

        Actions.runBlocking(
                new com.acmerobotics.roadrunner.ParallelAction(
                        path,
                        new Turret()
                )
        );
    }
}