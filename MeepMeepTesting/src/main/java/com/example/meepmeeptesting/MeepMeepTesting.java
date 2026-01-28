package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.MinVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

import java.util.Arrays;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(60, 13.5, Math.toRadians(180)))

                .setTangent(Math.toRadians(180))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.toRadians(180))
                .waitSeconds(3)

                .setTangent(Math.toRadians(0))
                .splineToLinearHeading(new Pose2d(35, 30, Math.toRadians(90)), Math.toRadians(0))
                .setTangent(Math.toRadians(90))
                .splineToLinearHeading(
                        new Pose2d(35, 50, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(10),
                        new ProfileAccelConstraint(-25, 25))

                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(-15, 15, Math.toRadians(135)), Math.toRadians(180))
                .waitSeconds(3)
                .setTangent(Math.toRadians(45))
                .splineToLinearHeading(new Pose2d(20, 20, Math.toRadians(90)), Math.toRadians(0))


                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
