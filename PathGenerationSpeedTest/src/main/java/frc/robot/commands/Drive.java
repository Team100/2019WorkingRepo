/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import edu.wpi.first.networktables.*;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;

public class Drive extends Command {
  int i;
  NetworkTableInstance networkTables = NetworkTableInstance.getDefault();
  NetworkTable table = networkTables.getTable("camera");

  public Drive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    i=0;
    networkTables.setUpdateRate(0.02);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
//    if(OI.moveAndTurn.get()){
      //Robot.driveTrain.driveTrainDifferentialDrive1.arcadeDrive(-Math.min(Math.abs(OI.leftJoystick.getY()),0.6),  turnFunc(-OI.leftJoystick.getY(), Robot.driveTrain.getVisionAngle(table), Robot.driveTrain.getPlane(table), Robot.driveTrain.getDistance(table), 0.9, 6));
    //  System.out.println("Error:" +  Robot.driveTrain.getVisionAngle(table));
  //  }else{
    Robot.driveTrain.driveTrainDifferentialDrive1.arcadeDrive(OI.leftJoystick.getY()*0.8,- OI.leftJoystick.getZ()*0.8);
    //}i++;
  }

  private double turnFunc(double x, double a, double p, double d, double b, double c){
    if(a != 0.0){
      if(d >= 40){
        return Math.tanh((c*a*b*x)/d)*b;
      } else {
        return Math.tanh((c*a*0.3*(-p)*b*x)/d)*b;
      }
    } else return 0.0;
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
