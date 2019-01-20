package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.subsystems.BallHandling.BallHandlingState;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class Shoot extends Command {
	
	private double t;
	private Timer timer = new Timer();
	
	private boolean firstTime = true;
	
	private BallHandlingState iState;
	private BallHandlingState cState;

    public Shoot(double defultClearingTime) {
    	requires(Robot.ballHandling);
    	t = defultClearingTime;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	iState = Robot.ballHandling.getState();
		timer.reset();
		timer.start();
		switch(iState){
			case shooting: 
			case readyToShoot:
				Robot.ballHandling.setState(BallHandlingState.shooting);
				cState = Robot.ballHandling.getState();
				break;
			case pickingUp:
			case readyToPickupOrDump: 
			case dumping:
				Robot.ballHandling.setState(BallHandlingState.clearPickUp);
				cState = Robot.ballHandling.getState();
				break;
			case clearElevator:
			case clearPickUp:
				System.out.println("Called the Shoot command while you are in an intermeate step - Shoot.java - init - case clearElevator/clearPickUp");
				break;
		}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch(cState){
			case shooting: 
			case readyToShoot:
				Robot.ballHandling.setDumperLift(true);
	    		//Robot.ballHandling.pickUpFlap.set(false);
	    		Robot.ballHandling.setElevator(1);		//add pref for speed?
		    	Robot.ballHandling.setOutsideRoller(-1);	//add pref for speed?
				
				Robot.ballHandling.setState(BallHandlingState.shooting);
				cState = Robot.ballHandling.getState();
				
				break;
			case pickingUp: 
			case dumping:
			case readyToPickupOrDump:
				System.out.println("somehow you sliped through the inti call - Shoot.java - execute - case pickingUp/dumping/readyToPickupOrDump");
				
		    	Robot.ballHandling.setState(BallHandlingState.clearPickUp);
				cState = Robot.ballHandling.getState();
				
				break;
			case clearElevator:
				System.out.println("You called while in an intermedet step - Shoot.java - execute - case clearElevator");
				
		    	Robot.ballHandling.setState(BallHandlingState.shooting);
				cState = Robot.ballHandling.getState();
				
				break;
			case clearPickUp:
				if(firstTime){
					firstTime = false;
				}
				
				Robot.ballHandling.setDumperLift(true);
				//Robot.ballHandling.pickUpFlap.set(true);
				Robot.ballHandling.setElevator(-1); 		//add pref for speed?
		    	Robot.ballHandling.setOutsideRoller(-1); 	//add pref for speed?
		    	
				if(intermediantStepDone()){
					Robot.ballHandling.setState(BallHandlingState.shooting);
					cState = Robot.ballHandling.getState();
				}
				
				break;
		}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    protected void interrupted() {
    	Robot.ballHandling.setState(cState);
    }
    
    protected boolean intermediantStepDone() {
    	return timer.get() > t;
    }
}
