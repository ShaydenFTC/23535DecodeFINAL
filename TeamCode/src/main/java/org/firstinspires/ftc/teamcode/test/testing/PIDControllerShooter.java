package org.firstinspires.ftc.teamcode.test.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@Disabled
@TeleOp(name = "PIDControllerShooter", group = "test")
public class PIDControllerShooter extends OpMode {
    DcMotorEx launcher = null;

    public static double Kp = 0.001;
    public static double Ki = 0.0;
    public static double Kd = 0.0;
    public static double target = -1000; // You can now also tune target RPM
    private double integral;
    private double lastError;
    private long lastTime;

    private int lastPosition = 0;

    private long lastRPMTime = 0;



    private FtcDashboard dashboard;

    public void init(){

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");

        launcher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);

        lastTime = System.currentTimeMillis();

        lastPosition = launcher.getCurrentPosition();
        lastRPMTime = System.nanoTime();

    }

    public void loop() {
            long currentTime = System.currentTimeMillis();
            double dt = (currentTime - lastTime) / 1000.0;

            int currentPosition = launcher.getCurrentPosition();
            long currentRPMTime = System.nanoTime();


        if (dt >= 0.01) {

            int deltaTicks = currentPosition - lastPosition;
            long deltaTime = currentRPMTime - lastRPMTime;
            double manualRPM = (deltaTicks / 28.0) / (deltaTime / 60_000_000_000.0);

            double alpha = 0.2;
            double smoothedRPM = alpha * manualRPM + (1 - alpha);

            lastPosition = currentPosition;
            lastRPMTime = currentRPMTime;


            // Target value
            double target = PIDControllerShooter.target;
            ; // Make this the desired RPM: -3900 MAX
            double error = target - smoothedRPM;
            integral += error * dt;
            double derivative = (error - lastError) / dt;

            double result = Kp * error + Ki * integral + Kd * derivative;

            lastError = error;
            lastTime = currentTime;

            if (!gamepad2.x) {
                if (target > 0) {
                launcher.setPower(Math.max(0, Math.min(0.7, result)));
                } else {
                    launcher.setPower(Math.max(-0.7, Math.min(0, result)));
                }
            }


            telemetry.addData("Motor Power: ", launcher.getPower());
            telemetry.addData("Motor RPM: ", manualRPM);
            telemetry.addData("Motor Encoder: ", launcher.getCurrentPosition());
            dashboard = FtcDashboard.getInstance();

            TelemetryPacket packet = new TelemetryPacket();

            packet.put("actualRPM", manualRPM);
            packet.put("target", target);
            packet.put("Power", (launcher.getPower() * Math.abs(target)));


            dashboard.sendTelemetryPacket(packet);
            telemetry.update();

        }


    }

    private void reset() {
        integral = 0;
        lastError = 0;
        lastTime = System.currentTimeMillis();
    }

}


// Result value based on specific starting point from encoder:




