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
                .setConstraints(55, 55, Math.toRadians(180), Math.toRadians(180), 15)
                .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(60, 15, Math.toRadians(0)))

                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, 17, Math.toRadians(345)), Math.toRadians(180))
                .waitSeconds(6)
                /// moving to intake area
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(34, 35, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .splineToSplineHeading(
                        new Pose2d(34, 55, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-15, 15))
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(48, 17, Math.toRadians(345)), Math.toRadians(0))
                .waitSeconds(6)
                .setReversed(true)
                .splineToSplineHeading(new Pose2d(40, 17, Math.toRadians(345)), Math.toRadians(0))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
