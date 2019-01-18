package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.subsystems.BallHandling.BallHandlingState;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenFlap extends Command {
	
	private double t;
	private Timer timer = new Timer();
	
	private boolean firstTime = true;
	private boolean done = false;
	
	private BallHandlingState iState;
	private BallHandlingState cState;

    public OpenFlap(double defultClearingTime) {
    	requires(Robot.gearMech);
    	requires(Robot.ballHandling);
    	t = defultClearingTime;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	iState = Robot.ballHandling.getState();
    	done = false;
		switch(iState){
			case shooting: 
			case readyToShoot:
				Robot.ballHandling.setState(BallHandlingState.clearElevator);
				cState = Robot.ballHandling.getState();
				
				break;
			case pickingUp:
			case readyToPickupOrDump: 
			case dumping:
				Robot.ballHandling.setState(BallHandlingState.pickingUp);
				cState = Robot.ballHandling.getState();
				
				break;
			case clearElevator:
			case clearPickUp:
				System.out.println("Called the OpenFlap command while you are in an intermeate step - OpenFlap.java - init - case clearElevator/clearPickUp");
				
				break;
		}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(BallHandlingState.readyToPickupOrDump == Robot.ballHandling.getState() || BallHandlingState.pickingUp == Robot.ballHandling.getState()){
    		Robot.gearMech.gearMechFlap.set(true);
    		done = true;
    	}else{
    		switch(cState){
				case shooting: 
				case readyToShoot:
					System.out.println("somehow you sliped through the inti call - OpenFlap.java - execute - case shooting/readyToShoot");
					
					Robot.ballHandling.setState(BallHandlingState.clearElevator);
					cState = Robot.ballHandling.getState();
					
					break;
				case pickingUp:
				case readyToPickupOrDump: 
				case dumping:
		    		Robot.ballHandling.dumperLift.set(true);
		    		Robot.ballHandling.pickUpFlap.set(true);
		    		Robot.ballHandling.setElevator(-1);		//add pref for speed?
			    	Robot.ballHandling.setOutsideRoller(1);	//add pref for speed?
			    		
			    	Robot.ballHandling.setState(BallHandlingState.readyToPickupOrDump);
					cState = Robot.ballHandling.getState();
					
		    		done = true;
					
					break;
				case clearElevator:
					if(firstTime){
						timer.reset();
						timer.start();
						firstTime = false;
					}
					
					Robot.ballHandling.dumperLift.set(true);
					Robot.ballHandling.pickUpFlap.set(true);
					Robot.ballHandling.setElevator(-1); 		//add pref for speed?
			    	Robot.ballHandling.setOutsideRoller(-1); 	//add pref for speed?
			    	
					if(intermediantStepDone()){
						Robot.ballHandling.setState(BallHandlingState.pickingUp);
						cState = Robot.ballHandling.getState();
						
						Robot.ballHandling.dumperLift.set(true);
			    		Robot.ballHandling.pickUpFlap.set(true);
			    		Robot.ballHandling.setElevator(-1);		//add pref for speed?
				    	Robot.ballHandling.setOutsideRoller(1);	//add pref for speed?

			    		done = true;
					}
					
					break;
				case clearPickUp:
					System.out.println("You called while in an intermedet step - OpenFlap.java - execute - case clearPickUp");
					
					Robot.ballHandling.dumperLift.set(true);
		    		Robot.ballHandling.pickUpFlap.set(true);
		    		Robot.ballHandling.setElevator(-1);		//add pref for speed?
			    	Robot.ballHandling.setOutsideRoller(1);	//add pref for speed?
		
			    	Robot.ballHandling.setState(BallHandlingState.pickingUp);
					cState = Robot.ballHandling.getState();
					
					break;
			}
    	}
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.ballHandling.setState(cState);
    	timer.stop();
    }
    
    protected void interrupted() {
    	Robot.ballHandling.setState(cState);
    	timer.stop();
    }
    
    protected boolean intermediantStepDone() {
    	return timer.get() > t;
    }
}