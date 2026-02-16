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
import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;


@Autonomous (name="RedHartfordAuto")
public class RedHartfordAuto extends LinearOpMode {


    Intake_transfer intake = new Intake_transfer();

    PIDshooter pidShooter = new PIDshooter();

    PIDTurret PIDTurret = new PIDTurret();

    WebCamTest sharedCam = new WebCamTest();

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
            intake.intake(0.75,0.75);
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
            shooterTarget = -2350;
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

    @Override
    public void runOpMode() throws InterruptedException {

        sharedCam.init(hardwareMap);
        intake.init(hardwareMap);
        pidShooter.init(hardwareMap);
        PIDTurret.init(hardwareMap, sharedCam);

        /// Set coordinates as starting position and angle as direction robot is facing
        Pose2d beginPose = new Pose2d(-53.5, 49.5, Math.toRadians(305));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action path = drive.actionBuilder(beginPose)
                /// Moving to shooting position
                .stopAndAdd(new Shoot())
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(315))
                /// shooting
                .waitSeconds(0.5)
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(1)
                .stopAndAdd(new stopIntake())
                .waitSeconds(0.1)
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())

                .stopAndAdd(new stopShoot())
                /// moving to intake area
                .splineToSplineHeading(new Pose2d(-12, 30, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(-12, 55, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-30, 30))
                .stopAndAdd(new stopIntake())
                /// moving to shoot
                .stopAndAdd(new ReverseIntake())
                .stopAndAdd(new ReverseShoot())
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(135))
                .stopAndAdd(new stopIntake())
                .stopAndAdd(new Shoot())
                /// shooting
                .waitSeconds(1.5)
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(1)
                .stopAndAdd(new stopIntake())
                .waitSeconds(0.5)
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())

                .stopAndAdd(new stopShoot())
                /// moving to intake
                .splineToSplineHeading(new Pose2d(8, 30, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .stopAndAdd(new Intake())
                .splineToSplineHeading(
                        new Pose2d(8, 55, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-30, 30))
                .stopAndAdd(new stopIntake())
                /// moving to shoot
                .stopAndAdd(new ReverseIntake())
                .stopAndAdd(new ReverseShoot())
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(135))
                .stopAndAdd(new stopIntake())
                .stopAndAdd(new Shoot())
                /// shooting
                .waitSeconds(1.5)
                .stopAndAdd(new IntakeShoot())
                .waitSeconds(1)
                .stopAndAdd(new stopIntake())
                .waitSeconds(0.5)
                .stopAndAdd(new kickerUp())
                .waitSeconds(1.5)
                .stopAndAdd(new kickerDown())

                .stopAndAdd(new stopShoot())
                /// moving to gate
                .splineToLinearHeading(new Pose2d(0, 35, Math.toRadians(90)), Math.toRadians(90))
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
