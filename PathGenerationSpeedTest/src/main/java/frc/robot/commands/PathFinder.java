package frc.robot.commands;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.Timer;
import java.util.TimerTask;

import frc.robot.subsystems.DriveTrain;
import frc.robot.*;
import com.ctre.phoenix.motorcontrol.ControlMode;

/**
 * The rewrite of the Jaci Pathfinding <i>Black box</i>
 * 
 * This function takes an array of a path and then runs it. It is designed for
 * use of pre-run autonomus'.
 * 
 * @author Team100
 *
 */
public class PathFinder extends Command {
	private boolean finished;
	private static double[][] path;
	private int lengthOfPath;
	private static int lineInPath;
	private static double leftVelocity;
	private static double rightVelocity;
	private static double angle;	
	private static double globalAngle;
	//private PathfindingV2Issue4 runner = new PathfindingV2Issue4();
	Timer timer;
	public PathFinder() {

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTOR

		// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

		requires(Robot.driveTrain);

		// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
	}

	/**
	 * The code to process to process the desired autonomous
	 * 
	 * @param mode
	 */
	public PathFinder(double[][] path_) {
		requires(Robot.driveTrain);
		path = path_;

		/*This sets the PIDF values as defined in the constants file*/
		DriveTrain.driveTrainRightMaster.config_kP(0, Constants.DRIVETRAIN_P, 10);
		DriveTrain.driveTrainRightMaster.config_kI(0, Constants.DRIVETRAIN_I, 10); 
		DriveTrain.driveTrainRightMaster.config_kD(0, Constants.DRIVETRAIN_D, 10);
		DriveTrain.driveTrainRightMaster.config_kF(0, Constants.DRIVETRAIN_F, 10);

		DriveTrain.driveTrainLeftMaster.config_kP(0, Constants.DRIVETRAIN_P, 10);
		DriveTrain.driveTrainLeftMaster.config_kI(0, Constants.DRIVETRAIN_I, 10); 
		DriveTrain.driveTrainLeftMaster.config_kD(0, Constants.DRIVETRAIN_D, 10);
		DriveTrain.driveTrainLeftMaster.config_kF(0, Constants.DRIVETRAIN_F, 10);

	}
	public PathFinder(String _mode){
		requires(Robot.driveTrain);
		path = Paths.getPath(_mode);
		
		/*This sets the PIDF values as defined in the constants file*/
		DriveTrain.driveTrainRightMaster.config_kP(0, Constants.DRIVETRAIN_P, 10);
		DriveTrain.driveTrainRightMaster.config_kI(0, Constants.DRIVETRAIN_I, 10); 
		DriveTrain.driveTrainRightMaster.config_kD(0, Constants.DRIVETRAIN_D, 10);
		DriveTrain.driveTrainRightMaster.config_kF(0, Constants.DRIVETRAIN_F, 10);

		DriveTrain.driveTrainLeftMaster.config_kP(0, Constants.DRIVETRAIN_P, 10);
		DriveTrain.driveTrainLeftMaster.config_kI(0, Constants.DRIVETRAIN_I, 10); 
		DriveTrain.driveTrainLeftMaster.config_kD(0, Constants.DRIVETRAIN_D, 10);
		DriveTrain.driveTrainLeftMaster.config_kF(0, Constants.DRIVETRAIN_F, 10);
	}
	/**
	 *Called just before this Command runs the first time
	 * This-
	 *  will find the path (function called from the Paths.java file and set a start time)
	 */
	@Override
	protected void initialize() {
		Robot.driveTrain.driveTrainShifter.set(true);
		Robot.driveTrain.driveTrainLeftMaster.setSensorPhase(false);
		Robot.driveTrain.driveTrainRightMaster.setSensorPhase(false);
		//path = Paths.getPath("test");	
		lineInPath = 0;
		finished = false;
		DriveTrain.driveTrainLeftMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
		DriveTrain.driveTrainRightMaster.configClosedloopRamp(Constants.RAMP_RATE_DRIVETRAIN, 0);
		/*This is a very long number becuase it is the time in milliseconds hence the long type*/
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
		leftVelocity = path[lineInPath][1];
		rightVelocity = path[lineInPath][0];
		angle = path[lineInPath][2];
		System.out.println(Robot.driveTrain.driveTrainRightMaster.getClosedLoopTarget());
		SmartDashboard.putNumber("RightCommand", (rightVelocity * Constants.RIGHT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER);
		SmartDashboard.putNumber("LeftCommand", (leftVelocity * Constants.LEFT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER);
		SmartDashboard.putNumber("Right Position", Robot.driveTrain.driveTrainRightMaster.getSelectedSensorPosition());
		SmartDashboard.putNumber("Left Position", Robot.driveTrain.driveTrainLeftMaster.getSelectedSensorPosition());
		SmartDashboard.putNumber("Pathfinder ticks per meter", Constants.DRIVETRAIN_TICKS_PER_METER);
		SmartDashboard.putNumber("RightVelocity", Robot.driveTrain.driveTrainRightMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("LeftVelocity", Robot.driveTrain.driveTrainLeftMaster.getSelectedSensorVelocity(0));
		SmartDashboard.putNumber("RightErrorAtSRX", Robot.driveTrain.driveTrainRightMaster.getClosedLoopError());
		SmartDashboard.putNumber("LeftErrorAtSRX", Robot.driveTrain.driveTrainLeftMaster.getClosedLoopError());
		SmartDashboard.putString("LeftMode", Robot.driveTrain.driveTrainLeftMaster.getControlMode().toString());
		SmartDashboard.putNumber("LeftCommandReceived", Robot.driveTrain.driveTrainLeftMaster.getClosedLoopTarget(0));
		SmartDashboard.putNumber("LeftVoltage", Robot.driveTrain.driveTrainLeftMaster.getMotorOutputVoltage());
		globalAngle = Robot.ahrs.getFusedHeading();
		double angleDifference = DriveTrain.boundHalfDegrees(angle - globalAngle);
		//double turn = 0.8 * (-1.0/80.0) * angleDifference; //magic numbers are straight from Screensteps so they might need editing (https://wpilib.screenstepslive.com/s/currentCS/m/84338/l/1021631-integrating-path-following-into-a-robot-program)
		// Set the motors to their desired value
		DriveTrain.driveTrainLeftMaster.set(ControlMode.Velocity, ((leftVelocity * Constants.LEFT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER));// + turn);
		DriveTrain.driveTrainRightMaster.set(ControlMode.Velocity, ((rightVelocity * Constants.RIGHT_DRIVETRAIN_MODIFIER) * Constants.DRIVETRAIN_TICKS_PER_METER));// - turn);
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