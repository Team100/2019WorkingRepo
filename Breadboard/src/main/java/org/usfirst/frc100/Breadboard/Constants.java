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

    public static int DRIVE_TRAIN_LEFT_MASTER_CANID = 0;
    public static int DRIVE_TRAIN_LEFT_FOLLOWER_CANID = 1;
    public static int DRIVE_TRAIN_RIGHT_MASTER_CANID = 15;
    public static int DRIVE_TRAIN_RIGHT_FOLLOWER_CANID = 14;
    public static boolean DRIVE_TRAIN_LEFT_MASTER_INVERT = false;
    public static InvertType DRIVE_TRAIN_LEFT_FOLLOWER_INVERT = InvertType.FollowMaster;
    public static boolean DRIVE_TRAIN_RIGHT_MASTER_INVERT = false;
    public static InvertType DRIVE_TRAIN_RIGHT_FOLLOWER_INVERT = InvertType.FollowMaster;

    public static double DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT = 0.2;
    public static double DRIVE_TRAIN_MAX_MOTOR_OUTPUT = 0.5;

    public static SerialPort.Port NAVX_COMM_PORT = SerialPort.Port.kUSB;
    public static int TURN_BUFFER = 11;


    public static int BALL_PICKUP_INTAKE_PWM = 8;
    
    public static double BALL_PICKUP_DURATION = 0.5;

}
