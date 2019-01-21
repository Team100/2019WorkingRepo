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
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnRelative extends Command {
  double targetAngle;
  double targetHeading;
  double degreeTolerance;
  boolean first = true;
  int onTargetCount = 0;
  public TurnRelative(double targetAngle) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.drivetrain);
    while(targetAngle > 180)targetAngle -= 360;
    while(targetAngle < -180)targetAngle += 360;
    this.targetAngle = targetAngle;
    degreeTolerance = Constants.DT_TURN_ABSOLUTE_TOLERANCE;
  }

  public TurnRelative(double targetAngle, double degreeTolerance){
    requires(Robot.drivetrain);
    while(targetAngle > 180)targetAngle -= 360;
    while(targetAngle < -180)targetAngle += 360;
    this.targetAngle = targetAngle;
    this.degreeTolerance = degreeTolerance;
  }
  

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(first){
      targetHeading = targetAngle;
      Robot.drivetrain.turnPID.setSetpoint(targetHeading);
      Robot.drivetrain.turnPID.enable();
      first = false;
    }

    Robot.drivetrain.pidTurn();
    SmartDashboard.putNumber("Turn Error", Robot.drivetrain.turnPID.getError());
  }

  private boolean isOnTarget(){
    return Math.abs(Robot.drivetrain.getHeading()-targetAngle)<degreeTolerance;
  }


  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    SmartDashboard.putNumber("OnTargetCount", onTargetCount);

    if(isOnTarget()){
      onTargetCount++;
    }
    else{
      onTargetCount = 0;
    }
    if(onTargetCount >= 1){
      System.out.println("Finished turn");
      first = true;
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.drivetrain.stop();
    Robot.drivetrain.turnPID.disable();
    onTargetCount = 0;
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}
