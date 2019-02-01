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
    public static int DRIVE_TRAIN_RIGHT_FOLLOWER_CANID = 14;
    public static int DRIVE_TRAIN_RIGHT_MASTER_CANID = 15;
    public static SerialPort.Port NAVX_COMM_PORT = SerialPort.Port.kUSB;
    public static int ELEVATOR_MASTER_CANID = 10;
    public static int ELEVATOR_FOLLOWER_CANID = 11;
    public static int BALL_PICKUP_INTAKE_PWM = 8;

    /*
     * Drivetrain
     */
    public static double DRIVE_TRAIN_MAX_MOTOR_OUTPUT = 0.8;
    public static boolean DRIVE_TRAIN_LEFT_MASTER_INVERT = false;
    public static InvertType DRIVE_TRAIN_LEFT_FOLLOWER_INVERT = InvertType.FollowMaster;
    public static boolean DRIVE_TRAIN_RIGHT_MASTER_INVERT = false;
    public static InvertType DRIVE_TRAIN_RIGHT_FOLLOWER_INVERT = InvertType.FollowMaster;
    //PID Rotation with NavX
    public static double DT_MASTER_P = 0.007;
    public static double DT_MASTER_I = 0.00004;
    public static double DT_MASTER_D = 0.002;
    public static double DT_MASTER_F = 0;
    public static double DT_TURN_MIN_ROTATION_ANGLE = -180;
    public static double DT_TURN_MAX_ROTATION_ANGLE = 180;
    public static double DT_TURN_MIN_OUTPUT = -1;
    public static double DT_TURN_MAX_OUTPUT = 1;
    public static double DT_TURN_ABSOLUTE_TOLERANCE = 1;
    public static double DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT = 0.5;
    public static boolean ARCADE_DRIVE_MODE = true;
    //Pathfinding
    public static final long EXECUTION_LOOP_INTERVAL = 50;
    public static double DT_MASTER_kP = 1.0;
    public static double DT_MASTER_kI = 0.0;
    public static double DT_MASTER_kD = 0.0;
    public static double DT_MASTER_kF = 2.0;
	public static final double RAMP_RATE_DRIVETRAIN = 0.25;
	public static final double LEFT_DRIVETRAIN_MODIFIER = 1.0;
	public static final double RIGHT_DRIVETRAIN_MODIFIER = -1.0;
    public static final double DRIVETRAIN_TICKS_PER_METER = 962.446539102*16/37; //360*4*(1/3)*(1/(0.15875*3.1416));
    public static final String[] PATHS_STRINGS = {"test"};

    /*
     * Elevator
     */
    public static double ELEVATOR_GRAVITY_COUNTER = 0.1;
    public static double ELEVATOR_MAX_MOTOR_OUTPUT = 0.2;
    public static boolean ELEVATOR_MASTER_INVERT = false;
    public static InvertType ELEVATOR_FOLLOWER_INVERT = InvertType.FollowMaster;
    

}
