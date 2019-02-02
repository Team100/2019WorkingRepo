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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TeleopArmController extends Command {
  
  /**
   * Constructor for the Teleop Arm Controller
   */
  public TeleopArmController() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.robotArm);
   
  }

  // Called just before this Command runs the first time
  /**
   * Initializes the State Machine controller
   */
  @Override
  protected void initialize() {
    
  }


  /**
   * Implements state controller actioins each loop
   */
  @Override
  protected void execute() {
    
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
