
package org.usfirst.frc100.Robot2017.commands;

import org.usfirst.frc100.Robot2017.Robot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class updatePreferneces extends Command {

	//@Override
	public double posP;
	public double posI;
	public double posF;
	public double velP;
	public double velI;
	public double velF;
	public double anP;
	public double anI;
	public double anD;
	protected void initialize() {
		posP = Robot.prefs.getDouble("driveTrain_kP",0);
		posI = Robot.prefs.getDouble("driveTrain_kI",0);
		posF = Robot.prefs.getDouble("driveTrain_kF",0);
		Robot.driveTrain.pidPosRight.setPID(posP, posI,0,  posF);
		
	
		
		//-----------------------------------------------------
		velP = Robot.prefs.getDouble("driveVelP",0);
		velI = Robot.prefs.getDouble("driveVelI",0);
		velF = Robot.prefs.getDouble("driveVelF",0);
		Robot.driveTrain.pidPosLeft.setPID(velP, velI,0, velF);
	//	Robot.driveTrain.pidPosRight.setPID(posP, posI, 0, posF);
	//	Robot.driveTrain.pidPosLeft.setPID(posP, posI, 0, posF);
		/////////////////////////////////////////////////////
		anP = Robot.prefs.getDouble("angleP", 0);
		anI = Robot.prefs.getDouble("angleI", 0);
		anD = Robot.prefs.getDouble("andleD", 0);
		Robot.driveTrain.pidAngle.setPID(anP, anI, anD);
		
		
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("p", Robot.driveTrain.pidPosRight.getP());
		SmartDashboard.putNumber("i", posI);
		SmartDashboard.putNumber("f", posF);
		//-------------------------------------------------
		SmartDashboard.putNumber("pV", velP);
		SmartDashboard.putNumber("iV", velI);
		SmartDashboard.putNumber("fV", velF);
		//---------------------------------------------
		SmartDashboard.putNumber("pA", anP);
		SmartDashboard.putNumber("pI", anI);
		SmartDashboard.putNumber("pD", anD);
		
		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}

}
