package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbJoysticks extends Command {

    public ClimbJoysticks() {
    	requires(Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics.climbJoysticks(Robot.oi.operater);
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
