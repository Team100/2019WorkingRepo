package org.usfirst.frc100.RobotArmWithEncoder;

public class Constants{
    /*
     * ARM ENCODER BUFFERS
     */
    public static int ARM_ENCODER_LOWER_BUFFER = 100;
    public static int ARM_ENCODER_MAX_VALUE = 55000;
    public static int ARM_ENCODER_TOP_BUFFER = 1000;

    /*
     * ARM MOVEMENT DURATIONS
     */
    public static double ROBOT_ELEVATOR_STATE_CONTROLLER_MAX_TRANSITION_TIME_TOP_BOTTOM = 7500;
    
    /*
     * HOMING Config
     */

    public static double ARM_MOTOR_HOMING_POWER = 0.5;
    public static int HOMING_GOING_DOWN_MAX_DURATION = 750;
    public static int HOMING_GOING_UP_MAX_DURATION = 750;

    /*
     * ARM MOVEMENT SPEEDS
     */
    public static double ELEVATOR_UP_SPEED = 1;
    public static double ELEVATOR_DOWN_SPEED = 1;

    /*
     * ARM MOVEMENT MODIFIERS
     */
    public static int ELEVATOR_DOWN_MODIFIER = -1;
    public static int ELEVATOR_UP_MODIFIER = 1;

    /*
     * MISC 
     */
    public static double ELEVATOR_OI_INTERRUPT_DEADBAND = 0.25;



    //                  NEW VERSION

    public static int ELEVATOR_SETPOINT_ACCEPTABLE_VARIATION = 1000;

    public static double ELEVATOR_P = 0.0005;
    public static double ELEVATOR_I = 0;
    public static double ELEVATOR_D = 0;
    public static double ELEVATOR_F = 1;

   
}