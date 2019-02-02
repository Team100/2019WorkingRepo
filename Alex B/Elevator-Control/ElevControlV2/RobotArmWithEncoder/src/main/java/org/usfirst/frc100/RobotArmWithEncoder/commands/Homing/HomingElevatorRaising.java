/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.RobotArmWithEncoder.commands.Homing;

import org.usfirst.frc100.RobotArmWithEncoder.Constants;
import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.commands.AtSetpoint;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.HomeStates;
import org.usfirst.frc100.RobotArmWithEncoder.subsystems.RobotArm.States;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class HomingElevatorRaising extends Command {
  private boolean done;
  public HomingElevatorRaising() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.robotArm);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.robotArm.homeState = HomeStates.ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH;
    Robot.robotArm.robotArmMotor.set(Constants.ELEVATOR_UP_MODIFIER * Constants.ELEVATOR_UP_SPEED);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    SmartDashboard.putBoolean("LOWER TRIGGERED", Robot.robotArm.lowerLimitSwitch.get());
    if(!Robot.robotArm.lowerLimitSwitch.get()){
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
    System.out.println("RISING DONE");
    Robot.robotArm.robotArmMotor.set(0);
    Robot.robotArm.robotArmEncoder.reset();
    System.out.println(Robot.robotArm.robotArmEncoder.get());

    Robot.robotArm.setpoint = 1000;
    Robot.robotArm.updateSetpoint();
    Robot.robotArm.state = States.AT_SETPOINT;
    Robot.robotArm.getPIDController().enable();
    System.out.println("DONE"); 
    
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
