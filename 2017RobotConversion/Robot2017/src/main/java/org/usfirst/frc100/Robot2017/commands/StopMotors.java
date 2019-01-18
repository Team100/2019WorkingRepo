package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.subsystems.BallHandling.BallHandlingState;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class StopMotors extends Command {
	
	private double t;
	private Timer timer = new Timer();
	
	private BallHandlingState iState;
	private BallHandlingState cState;

    public StopMotors() {
    	requires(Robot.ballHandling);
    }

    protected void initialize() {
    	iState = Robot.ballHandling.getState();
    	cState = iState;
    }

    protected void execute() {
    	switch(cState){
			case shooting: 
			case readyToShoot:
				Robot.ballHandling.setDumperLift(false);
				//Robot.ballHandling.pickUpFlap.set(false);
				Robot.ballHandling.setOutsideRoller(0);
				Robot.ballHandling.setElevator(0);
				
				Robot.ballHandling.setState(BallHandlingState.readyToShoot);
				cState = Robot.ballHandling.getState();
				break;
			case pickingUp:
			case dumping:
			case readyToPickupOrDump: 
				Robot.ballHandling.setDumperLift(true);
				//Robot.ballHandling.pickUpFlap.set(true);
				Robot.ballHandling.setOutsideRoller(0);
				Robot.ballHandling.setElevator(0);
				
				Robot.ballHandling.setState(BallHandlingState.readyToPickupOrDump);
				cState = Robot.ballHandling.getState();
				break;
			case clearElevator:
				System.out.println("Cant Stop Motors in intermidte step");
				
				if(timer.get() > 0.75){
					Robot.ballHandling.setState(BallHandlingState.readyToShoot);
				}
				
				cState = Robot.ballHandling.getState();
				break;
			case clearPickUp:
				System.out.println("Cant Stop Motors in intermidte step");
				
				if(timer.get() > 0.25){
					Robot.ballHandling.setState(BallHandlingState.readyToPickupOrDump);
				}
				
				cState = Robot.ballHandling.getState();
				break;
    	}
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
		Robot.ballHandling.setState(cState);
    }
    
    protected boolean intermediantStepDone() {
    	return timer.get() > t;
    }
}
