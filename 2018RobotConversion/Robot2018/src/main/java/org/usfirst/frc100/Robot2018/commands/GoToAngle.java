package org.usfirst.frc100.Robot2018.commands;

import java.util.ArrayList;

import org.usfirst.frc100.Robot2018.Robot;
import org.usfirst.frc100.Robot2018.RobotMap;
import org.usfirst.frc100.Robot2018.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Waypoint;

/**
 *
 */
public class GoToAngle extends Command {
	public static double degrees;
	public PIDController a;
	public AutoGenerate generateAngle;
	public static ArrayList<Double> angles;
	public int count;
	
    public GoToAngle(float degreesToTurn) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	degrees = degreesToTurn;
    	
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	//System.out.println("turn");
    	/*
    	count = 0;
    	Robot.ahrs.reset();
    	
    	ParseJSONFile a = new ParseJSONFile("angle");
    	String an = a.Data();
    	double angle = Double.parseDouble(an);
		Robot.driveTrain.pidAngle.setPID(Robot.prefs.getDouble("angleP", 0), Robot.prefs.getDouble("angleI", 0), Robot.prefs.getDouble("angleD", 0));
    	generateAngle = new AutoGenerate(angle, 7, "angle");
		generateAngle.generateProfile();
		angles = generateAngle.returnPos();
		Robot.driveTrain.pidAngle.reset();
		Robot.driveTrain.pidAngle.enable();
		Robot.driveTrain.pidAngle.setAbsoluteTolerance(.01);
		*/
		//Robot.driveTrain.pidAngle.setSetpoint(degrees);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/*
    	if(count < angles.size()){
    		Robot.driveTrain.pidAngle.setSetpoint(angles.get(count));
    		count++;
    	}
    
    	*/
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {/*if(count>= angles.size()){return true;} else {return false;}*/
    	return false;
    }
      

    // Called once after isFinished returns true
    protected void end() {
  //  	Robot.driveTrain.pidAngle.disable();
    //	RobotMap.driveTrainDifferentialDrive1.arcadeDrive(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}