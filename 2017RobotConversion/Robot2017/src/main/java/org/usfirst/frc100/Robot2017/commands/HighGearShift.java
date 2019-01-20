package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class HighGearShift extends Command {

	private double t;
	private Timer timer = new Timer();
	
    public HighGearShift(double time) {
    	requires(Robot.driveTrain);
    	t = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.reset();
		timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.driveTrain.setDriveTrainShifter(true);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return timer.get() > t;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
