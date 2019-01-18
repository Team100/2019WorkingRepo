package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class ClimbNudge extends Command {
	
	private static final double DEFAULT_NUDGE_UP = 1.0;
	 private static final double DEFAULT_NUDGE_DOWN= -1.0;
	 private String st;
	 private double climberNudgeUp;
	 private double climberNudgeDown;
	 private double t;
	 private Timer timer = new Timer();
	 private boolean done = false;

    public ClimbNudge(String state, double time) {st = state;
    	requires(Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics);
    	if (!Robot.prefs.containsKey("climberNudgeUp")) {
    		Robot.prefs.putDouble("climberNudgeUp", DEFAULT_NUDGE_DOWN);
    	}
    	if (!Robot.prefs.containsKey("climberNudgeDown")) {
    		Robot.prefs.putDouble("climberNudgeDown", DEFAULT_NUDGE_DOWN);
    	}
    	t = time;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	climberNudgeUp = Robot.prefs.getDouble("climberNudgeUp",DEFAULT_NUDGE_UP);
    	
    	climberNudgeDown = Robot.prefs.getDouble("climberNudgeDown",DEFAULT_NUDGE_DOWN);
    	done = false;
    	
    	timer.reset();
    	timer.start();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(st == "up"){
    		 Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics.climbNudge(climberNudgeUp);
    	} else {
    		 Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics.climbNudge(climberNudgeDown);
    	}
    	
    	if(timerDone()){
    		 done = true;
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.peterssUnbeatableScalingMechanismWithoutpNeumatics.climbNudge(0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	timer.stop();
    }
    protected boolean timerDone(){
    	return timer.get() > t;
    }
}
