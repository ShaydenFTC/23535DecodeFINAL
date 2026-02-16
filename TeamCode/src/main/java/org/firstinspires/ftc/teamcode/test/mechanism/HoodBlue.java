package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;

public class HoodBlue  {
    private Servo hood = null;

    private Servo CameraMover;

    WebCamTest lensCam;

    private ElapsedTime timer = new ElapsedTime();


    public void init(HardwareMap hardwareMap, WebCamTest sharedCam) {
        hood = hardwareMap.get(Servo.class,"hood");

        this.lensCam = sharedCam;

        CameraMover = hardwareMap.get(Servo.class, "CameraMover");

        CameraMover.setPosition(0.32);
        timer.reset();
    }
    public void MoveHood() {

        double size = lensCam.CamAprilTagsRange("blue");

        double Height = lensCam.CamAprilTagsElevation("blue");

        if (timer.seconds() > 0.05 && Height != 0) {
            if (Height > 1)  {
                CameraMover.setPosition(CameraMover.getPosition() - ((Height * 0.002) / (size * 0.03)));
                timer.reset();
            } else if (Height < -1) {
                CameraMover.setPosition(CameraMover.getPosition() -  ((Height * 0.0025) / (size * 0.03)));
                timer.reset();
            }
        } else if (timer.seconds() > 0.5) {
            CameraMover.setPosition(0.3);
        }
        hood.setPosition(7.43E-03 * size + -0.174);
    }
}
