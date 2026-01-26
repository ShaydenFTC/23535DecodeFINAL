package org.firstinspires.ftc.teamcode.test.mechanism;


import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
public class Launcher {
    private DcMotorEx Launcher = null;

    public void init(HardwareMap hardwareMap) {

        Launcher = hardwareMap.get(DcMotorEx.class,"launcher");

    }

    public void setLauncher(boolean ypressed) {
        if (ypressed) {
            Launcher.setPower(1);
        } else {
            Launcher.setPower(0);
        }
    }





}
