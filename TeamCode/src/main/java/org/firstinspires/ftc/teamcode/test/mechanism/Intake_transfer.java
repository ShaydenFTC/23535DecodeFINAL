package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Intake_transfer {
    private DcMotorEx intake = null;
    private DcMotorEx transfer_motor = null;
    private Servo kicker = null;

    private CRServo back_transfer_servo;

    private double intakePower = 1.0;

    private double transferMotorPower = 1.0;

    private ElapsedTime runtime = new ElapsedTime();

    private ElapsedTime kickerTimer = new ElapsedTime();

    boolean StartTimer = false;

    public boolean Kicking = false;


    public void init(HardwareMap hardwareMap) {

        intake = hardwareMap.get(DcMotorEx.class,"Intake");

        transfer_motor = hardwareMap.get(DcMotorEx.class,"Transfer_motor");

        kicker = hardwareMap.get(Servo.class,"Kicker");

        kicker.setDirection(Servo.Direction.FORWARD);

        back_transfer_servo = hardwareMap.get(CRServo.class,"back_transfer_servo");


    }

    public void intake(double Gamepad1power, double Gamepad2power) {
        if (!Kicking) {
        intake.setPower(-intakePower * Gamepad1power);
        transfer_motor.setPower(transferMotorPower * Gamepad2power);
        back_transfer_servo.setPower(transferMotorPower * Gamepad2power); }
    }

    public void intakeShoot(boolean ButtonPressed) {
        if (ButtonPressed) {
        intake.setPower(-1);
        transfer_motor.setPower(1);
        back_transfer_servo.setPower(1);
        } else {
            intake.setPower(0);
            transfer_motor.setPower(0);
            back_transfer_servo.setPower(0);
        }
    }

    public void setKicker(boolean ButtonPressed) {
        if (ButtonPressed) {
            kicker.setPosition(0.43);
            Kicking = true;
        } else {
            kicker.setPosition(0.18);
            Kicking = false;
        }
    }

    public void intakein(boolean ButtonPressed) {
        intake.setPower(-1);
    }

    public void transfer(boolean ButtonPressed) {
        intake.setPower(1);
    }

    public void transferback(boolean ButtonPressed) {
        intake.setPower(1);
    }




}
