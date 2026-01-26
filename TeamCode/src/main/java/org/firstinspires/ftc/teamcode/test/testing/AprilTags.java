package org.firstinspires.ftc.teamcode.test.testing;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
@Disabled

@TeleOp(name="AprilTags")
public class AprilTags extends OpMode {

    private DcMotorEx aim = null;

    private HuskyLens huskylens;
    private int teamid = 0;

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        huskylens = hardwareMap.get(HuskyLens.class, "huskylens");
        huskylens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

        aim = hardwareMap.get(DcMotorEx.class,"aim");
        aim.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        aim.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        aim.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);


        boolean blueteam = false;
        boolean redteam = false;

        runtime.reset();

    }

    @Override
    public void loop() {

        if(gamepad1.dpad_left && aim.getCurrentPosition() > -20) {
            aim.setPower(-0.15);
        }
        else if(gamepad1.dpad_right && aim.getCurrentPosition() < 20) {
            aim.setPower(0.15);
        }
        else {
            aim.setPower(0);
        }

        telemetry.addData("ZeroPowerBehavior", aim.getZeroPowerBehavior());
        telemetry.addData("Encoder Position", aim.getCurrentPosition());
        telemetry.addData("power " , aim.getPower());


     /* if (gamepad1.x) {
            boolean blueteam = true;
            teamid = 1;
        }
        if (gamepad1.y) {
            boolean redteam = true;
            teamid = 5;
        }

        HuskyLens.Block[] blocks = huskylens.blocks(); // ID1=20, 2=21, 3=22, 4=23, 5=24
        for (HuskyLens.Block block : blocks) {
            int id = block.id;
            double xcoord = 0;
            double ycoord = 0;
            String order = null;

            if (id == 2) {
                order = "GPP";
            }
            if (id == 3) {
                order = "PGP";
            }
            if (id == 4) {
                order = "PPG";
            }

            if (block.id == teamid) {
                double width = block.width;
                double height = block.height;
                xcoord = block.x - 160;
                ycoord = block.y;


                if (xcoord > 25) {
                    aim.setPower(0.5);
                }
                else if (xcoord < -25) {
                    aim.setPower(-0.5);
                }
                else {
                    aim.setPower(0);
                }

            }

            telemetry.addData("ID", id);
            telemetry.addData("X", xcoord);
            telemetry.addData("Y", ycoord);
            telemetry.addData("Order", order);
            telemetry.addData("Status", "Run Time: " + runtime.seconds());

        }

        if (blocks.length == 0) {
            aim.setPower(0);
        } */

    }
}


