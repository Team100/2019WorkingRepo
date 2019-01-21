/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.Breadboard;

import com.ctre.phoenix.motorcontrol.InvertType;

import edu.wpi.first.wpilibj.SerialPort;

/**
 * Add your docs here.
 */
public class Constants {

    /*
     * CAN/PWM IDs
     */
    public static int DRIVE_TRAIN_LEFT_MASTER_CANID = 0;
    public static int DRIVE_TRAIN_LEFT_FOLLOWER_CANID = 1;
    public static int DRIVE_TRAIN_RIGHT_MASTER_CANID = 15;
    public static int DRIVE_TRAIN_RIGHT_FOLLOWER_CANID = 14;
    public static SerialPort.Port NAVX_COMM_PORT = SerialPort.Port.kUSB;

    /*
     * Subsystem-specific constants
     */
    /*Drivetrain*/
    public static double DRIVE_TRAIN_MAX_MOTOR_OUTPUT = 0.5;
    public static boolean DRIVE_TRAIN_LEFT_MASTER_INVERT = false;
    public static InvertType DRIVE_TRAIN_LEFT_FOLLOWER_INVERT = InvertType.FollowMaster;
    public static boolean DRIVE_TRAIN_RIGHT_MASTER_INVERT = false;
    public static InvertType DRIVE_TRAIN_RIGHT_FOLLOWER_INVERT = InvertType.FollowMaster;
    /*SmartPivot system (NavX + Drivetrain)*/
    public static double DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT = 0.5;
    //NOT ACTUAL SPEED, PERCENTAGE OF PIVOT_MOTOR_OUTPUT(LINE ABOVE) [0 -> 1]
    public static double DRIVE_TRAIN_PIVOT_SLOW_PERCENTAGE = 0.6;
    public static int TURN_STOP_BUFFER = (int)(25*DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
    public static int TURN_SLOW_BUFFER = (int)(80*DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);


    //Ball Pickup
    public static int BALL_PICKUP_INTAKE_PWM = 8;

}
