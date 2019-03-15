/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class PathfindingSimpler extends Command {
  NetworkTableInstance networkTables = NetworkTableInstance.getDefault();
  NetworkTable table = networkTables.getTable("camera");

  public PathfindingSimpler() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.driveTrain.driveTrainLeftMaster.config_kP(0, Constants.DRIVETRAIN_P);
    Robot.driveTrain.driveTrainLeftMaster.config_kI(0, Constants.DRIVETRAIN_I);
    Robot.driveTrain.driveTrainLeftMaster.config_kD(0, Constants.DRIVETRAIN_D);
    Robot.driveTrain.driveTrainLeftMaster.config_kF(0, Constants.DRIVETRAIN_F);

    Robot.driveTrain.driveTrainRightMaster.config_kP(0, Constants.DRIVETRAIN_P);
    Robot.driveTrain.driveTrainRightMaster.config_kI(0, Constants.DRIVETRAIN_I);
    Robot.driveTrain.driveTrainRightMaster.config_kD(0, Constants.DRIVETRAIN_D);
    Robot.driveTrain.driveTrainRightMaster.config_kF(0, Constants.DRIVETRAIN_F);
    networkTables.setUpdateRate(0.03);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    try{
      double [] speeds = getWheelSpeeds();
      System.out.println(speeds[0] + ", " + speeds[1]);
      Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput, -speeds[1]/5);
      Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, speeds[0]/5);
      // Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.Velocity, speeds[0]);
      // Robot.driveTrain.driveTrainRightMaster.set(ControlMode.Velocity, speeds[1]);
    }catch(Exception e){
      System.out.println("caught: ladies and gentlemen we gottem");
    }
  }

  public double[] getWheelSpeeds(){
    double averageVelocity = Math.min(/*speed function based on distance*/averageVelocityFunction( Robot.targets.get(0).getDistance()), 4.75);
    double deltaTheta = Robot.targets.get(0).getAngle() * (averageVelocity/Robot.targets.get(0).getDistance());
    double wheelDiameter = 1.0;
    double speedDifferenceMultiplier = (0.5);
    double speedDifference = speedDifferenceMultiplier * deltaTheta * wheelDiameter;
    double mid = averageVelocity - Math.abs(speedDifference);
    double leftVelocity = mid - speedDifference;
    double rightVelocity = mid + speedDifference;
    return new double[]{leftVelocity, rightVelocity};   
  }
  
  private double averageVelocityFunction(double distance) {
    double vel = 2.375;//calculation is max / 2
    return vel;
  }
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }
  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
