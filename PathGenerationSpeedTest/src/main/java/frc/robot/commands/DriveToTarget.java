/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.OI;
import frc.robot.Robot;

public class DriveToTarget extends Command {
  public double SpeedForRobot;
  public double turnConstantForAngle;
  public DriveToTarget() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    turnConstantForAngle = 1.0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double d = 0.6;
    SpeedForRobot = d;
    double currentAngle = OI.rightJoystick.getX();//Robot.driveTrain.getAngle(Robot.driveTrain.table);
    Robot.driveTrain.driveTrainDifferentialDrive1.arcadeDrive(SpeedForRobot, currentAngle*turnConstantForAngle);
//    System.out.println("in execute in DriveToTarget");
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
