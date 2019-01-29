/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands;

import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.HomeStates;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.States;

import edu.wpi.first.wpilibj.command.Command;

public class TeleopArmController extends Command {
  public TeleopArmController() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    switch(Robot.robotArm.state){
      case INIT:
        Robot.robotArm.homeState = HomeStates.NOT_STARTED;
        Robot.robotArm.state = States.HOMING;
        break;
      case HOMING:
        home();
        break;
      case GOING_DOWN:
        break;
      case DOWN:
        break;
      case GOING_UP:
        break;
      case UP:
        break;
      case TELEOP:
        break;
      case ERROR:
        break;

    }
  }

  private void home(){
    switch(Robot.robotArm.homeState){
      case NOT_STARTED:
        //If Limit Switch Pressed... set to "At Limit Switch"
        //Else Set to "Lowering to Limit Switch"

        /*
        if(limitSwitch.isPressed()){
          Robot.robotArm.homeState=HomeStates.ELEV_AT_LIMIT_SWITCH;
        }
        else{
          Robot.robotArm.homeState = HomeStates.ELEV_LOWERING_TO_LIMIT_SWITCH;
          Robot.robotArm.robotArmMotor.set(-0.33);
        }
        */
        
        break;

      case  ELEV_LOWERING_TO_LIMIT_SWITCH:
        //If Limit Switch Pressed... set to "At Limit Switch"
        //If timer expires... stop everything
        /*
        if(limitSwitch.isPressed()){
          Robot.robotArm.homeState = HomeStates.ELEV_AT_LIMIT_SWITCH;
          Robot.robotArm.set(0);
        }
        else if (timer.expired()){
          Robot.robotArm.homeState = HomeStates.FATAL;
          Robot.robotArm.set(0);
        }
        */
        break;

      case ELEV_AT_LIMIT_SWITCH:
        //Transition to raising above limit switch
        //Start timer
        /*
         * Robot.robotArm.set(0.33);
         * Robot.robotArm.homeState = HomeStates.ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH;
         */
        break;

      case ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH:
        //When limit switch not pressed...transition to COMPLETE
        //If timer expires... stop everything

        /*
         * if(!limitSwitch.isPressed()){
         *    Robot.robotArm.set(0);
         *    Robot.robotArm.homeState = HomeStates.COMPLETE;
         * } 
         */
        
        break;

      case COMPLETE:
        Robot.robotArm.state = States.DOWN;
        break;

      case FATAL:
        Robot.robotArm.state = States.ERROR;
        break;
      default:
        System.out.println("DEFAULT CASE FOR TELEOP ARM CONTROLLER REACHED... IS THIS INTENTIONAL?");

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
