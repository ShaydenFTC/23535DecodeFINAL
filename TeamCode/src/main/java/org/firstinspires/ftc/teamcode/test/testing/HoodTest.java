package org.firstinspires.ftc.teamcode.test.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.test.mechanism.HuskyLensCam;
@Disabled

@TeleOp(name = "HoodTest", group = "test")
public class HoodTest extends OpMode {
    private Servo hood = null;

    HuskyLensCam lensCam = new HuskyLensCam();

    @Override
    public void init() {
        hood = hardwareMap.get(Servo.class,"hood");

        lensCam.init(hardwareMap);
    }
    @Override
    public void loop() {

        double size = lensCam.SeeAprilTagsSize("red");

        hood.setPosition(-2.74E-04 * size + 0.791);
        telemetry.addData("size ",size);
        telemetry.addData("hood position", hood.getPosition());
        telemetry.addData("equation ", -2.74E-04 * size + 0.791);
    }
}
