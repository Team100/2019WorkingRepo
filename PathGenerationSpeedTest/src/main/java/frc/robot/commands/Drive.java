/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.OI;
import frc.robot.Robot;

public class Drive extends Command {
  int i;
  public Drive() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    i=0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    if(i%10 == 0){
      // System.out.println(Robot.driveTrain.driveTrainRightMaster.getSelectedSensorVelocity());
      // SmartDashboard.putNumber("Right Position", Robot.driveTrain.driveTrainRightMaster.getSelectedSensorPosition());
      // SmartDashboard.putNumber("Left Position", Robot.driveTrain.driveTrainLeftMaster.getSelectedSensorPosition());
      // SmartDashboard.putNumber("Pathfinder ticks per meter", Constants.DRIVETRAIN_TICKS_PER_METER);
      // SmartDashboard.putNumber("RightVelocity", Robot.driveTrain.driveTrainRightMaster.getSelectedSensorVelocity(0));
      // SmartDashboard.putNumber("LeftVelocity", Robot.driveTrain.driveTrainLeftMaster.getSelectedSensorVelocity(0));
      // SmartDashboard.putNumber("RightErrorAtSRX", Robot.driveTrain.driveTrainRightMaster.getClosedLoopError());
      // SmartDashboard.putNumber("LeftErrorAtSRX", Robot.driveTrain.driveTrainLeftMaster.getClosedLoopError());
      // SmartDashboard.putString("LeftMode", Robot.driveTrain.driveTrainLeftMaster.getControlMode().toString());
      // SmartDashboard.putNumber("LeftCommandReceived", Robot.driveTrain.driveTrainLeftMaster.getClosedLoopTarget(0));
      // SmartDashboard.putNumber("LeftVoltage", Robot.driveTrain.driveTrainLeftMaster.getMotorOutputVoltage());
      SmartDashboard.putNumber("Heading", Robot.ahrs.getAngle());
  
    }
    Robot.driveTrain.driveTrainDifferentialDrive1.arcadeDrive(OI.leftJoystick.getY(),- OI.rightJoystick.getX());
    i++;
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
