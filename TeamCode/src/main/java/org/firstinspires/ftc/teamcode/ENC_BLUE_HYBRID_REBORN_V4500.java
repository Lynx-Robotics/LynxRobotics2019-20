package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "ENC_BLUE_HYBRID_REBORN_4500")
public class ENC_BLUE_HYBRID_REBORN_V4500 extends autoBaseV3 {

    double timeUntilDetection = 0, timeUntilDetectionFinal = 0;


    int step = 1;
    int phase = 0;

    double RIGHT_DIST_STRAFE_ONE, CORRECTION_BEFORE_BACKWARDS_LEFT, CORRECTION_AFTER_TAPE_SPOTTED;
    double DISTANCE_GO_TO_FOUNDATION, DISTANCE_STRAFE_TO_TAPE;



    @Override
    public void runOpMode() throws InterruptedException {
        chart.init(hardwareMap);

        waitForStart();
        chart.globalTime.reset();

        //raise motor up until we get pretty high so sensors can sense
        while (opModeIsActive()) {
            if (gamepad1.a) {
                phase++;
                while (opModeIsActive() && gamepad1.a) ;
            }

            if (gamepad1.y) {
                step++;
                while (opModeIsActive() && gamepad1.y) ;
            }

            if (phase == 1) {
                chart.globalTime.reset();
                elevControl(chart.elevMotor, 500, 1.0);
                phase++;
            }
            //go forward to the blocks

            if (phase == 2) {
                goToPositionForward(distance2encoderNew(30), 0.8);
                sleep(350);
                phase++;
            }

            //strafe to the right for a certain distance
            if (phase == 3) {
                correctionRight(distance2encoderNew(1.95), 0.6);
                encoderStrafeRight(distance2encoderNew(5), 0.35);
                phase++;
            }

            //strafe left until we see a block
            if (phase == 4) {
                chart.globalTime.reset();
                strafeLeft(0.3);
                while (opModeIsActive() && !SkyStoneReBornRight(chart.colorSensorRight)) {
                }
                rest();
                timeUntilDetectionFinal = chart.globalTime.milliseconds();
                phase++;
            }
            rest();
            /*
            VARIABLE DECLARATION
            ---------------------------------------------------------------------------
             */
            if(isInRange(timeUntilDetectionFinal, 200, 434.65) || timeUntilDetectionFinal < 434.65){ //if first block spotted is skystone set following param
                RIGHT_DIST_STRAFE_ONE = 2.5;
                CORRECTION_BEFORE_BACKWARDS_LEFT = 0.95;
                CORRECTION_AFTER_TAPE_SPOTTED = 0;
                DISTANCE_GO_TO_FOUNDATION = 64;
                DISTANCE_STRAFE_TO_TAPE = 60;
            } else if(isInRange(timeUntilDetectionFinal, 200, 1348.6765)){
                RIGHT_DIST_STRAFE_ONE = 4.0;
                CORRECTION_BEFORE_BACKWARDS_LEFT = 0.95;
                CORRECTION_AFTER_TAPE_SPOTTED = 0;
                DISTANCE_GO_TO_FOUNDATION = 64;
            } else if (isInRange(timeUntilDetectionFinal, 200, 2072.964)){
                RIGHT_DIST_STRAFE_ONE = 4.0;
                CORRECTION_BEFORE_BACKWARDS_LEFT = 0.95;
                CORRECTION_AFTER_TAPE_SPOTTED = 0;
                DISTANCE_GO_TO_FOUNDATION = 64;
            }
            /*
            ---------------------------------------------------------------------------
             */

            //strafe right for a certain distance
            if(phase == 5) {
                encoderStrafeRight(distance2encoderNew(2.5), 0.3);
                phase++;
            }

            //extract the block
            if(phase == 6) {
                elevMotorDown(chart.elevMotor, 5, -1.0);
                goToPositionForward(distance2encoderNew(1.7), 0.4);
                dropDL();
                sleep(500);
                phase++;
            }

            //raise the block and secure from Lava
            if(phase==7) {
                elevControl(chart.elevMotor, 360, 1.0);
                phase++;
            }

            //reposition backwards so we can strafe
            /*
            @9:40PM 2/5/2020 Diego has done the following changes:
            - replaced the phase 8 found at the bottom with a phase 7
            - added a sleep variable so that the motor can raise and lower at the same time.
            - decreased the max voltage goToPositionBackward(10), 1.0 to go a dist of 8
            - added a new distance of 2 with half speed
                - Should decrease error in stoppage
             */
            if(phase==8) {
                sleep(500);
//                correctionLeft(distance2encoderNew(0.95), 0.6);
                correctionLeft(distance2encoderNew(CORRECTION_BEFORE_BACKWARDS_LEFT), 0.6);
                goToPositionBackwardRealFast(distance2encoderNew(10), 1.0); //can be replaced if causes troubles
                phase++;
//                phase++;//should be removed if the phase of this condition is returned to 8
            }

            //strafe until we see tape
            if(phase==9) {
                encoderStrafeLeft(DISTANCE_STRAFE_TO_TAPE, 0.4);
                sleep(200);

                correctionRight(distance2encoderNew(CORRECTION_AFTER_TAPE_SPOTTED), 0.6);
                sleep(200);
                encoderStrafeLeft(distance2encoderNew(64), 0.35);
                phase++;
                phase++;
            }


            if(phase == 11){
                goToPositionForward(distance2encoderNew(DISTANCE_GO_TO_FOUNDATION), 0.6);
                phase++;
            }

            if(phase == 12){
                raiseDL();
                sleep(500);
                elevMotorDown(chart.elevMotor, 8, -1.0);
                phase++;
            }

            if(phase == 13){
                goToPositionBackward(distance2encoderNew(55), 0.8);
                phase++;
            }

            if(phase == 14){
                elevControl(chart.elevMotor, 500, 1.0);
                chart.middleGrab.setPosition(0.5);
                goToPositionForward(distance2encoderNew(3), 0.4);
                encoderStrafeRight(distance2encoderNew(48), 0.4);

                correctionRight(0.95, 0.6);
                goToPositionForward(distance2encoderNew(10), 0.6);

                strafeRight(0.38);
                while(opModeIsActive() && !bottomTapeSensorDetectedBlueReborn(chart.bottomColorSensor)){

                }
                rest();
                phase++;
            }

            waitUntilEnd();
        }
    }

    public void waitUntilEnd() {
        while (opModeIsActive() && !gamepad1.a) {
            telemetry.addData("Time Until Detection: (ms)", timeUntilDetectionFinal);
            telemetry.update();

        }
    }
}