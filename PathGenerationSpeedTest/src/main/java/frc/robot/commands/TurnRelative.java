/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.*;


import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnRelative extends Command {
  double targetAngle;
  double targetHeading;
  double degreeTolerance;
  boolean first = true;

  public TurnRelative(double targetAngle) {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
    targetAngle = normalize(targetAngle);
    this.targetAngle = targetAngle;
    degreeTolerance = Constants.DT_TURN_ABSOLUTE_TOLERANCE;

}

  public TurnRelative() {
    requires(Robot.driveTrain);
  }

public double normalize(double input){
    while(input > 180) input -= 360;
    while(input < -180) input += 360;
    return input;
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    //double targetAngleA = normalize(Robot.driveTrain.getVisionAngle());
    //this.targetAngle = targetAngleA;
    //degreeTolerance = Constants.DT_TURN_ABSOLUTE_TOLERANCE;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(first){
      System.out.println("INIT_RELATIVE");
      targetHeading = targetAngle;
      targetHeading += Robot.driveTrain.ahrs.getAngle();
      targetHeading = normalize(targetHeading);
      Robot.driveTrain.turnPID.setSetpoint(targetHeading);
      Robot.driveTrain.turnPID.enable();
      first = false;
    }

    Robot.driveTrain.pidTurn();
    SmartDashboard.putNumber("Turn Error", Robot.driveTrain.turnPID.getError());
    SmartDashboard.putNumber("TurnDest", targetHeading);
  }

  private boolean isOnTarget(){
    double current = Robot.driveTrain.ahrs.getFusedHeading();
    current = normalize(current - targetHeading);
    return Math.abs(current)<degreeTolerance;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    SmartDashboard.putBoolean("OnTarget", isOnTarget());
    if(isOnTarget()){
      System.out.println("Finished turn");
      first = true;
      return true;
    }
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveTrain.stop();
    Robot.driveTrain.turnPID.disable();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    end();
  }
}