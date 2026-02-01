package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;

public class Hood  {
    private Servo hood = null;

    private Servo CameraMover;

    WebCamTest lensCam = new WebCamTest();


    public void init(HardwareMap hardwareMap) {
        hood = hardwareMap.get(Servo.class,"hood");

        lensCam.init(hardwareMap);

        CameraMover = hardwareMap.get(Servo.class, "CameraMover");
    }
    public void MoveHood() {

        double size = lensCam.CamAprilTagsRange("red");

        double Height = lensCam.CamAprilTagsElevation("red");

        if (Height > 10 && CameraMover.getPosition() < 0.5)  {
            CameraMover.setPosition(CameraMover.getPosition() + 0.1);
        } else if (Height < -10 && CameraMover.getPosition() > 0.2) {
            CameraMover.setPosition(CameraMover.getPosition() - 0.1);
        }
        hood.setPosition(-2.74E-04 * size + 0.791);
    }
}
