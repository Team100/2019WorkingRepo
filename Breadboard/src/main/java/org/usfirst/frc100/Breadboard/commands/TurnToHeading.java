/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.Breadboard.commands;

import org.usfirst.frc100.Breadboard.Constants;
import org.usfirst.frc100.Breadboard.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class TurnToHeading extends Command {
  private double dest;
  boolean turnLeft = false;
  boolean done=false;
  boolean first = true;
  boolean add;

  public TurnToHeading() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
  }
  public TurnToHeading(boolean add, double destination){
    dest = destination;
    this.add = add;

    requires(Robot.drivetrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(first){
      if(add) dest = Robot.ahrs.getFusedHeading() + dest;
      done = false;
      while(dest >= 360){ dest -= 360; }
      System.out.println("Starting: " + Robot.ahrs.getFusedHeading());
      System.out.println("Target Heading: " + dest);

      if(dest < Robot.ahrs.getFusedHeading()){
        turnLeft=true;
      }
    first = false;
    }

    if(Math.abs(dest-Robot.ahrs.getFusedHeading()) < Constants.TURN_BUFFER){
      done=true;
      first = true;
      Robot.drivetrain.stop();
    }
    else if(turnLeft){
      Robot.drivetrain.pivotLeft();
    }
    else{
      Robot.drivetrain.pivotRight();
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
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
