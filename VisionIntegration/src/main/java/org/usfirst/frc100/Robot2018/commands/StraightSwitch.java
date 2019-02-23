package org.usfirst.frc100.Robot2018.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class StraightSwitch extends CommandGroup {

	public StraightSwitch() {
		
	}
	
    public StraightSwitch(String path) {
    	addSequential(new PathFinding(path));
    	if(path == "LeftLeftScale" || path == "RightRightScale" || path == "RightRightScaleFront" || path == "LeftLeftScaleFront") {
    		addSequential(new ElevatorUp());
    		addSequential(new IntakeOut());
    		addSequential(new ElevatorDown());
    	}else if(path != "CrossLine" || path != "null") {
    		addSequential(new IntakeOut());
    	}
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
