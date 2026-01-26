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

    private double result;

    private int lastPosition = 0;

    private long lastRPMTime = 0;

    public void init(HardwareMap hardwareMap){

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");

        launcher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);
        launcher.setDirection(DcMotorSimple.Direction.REVERSE);

        lastTime = System.currentTimeMillis();

        lastPosition = launcher.getCurrentPosition();
        lastRPMTime = System.nanoTime();

    }

    public void LauncherPID(double target, double Kp, double Ki, double Kd) {
            long currentTime = System.currentTimeMillis();
            double dt = (currentTime - lastTime) / 1000.0;

            int currentPosition = launcher.getCurrentPosition();
            long currentRPMTime = System.nanoTime();


        if (dt >= 0.01) {

            int deltaTicks = currentPosition - lastPosition;
            long deltaTime = currentRPMTime - lastRPMTime;
            manualRPM = (deltaTicks / 28.0) / (deltaTime / 60_000_000_000.0);

            double alpha = 0.2;
            double smoothedRPM = alpha * manualRPM + (1 - alpha);

            lastPosition = currentPosition;
            lastRPMTime = currentRPMTime;

            ; // Make this the desired RPM: -3900 MAX
            double error = target - smoothedRPM;
            integral += error * dt;
            double derivative = (error - lastError) / dt;

            result = Kp * error + Ki * integral + Kd * derivative;

            lastError = error;
            lastTime = currentTime;

            if (Math.abs(result) < 0.05) {  // If result is between -0.05 and 0.05
                launcher.setPower(0);
            } else {
                launcher.setPower(Math.max(-1.0, Math.min(1.0, result)));
            }
            }

        }
    public double getRPM() {
        return manualRPM; }

    public double getPower() {
        return launcher.getPower(); }

    public void reset() {
        integral = 0;
        lastError = 0;
        lastTime = System.currentTimeMillis();
    }

    }



// Result value based on specific starting point from encoder:




