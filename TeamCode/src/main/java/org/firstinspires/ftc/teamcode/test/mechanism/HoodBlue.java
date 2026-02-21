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

        CameraMover.setPosition(0.2);
        timer.reset();
    }
    public void MoveHood() {

        double size = lensCam.CamAprilTagsRange("blue");

        double Height = lensCam.CamAprilTagsElevation("blue");

        if (timer.seconds() > 0.05) {
            if (Height > 0.1)  {
                CameraMover.setPosition(CameraMover.getPosition() - (0.0075));
                timer.reset();
            } else if (Height < -0.1) {
                CameraMover.setPosition(CameraMover.getPosition() +  (0.0075));
                timer.reset();
            }
        } else if (timer.seconds() > 2) {
            CameraMover.setPosition(0.2);
            timer.reset();
        }
        hood.setPosition(9.85E-03 * size + -0.262);
    }
}
