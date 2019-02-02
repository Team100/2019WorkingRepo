/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands.Homing;

import org.usfirst.frc100.RobotArmWithEncoder.Constants;
import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.HomeStates;

import edu.wpi.first.wpilibj.command.Command;

public class HomingElevatorLowering extends Command {
  private boolean done;
  public HomingElevatorLowering() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.robotArm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.robotArm.homeState = HomeStates.ELEV_LOWERING_TO_LIMIT_SWITCH;
    Robot.robotArm.robotArmMotor.set(Constants.ELEVATOR_DOWN_MODIFIER*Constants.ELEVATOR_DOWN_SPEED);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.robotArm.lowerLimitSwitch.get()){
      done = true;
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return done;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("HOMING ELEVATOR DONE");
    Robot.robotArm.homeState = HomeStates.ELEV_AT_LIMIT_SWITCH;
    new HomingElevatorAtLimitSwitch().start();
  }


  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
