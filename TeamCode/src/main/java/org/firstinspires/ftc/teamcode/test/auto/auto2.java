package org.firstinspires.ftc.teamcode.test.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.test.mechanism.Drive;
import org.firstinspires.ftc.teamcode.test.mechanism.Intake_transfer;

@Autonomous(name = "auto2", group = "test")
public class auto2 extends OpMode {

    DcMotorEx launcher = null;
    Intake_transfer intake_transfer = new Intake_transfer();
    Drive drive = new Drive();

    double speed_percentage = 20.0;
    private ElapsedTime runtime = new ElapsedTime();
    private ElapsedTime kickerTimer = new ElapsedTime();

    private boolean hasStarted = false;

    @Override
    public void init() {
        drive.init(hardwareMap);

        launcher = hardwareMap.get(DcMotorEx.class, "launcher");
        launcher.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        launcher.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        launcher.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.FLOAT);

        intake_transfer.init(hardwareMap);

        telemetry.addData("Status", "Initialized - Ready to run");
        telemetry.update();
    }

    @Override
    public void start() {
        runtime.reset();
        kickerTimer.reset();
        hasStarted = true;
        intake_transfer.setKicker(false);
    }

    @Override
    public void loop() {
        double currentTime = runtime.seconds();

        // Shooter runs at 45% power the whole time
        launcher.setPower(0.45);

        // Kicker kicks every 2 seconds
        if (kickerTimer.seconds() >= 8.0) {
            intake_transfer.setKicker(true);
            kickerTimer.reset(); }

        // Intake and transfer run after 2 seconds at 50% speed
        if (currentTime >= 2.0 && currentTime < 12.0) {
            intake_transfer.intake(0.45, 0.45);
        } else if (currentTime < 2.0) {
            intake_transfer.intake(0, 0);
        }

        // After 12 seconds, back up
        if (currentTime >= 12.0) {
            double axial = -1;  // Negative for backing up
            double lateral = 0;
            double yaw = 0;
            drive.setDriveMotorPower(axial, lateral, yaw, speed_percentage);

            // Stop intake when backing up
            intake_transfer.intake(0, 0);
        } else {
            // Don't move for the first 12 seconds
            drive.setDriveMotorPower(0, 0, 0, speed_percentage);
        }

        // Telemetry
        telemetry.addData("Status", "Run Time: %.1f", currentTime);
        telemetry.addData("Launcher Power", "45%%");
        telemetry.addData("Kicker Timer", "%.1f", kickerTimer.seconds());
        telemetry.addData("Phase", currentTime < 2.0 ? "Shooting Only" :
                currentTime < 12.0 ? "Shooting + Intake" : "Backing Up");
        telemetry.update();
    }
}