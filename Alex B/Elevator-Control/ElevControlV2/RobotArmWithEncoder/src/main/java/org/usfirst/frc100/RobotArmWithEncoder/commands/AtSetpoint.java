/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands;

import org.usfirst.frc100.RobotArmWithEncoder.Constants;
import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.commands.Homing.HomingInit;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.States;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AtSetpoint extends Command {
  public AtSetpoint() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.robotArm);
  }
  

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putNumber("ArmEnc",Robot.robotArm.robotArmEncoder.get());
    if(Robot.robotArm.state == States.HOMING){
      new HomingInit().start();
    }
    else if(Robot.oi.down.get()){

      new MoveToSetpoint(Constants.ARM_ENCODER_LOWER_BUFFER).start();
    }
    else if(Robot.oi.up.get()){
      new MoveToSetpoint(Constants.ARM_ENCODER_MAX_VALUE-Constants.ARM_ENCODER_TOP_BUFFER).start();
    }
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
