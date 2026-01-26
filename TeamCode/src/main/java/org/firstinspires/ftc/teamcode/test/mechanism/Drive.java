package org.firstinspires.ftc.teamcode.test.mechanism;



import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drive {


    private DcMotorEx leftFrontDrive  = null;
    private DcMotorEx rightFrontDrive = null;
    private DcMotorEx leftBackDrive   = null;
    private DcMotorEx rightBackDrive  = null;


    public void init(HardwareMap hardwareMap)
    {

        //Hardware mapping
        leftFrontDrive  = hardwareMap.get(DcMotorEx.class,"leftFront");
        rightFrontDrive = hardwareMap.get(DcMotorEx.class,"rightFront");
        leftBackDrive   = hardwareMap.get(DcMotorEx.class,"leftBack");
        rightBackDrive  = hardwareMap.get(DcMotorEx.class,"rightBack");

        // Setting the direction
        leftFrontDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBackDrive.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotorSimple.Direction.REVERSE);

    }

    public void setDriveMotorPower(double forward, double right, double rotate, double cap)
    {

        // Drivetrain

        double leftFrontPower   = 0.0;
        double leftBackPower    = 0.0;
        double rightFrontPower  = 0.0;
        double rightBackPower   = 0.0;

        leftFrontPower  = forward + right + rotate;
        leftBackPower   = forward - right + rotate;
        rightFrontPower = forward - right - rotate;
        rightBackPower  = forward + right - rotate;

        double maxPower = 1.0;

        maxPower = Math.max(Math.abs(leftFrontPower), Math.abs(leftBackPower));
        maxPower = Math.max(maxPower, Math.abs(rightFrontPower));
        maxPower = Math.max(maxPower, Math.abs(rightBackPower));


        if (maxPower > 1) {
            leftFrontPower  = leftFrontPower / maxPower;
            leftBackPower   = leftBackPower / maxPower;
            rightFrontPower = rightFrontPower / maxPower;
            rightBackPower  = rightBackPower / maxPower;
        }


        leftFrontPower  = leftFrontPower * cap / 100.0;
        leftBackPower   = leftBackPower * cap / 100.0;
        rightFrontPower = rightFrontPower * cap / 100.0;
        rightBackPower  = rightBackPower * cap / 100.0;

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);


    }
    public void motor_test(double leftFrontPower, double rightFrontPower, double leftBackPower, double rightBackPower) {
        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }


}
