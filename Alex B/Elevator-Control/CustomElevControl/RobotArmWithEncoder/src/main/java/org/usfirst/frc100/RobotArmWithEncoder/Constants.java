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
    public static int ELEVATOR_DOWN_MODIFIER = 1;
    public static int ELEVATOR_UP_MODIFIER = -1;
}