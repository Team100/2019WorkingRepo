package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class TeleopDriveToGear extends CommandGroup {

    public TeleopDriveToGear() {
    	addSequential(new TurnToAngle("vision"));
    	addSequential(new FollowMotionProfile());
    //	double DEFAULT_SOLINOIDWAIT = .25;
     	//addSequential(new OpenGear(Robot.prefs.getDouble("robot_solinoidWait", DEFAULT_SOLINOIDWAIT)));
    //   	addSequential(new FollowMotionProfile(-3.0));
       
    }
}
