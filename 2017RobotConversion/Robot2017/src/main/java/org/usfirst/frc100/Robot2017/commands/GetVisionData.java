package org.usfirst.frc100.Robot2017.commands;




import org.usfirst.frc100.Robot2017.RobotMap;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;

public class GetVisionData{
	//private  final double  PEG_LENGTH = 1.125; 
	private double turnAngle;
	private static double distance;
	private double angle;
	private boolean isValid;
	double empty;
	public NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");
	public GetVisionData(){ 
		
		distance = table.getNumber("distance", empty); //
		angle = table.getNumber("degreeOffset", empty);
		isValid = table.getBoolean("angleIsValid", false);
	}
	
	public  double calculateDistance(){
		return distance;
	}
	
	public double calculateAngle(){
		double sideTwo  = Math.sqrt((Math.pow(distance, 2) + (Math.pow(1.125, 2)) - (2*distance*1.125) * Math.cos(Math.toRadians(angle))));
		turnAngle = Math.asin((1.125 * Math.sin(RobotMap.gyro.getAngle()))/sideTwo);
		System.out.println("angle" + angle);
		if(angle < 0){
			//turnAngle *= -1;
			System.out.println("neg" + (angle + turnAngle));
			return (angle + turnAngle);
		}
		else {
			System.out.println(angle + turnAngle);
			return (angle + turnAngle);
			
		}
		
	}

	
	public void valueChanged(ITable source, String key, Object value,
			boolean isNew) {
		 isValid = table.getBoolean("angleIsValid", false);
    	 distance = table.getNumber("Distance", empty);
    	 angle = table.getNumber("Angle", empty);
	}
}

