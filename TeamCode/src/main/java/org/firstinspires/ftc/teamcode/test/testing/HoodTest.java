package org.firstinspires.ftc.teamcode.test.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.test.mechanism.HuskyLensCam;

@TeleOp(name = "HoodTest", group = "test")
public class HoodTest extends OpMode {
    private Servo hood = null;

    WebCamTest lensCam = new WebCamTest();

    private Servo CameraMover;


    @Override
    public void init() {
        hood = hardwareMap.get(Servo.class,"hood");

        lensCam.init(hardwareMap);

        CameraMover = hardwareMap.get(Servo.class, "CameraMover");
        CameraMover.setPosition(0.35);
    }
    @Override
    public void loop() {

        double range = lensCam.CamAprilTagsRange("red");

        double Height = lensCam.CamAprilTagsElevation("red");

        if (Height > 5 && CameraMover.getPosition() <= 1)  {
            CameraMover.setPosition(CameraMover.getPosition() - 0.0005);
        } else if (Height < -5 && CameraMover.getPosition() >= 0) {
            CameraMover.setPosition(CameraMover.getPosition() + 0.0005);
        }

       /// hood.setPosition(-2.74E-04 * range + 0.791);
        telemetry.addData("size ",range);
        telemetry.addData("hood position", hood.getPosition());
        telemetry.addData("equation ", -2.74E-04 * range + 0.791);
        telemetry.addData("Camera position", CameraMover.getPosition());
        telemetry.addData("height", Height);
    }
}
