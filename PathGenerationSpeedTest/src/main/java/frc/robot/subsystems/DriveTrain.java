/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.commands.*;
/**
 * Add your docs here.
 */
public class DriveTrain extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public static WPI_TalonSRX driveTrainRightMaster;
  public static WPI_TalonSRX driveTrainLeftMaster;
  public static DifferentialDrive driveTrainDifferentialDrive1;
  public static WPI_VictorSPX driveTrainRightFollower;
  public static WPI_VictorSPX driveTrainLeftFollower;
  public static Solenoid driveTrainShifter;
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

        driveTrainLeftMaster.setInverted(Constants.DRIVE_TRAIN_LEFT_MASTER_INVERT);
        driveTrainLeftFollower.setInverted(Constants.DRIVE_TRAIN_LEFT_FOLLOWER_INVERT);
        driveTrainRightMaster.setInverted(Constants.DRIVE_TRAIN_RIGHT_MASTER_INVERT);
        driveTrainRightFollower.setInverted(Constants.DRIVE_TRAIN_RIGHT_FOLLOWER_INVERT);
        driveTrainLeftMaster.setSensorPhase(true);
        driveTrainRightMaster.setSensorPhase(true);
        driveTrainLeftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        driveTrainRightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

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
}
