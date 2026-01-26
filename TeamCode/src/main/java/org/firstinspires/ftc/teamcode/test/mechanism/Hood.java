package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Hood  {
    private Servo hood = null;

    HuskyLensCam lensCam = new HuskyLensCam();


    public void init(HardwareMap hardwareMap) {
        hood = hardwareMap.get(Servo.class,"hood");

        lensCam.init(hardwareMap);
    }
    public void MoveHood() {

        double size = lensCam.SeeAprilTagsSize("red");

        hood.setPosition(-2.74E-04 * size + 0.791);
    }
}
