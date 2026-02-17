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
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.roadrunner.tests.MecanumDrive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDTurret;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDshooter;
import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;


@Autonomous (name="FarAutoRed1")
public class FarAutoRed1 extends LinearOpMode {


    Intake_transfer intake = new Intake_transfer();

    PIDshooter pidShooter = new PIDshooter();

    PIDTurret PIDTurret = new PIDTurret();

    WebCamTest sharedCam = new WebCamTest();

    private Servo hood;

    private double shooterTarget = 0;
    private double turretTarget = 0;

    /// Background actions to maintain PID loops
    public class TurretAction implements Action {
        @Override
        public boolean run(@NonNull com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
            PIDTurret.TurretPID(turretTarget, 0.007, 0, 0.0002, 0.001);
            return true;
        }
    }

    public class ShooterAction implements Action {
        @Override
        public boolean run(@NonNull com.acmerobotics.dashboard.telemetry.TelemetryPacket packet) {
            pidShooter.LauncherPID(shooterTarget, 0.005, 0, 0);
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
            intake.intake(1,1);
        }
    }
    public class ReverseIntake implements InstantFunction {
        @Override
        public void run() {
            intake.intake(0.5,-1);
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

    /// Shooter target setters
    public class Shoot implements InstantFunction {
        @Override
        public void run() {
            shooterTarget = -3620;
        }
    }

    public class stopShoot implements InstantFunction {
        @Override
        public void run() {
            shooterTarget = 0;
        }
    }

    public class ReverseShoot implements InstantFunction {
        @Override
        public void run() {
            shooterTarget = 1000;
        }
    }
    public class MoveHood implements InstantFunction {
        @Override
        public void run() {
            hood.setPosition(0.95);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {

        sharedCam.init(hardwareMap);
        intake.init(hardwareMap);
        pidShooter.init(hardwareMap);
        PIDTurret.init(hardwareMap, sharedCam);
        hood = hardwareMap.get(Servo.class,"hood");

        /// Set coordinates as starting position and angle as direction robot is facing
        Pose2d beginPose = new Pose2d(60, 15, Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action path = drive.actionBuilder(beginPose)
                .stopAndAdd(new MoveHood())
                .stopAndAdd(new Shoot())
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(55, 17, Math.toRadians(330)), Math.toRadians(180))
                .waitSeconds(3)
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(3)
                .stopAndAdd(new stopIntake())
                .waitSeconds(0.5)
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())

                .stopAndAdd(new stopShoot())

                .stopAndAdd(new stopShoot())
                /// moving to intake area
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(34, 35, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(34, 65, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-30, 30))
                .stopAndAdd(new ReverseIntake())
                .stopAndAdd(new ReverseShoot())
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(55, 17, Math.toRadians(340)), Math.toRadians(0))
                .stopAndAdd(new stopIntake())
                .stopAndAdd(new Shoot())
                .waitSeconds(3)
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(3)
                .stopAndAdd(new stopIntake())
                .waitSeconds(0.5)
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())

                .stopAndAdd(new stopShoot())
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(30, 17, Math.toRadians(345)), Math.toRadians(0))
                .build();

        Actions.runBlocking(
                new ParallelAction(
                        path,
                        new TurretAction(),
                        new ShooterAction()
                )
        );
    }
}
