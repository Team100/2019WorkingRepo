package org.usfirst.frc100.Robot2017.commands;



import org.usfirst.frc100.Robot2017.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class AutoDriveToPegOtherSide extends CommandGroup {

    public AutoDriveToPegOtherSide() {
    	
    	 //addSequential(new GenerateSPath(4.5, 7.8)); //4.5 7.8
    	 addSequential(new FollowMotionProfile(5.0));
         addSequential(new TurnToAngle(-50));
     	 addSequential(new FollowMotionProfile("go to 2 feet"));
     	 addSequential(new TurnToAngle("vision"));
     	 addSequential(new FollowMotionProfile());    
     	//addSequential(new TurnToAngle("vision"));
     	//addSequential(new FollowMotionProfile());
     	double DEFAULT_SOLINOIDWAIT = .25;
     	addSequential(new OpenFlap(Robot.prefs.getDouble("robot_solinoidWait", DEFAULT_SOLINOIDWAIT)));
     	addSequential(new FollowMotionProfile(-3.0));
    }
}
