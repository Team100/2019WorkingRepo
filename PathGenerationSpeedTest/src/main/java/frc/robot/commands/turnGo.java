/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.analog.adis16448.frc.ADIS16448_IMU;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.robot.FalconPathPlanner;

import frc.robot.*;

import edu.wpi.first.networktables.*;
public class turnGo extends Command {
  private boolean fin;
  double distance;
  double extrusion;
  double angle;
  double endLocationX;
  double endLocationY;
  double[][] waypoints;

  public turnGo(){

  }

	// Called just before this Command runs the first time
  @Override
  protected void initialize() {
    String state = "";
    double globalAngle = Robot.ahrs.getFusedHeading();
    NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable table = inst.getTable("camera");
    String cameraData = table.getEntry("data").getString(null);
    if(cameraData == null){
      cameraData = "";
      System.out.println("help");
    }else{
    try{
      angle = Double.parseDouble(cameraData.substring(cameraData.indexOf("angle\":") + 7, cameraData.indexOf("angle\":") + 11));

    }catch(Error e){
    }
    try{
      distance = Double.parseDouble(cameraData.substring(cameraData.indexOf("distance\":") + 11, cameraData.indexOf("distance\":") + 15));
    }catch(Error e){

    }
  }
  angle = 20;
  distance = 136;
  double relativeAngle = 0;
  if(globalAngle < 90 && globalAngle > 0){
    //This state is leftleft
    relativeAngle = (360-globalAngle) - angle;
  }else if(globalAngle < 180 && globalAngle > 90){
    //this state is leftright
    relativeAngle = (360-globalAngle) + angle;
  }else if(globalAngle < 270 && globalAngle > 180){
    //this state is rightleft
    relativeAngle = (360-globalAngle) + angle;
  }else if(globalAngle < 360 && globalAngle > 270){
    //this state is rightright
    relativeAngle = (360-globalAngle) - angle;
  }
  endLocationX = Math.cos(relativeAngle) * distance/12;
  endLocationY = Math.sin(relativeAngle) * distance/12;
    extrusion = distance/12/4; 
    double[][] waypoints = new double[][]{
      {0.0, 0.0},
      {endLocationX/5, 0.0},
      // {Math.cos(approachAngle)*(distance/2), -Math.sin(approachAngle)*(distance/2)},
      {endLocationX, endLocationY*3/4},
      {endLocationX, endLocationY}
  }; 
		fin = false;
		long start = System.currentTimeMillis();

		//create waypoint path
    /*double[][] path = {}*/
		double totalTime = (((distance/12)/11)+3); //seconds
		double timeStep = 0.02; //period of control loop on Rio, seconds
		double robotTrackWidth = 2; //distance between left and right wheels, feet
		final FalconPathPlanner path = new FalconPathPlanner(waypoints);
		path.calculate(totalTime, timeStep, robotTrackWidth);

    System.out.println("Time in ms: " + (System.currentTimeMillis()-start));
    
    double[][] pathfinderPath = new double[path.smoothRightVelocity.length][3];
    for(int i = 0; i < path.smoothLeftVelocity.length; i++){
			System.out.print(path.smoothLeftVelocity[i][1] + ", ");
			System.out.print(path.smoothRightVelocity[i][1] + ", ");
			System.out.print(path.smoothPath[i][0] + ", ");
			System.out.print(path.smoothPath[i][1] + ", ");
			System.out.println(path.heading[i][1]);
      pathfinderPath[i][0] = path.smoothLeftVelocity[i][1];
      pathfinderPath[i][1] = path.smoothRightVelocity[i][1];
      pathfinderPath[i][2] = path.heading[i][1];
    }
    System.out.println("X end pos =" + (endLocationX) + ". Y end pos" + endLocationY + ". Angle: " + angle + ". Global angle" + globalAngle);
    
    // calculations for curvature https://www.desmos.com/calculator/vhrlmfzv7p
    //new PathFinder(pathfinderPath);
    System.out.println(System.currentTimeMillis() - start);
  }
  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    fin = true;
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return fin;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
