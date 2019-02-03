/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.analog.adis16448.frc.ADIS16448_IMU;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

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

  public static double boundHalfDegrees(double angle_degrees) {
    while (angle_degrees >= 180.0) angle_degrees -= 360.0;
    while (angle_degrees < -180.0) angle_degrees += 360.0;
    return angle_degrees;
}

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
