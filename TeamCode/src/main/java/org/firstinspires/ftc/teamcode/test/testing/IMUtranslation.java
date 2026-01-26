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
public class IMUtranslation extends OpMode {

    WebCamTest lensCam = new WebCamTest();
    DcMotorEx aim = null;

    private IMU imu;

    double IMU = 0;

    double offset;

    private double integral;

    double OldXcoord;

    private FtcDashboard dashboard;

    public void init() {
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
    public void loop() {
        double xcoord = lensCam.CamAprilTags("red");


        double Encoder = aim.getCurrentPosition() * 1;

        if (xcoord != OldXcoord) {
            imu.resetYaw();
            offset = Encoder;
        }

        IMU = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES) - offset;



        double turretWorld = (Encoder + IMU);


        double TargetPos = turretWorld + (xcoord * 1.71875) % 360;

        OldXcoord = xcoord;

        telemetry.addData("IMU ", IMU);
        telemetry.addData("Actuall IMU ", -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES));
        telemetry.addData("OldXcoord ", OldXcoord);
        telemetry.addData("Encoder ", Encoder);
        telemetry.addData("TurretWorld ", turretWorld);
        telemetry.addData("xcoord ", xcoord);
        telemetry.addData("targetPose ", TargetPos);

            dashboard = FtcDashboard.getInstance();

            TelemetryPacket packet = new TelemetryPacket();

            packet.put("xcoord", xcoord);
            packet.put("IMU", aim.getPower() * 100);

            dashboard.sendTelemetryPacket(packet);
            telemetry.update();
        }



    // Can use class HuskyLensCam for xvalue of apriltag

    }

