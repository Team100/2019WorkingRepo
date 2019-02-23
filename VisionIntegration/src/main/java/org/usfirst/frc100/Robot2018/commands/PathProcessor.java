package org.usfirst.frc100.Robot2018.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class PathProcessor extends CommandGroup {

	public PathProcessor() {

	}

	public PathProcessor(String path) {
		addSequential(new PathfindingV2Issue4(path));
		if (path == "LeftLeftScale" || path == "RightRightScale" || path == "RightRightScaleFront"
				|| path == "LeftLeftScaleFront") {
			addSequential(new ElevatorUp());
			addSequential(new IntakeOut());
			addSequential(new ElevatorDown());
		} else if (path != "CrossLine" || path != "null") {
			addSequential(new IntakeOut());
		}
	}
}
