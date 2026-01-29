package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class PIDshooter {
    DcMotorEx launcher = null;

    private double integral;
    private double lastError;
    private long lastTime;

    private double manualRPM;
    private double smoothedRPM = 0;

    private double result;

    public void init(HardwareMap hardwareMap){

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");

        launcher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        // Using RUN_WITHOUT_ENCODER as per PIDControllerShooter improvements
        launcher.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);

        lastTime = System.currentTimeMillis();
    }

    public void LauncherPID(double target, double Kp, double Ki, double Kd) {
        long currentTime = System.currentTimeMillis();
        double dt = (currentTime - lastTime) / 1000.0;

        if (dt >= 0.01) {
            // Using launcher.getVelocity() for more accurate/consistent RPM calculation
            double ticksPerSecond = launcher.getVelocity();
            manualRPM = (ticksPerSecond * 60) / 28.0;

            // Fixed smoothing logic from PIDControllerShooter
            double alpha = 0.2;
            smoothedRPM = (alpha * manualRPM) + (1 - alpha) * smoothedRPM;

            double error = target - smoothedRPM;
            integral += error * dt;

            // Windup protection for integral from PIDControllerShooter
            if (Math.abs(error) < 10) integral = 0;

            double derivative = (error - lastError) / dt;

            result = (Kp * error) + (Ki * integral) + (Kd * derivative);

            lastError = error;
            lastTime = currentTime;

            // Using power clamping instead of just the deadzone check
            if (launcher.getPower() > 0.01 && target > 0 && target < 0) {
            launcher.setPower(Math.max(-1.0, Math.min(1.0, result)));
            } else {
                launcher.setPower(0);
            }
        }

    }
    public double getRPM() {
        return manualRPM; }

    public double getSmoothedRPM() {
        return smoothedRPM;
    }

    public double getPower() {
        return launcher.getPower(); }

    public void reset() {
        integral = 0;
        lastError = 0;
        lastTime = System.currentTimeMillis();
        smoothedRPM = 0;
    }

}
