package org.usfirst.frc100.Robot2017.commands;


import java.util.ArrayList;



import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import org.usfirst.frc100.Robot2017.Robot;
import org.usfirst.frc100.Robot2017.RobotMap;
//import org.usfirst.frc100.RobotAndrew.commands.AutoGenerate;
//import org.usfirst.frc100.RobotAndrew.commands.GetVisionData;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class FollowMotionProfile extends Command{
	int count = 0;
	double maxVal = 100; 
	int changevalue = 0;
	double setP;
	double setPP;
	double dist;
	double distanceHolder;
	public GetVisionData vision;
	public static ArrayList<Double> position; //= new ArrayList<Double>();
	public static ArrayList<Double> velocity; //= new ArrayList<Double>();
	public AutoGenerate profile;
	public AutoGenerate profileR;
	public AutoGenerate profileL;
	public static ArrayList<Double> positionR;
	public static ArrayList<Double> positionL;
	public boolean useVision;
	public double stageValue;
	static Timer timer = new Timer();
	private static final String SmartDashoard = null;

	public FollowMotionProfile() {
		stageValue = 0;
		useVision = true;
		requires(Robot.driveTrain);
	}
	
	public FollowMotionProfile(String stage){
		useVision = true;
		stageValue = 2;
		requires(Robot.driveTrain);
	}
	
	public FollowMotionProfile(double distR, double distL){
		System.out.println(distR);
		useVision = false;
		dist = 20;
		
		
	}
	public FollowMotionProfile(double dista) {
		useVision = false;
		dist = dista;
		if(dist < 0){
			distanceHolder = dist*-1;
		} else {
			distanceHolder = dist;
		}
		requires(Robot.driveTrain);
	}
	
	public void initialize() {
		if(useVision == true){
			vision = new GetVisionData();
			dist = (((vision.calculateDistance())/12)) - stageValue;// - stageValue);//- 6; -12//(vision.calculateDistance()-20)/12);
			profile = new AutoGenerate(dist, 2.5); //3.5 dist
			profile.generateProfile();
		} else { 
			profile = new AutoGenerate(distanceHolder, 2.5);
			profile.generateProfile();
		}
		position = profile.returnPos();
		velocity = profile.returnVel();
		count = 0;
		RobotMap.driveTrainLeftEncoder.reset(); 
		RobotMap.driveTrainRightEncoder.reset();
		Robot.driveTrain.pidPosRight.setAbsoluteTolerance(0.1);
		Robot.driveTrain.pidPosLeft.setAbsoluteTolerance(0.1);
		Robot.driveTrain.pidVelRight.setAbsoluteTolerance(0.01);
		Robot.driveTrain.pidVelLeft.setAbsoluteTolerance(0.01);
		Robot.driveTrain.pidPosRight.enable();
		Robot.driveTrain.pidPosLeft.enable();
	//	System.out.println(dist); 
	}
	
	
	public void execute() {
		
		if(count < position.size()){
			if(useVision == true || dist > 0){
				Robot.driveTrain.pidPosLeft.setSetpoint(position.get(count));
				Robot.driveTrain.pidPosRight.setSetpoint(position.get(count));
			} 
			else if(useVision == false && dist < 0){
				Robot.driveTrain.pidPosLeft.setSetpoint(-position.get(count));
				Robot.driveTrain.pidPosRight.setSetpoint(-position.get(count));
			} 
			else {
				Robot.driveTrain.pidPosLeft.setSetpoint(position.get(count));
				Robot.driveTrain.pidPosRight.setSetpoint(position.get(count));
			}
			count++;
			SmartDashboard.putNumber("counter fMP", count);
			SmartDashboard.putNumber("sizeDistP", position.size());
		}
	}

	protected boolean isFinished() {
		if(count >= position.size() || dist > 100){ return true;} else { return false;}
	}
	protected void end(){
		Robot.driveTrain.pidPosLeft.disable();
		Robot.driveTrain.pidPosRight.disable();
		Robot.driveTrain.pidPosLeft.reset();
		Robot.driveTrain.pidPosRight.reset();
		//Robot.driveTrain.stop();
	}
}
