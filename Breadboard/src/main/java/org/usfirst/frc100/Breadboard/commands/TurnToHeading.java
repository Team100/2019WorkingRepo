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
  double heading;
  boolean turnLeft = false;
  boolean done=false;
  boolean first = true;
  boolean add;

  public TurnToHeading() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
  }
  public TurnToHeading(boolean add, double destination){
    heading = destination;
    this.add = add;

    requires(Robot.drivetrain);
  }

  public void setAddHeading(double destination){
    System.out.println("DesiredDest: " + destination);

    heading = destination;
    add = true;
    first = true;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double current = Robot.ahrs.getYaw();

    if(first){
      dest = heading;
      if(add) dest += current;
      done = false;
      while (dest > 180) dest -= 360;
      while (dest < -180) dest += 360;
      System.out.println("Starting: " + current+180);
      System.out.println("Target Heading: " + dest+180);

      double diff;

      if((current-dest) > 0){//current is larger
        if(Math.abs(current-dest) > Math.abs(current-360-dest)) diff = Math.abs(current-360-dest);
        else diff = Math.abs(current-dest);
      } else {
        if(Math.abs(dest-current) > Math.abs(dest-360-current)) diff = Math.abs(dest-360-current);
        else diff = Math.abs(dest-current);
      }

      if(current+diff == dest) turnLeft = false;
      else if(current-diff == dest) turnLeft = true;

    first = false;
    }

    if(Math.abs(dest-current) < Constants.TURN_STOP_BUFFER){
      done=true;
      first = true;
      Robot.drivetrain.stop();
    }else if(Math.abs(dest-current) < Constants.TURN_SLOW_BUFFER){
      if(turnLeft) Robot.drivetrain.pivotLeft(Constants.DRIVE_TRAIN_PIVOT_SLOW_PERCENTAGE);
      else Robot.drivetrain.pivotRight(Constants.DRIVE_TRAIN_PIVOT_SLOW_PERCENTAGE);
    } else {
      if(turnLeft) Robot.drivetrain.pivotLeft(Constants.DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
      else Robot.drivetrain.pivotRight(Constants.DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
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
