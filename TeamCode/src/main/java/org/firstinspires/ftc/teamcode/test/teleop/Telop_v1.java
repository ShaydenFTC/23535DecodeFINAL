package org.firstinspires.ftc.teamcode.test.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.test.mechanism.Drive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;

@TeleOp(name = "Teleop TeleOpv1", group = "test")
public class Telop_v1 extends OpMode {
    Drive drive = new Drive();
    Intake_transfer intake_transfer = new Intake_transfer();

    private Servo hood = null;


    DcMotorEx aim = null;

    private final ElapsedTime runtime = new ElapsedTime();

    DcMotorEx launcher = null;

    String speedCap = "Normal";
    double speed_percentage = 100.0;

    double target = 0;

    @Override
    public void init() {
        runtime.reset();

        drive.init(hardwareMap);

        intake_transfer.init(hardwareMap);

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");

        launcher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);

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
        ///  Launcher code


        if (gamepad2.left_stick_y > 0.1) {
            launcher.setPower(0.43);
        } else if (gamepad2.left_stick_y < -0.1) {
            launcher.setPower(-0.43);
        } else {
            launcher.setPower(0);
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
        drive.setDriveMotorPower(axial, lateral, yaw, speed_percentage);

        telemetry.setMsTransmissionInterval(50);

        /// Display
        telemetry.addData("Status", "Run Time: " + runtime.seconds());
        ///Displaying current speed.
        telemetry.addData("Current Speed", speedCap, " / " , speed_percentage);
        /// Display launcher power
        telemetry.addData("Launcher Power", launcher.getPower());
        ///  Display launcher encoder
        telemetry.addData("Launcher Encoder", launcher.getCurrentPosition());

        double ticksPerSecond = launcher.getVelocity();
        double rpm = (ticksPerSecond * 60) / 28;
        telemetry.addData("RPM", rpm);



    }
}
