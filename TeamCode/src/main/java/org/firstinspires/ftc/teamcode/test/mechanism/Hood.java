package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.test.testing.WebCamTest;

public class Hood  {
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

        double size = lensCam.CamAprilTagsRange("red");

        double Height = lensCam.CamAprilTagsElevation("red");

        if (timer.seconds() > 0.05) {
            if (Height > 2)  {
                CameraMover.setPosition(CameraMover.getPosition() - 0.01);
                timer.reset();
            } else if (Height < -2) {
                CameraMover.setPosition(CameraMover.getPosition() + 0.01);
                timer.reset();
            }
        }
        hood.setPosition(7.43E-03 * size + -0.174);
    }
}
