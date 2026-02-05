package org.firstinspires.ftc.teamcode.test.testing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "HoodTest", group = "test")
public class HoodTest extends OpMode {
    private Servo hood = null;
    WebCamTest lensCam = new WebCamTest();
    private Servo CameraMover;
    private ElapsedTime timer = new ElapsedTime();

    @Override
    public void init() {
        hood = hardwareMap.get(Servo.class,"hood");
        lensCam.init(hardwareMap);
        CameraMover = hardwareMap.get(Servo.class, "CameraMover");
        CameraMover.setPosition(0.3);
        timer.reset();
    }

    @Override
    public void loop() {
        double range = lensCam.CamAprilTagsRange("red");
        double Height = lensCam.CamAprilTagsElevation("red");

        if (timer.seconds() > 0.05) {
            if (Height > 3)  {
                CameraMover.setPosition(CameraMover.getPosition() - 0.005);
                timer.reset();
            } else if (Height < -3) {
                CameraMover.setPosition(CameraMover.getPosition() + 0.005);
                timer.reset();
            }
        }

        telemetry.addData("size ",range);
        telemetry.addData("hood position", hood.getPosition());
        telemetry.addData("Camera position", CameraMover.getPosition());
        telemetry.addData("height", Height);
    }
}
