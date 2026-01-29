package org.firstinspires.ftc.teamcode.test.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Config
@TeleOp(name = "PIDControllerShooter", group = "test")
public class PIDControllerShooter extends OpMode {
    DcMotorEx launcher = null;

    public static double Kp = 0.005;
    public static double Ki = 0; // Added a tiny bit of Ki to help with steady-state error
    public static double Kd = 0;
    public static double target = -1000;
    private double integral;
    private double lastError;
    private long lastTime;

    private double smoothedRPM = 0;
    private FtcDashboard dashboard;

    public void init(){
        dashboard = FtcDashboard.getInstance();
        launcher = hardwareMap.get(DcMotorEx.class, "launcher");

        launcher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        // Use RUN_WITHOUT_ENCODER when running your own PID
        launcher.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);

        lastTime = System.currentTimeMillis();
    }

    public void loop() {
        long currentTime = System.currentTimeMillis();
        double dt = (currentTime - lastTime) / 1000.0;

        if (dt >= 0.01) {
            double ticksPerSecond = launcher.getVelocity();
            double manualRPM = (ticksPerSecond * 60) / 28.0;

            // Fixed smoothing logic
            double alpha = 0.2;
            smoothedRPM = (alpha * manualRPM) + (1 - alpha) * smoothedRPM;

            double error = target - smoothedRPM;
            integral += error * dt;

            // Windup protection for integral
            if (Math.abs(error) < 10) integral = 0;

            double derivative = (error - lastError) / dt;

            double result = (Kp * error) + (Ki * integral) + (Kd * derivative);

            lastError = error;
            lastTime = currentTime;

            if (!gamepad2.x) {
                // Constrain power to -1 to 1 range
                launcher.setPower(Math.max(-1, Math.min(1, result)));
            } else {
                launcher.setPower(0);
                reset();
            }

            TelemetryPacket packet = new TelemetryPacket();
            packet.put("actualRPM", manualRPM);
            packet.put("smoothedRPM", smoothedRPM);
            packet.put("target", target);
            packet.put("Power", launcher.getPower());
            dashboard.sendTelemetryPacket(packet);

            telemetry.addData("Motor Power", launcher.getPower());
            telemetry.addData("Motor RPM", manualRPM);
            telemetry.update();
        }
    }

    private void reset() {
        integral = 0;
        lastError = 0;
        lastTime = System.currentTimeMillis();
        smoothedRPM = 0;
    }
}
