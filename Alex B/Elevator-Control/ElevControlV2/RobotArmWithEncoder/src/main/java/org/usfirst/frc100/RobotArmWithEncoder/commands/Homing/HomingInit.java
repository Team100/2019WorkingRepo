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

public class HomingInit extends Command {
  private boolean done;
  public HomingInit() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.robotArm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("BEGAN HOMING SEQUENCE");
    Robot.robotArm.homeState = HomeStates.NOT_STARTED;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(Robot.robotArm.lowerLimitSwitch.get()){
      Robot.robotArm.homeState = HomeStates.ELEV_AT_LIMIT_SWITCH;
    }
    else{
      Robot.robotArm.homeState = HomeStates.ELEV_LOWERING_TO_LIMIT_SWITCH;
    }
    done = true;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return done;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    if(Robot.robotArm.homeState == HomeStates.ELEV_AT_LIMIT_SWITCH){ 
      new HomingElevatorAtLimitSwitch().start();
    }
    else if(Robot.robotArm.homeState == HomeStates.ELEV_LOWERING_TO_LIMIT_SWITCH){
      new HomingElevatorLowering().start();

    }
    else{
      throw new Error("Invalid end state for HomingInit "+Robot.robotArm.homeState);
    }
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
