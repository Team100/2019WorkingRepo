package org.usfirst.frc100.Robot2017.subsystems;

import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.RobotMap;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BallHandling extends Subsystem {

	public final VictorSP outsideRoller = RobotMap.outsideRoller;
	public final VictorSP elevator = RobotMap.elevator;
	public final Solenoid dumperLift = RobotMap.dumperLift;
	public final Solenoid pickUpFlap = RobotMap.pickUpFlap;
	public final Encoder elevatorEncoder = RobotMap.elevatorEncoder;
	//public static double timeToSwitch = 2;
	//public static double totalLoopChanges = timeToSwitch/0.002;
	//public static double defultRamp = totalLoopChanges/timeToSwitch;
	private static final double DEFAULT_BALL_HANDLING_RAMP = 0.1;
	private static final double DEFAULT_SOLINOIDWAIT = 0.75;
	public double ballHandling_ramp;
	public double robot_solinoidWait;
	
	private BallHandlingState mState = BallHandlingState.readyToPickupOrDump;
	
	public BallHandling(){
		if (!Robot.prefs.containsKey("ballHandling_ramp")) {
			Robot.prefs.putDouble("ballHandling_ramp", DEFAULT_BALL_HANDLING_RAMP);
		}
		ballHandling_ramp = Robot.prefs.getDouble("ballHandling_ramp", DEFAULT_BALL_HANDLING_RAMP);
		if (!Robot.prefs.containsKey("robot_solinoidWait")) {
			Robot.prefs.putDouble("robot_solinoidWait", DEFAULT_SOLINOIDWAIT);
		}
		robot_solinoidWait = Robot.prefs.getDouble("robot_solinoidWait", DEFAULT_SOLINOIDWAIT);
	}
	
	public void updateDashboard() {
		SmartDashboard.putNumber("BallHandling/Elevator Encoder Raw", elevatorEncoder.getRaw());
		SmartDashboard.putNumber("BallHandling/Elevator Encoder Count", elevatorEncoder.get());
		SmartDashboard.putNumber("BallHandling/Elevator Encoder Distance", elevatorEncoder.getDistance());
    	SmartDashboard.putNumber("BallHandling/Elevator Encoder Rate", elevatorEncoder.getRate());

    	SmartDashboard.putNumber("BallHandling/Outside Roller Rate", outsideRoller.get());
    	
    	SmartDashboard.putNumber("BallHandling/Elevator Rate", elevator.get());
    	
    	SmartDashboard.putBoolean("BallHandling/Dumper Lift state", dumperLift.get());
    	
    	SmartDashboard.putBoolean("BallHandling/Pickup Flap state", pickUpFlap.get()); // :C
	}
	
	public enum BallHandlingState{
    	
    	shooting 			(1),
    	readyToShoot 		(2),
    	pickingUp 			(3),
    	dumping				(4),
    	readyToPickupOrDump (5),
    	clearElevator 		(6),
    	clearPickUp 		(7);
    	
    	public final int number;
    	
    	private BallHandlingState(int number){
    		this.number = number;
    	}
    }
	
	
	public BallHandlingState getState(){
		return mState;
	}

	public void setState(BallHandlingState state){
		mState = state;
	}
	
    public void initDefaultCommand() {
    	
    }
    
    public void setOutsideRoller(double value){
    	if(value == 0){
    		outsideRoller.set(0);
		}else if(Math.abs(value*ballHandling_ramp + outsideRoller.get()) <= 1){
    		outsideRoller.set(value*ballHandling_ramp + outsideRoller.get());
    	}else if(value*ballHandling_ramp + outsideRoller.get() > 1){
    		outsideRoller.set(1);
    	}else if(value*ballHandling_ramp + outsideRoller.get() < -1){
    		outsideRoller.set(-1);
    	}else{
    		outsideRoller.set(0);
    	}
    }
    
    public void setElevator(double value){
    	if(value == 0){
    		elevator.set(0);
    	}else if(Math.abs(value*ballHandling_ramp + elevator.get()) <= 1){
    		elevator.set(value*ballHandling_ramp + elevator.get());
    	}else if(value*ballHandling_ramp + elevator.get() > 1){
    		elevator.set(1);
    	}else if(value*ballHandling_ramp + elevator.get() < -1){
    		elevator.set(-1);
    	}else {
    		elevator.set(0);
    	}
    }
    
    public boolean isDumperLiftOpen(){
    	return dumperLift.get();
    }
    
    public void setDumperLift(boolean open){
    	if(open){
    		dumperLift.set(open);
    	}else{
    		dumperLift.set(open);
    	}
    }
    
    public boolean isPickUpFlapClosed(){
    	return pickUpFlap.get();
    }
    
    public void setPickUpFlap(boolean open){
    	if(open){
    		pickUpFlap.set(open);
    	}else{
    		pickUpFlap.set(!open);
    	}
    }
}

