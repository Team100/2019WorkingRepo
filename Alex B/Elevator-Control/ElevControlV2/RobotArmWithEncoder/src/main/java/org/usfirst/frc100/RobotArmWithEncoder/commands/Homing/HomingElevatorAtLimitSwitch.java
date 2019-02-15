/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands.Homing;

import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.HomeStates;

import edu.wpi.first.wpilibj.command.Command;

public class HomingElevatorAtLimitSwitch extends Command {
  public HomingElevatorAtLimitSwitch() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.robotArm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.robotArm.homeState = HomeStates.ELEV_AT_LIMIT_SWITCH;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("Elevator at limit switch end");
    Robot.robotArm.homeState = HomeStates.ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH;
    new HomingElevatorRaising().start();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
