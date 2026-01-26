package org.firstinspires.ftc.teamcode.test.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
@Disabled

@TeleOp(name="encoder_reader", group="Sample")
public class encoder_reader extends LinearOpMode {

    DcMotorEx aim = null;

    // Add these variables
    int lastPosition = 0;
    long lastTime = 0;

    @Override
    public void runOpMode() {

        aim = hardwareMap.get(DcMotorEx.class, "launcher");

        aim.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        aim.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        // Initialize after start
        lastPosition = aim.getCurrentPosition();
        lastTime = System.nanoTime();

        while (opModeIsActive()) {

            aim.setPower(gamepad2.right_trigger-gamepad2.left_trigger);

            double currentPosition = aim.getCurrentPosition();

            double currentVelocityTPS = aim.getVelocity();
            double currentVelocityRPM = (currentVelocityTPS * 60) / 28;

            // Manual RPM calculation
            int deltaTicks = (int)currentPosition - lastPosition;
            long deltaTime = System.nanoTime() - lastTime;
            double manualRPM = (deltaTicks / 28.0) / (deltaTime / 60_000_000_000.0);
            lastPosition = (int)currentPosition;
            lastTime = System.nanoTime();

            telemetry.setMsTransmissionInterval(100);
            telemetry.addData("Motor RPM/1", currentVelocityRPM);
            telemetry.addData("Motor RPM/2", manualRPM);
            telemetry.addData("Motor Encoder Position", currentPosition);
            telemetry.update();

            sleep(10);
        }
    }
}