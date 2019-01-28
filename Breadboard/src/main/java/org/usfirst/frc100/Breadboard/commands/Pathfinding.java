/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.Breadboard.commands;

import java.util.Timer;
import java.util.TimerTask;


import com.ctre.phoenix.motorcontrol.ControlMode;

import org.usfirst.frc100.Breadboard.Constants;
import org.usfirst.frc100.Breadboard.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Pathfinding extends Command {

  private boolean finished;
	private static double[][] path;
	private int lengthOfPath;
	private static int lineInPath;
	private static double leftVelocity;
	private static double rightVelocity;
	Timer timer;

	public Pathfinding() {
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
    lineInPath = 0;
		finished = false;
		path = Robot.drivetrain.generatePath();
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
  }

  /**
	 * This method is run every x amount of milliseconds and controls the robot during the path.
	 */
	public void executePath(){
		// Get the velocities and angle from the Array
		leftVelocity = path[lineInPath][0]/3.28;
    rightVelocity = path[lineInPath][1]/3.28;
		
		// Set the ramp rates for both sides
		Robot.drivetrain.leftMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
		Robot.drivetrain.rightMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
		
		// Set the motors to their desired value
		Robot.drivetrain.leftMaster.set(ControlMode.Velocity, (leftVelocity * Constants.LEFT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER);
		Robot.drivetrain.rightMaster.set(ControlMode.Velocity, (rightVelocity * Constants.RIGHT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER);
    System.out.println(leftVelocity + ", "+ Constants.LEFT_DRIVETRAIN_MODIFIER +", " + Constants.DRIVETRAIN_TICKS_PER_METER + ", " + rightVelocity + ", " + Constants.RIGHT_DRIVETRAIN_MODIFIER + ", " + Constants.DRIVETRAIN_TICKS_PER_METER);
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
