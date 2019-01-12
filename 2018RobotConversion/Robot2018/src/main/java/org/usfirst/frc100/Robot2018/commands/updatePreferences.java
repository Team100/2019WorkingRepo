package org.usfirst.frc100.Robot2018.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc100.Robot2018.Robot;

/**
 * 
 * @author Team100
 *
 *<h1> Update Preferences </h1>
 *<p> Updates the preferences </p>
 * 
 */
public class updatePreferences extends Command {
	/**
	 * DEFINE YOUR PREFERENCES HERE
	 * Preference Format: {@code PREF<Season><Subsystem><Group><name> ===> PREF2018LiftPIDFkP}
	 * Variable Format: {@code FRC<Season><Subsystem><Group><name>  ===> FRC2018LiftPIDFkP}
	 */
	
	public double FRC2018TestPreferencesOne;
	public double kP;
	public double kI;
	public double kD;
	public double kA;
	public double kP2;
	public double kI2;
	public double kD2;
	public double kA2;
	
	/*
	 * END OF Variable creation
	 */
	
	
	
    public updatePreferences() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	
    	/**
    	 * Constructor not used
    	 */
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	/**
    	 * Utilizes a variable to get the data from preferences
    	 * Example: PREF2018TestPreferencesOne = Robot.prefs.getDouble("2018-Test-Preferences-One", 0.0);
    	 * Duplicate the line below and change the appropriate values for your new variable
    	 */
    	
    	
    	
    	FRC2018TestPreferencesOne = Robot.prefs.getDouble("PREF2018TestPreferencesOne", 1);
    	SmartDashboard.putString("TestPref", Double.toString(FRC2018TestPreferencesOne));
    	
    	kP = Robot.prefs.getDouble("P",
				0);
    	kI = Robot.prefs.getDouble("I",
				0);
    	kD = Robot.prefs.getDouble("D",
				0);
    	kA = Robot.prefs.getDouble("F",
				0);
    	
    	kP2 = Robot.prefs.getDouble("P",
				0);
    	kI2 = Robot.prefs.getDouble("I",
				0);
    	kD2 = Robot.prefs.getDouble("D",
				0);
    	kA2 = Robot.prefs.getDouble("F",
				0);
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	/**
    	 * Update data to SmartDashboard
    	 */
    	SmartDashboard.putString("TestPref", Double.toString(FRC2018TestPreferencesOne));
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
        /**
         * isFinished NOT USED
         */
    }

    // Called once after isFinished returns true
    protected void end() {
    	/**
    	 * end NOT USED
    	 */
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run	
    protected void interrupted() {
    	/**
    	 * Interrupted Not Used 
    	 */
    }
}