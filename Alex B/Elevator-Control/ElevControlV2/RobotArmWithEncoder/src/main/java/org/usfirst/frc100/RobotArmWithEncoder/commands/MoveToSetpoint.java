/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands;


import org.usfirst.frc100.RobotArmWithEncoder.Constants;
import org.usfirst.frc100.RobotArmWithEncoder.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MoveToSetpoint extends Command {
  private double setpoint;
  private boolean finished;
  public MoveToSetpoint() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.robotArm);
  }
  public MoveToSetpoint(int setpoint){
    requires(Robot.robotArm);
    this.setpoint = setpoint;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.robotArm.setpoint = setpoint;
    Robot.robotArm.updateSetpoint();
    Robot.robotArm.t.reset();

  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putNumber("ArmEnc",Robot.robotArm.robotArmEncoder.get());
    SmartDashboard.putNumber("Setpoint",Robot.robotArm.setpoint);
    if(Robot.robotArm.t.get() > Constants.ELEVATOR_MAX_TRAVEL_TIME){
      new TeleopArmDrive().start();
    }
    if(Math.abs(Robot.robotArm.setpoint-Robot.robotArm.robotArmEncoder.get())<Constants.ELEVATOR_SETPOINT_ACCEPTABLE_VARIATION){
      finished = true;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return finished;
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
