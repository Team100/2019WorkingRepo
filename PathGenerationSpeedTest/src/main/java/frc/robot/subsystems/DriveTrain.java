/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.*;
/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem implements PIDOutput{
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public static WPI_TalonSRX driveTrainRightMaster;
  public static WPI_TalonSRX driveTrainLeftMaster;
  public static DifferentialDrive driveTrainDifferentialDrive1;
  public static WPI_VictorSPX driveTrainRightFollower;
  public static WPI_VictorSPX driveTrainLeftFollower;
  public static Solenoid driveTrainShifter;
  public PIDController turnPID;
  public static AHRS ahrs;


  public DriveTrain(){
    driveTrainShifter=new Solenoid(0);
        driveTrainLeftMaster = new WPI_TalonSRX(Constants.DRIVE_TRAIN_LEFT_MASTER_CANID);
        driveTrainRightMaster = new WPI_TalonSRX(Constants.DRIVE_TRAIN_RIGHT_MASTER_CANID);
        driveTrainDifferentialDrive1 = new DifferentialDrive(driveTrainLeftMaster, driveTrainRightMaster);
        addChild("Differential Drive 1", driveTrainDifferentialDrive1);
        driveTrainDifferentialDrive1.setSafetyEnabled(false);
        driveTrainDifferentialDrive1.setExpiration(0.7);
        driveTrainDifferentialDrive1.setMaxOutput(Constants.DRIVE_TRAIN_MAX_MOTOR_OUTPUT);
        
        driveTrainLeftFollower = new WPI_VictorSPX(Constants.DRIVE_TRAIN_LEFT_FOLLOWER_CANID);
        driveTrainRightFollower = new WPI_VictorSPX(Constants.DRIVE_TRAIN_RIGHT_FOLLOWER_CANID);

        driveTrainLeftFollower.follow(driveTrainLeftMaster);
        driveTrainRightFollower.follow(driveTrainRightMaster);
        ahrs=new AHRS(Port.kOnboard);
        driveTrainLeftMaster.setInverted(Constants.DRIVE_TRAIN_LEFT_MASTER_INVERT);
        driveTrainLeftFollower.setInverted(Constants.DRIVE_TRAIN_LEFT_FOLLOWER_INVERT);
        driveTrainRightMaster.setInverted(Constants.DRIVE_TRAIN_RIGHT_MASTER_INVERT);
        driveTrainRightFollower.setInverted(Constants.DRIVE_TRAIN_RIGHT_FOLLOWER_INVERT);
        driveTrainLeftMaster.setSensorPhase(true);
        driveTrainRightMaster.setSensorPhase(true);
        driveTrainLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        driveTrainRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        turnPID = new PIDController(Constants.DRIVETRAIN_P, Constants.DRIVETRAIN_I, Constants.DRIVETRAIN_D, ahrs, this);
        turnPID.setInputRange(Constants.DT_TURN_MIN_ROTATION_ANGLE, Constants.DT_TURN_MAX_ROTATION_ANGLE);
        turnPID.setContinuous(true);
        turnPID.setOutputRange(Constants.DT_TURN_MIN_OUTPUT, Constants.DT_TURN_MAX_OUTPUT);
        turnPID.setAbsoluteTolerance(Constants.DT_TURN_ABSOLUTE_TOLERANCE);
        driveTrainLeftMaster.setSensorPhase(true);
        driveTrainRightMaster.setSensorPhase(true);
      }
  public static double boundHalfDegrees(double angle_degrees) {
    while (angle_degrees >= 180.0) angle_degrees -= 360.0;
    while (angle_degrees < -180.0) angle_degrees += 360.0;
    return angle_degrees;
}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    setDefaultCommand(new Drive());
  }

  @Override
  public void pidWrite(double output) {
    driveTrainLeftMaster.set(ControlMode.PercentOutput, output*Constants.DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
    driveTrainRightMaster.set(ControlMode.PercentOutput, output*Constants.DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
  }
  public void stop(){
    driveTrainLeftMaster.set(ControlMode.PercentOutput, 0);
    driveTrainRightMaster.set(ControlMode.PercentOutput, 0);
  }
  public void turn(double leftPower, double rightPower){
    driveTrainLeftMaster.set(ControlMode.PercentOutput, rightPower);
    driveTrainRightMaster.set(ControlMode.PercentOutput, leftPower);
  }
  public void pidTurn(){
    turn(turnPID.get(), turnPID.get());
  }
  public double getVisionAngle() {
    double angle;
    try{
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("camera");
    String cameraData = table.getEntry("data").getString(null);
    //if(){}
    angle = Double.parseDouble(cameraData.substring(cameraData.indexOf("angle\":") + 7, cameraData.indexOf("angle\":") + 11));   
  }catch(Exception e){
      angle=0;
    }
    System.out.println("Angle: " + angle);

    return angle;
  }
}
