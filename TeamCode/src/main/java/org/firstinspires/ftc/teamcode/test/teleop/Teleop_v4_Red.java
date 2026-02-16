package org.firstinspires.ftc.teamcode.test.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.test.mechanism.Drive;
import org.firstinspires.ftc.teamcode.test.mechanism.HoodRed;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDTurretRed;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDshooter;
import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;

@TeleOp(name = "Teleop TeleOpv4 Red", group = "test")
public class Teleop_v4_Red extends OpMode {
    Drive drive = new Drive();
    Intake_transfer intake_transfer = new Intake_transfer();

    HoodRed Hood = new HoodRed();

    PIDshooter pidShooter = new PIDshooter();

    PIDTurretRed PIDTurret = new PIDTurretRed();

    WebCamTest sharedCam = new WebCamTest();

    private Servo hood = null;

    DcMotorEx aim = null;

    private final ElapsedTime runtime = new ElapsedTime();
    String speedCap = "Normal";
    double speed_percentage = 100.0;

    // PID constants from PIDControllerShooter
    public static double Kp = 0.0075;
    public static double Ki = 0;
    public static double Kd = 0;

    public static double TKp = 0.008;
    public static double TKi = 0;
    public static double TKd = 0.0002;

    public static double TTarget = -10 ;
    double targetRPM = 0;

    double ServoPosition;

    boolean auto = true;

    @Override
    public void init() {
        runtime.reset();

        sharedCam.init(hardwareMap);

        drive.init(hardwareMap);
        intake_transfer.init(hardwareMap);
        pidShooter.init(hardwareMap);
        PIDTurret.init(hardwareMap, sharedCam);
        Hood.init(hardwareMap, sharedCam);

        aim = hardwareMap.get(DcMotorEx.class, "aim");
        aim.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        aim.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        aim.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        hood = hardwareMap.get(Servo.class,"hood");

    }
    @Override
    public void loop() {

        if (gamepad2.x) {
            auto = false;
        } else if (gamepad2.y) {
            auto = true;
        }
        double g1Intake = gamepad1.right_trigger - gamepad1.left_trigger;
        double g2Intake = gamepad2.right_trigger - gamepad2.left_trigger;


        /// Intake and transfer controls
        intake_transfer.intake(g1Intake, g2Intake);

        intake_transfer.setKicker(gamepad2.right_bumper);

        /// Launcher PID control
        if (auto) {
            targetRPM =  -23.2 * sharedCam.CamAprilTagsRange("red") + -1605;
        } else if (!auto) {
            targetRPM = -2200;
        }

        if (gamepad2.left_stick_y > 0.1) {
            pidShooter.LauncherPID(-targetRPM, Kp, Ki, Kd);
        } else if (gamepad2.left_stick_y < -0.1) {
            pidShooter.LauncherPID(targetRPM, Kp, Ki, Kd);
        } else {
            pidShooter.LauncherPID(0, Kp, Ki, Kd);
        }

        /// Turret Controls
        if (auto && Math.abs(gamepad2.left_stick_y) > 0.1){
            PIDTurret.TurretPID(TTarget, TKp, TKi, TKd, 0);
        } else if (!auto || Math.abs(gamepad2.left_stick_y) < 0.1) {
            PIDTurret.TurretPID(TTarget, TKp, TKi, TKd, 0.01);
        }
        /// hood controls
        if (auto) {
            Hood.MoveHood();
        } else if (!auto) {
            hood.setPosition(0);
        }

        /// Sets the speed cap for driver 1.
        if (gamepad1.y) {
            speedCap = "Normal";
            speed_percentage = 100.0;
        } else if (gamepad1.a) {
            speedCap = "Slow";
            speed_percentage = 30.0; }


        /// Drivetrain controls.
        double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives a negative value.
        double lateral =  gamepad1.left_stick_x;
        double yaw     =  gamepad1.right_stick_x;
        drive.setDriveMotorPower((axial), lateral, yaw, speed_percentage);

        telemetry.setMsTransmissionInterval(50);

        /// Display
        telemetry.addData("Status", "Run Time: " + runtime.seconds());
        telemetry.addData("Current Speed", speedCap, " / " , speed_percentage);
        telemetry.addData("Launcher Power", pidShooter.getPower());
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Actual RPM", pidShooter.getRPM());
        telemetry.addData("Hood Position", hood.getPosition());
        telemetry.addData("AprilTag Range", sharedCam.CamAprilTagsElevation("red"));
        telemetry.addData("AprilTag Range", sharedCam.CamAprilTagsRange("red"));
        telemetry.addData("xcoord", PIDTurret.xcoord);

        telemetry.update();
    }
}
