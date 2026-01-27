package org.firstinspires.ftc.teamcode.test.auto;


import com.acmerobotics.roadrunner.InstantFunction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Actions;
import com.acmerobotics.roadrunner.SequentialAction;

import org.firstinspires.ftc.teamcode.roadrunner.tests.MecanumDrive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;


@Autonomous (name="RoadRunnerTest")
public class RoadRunnerTest extends LinearOpMode {


    Intake_transfer intake = new Intake_transfer();

    /// Intake
    public class IntakeOn implements InstantFunction {
        @Override
        public void run() {
            intake.intakein(true);
        }
    }
    public class IntakeOff implements InstantFunction {
        @Override
        public void run() {
            intake.intakein(false);
        }
    }

    /// Intake Transfer
    public class IntakeTransferOn implements InstantFunction {
        @Override
        public void run() {
            intake.transfer(true);
        }
    }
    public class IntakeTransferOff implements InstantFunction {
        @Override
        public void run() {
            intake.transfer(false);
        }
    }
    /// Intake Transfer Back


    /// Kicker Controls
    public class KickerUp implements InstantFunction {
        @Override
        public void run() {
            intake.setKicker(true);
        }
    }
    public class KickerDown implements InstantFunction {
        @Override
        public void run() {
            intake.setKicker(false);
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {

        /// Set coordinates as starting position and angle as direction robot is facing
        Pose2d beginPose = new Pose2d(new Vector2d(0,0), Math.toRadians(0));

        MecanumDrive drive = new MecanumDrive(hardwareMap, beginPose);

        waitForStart();

        Action path = drive.actionBuilder(beginPose)
                .stopAndAdd(new KickerUp())
                .stopAndAdd(new KickerDown())
        .build();

        //Actions.runBlocking(new SequentialAction(path));

    }
}