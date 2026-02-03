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
    private final ElapsedTime jamTimer = new ElapsedTime();
    private boolean isJamming = false;

    String speedCap = "Normal";
    double speed_percentage = 100.0;

    // PID constants from PIDControllerShooter
    public static double Kp = 0.005;
    public static double Ki = 0;
    public static double Kd = 0;
    double targetRPM = 0;

    double ServoPosition;

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
        
        // Start timer high so jam can trigger immediately on start
        jamTimer.reset();
    }
    @Override
    public void loop() {
        double g1Intake = gamepad1.right_trigger - gamepad1.left_trigger;
        double g2Intake = gamepad2.right_trigger - gamepad2.left_trigger;


        if (targetRPM == 0 && pidShooter.getPower() == 0 && pidShooter.getRPM() < -40 && !isJamming && (g1Intake > 0.1 || g2Intake > 0.1) && jamTimer.seconds() > 1.5) {
            isJamming = true;
            jamTimer.reset();
        }

        if (isJamming) {
            if (jamTimer.seconds() < 1) {
                intake_transfer.intake(0, -1);
                if (jamTimer.seconds() < 0.5) {
                    pidShooter.LauncherPID(500, Kp, Ki, Kd);
                }
            } else {
                isJamming = false;
            }
        }

        if (!isJamming || jamTimer.seconds() > 0.5) {
            /// Intake and transfer controls
            intake_transfer.intake(g1Intake, g2Intake);

            intake_transfer.setKicker(gamepad2.right_bumper);

            /// Launcher PID control
            if (gamepad2.left_stick_y > 0.1) {
                targetRPM = 2150;
            } else if (gamepad2.left_stick_y < -0.1) {
                targetRPM = -2150;
            } else {
                targetRPM = 0;
            }
            pidShooter.LauncherPID(targetRPM, Kp, Ki, Kd);
        }

        /// hood controls

        if (gamepad2.x && ServoPosition < 1) {
            ServoPosition = ServoPosition + 0.003;
        } else if (gamepad2.y && ServoPosition > 0) {
            ServoPosition = ServoPosition - 0.003;
        }

        hood.setPosition(ServoPosition);
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
        telemetry.addData("Jamming?", isJamming);
        telemetry.addData("Current Speed", speedCap, " / " , speed_percentage);
        telemetry.addData("Launcher Power", pidShooter.getPower());
        telemetry.addData("Target RPM", targetRPM);
        telemetry.addData("Actual RPM", pidShooter.getRPM());
        telemetry.addData("Smoothed RPM", pidShooter.getSmoothedRPM());
        telemetry.addData("Hood Position", hood.getPosition());

        telemetry.update();
    }
}
