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

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(60, 15, Math.toRadians(180)))
                /// Moving to shooting position
                .setTangent(Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-55, 10, Math.toRadians(290)), Math.toRadians(180))
                /// shooting
                .waitSeconds(3)
                /// moving to intake area
                .setTangent(Math.toRadians(0))
                .splineToSplineHeading(new Pose2d(34, 35, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .splineToSplineHeading(
                        new Pose2d(36, 50, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-15, 15))
                /// moving to shoot
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-55, 10, Math.toRadians(290)), Math.toRadians(180))
                /// shooting
                .waitSeconds(3)
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
