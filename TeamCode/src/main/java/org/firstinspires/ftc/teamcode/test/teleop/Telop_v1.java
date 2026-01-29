package org.firstinspires.ftc.teamcode.test.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.test.mechanism.Drive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;
import org.firstinspires.ftc.teamcode.test.mechanism.PIDshooter;

@TeleOp(name = "Teleop TeleOpv1", group = "test")
public class Telop_v1 extends OpMode {
    Drive drive = new Drive();
    Intake_transfer intake_transfer = new Intake_transfer();
    PIDshooter pidShooter = new PIDshooter();

    private Servo hood = null;

    DcMotorEx aim = null;

    private final ElapsedTime runtime = new ElapsedTime();

    String speedCap = "Normal";
    double speed_percentage = 100.0;

    // PID constants from PIDControllerShooter
    public static double Kp = 0.005;
    public static double Ki = 0;
    public static double Kd = 0;
    double targetRPM = 0;

    @Override
    public void init() {
        runtime.reset();

        drive.init(hardwareMap);
        intake_transfer.init(hardwareMap);
        pidShooter.init(hardwareMap);

        aim = hardwareMap.get(DcMotorEx.class, "aim");
        aim.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        aim.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        aim.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        hood = hardwareMap.get(Servo.class,"hood");
    }
    @Override
    public void loop() {

        /// Intake and transfer controls
        intake_transfer.intake(gamepad1.right_trigger - gamepad1.left_trigger,
                gamepad2.right_trigger - gamepad2.left_trigger);

        intake_transfer.setKicker(gamepad2.right_bumper);

        /// Launcher PID control
        if (gamepad2.left_stick_y > 0.1) {
            targetRPM = 1500;
        } else if (gamepad2.left_stick_y < -0.1) {
            targetRPM = -1500;
        } else {
            targetRPM = 0;
        }
            pidShooter.LauncherPID(targetRPM, Kp, Ki, Kd);

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
        drive.setDriveMotorPower(axial, lateral, yaw, speed_percentage);

        telemetry.setMsTransmissionInterval(50);

        /// Display
        telemetry.addData("Status", "Run Time: " + runtime.seconds());
        telemetry.addData("Current Speed", speedCap, " / " , speed_percentage);
        telemetry.addData("Launcher Power", pidShooter.getPower());
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Actual RPM", pidShooter.getRPM());
        telemetry.addData("Smoothed RPM", pidShooter.getSmoothedRPM());

        telemetry.update();
    }
}
