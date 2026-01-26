package org.firstinspires.ftc.teamcode.test.mechanism;

import com.qualcomm.hardware.dfrobot.HuskyLens;
import com.qualcomm.robotcore.hardware.HardwareMap;

//FOR THIS TO WORK: Make it in telop so we can set to blueteam (id = 1) or redteam (id = 5)

//Can give us the pattern (order) and x-value of apriltag

public class HuskyLensCam {
    private HuskyLens huskylens;
    private int teamid = 2;

    private double lastSeenXCoord = 0;
    private double lastSeenSize = 0;

    public void init(HardwareMap hardwareMap) {
        huskylens = hardwareMap.get(HuskyLens.class, "huskylens");
        huskylens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION);
    }

    public double SeeAprilTagsX(String team) {

        if (team.equals("red")) { //Set for testing, not actual values
            teamid = 2;
        }
        else {
            teamid = 1;
        }

        HuskyLens.Block[] blocks = huskylens.blocks(); // ID1=20 (Blue), 2=21, 3=22, 4=23, 5=24 (Red)
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
                ycoord = block.y - 120;
                lastSeenXCoord = xcoord;
                return xcoord;

            }


        }


        return lastSeenXCoord; //Returns large number, not 0, so PID knows not to go there
    }
    public double SeeAprilTagsSize(String team) {

        if (team.equals("red")) { //Set for testing, not actual values
            teamid = 2;
        }
        else {
            teamid = 1;
        }

        HuskyLens.Block[] blocks = huskylens.blocks(); // ID1=20 (Blue), 2=21, 3=22, 4=23, 5=24 (Red)
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
                double size = Math.abs(width) * Math.abs(height);
                xcoord = block.x - 160;
                ycoord = block.y - 120;
                lastSeenSize = size;
                return size;

            }


        }


        return lastSeenSize; //Returns large number, not 0, so PID knows not to go there
    }

    }



