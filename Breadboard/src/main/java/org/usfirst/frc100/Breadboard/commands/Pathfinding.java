/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.Breadboard.commands;

import java.util.Timer;
import java.util.TimerTask;

import javax.management.RuntimeErrorException;

import com.ctre.phoenix.motorcontrol.ControlMode;

import org.usfirst.frc100.Breadboard.Constants;
import org.usfirst.frc100.Breadboard.Robot;
import org.usfirst.frc100.Breadboard.utils.Paths;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Pathfinding extends Command {

  private boolean finished;
	private static double[][] path;
	private int lengthOfPath;
	private static int lineInPath;
	private static double leftVelocity;
	private static double rightVelocity;
	private String currentMode;
	private boolean generate = false;
	Timer timer;

	public Pathfinding() {
		generate = true;

		/*This sets the PIDF values as defined in the constants file*/
		Robot.drivetrain.rightMaster.config_kP(0, Constants.DT_MASTER_kP, 10);
		Robot.drivetrain.rightMaster.config_kI(0, Constants.DT_MASTER_kI, 10); 
		Robot.drivetrain.rightMaster.config_kD(0, Constants.DT_MASTER_kD, 10);
		Robot.drivetrain.rightMaster.config_kF(0, Constants.DT_MASTER_kF, 10);

		Robot.drivetrain.leftMaster.config_kP(0, Constants.DT_MASTER_kP, 10);
		Robot.drivetrain.leftMaster.config_kI(0, Constants.DT_MASTER_kI, 10); 
		Robot.drivetrain.leftMaster.config_kD(0, Constants.DT_MASTER_kD, 10);
    	Robot.drivetrain.leftMaster.config_kF(0, Constants.DT_MASTER_kF, 10);
    
    	requires(Robot.drivetrain);

	}

	public Pathfinding(String mode) {
		generate = false;
		currentMode = mode;
		boolean modeFound = false;
		/*This compares the mode that is given to any available mode. Modes can be found in the constants file*/
		for(int i =  0; i < Constants.PATHS_STRINGS.length; i++) {
			if(Constants.PATHS_STRINGS[i].equals(mode)){
				modeFound = true;
			}
		}
		if(!modeFound) {
			throw new RuntimeErrorException(new Error("Unknown Path:" + mode));
		}
		/*This sets the PIDF values as defined in the constants file*/
		Robot.drivetrain.rightMaster.config_kP(0, Constants.DT_MASTER_kP, 10);
		Robot.drivetrain.rightMaster.config_kI(0, Constants.DT_MASTER_kI, 10); 
		Robot.drivetrain.rightMaster.config_kD(0, Constants.DT_MASTER_kD, 10);
		Robot.drivetrain.rightMaster.config_kF(0, Constants.DT_MASTER_kF, 10);

		Robot.drivetrain.leftMaster.config_kP(0, Constants.DT_MASTER_kP, 10);
		Robot.drivetrain.leftMaster.config_kI(0, Constants.DT_MASTER_kI, 10); 
		Robot.drivetrain.leftMaster.config_kD(0, Constants.DT_MASTER_kD, 10);
		Robot.drivetrain.leftMaster.config_kF(0, Constants.DT_MASTER_kF, 10);
		
		requires(Robot.drivetrain);
	}

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {

	Robot.drivetrain.leftMaster.setSensorPhase(true);
	Robot.drivetrain.rightMaster.setSensorPhase(true);

	/*This sets the PIDF values as defined in the constants file*/
	Robot.drivetrain.rightMaster.config_kP(0, Constants.DT_MASTER_kP, 10);
	Robot.drivetrain.rightMaster.config_kI(0, Constants.DT_MASTER_kI, 10); 
	Robot.drivetrain.rightMaster.config_kD(0, Constants.DT_MASTER_kD, 10);
	Robot.drivetrain.rightMaster.config_kF(0, Constants.DT_MASTER_kF, 10);

	Robot.drivetrain.leftMaster.config_kP(0, Constants.DT_MASTER_kP, 10);
	Robot.drivetrain.leftMaster.config_kI(0, Constants.DT_MASTER_kI, 10); 
	Robot.drivetrain.leftMaster.config_kD(0, Constants.DT_MASTER_kD, 10);
	Robot.drivetrain.leftMaster.config_kF(0, Constants.DT_MASTER_kF, 10);

	Robot.drivetrain.leftMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
	Robot.drivetrain.rightMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);

    lineInPath = 0;
		finished = false;
		if (generate) path = Robot.drivetrain.generatePath();
		else path = Paths.getPath(currentMode);
		/*That path that is called can be found in the paths file*/
		lengthOfPath =  path.length;
		timer = new Timer();
		/*This makes the function execute the executePath method every 20 milliseconds*/
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				executePath();
			}
		}, 0, Constants.EXECUTION_LOOP_INTERVAL); // this number must match refresh rate

		Robot.drivetrain.leftMaster.selectProfileSlot(0, 0);
		Robot.drivetrain.rightMaster.selectProfileSlot(0, 0);
  }

  	/**
	 * This method is run every x amount of milliseconds and controls the robot during the path.
	 */
	public void executePath(){
		leftVelocity = path[lineInPath][0];
    	rightVelocity = path[lineInPath][1];
		
		// Set the ramp rates for both sides
		// Robot.drivetrain.leftMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
		// Robot.drivetrain.rightMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
		
		// Set the motors to their desired value
		Robot.drivetrain.leftMaster.set(ControlMode.Velocity, (leftVelocity * Constants.LEFT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER * Constants.TALON_SRX_UNIT_CONVERSION);
		Robot.drivetrain.rightMaster.set(ControlMode.Velocity, (rightVelocity * Constants.RIGHT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER * Constants.TALON_SRX_UNIT_CONVERSION);
		
		// System.out.println(leftVelocity + ", "+ Constants.LEFT_DRIVETRAIN_MODIFIER +", " + Constants.DRIVETRAIN_TICKS_PER_METER + ", " + rightVelocity + ", " + Constants.RIGHT_DRIVETRAIN_MODIFIER + ", " + Constants.DRIVETRAIN_TICKS_PER_METER);
		SmartDashboard.putNumber("RightCommand", (rightVelocity * Constants.RIGHT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER);
		SmartDashboard.putNumber("LeftCommand", (leftVelocity * Constants.LEFT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER);
		SmartDashboard.putNumber("RightVelocity", Robot.drivetrain.rightMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("LeftVelocity", Robot.drivetrain.leftMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("RightErrorAtSRX", Robot.drivetrain.rightMaster.getClosedLoopError());
		SmartDashboard.putNumber("LeftErrorAtSRX", Robot.drivetrain.leftMaster.getClosedLoopError());
		SmartDashboard.putString("LeftMode", Robot.drivetrain.leftMaster.getControlMode().toString());
		SmartDashboard.putNumber("LeftCommandReceived", Robot.drivetrain.leftMaster.getClosedLoopTarget(0));
		SmartDashboard.putNumber("LeftVoltage", Robot.drivetrain.leftMaster.getMotorOutputVoltage());
	
		lineInPath += 1;
		if(lineInPath >= lengthOfPath) {
			finished = true;
			timer.cancel();
		}
	}

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return finished;
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
