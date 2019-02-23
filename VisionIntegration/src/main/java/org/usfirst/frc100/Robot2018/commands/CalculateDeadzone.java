package org.usfirst.frc100.Robot2018.commands;

import org.usfirst.frc100.Robot2018.RobotMap;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CalculateDeadzone extends Command{
	public static double posDeadzone;
	public static double negDeadzone;
	public boolean calculate = true;
	public boolean calculateback = true;
	public CalculateDeadzone(){
		posDeadzone = 0;
		negDeadzone = 0;
	}

	public void initizlize(){
		
	}
	
	public void execute() {
		
		if(calculate && RobotMap.driveTrainRightMaster.getSelectedSensorVelocity(0) > 0) {
			posDeadzone = RobotMap.driveTrainRightMaster.get();
			calculate = false;
		}
		
		if(calculateback && RobotMap.driveTrainLeftMaster.getSelectedSensorVelocity(0) < 0) {
			negDeadzone = RobotMap.driveTrainLeftMaster.get();
			calculateback = false;
		}
		new UpdateSmartDashboard();
	}
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
