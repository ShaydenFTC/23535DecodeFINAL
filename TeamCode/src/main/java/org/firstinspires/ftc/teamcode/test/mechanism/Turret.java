package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret {
    private DcMotorEx aim  = null;
    private HuskyLens huskylens;
    private int teamid = 0;


        public void init(HardwareMap hardwareMap) {
            aim  = hardwareMap.get(DcMotorEx.class,"aim");

            huskylens = hardwareMap.get(HuskyLens.class, "huskylens");
            huskylens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);

            boolean blueteam = false;
            boolean redteam = false;

        }
        public void TurretF(boolean y_pressed, boolean x_pressed) {

            if (x_pressed == true) {
                boolean blueteam = true;
                teamid = 1;
            }
            if (y_pressed == true) {
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

                if (block.id == 0) {
                    double width = block.width;
                    double height = block.height;
                    xcoord = block.x - 160;
                    ycoord = block.y;

                    aim.setPower(xcoord/200);

                    // Calculate turret power based on y-value
                    // y-value/240 (0-1)--> adjust power based on those values
                    // OR set max motor power (1) to furthest shooting distance (y-value) and go to 0
                    // Also hood servo?

                }


            }

        }
    }

