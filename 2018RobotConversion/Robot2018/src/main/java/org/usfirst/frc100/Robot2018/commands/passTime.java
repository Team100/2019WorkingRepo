package org.usfirst.frc100.Robot2018.commands;


import java.util.Timer;
import java.util.TimerTask;


import org.usfirst.frc100.Robot2018.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class passTime extends Command {
	public static double timePassed;
	public static boolean done;
	public static long StartTime;
	public static double t;
	public static boolean transfer;
	Timer timer;
    public passTime(double t){

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	transfer = false;
    	done=false;
    	StartTime = System.currentTimeMillis();
    	timer = new Timer();
    	timer.schedule(new TimerTask() {
    	    @Override
    	    public void run() {
    	    	run();
    	    }
    	  }, 0, 20);
    	//taking the time in nanoseconds and  converting to milliseconds 
    	//timePassed = System.nanoTime()/1000000000;
    }
    protected void run(){
    	if((System.currentTimeMillis() - StartTime)/1000 > (5)) {
    		done = true;
    		//you found the secret message proceed to checkout please, and keep on t-posing!
    	}
    	//System.out.println("running");

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	//if(System.nanoTime()/1000000000 >=  timePassed + (t)) {
    		//done = true;
    	//}
    

    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {

    	transfer = true;
    	

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}

