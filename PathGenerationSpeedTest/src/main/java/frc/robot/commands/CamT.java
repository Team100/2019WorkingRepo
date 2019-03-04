/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class CamT extends Command {
  public boolean finished;
  double currentDistance;
  double currentAngle;
  public static NetworkTableInstance inst;
  public static NetworkTable table;
  public int missingFrame;
  public CamT() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("in initialize ln: 30");
    inst = NetworkTableInstance.getDefault();
    table = inst.getTable("camera");
    System.out.println("in initialize ln: 33");
    finished = false;
    missingFrame = 0;
    currentAngle = Robot.driveTrain.getAngle(table);
    currentDistance = Robot.driveTrain.getDistance(table);
    System.out.println("in initialize ln: 35. Distance = "+currentDistance+". Angle = " + currentAngle);
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    currentAngle = Robot.driveTrain.getAngle(table);
    currentDistance = Robot.driveTrain.getDistance(table);
    System.out.println("current angle:" + currentAngle);
    if(missingFrame<4){
        if(currentAngle == 0&&currentDistance==0){
          missingFrame++;
        }
    }else{
      if(currentDistance <= (3 * 12)){
        finished = true;
      }
    }
    if(!finished){
      if(currentAngle == 0){
        Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput,  (4.0/100.0));
        Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, (4.0/100.0));
        System.out.println("forward");
      }else if(currentAngle < 0){
        //Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (2.0/100.0));
        //Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (5.0/100.0));
        Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (-2.0/100.0));
        Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (5.0/100.0));
        System.out.println("going right");
      }else if(currentAngle > 0){
        //Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (2.0/100.0));
        //Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (2.0/100.0));
        Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (-5.0/100.0));
        Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, Math.abs(currentAngle) * (2.0/100.0));
        System.out.println("going left");
      }
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return finished;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    Robot.driveTrain.driveTrainLeftMaster.set(ControlMode.PercentOutput, 0);
    Robot.driveTrain.driveTrainRightMaster.set(ControlMode.PercentOutput, 0);

  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
