package org.firstinspires.ftc.teamcode.test.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;

@Config
@Disabled

@TeleOp(name = "PIDControllerTurret", group = "test")
public class PIDControllerTurret extends OpMode {

    public static double Kp = 0.01;
    public static double Ki = 0.0;
    public static double Kd = 0.0015;
    public static double target = 0;

    public static double result = 0;

    public static double TargetPos = 0;

    public static double xcoord = 0;

    public static double DeadZone = 0;

    double IMU = 0;

    double offset;

    double OldXcoord;

    private IMU imu;

    WebCamTest lensCam = new WebCamTest();
    DcMotorEx aim = null;
    private double integral;
    private double lastError;
    private long lastTime;

    private FtcDashboard dashboard;

    public void init() {
        aim = hardwareMap.get(DcMotorEx.class, "aim");
        lensCam.init(hardwareMap);

        aim.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        aim.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        aim.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        imu = hardwareMap.get(IMU.class, "imu");

        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.BACKWARD,
                RevHubOrientationOnRobot.UsbFacingDirection.DOWN

        );

        imu.initialize(new IMU.Parameters(RevOrientation));
        imu.resetYaw();


    }

    public void loop() {

        long currentTime = System.currentTimeMillis();
        double dt = (currentTime - lastTime) / 1000.0;

        xcoord = lensCam.CamAprilTags("red");
        // Target value
        double target = PIDControllerTurret.target;

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

            result = Kp * error + Ki * integral + Kd * derivative;

            lastError = error;
            lastTime = currentTime;

            if (Math.abs(error) < Math.abs(DeadZone)) {
                result = 0;
            } else if (aim.getCurrentPosition() < -30 && result < 0) {
                result = 0;
            } else if (aim.getCurrentPosition() > 30 && result > 0) {
                result = 0;
            }

                aim.setPower(result);




            dashboard = FtcDashboard.getInstance();

            TelemetryPacket packet = new TelemetryPacket();

            packet.put("Encoder Position", aim.getCurrentPosition());
            packet.put("Xcoord", xcoord);
            packet.put("target", target);
            packet.put("Power", aim.getPower() * 100);
            packet.put("targetpos ", TargetPos);
            packet.put("deadzone ", DeadZone);
            packet.put("error", error);

            dashboard.sendTelemetryPacket(packet);
            telemetry.update();
        }
    }
}


