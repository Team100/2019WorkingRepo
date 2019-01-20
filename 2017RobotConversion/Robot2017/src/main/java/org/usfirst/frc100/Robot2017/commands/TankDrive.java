package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.RobotMap;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TankDrive extends Command {
	
	 boolean direction = true;
	 int distanceTraveled;
	 int goToAngles; 
	 public int setpoint;
	 public double iValue;
	 public static double iValue2;
     int incrementalAngle;
     private static double OutputOldY;
 
	 public TankDrive(){
		requires(Robot.driveTrain); 
	 }
	 
	 public TankDrive(boolean driving) {
    	direction = driving; 
    	requires(Robot.driveTrain); 
    }
	 
	public TankDrive(int incrementalAngle){
		this.incrementalAngle = incrementalAngle; 
		requires(Robot.driveTrain);
	}
	 
    // Called just before this Command runs the first time
    protected void initialize() {
    	OutputOldY = 0;
    }
    
    public double GetPositionFiltered(double RawValueReadFromHw) {
		// double tempFilterNumber = 0.01;
		if (!Robot.prefs.containsKey("filterNumber")) {
			Robot.prefs.putDouble("filterNumber", 0.01);
		}
		double filteringNumber = Robot.prefs.getDouble("filterNumber", 0.01);
		double FilteredPosition = (filteringNumber * RawValueReadFromHw) + ((1.0 - filteringNumber) * OutputOldY);
		OutputOldY = FilteredPosition;
		return FilteredPosition;
	}

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//Robot.driveTrain.driveRobot(Robot.oi.leftController, Robot.oi.rightController);
    	//SmartDashboard.putNumber("Number", Robot.driveTrain.getSetPoint());
    	double localX = Robot.oi.rightController.getX();
		double localY = Robot.oi.leftController.getY();
		double filteredLocalY = GetPositionFiltered(localY);

		
		Robot.driveTrain.driveRobot(localX, -filteredLocalY);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        //return Robot.driveTrain.onTarget(); 
    	return false; 
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stop(); 
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	this.end(); 
    }
}
