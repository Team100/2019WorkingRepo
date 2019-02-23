package org.usfirst.frc100.Robot2018.commands;

import org.usfirst.frc100.Robot2018.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class toggleElevatorSpeed extends Command {

    public toggleElevatorSpeed() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	if(Robot.elevatorMultiplier == Robot.kElevatorMultiplierMax) {
    		Robot.elevatorMultiplier = Robot.kElevatorMultiplierMin;
    	}
    	else if(Robot.elevatorMultiplier == Robot.kElevatorMultiplierMin) {
    		Robot.elevatorMultiplier = Robot.kElevatorMultiplierMin;
    	}
    	else {
    		Robot.elevatorMultiplier = Robot.kElevatorMultiplierMin; //Go to min if issue
    	}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
