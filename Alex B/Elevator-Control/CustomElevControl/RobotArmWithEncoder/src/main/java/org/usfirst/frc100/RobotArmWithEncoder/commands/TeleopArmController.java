/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands;


import org.usfirst.frc100.RobotArmWithEncoder.Constants;
import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.HomeStates;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.States;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Joystick.AxisType;
import edu.wpi.first.wpilibj.command.Command;

public class TeleopArmController extends Command {
  private Timer t;
  private Timer homerTimer;
  private enum JoystickButtons{
    GO_UP,GO_DOWN,NONE
  }
  private JoystickButtons joy =  JoystickButtons.NONE;
  private int cyclesInErrorState;
  public TeleopArmController() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    t.reset();
    t.start();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    joy = JoystickButtons.NONE;
    if(Robot.oi.up.get()){
      joy = JoystickButtons.GO_UP;
    }
    else if (Robot.oi.down.get()){
      joy = JoystickButtons.GO_DOWN;
    }
    switch(Robot.robotArm.state){
      case INIT:
        Robot.robotArm.homeState = HomeStates.NOT_STARTED;
        Robot.robotArm.state = States.HOMING;
        break;
      case HOMING:
        home();
        break;
      case GOING_DOWN:
        if(Robot.robotArm.robotArmEncoder.get() < Constants.ARM_ENCODER_LOWER_BUFFER){
          Robot.robotArm.robotArmMotor.set(0);
          Robot.robotArm.state = States.DOWN;
          t.reset();
        }
        else if(t.get()>Constants.ROBOT_ELEVATOR_STATE_CONTROLLER_MAX_TRANSITION_TIME_TOP_BOTTOM){
          Robot.robotArm.state = States.ERROR;
          System.out.println("ERROR RECIEVED AT FPGA:"+Timer.getFPGATimestamp()+" GOING_DOWN_STATE_MACHINE_TIMER_EXCEEDED");
          t.reset();
        }
        break;
      case DOWN:
        if(joy == JoystickButtons.GO_UP){
          Robot.robotArm.state = States.GOING_UP;
          Robot.robotArm.robotArmMotor.set(Constants.ELEVATOR_UP_SPEED);
        }
        break;
      case GOING_UP:
        if(t.get() > Constants.ROBOT_ELEVATOR_STATE_CONTROLLER_MAX_TRANSITION_TIME_TOP_BOTTOM){
          Robot.robotArm.state = States.ERROR;
          System.out.println("ERROR RECIEVED AT FPGA:"+Timer.getFPGATimestamp()+" GOING_UP_STATE_MACHINE_TIMER_EXCEEDED");

          t.reset();
        }
        if(Robot.robotArm.robotArmEncoder.get() > Constants.ARM_ENCODER_MAX_VALUE-Constants.ARM_ENCODER_TOP_BUFFER){
          Robot.robotArm.state = States.UP;
          t.reset();
        }
        break;
      case UP:
        if(joy == JoystickButtons.GO_DOWN){
          Robot.robotArm.state = States.GOING_DOWN;
          Robot.robotArm.robotArmMotor.set(Constants.ELEVATOR_DOWN_SPEED);
        }
        break;
      case TELEOP:
        if(Robot.robotArm.limitSwitch.get()){
          Robot.robotArm.robotArmMotor.set(0);
        }
        else{
          Robot.robotArm.robotArmMotor.set(Robot.oi.teleopJoystick.getX());
        }
        
        break;
      case ERROR:
        cyclesInErrorState += 1;
        Robot.robotArm.state = States.TELEOP;
        break;

    }
  }

  private void home(){
    switch(Robot.robotArm.homeState){
      case NOT_STARTED:
        //If Limit Switch Pressed... set to "At Limit Switch"
        //Else Set to "Lowering to Limit Switch"

        homerTimer.reset();
        homerTimer.start();
        if(Robot.robotArm.limitSwitch.get()){
          Robot.robotArm.homeState=HomeStates.ELEV_AT_LIMIT_SWITCH;
        }
        else{
          Robot.robotArm.homeState = HomeStates.ELEV_LOWERING_TO_LIMIT_SWITCH;
          Robot.robotArm.robotArmMotor.set(-1*Constants.ARM_MOTOR_HOMING_POWER);
        }
        
        
        break;

      case  ELEV_LOWERING_TO_LIMIT_SWITCH:
        //If Limit Switch Pressed... set to "At Limit Switch"
        //If timer expires... stop everything
        
        if(Robot.robotArm.limitSwitch.get()){
          Robot.robotArm.homeState = HomeStates.ELEV_AT_LIMIT_SWITCH;
          Robot.robotArm.robotArmMotor.set(0);
        }
        else if (homerTimer.get() >= Constants.HOMING_GOING_DOWN_MAX_DURATION){
          Robot.robotArm.homeState = HomeStates.FATAL;
          System.out.println("ERROR RECIEVED AT FPGA:"+Timer.getFPGATimestamp()+" HOMING_GOING_DOWN_STATE_MACHINE_TIMER_EXCEEDED");

          Robot.robotArm.robotArmMotor.set(0);
          
        }
        
        break;

      case ELEV_AT_LIMIT_SWITCH:
        //Transition to raising above limit switch
        //Start timer
        homerTimer.stop();
        homerTimer.reset();
        homerTimer.start();
        Robot.robotArm.robotArmMotor.set(Constants.ARM_MOTOR_HOMING_POWER);
        Robot.robotArm.homeState = HomeStates.ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH;
         
        break;

      case ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH:
        //When limit switch not pressed...transition to COMPLETE
        //If timer expires... stop everything

        if(!Robot.robotArm.limitSwitch.get()){
          Robot.robotArm.robotArmMotor.set(0);
          Robot.robotArm.homeState = HomeStates.COMPLETE;
        } 
        else if(homerTimer.get()>Constants.HOMING_GOING_UP_MAX_DURATION){
          Robot.robotArm.robotArmMotor.set(0);
          Robot.robotArm.homeState = HomeStates.FATAL;
        }
        
        break;

      case COMPLETE:
        Robot.robotArm.state = States.DOWN;
        Robot.robotArm.homeState = HomeStates.NOT_STARTED;
        homerTimer.stop();    
        break;

      case FATAL:
        Robot.robotArm.state = States.ERROR;
        homerTimer.stop();
        break;
      default:
        System.out.println("DEFAULT CASE FOR TELEOP ARM CONTROLLER REACHED... IS THIS INTENTIONAL?");
        break;

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
