package org.usfirst.frc100.Robot2017.commands;

import java.util.ArrayList;


import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.RobotMap;
//import org.usfirst.frc100.RobotAndrew.commands.AutoGenerate;
//import org.usfirst.frc100.RobotAndrew.commands.GetVisionData;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class TurnToAngle extends Command{
	int countOnTarget = 0;
	double initialAngle = 0;
	boolean cancelPID;
	double desiredAngle;
	double currentAngle;
	double angless;
	public GetVisionData v;
	public AutoGenerate generateAngle;
	double visionData;
	int counter; 
	public static ArrayList<Double> angles;
 	String state;
	
	public TurnToAngle(){
		cancelPID = true;
		state = "stop";
	}
	
	public TurnToAngle(String vision){
		state = "vision";
	}
	
	public TurnToAngle(double angle){
		cancelPID = false;
		desiredAngle = angle ;
		state = "    ";
	}
	
	public void initialize(){
		counter = 0;
		RobotMap.gyro.reset();
		Robot.driveTrain.pidAngle.reset();    // this part can be changes for law of sines
		Robot.driveTrain.pidAngle.setAbsoluteTolerance(0.3);
		if(state == "vision"){
			
			v = new GetVisionData();
			visionData = v.calculateAngle();
			SmartDashboard.putNumber("vision Datas", visionData);
			angless = visionData;
			if(visionData < 0){
				angless *= -1;
			} else {
				angless *= .8;
			}
			generateAngle = new AutoGenerate(angless, .8, "angle");
			generateAngle.generateProfile();
			angles = generateAngle.returnPos();
			//Robot.driveTrain.pidAngle.setSetpoint(visionData);
			Robot.driveTrain.pidAngle.enable();
			initialAngle = RobotMap.gyro.getAngle();
			//System.out.println("im in!");
			SmartDashboard.putNumber("length Angle", angles.size());
		} else {
			generateAngle = new AutoGenerate(desiredAngle, .8, "angle");
			generateAngle.generateProfile();
			angles = generateAngle.returnPos();
		//	Robot.driveTrain.pidAngle.setSetpoint(desiredAngle);
			Robot.driveTrain.pidAngle.enable();//initialAngle +
			Robot.driveTrain.pidAngle.setSetpoint( (angles.get(counter)));
		}
	}
	
	public void execute(){ //initialAngle + 
		if(counter < angles.size()){
			if(visionData < 0){
				Robot.driveTrain.pidAngle.setSetpoint(  -(angles.get(counter))); //initialAngle - // this part can be changes for law of sines
				//System.out.println("anngle sets" + ((initialAngle -(angles.get(counter)))));
			}
			else{ 
				Robot.driveTrain.pidAngle.setSetpoint((angles.get(counter)));	//(initialAngle + // this part can be changes for law of sines
			//	System.out.println("anngle set" + initialAngle +(angles.get(counter)));  
			}
			counter++;
		}
		SmartDashboard.putBoolean("target", Robot.driveTrain.pidAngle.onTarget());
		SmartDashboard.putBoolean("pidState", cancelPID);
		SmartDashboard.putNumber("angleerrors", Robot.driveTrain.pidAngle.getError());
		SmartDashboard.putNumber("AverageAngleError", Robot.driveTrain.pidAngle.getAvgError());
		if(Robot.driveTrain.pidAngle.onTarget() && countOnTarget == 0){
			countOnTarget++;
		} else if(Robot.driveTrain.pidAngle.onTarget() && countOnTarget > 0){
			countOnTarget++;
		} else {
			countOnTarget = 0;
		}
		SmartDashboard.putNumber("countTraget", countOnTarget);
	}
	protected boolean isFinished() {

		if((counter >= angles.size() || RobotMap.gyro.getAngle() >= (Math.abs(desiredAngle) - .2)) && counter >= angles.size()-20 )//Robot.driveTrain.pidAngle.onTarget() && Math.abs(RobotMap.leftMaster.get()) < .1 && Robot.driveTrain.pidAngle.getAvgError() < .3 )//&& countOnTarget >= 3)
			return true;
			
		else
			return false;
	}
	public void end(){
		System.out.println("ended");
		Robot.driveTrain.pidAngle.disable();
		Robot.driveTrain.pidAngle.reset();
	}
}

