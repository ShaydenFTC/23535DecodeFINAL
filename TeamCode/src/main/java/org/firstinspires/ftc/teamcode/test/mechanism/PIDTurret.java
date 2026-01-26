package org.firstinspires.ftc.teamcode.test.mechanism;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;

@Config

public class PIDTurret {

    public static double xcoord = 0;

    public static double DeadZone = 5;

    double IMU = 0;

    double offset;

    double OldXcoord;

    private IMU imu;

    WebCamTest lensCam = new WebCamTest();
    DcMotorEx aim = null;
    private double integral;
    private double lastError;
    private long lastTime;


    public void init(HardwareMap hardwareMap) {
        aim = hardwareMap.get(DcMotorEx.class, "aim");
        lensCam.init(hardwareMap);

        aim.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        aim.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        aim.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.RIGHT

        );

        imu.initialize(new IMU.Parameters(RevOrientation));
        imu.resetYaw();


    }

    public void TurretPID(double target, double Kp, double Ki, double Kd) {

        long currentTime = System.currentTimeMillis();
        double dt = (currentTime - lastTime) / 1000.0;

        xcoord = lensCam.CamAprilTags("red");

        double Encoder = aim.getCurrentPosition() * 1;
        if (xcoord != OldXcoord) {
            imu.resetYaw();
            offset = Encoder;
        }
        IMU = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - offset;
        double turretWorld = (Encoder + IMU);
        double TargetPos = (turretWorld + (xcoord * 1.71875) % 360);
        OldXcoord = xcoord;


        if (dt >= 0.01) {

            double error = target - TargetPos;

            integral += error * dt;
            double derivative = (error - lastError) / dt;

            double result = Kp * error + Ki * integral + Kd * derivative;

            lastError = error;
            lastTime = currentTime;

            if (Math.abs(error) < Math.abs(DeadZone)) {
                result = 0;
            } else if (aim.getCurrentPosition() < -24 && result < 0) {
                result = 0;
            } else if (aim.getCurrentPosition() > 24 && result > 0) {
                result = 0;
            }

            aim.setPower(result);

        }
    }
}


