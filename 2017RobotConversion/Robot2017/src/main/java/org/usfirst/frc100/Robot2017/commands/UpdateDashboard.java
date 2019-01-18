package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class UpdateDashboard extends Command {

	public UpdateDashboard() {
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		SmartDashboard.putString("DriveTrain/~TYPE~", "SubSystem");
		SmartDashboard.putString("BallHandling/~TYPE~", "SubSystem");
		SmartDashboard.putString("GearMech/~TYPE~", "SubSystem");
		SmartDashboard.putString("PeterssUnbeatableScalingMechanismWithoutpNeumatics/~TYPE~", "SubSystem");
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		Robot.driveTrain.updateDashboard();
		Robot.ballHandling.updateDashboard();
		Robot.gearMech.updateDashboard();
		Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics.updateDashboard();
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