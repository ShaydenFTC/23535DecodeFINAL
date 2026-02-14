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

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(-49.5, 49.5, Math.toRadians(305)))

                /// Moving to shooting position
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(315))
                /// shooting
                /// moving to intake area
                .splineToSplineHeading(new Pose2d(-12, 35, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .splineToSplineHeading(
                        new Pose2d(-12, 57, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-30, 30))
                /// moving to gate
                .splineToSplineHeading(new Pose2d(-12, 40, Math.toRadians(90)), Math.toRadians(90))
                .setTangent(Math.toRadians(270))
                .splineToLinearHeading(new Pose2d(4, 57.5, Math.toRadians(90)), Math.toRadians(90))
                /// openning gate
                .waitSeconds(2)
                /// moving to shoot
                .splineToLinearHeading(new Pose2d(4, 40, Math.toRadians(90)), Math.toRadians(90))
                .setTangent(Math.toRadians(180))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(135))
                /// shooting
                /// moving to intake
                .splineToSplineHeading(new Pose2d(12, 35, Math.toRadians(90)), Math.toRadians(90))
                /// intaking
                .splineToSplineHeading(
                        new Pose2d(12, 57, Math.toRadians(90)), Math.toRadians(90),
                        new TranslationalVelConstraint(15),
                        new ProfileAccelConstraint(-30, 30))
                .setTangent(Math.toRadians(270))
                .splineToSplineHeading(new Pose2d(-35, 35, Math.toRadians(315)), Math.toRadians(135))
                /// shooting
                /// moving to gate
                .splineToLinearHeading(new Pose2d(0, 35, Math.toRadians(90)), Math.toRadians(90))
                .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
