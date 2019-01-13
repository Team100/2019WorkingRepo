// RobotBuilder Version: 2.
//esse guey
//esse guey
//esse guey
//esse guey
//esse guey  
package org.usfirst.frc100.Robot2018.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
//import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc100.Robot2018.Robot;
import org.usfirst.frc100.Robot2018.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import java.io.*;
import java.nio.file.Files;

/**
*
*/
public class PathFinding extends Command {

	private boolean finish;
	private int counter;
	// FalconPathPlanner path;
	Timer timer;
	// boolean finish;
	boolean fastCalculation;
	int countZero;
	EncoderFollower left;
	EncoderFollower right;
	Paths paths;
	double p;
	double i;
	double d;
	double a;
	double p2;
	double i2;
	double d2;
	double a2;
	Trajectory trajectory;
	Trajectory leftT;
	Waypoint points[];
	Trajectory rightT;
	long startTime;
	long currentTime;
	long timeInt;
	int rightM = -1;
	int leftM = 1;
	int length = 0;
	String mode;
	String fileName = "out.txt";
	double[][] path;
	// Paths p;

	public PathFinding() {

		requires(Robot.driveTrain);
		// //System.out.println("hi");

	}

	public PathFinding(String a) {
		rightM = -1;

		leftM = 1;
		requires(Robot.driveTrain);
		mode = a;
		if (mode == "null" || mode == "null" || mode == "Left" || mode == "Right") {
			fastCalculation = false;
			Robot.ahrs.reset();
		} else {
			fastCalculation = true;
			// Robot.ahrs.reset();
		}
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize() {

		paths = new Paths();
		timeInt = 100;
		finish = false;
		counter = 0;
		// timer = new Timer();
		startTime = System.currentTimeMillis();

		// System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		// System.out.println(mode);
		if (mode == "CrossLine") {
			path = paths.returnCrossLine();
		} else if (mode == "CenterLeft") {
			path = paths.returnCenterLeft();
		} else if (mode == "CenterRight") {
			path = paths.returnCenterRight();
		} else if (mode == "LeftLeft") {
			path = paths.returnLeftLeft();
		} else if (mode == "RightRight") {
			path = paths.returnRightRight();
		} else if (mode == "LeftLeftScale") {
			path = paths.returnLeftLeftScale();
		} else if (mode == "RightRightScale") {
			path = paths.returnRightRightScale();
		} else if (mode == "RightRightScaleFront") {
			path = paths.returnRightRightScaleFront();
		} else if (mode == "LeftLeftScaleFront") {
			path = paths.returnLeftLeftScaleFront();
		}

	 
		// When making waypoints (how far you wanna go, how far you wanna go left or
		// right(left is positinve, right is negative, and exit angle);
		// Everything needs to be in meters
		// Keep in mind that computing paths takes a long time
		// because the roborio isnt really that powerful
		// Once you have a path, it makes sense to load all the data you want to use
		// into an array

		p = Robot.prefs.getDouble("P", 0);
		i = Robot.prefs.getDouble("I", 0);
		d = Robot.prefs.getDouble("D", 0);
		a = Robot.prefs.getDouble("F", // .45
				0);

		p2 = Robot.prefs.getDouble("P", 0);
		i2 = Robot.prefs.getDouble("I", 0);
		d2 = Robot.prefs.getDouble("D", 0);
		a2 = Robot.prefs.getDouble("F", // .45
				0);
		// when tuning, use feedforward gain first, then tweak a little p
		// dont need to really touch the i or d gain

		RobotMap.driveTrainRightMaster.config_kP(0, p, 10); // .123
		RobotMap.driveTrainRightMaster.config_kI(0, i, 10); // .2
		RobotMap.driveTrainRightMaster.config_kD(0, d, 10);
		RobotMap.driveTrainRightMaster.config_kF(0, a, 10);

		RobotMap.driveTrainLeftMaster.config_kP(0, p2, 10); // .34 //.22
		RobotMap.driveTrainLeftMaster.config_kI(0, i2, 10); // .189
		RobotMap.driveTrainLeftMaster.config_kD(0, d2, 10); // 2.0E-4
		RobotMap.driveTrainLeftMaster.config_kF(0, a2, 10); // 0
		// RobotMap.gyro.reset();
		RobotMap.driveTrainLeftMaster.setSelectedSensorPosition(0, 0, 0);

		// ArrayList<Integer> y = //new ArrayList();//10.1, 16.7, 3.07 5.1
		// change this to 20 ms 1.7 1.7 2.5 2.5
		if (!fastCalculation) {
			Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
					Trajectory.Config.SAMPLES_HIGH, 0.02, 3.07 / 2.2, 5.1 / 2.2, 20);// 17.08);
			// Keep first and second arguement the same, the refresh rate in seconds, max
			// vel, max acc, max jerk);
			trajectory = Pathfinder.generate(points, config);
			TankModifier modifier = new TankModifier(trajectory).modify(.67); // modify the width between wheels

			leftT = modifier.getLeftTrajectory();
			rightT = modifier.getRightTrajectory();
			length = leftT.length();
			/*
			 * //System.out.println("WRITING"); File trajFile = new
			 * File("./home/lvusr/forward.traj"); //System.out.println("Working");
			 * Pathfinder.writeToFile(trajFile, trajectory); //System.out.println("DONE");
			 * ////System.out.println(trajFile.getAbsolutePath());
			 */

			//// System.out.println(trajFile.getPath());

			for (int i = 0; i < trajectory.length(); i++) {
				Trajectory.Segment segL = leftT.get(i);
				Trajectory.Segment segR = (rightT.get(i));
				System.out.println("{" +segL.velocity + ", " + segR.velocity + ", " +
				segR.heading +"},");

			}

		} else {
			length = path.length;
		}

		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				parseArray();
			}
		}, 0, 20); // this number must match refresh rate

	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute() {
		//// System.out.println("hi");
		// parseArray();
		long ellapsedTime = System.currentTimeMillis();

	}

	public void parseArray() {
		double setR = 0;
		double setL = 0;
		if (!fastCalculation) {
			Trajectory.Segment segL = leftT.get(counter); // get left and right profile data
			Trajectory.Segment segR = (rightT.get(counter)); // access each point and count every iterations
			/*
			 * double leftV = path[counter][0]; double rightV = path[counter][1]; double
			 * angle = path[counter][2];
			 */

			// double gyro_heading = RobotMap.gyro.getAngle();//... your gyro code here ...
			// // Assuming the gyro is giving a value in degrees
			double desired_heading = Pathfinder.r2d(segR.heading); // angle // Should also be in degrees

			double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - (Robot.ahrs.getAngle() * -1));
			double turn = .87 * (-1.0 / 80.0) * angleDifference; // .80 tweek the first value for how the robot tracks
																	// angle
			if (mode == "BackR") {
				setR = segR.velocity; // rightV
				setL = segL.velocity; // leftV
			} else {
				setR = segR.velocity;// -turn; //rightV - turn;
				setL = segL.velocity;// +turn; //leftV + turn;
			}
		} else {
			double leftV = path[counter][0];
			double rightV = path[counter][1];
			double angle = path[counter][2];
			double desired_heading1 = Pathfinder.r2d(angle); // angle // Should also be in degrees

			double angleDifference1 = Pathfinder.boundHalfDegrees(desired_heading1 - (Robot.ahrs.getAngle() * -1));
			double turn1 = .87 * (-1.0 / 80.0) * angleDifference1;
			if (mode != "null") {
				setR = rightV; // - turn1;
				setL = leftV; // + turn1;
			} else {
				setR = rightV - turn1;
				setL = leftV + turn1;
			}
			SmartDashboard.putNumber("Turn", turn1);
			// System.out.println(turn1);

			SmartDashboard.putNumber("DT/DesiredVelLeft", leftV);
			SmartDashboard.putNumber("DT/DesiredVelRight", rightV);

		}
		// this corrects the robots heading
		// you can access a lot of data at each segment index like heading, acc,
		// velocity etc
		// double setR = segR.velocity;
		// double setL = segL.velocity;

		SmartDashboard.putNumber("DT/leftS", (setL * 1508.965)); // this multiplier is a combination of gearing, how
																	// often encoder updates, and wheel diameter
		SmartDashboard.putNumber("DT/RightS", (setR * 1508.965));

		SmartDashboard.putNumber("DT/DT right Error", RobotMap.driveTrainRightMaster.getClosedLoopError(0));

		SmartDashboard.putNumber("DT/DT left Error", RobotMap.driveTrainLeftMaster.getClosedLoopError(0));

		RobotMap.driveTrainRightMaster.set(ControlMode.Velocity, (setR * rightM) * 1508.965);
		RobotMap.driveTrainLeftMaster.set(ControlMode.Velocity, (setL * leftM) * 1508.965);
		RobotMap.driveTrainRightMaster.configClosedloopRamp(0.25, 0);
		RobotMap.driveTrainLeftMaster.configClosedloopRamp(0.25, 0);

		SmartDashboard.putNumber("DT/LeftMOP", RobotMap.driveTrainLeftMaster.getMotorOutputPercent());
		SmartDashboard.putNumber("DT/RightMOP", RobotMap.driveTrainRightMaster.getMotorOutputPercent());

		// path.length
		if (counter < length) {
			counter++;
		}

		if (counter >= length) {
			finish = true;
			timer.cancel();

		}

		SmartDashboard.putBoolean("finish", finish);
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished() {
		return finish; // end command when the array is fully parsed
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {

		// RobotMap.driveTrainRightMaster.set(ControlMode.PercentOutput,0);
		// RobotMap.driveTrainLeftMaster.set(ControlMode.PercentOutput, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted() {
	}
}
