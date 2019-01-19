// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.Robot2017;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.DifferentialDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	
	/*
    public static SpeedController driveTrainleftMotor;
    public static SpeedController driveTrainrightMotor;
    public static DifferentialDrive driveTraindrive;
    public static Encoder driveTrainleftEncoder;
    public static Encoder driveTrainrightEncoder;
    public static Solenoid driveTrainleftShifter;
    public static Solenoid driveTrainrightShifter;
    public static AnalogGyro driveTraindigialGyroUno;
    public static Ultrasonic driveTrainultraSanic;
    public static SpeedController intakeoutsideRoller;
    public static SpeedController intakehopperRoller;
    public static SpeedController intakebeltRoller;
    public static DigitalInput intakeballSensor;
    public static SpeedController peterssUnbeatableScalingMechanismWithoutpNeumaticswinchMotor;
    public static Encoder peterssUnbeatableScalingMechanismWithoutpNeumaticswinchEncoder;
    public static SpeedController shootermoter;
    public static Encoder shooterincoder;
    public static Compressor knewmaticscompresser;
    public static Solenoid gearMechrelease;
	*/
	
	//DriveTrain Things
	public static Encoder driveTrainLeftEncoder;
	public static Encoder driveTrainRightEncoder;
	
	public static WPI_TalonSRX rightMaster;
    public static WPI_TalonSRX rightFollwer;
    public static WPI_TalonSRX leftMaster;
    public static WPI_TalonSRX leftFollower;
    
    public static DifferentialDrive driveTrainDifferentialDrive;
    
    public static ADXRS450_Gyro gyro;
    
    public static Solenoid driveTrainShifter;
	
	//GearMech Things
    public static Solenoid gearMechDrop;
    public static Solenoid gearMechFlap;
	
    public static Solenoid highGoalGate; //potental
    
    //PeterssUnbeatableScalingMechanismWithoutpNeumatics Things
	public static Encoder climberEncoder;
	
	public static VictorSP climberWinch;
	
	public static Solenoid climberDeployment; //potental
	
	//Ballhandling Things
	public static Encoder elevatorEncoder;
    
    public static VictorSP outsideRoller;
	public static VictorSP elevator;
	
    public static Solenoid dumperLift;
    public static Solenoid pickUpFlap;
    
    //Other Things
    public static Compressor compressor;
    public static Compressor pressureSwitch;
    public static DigitalInput leftA;
    public static DigitalInput leftB;
    public static DigitalInput rightA;
    public static DigitalInput rightB;
    public static PowerDistributionPanel pdp;
	
    public static void init() {
    	//DriveTrain Things 
    	leftA = new DigitalInput(0);
    	leftB = new DigitalInput(1);
    	rightA = new DigitalInput(2);
    	rightB  = new DigitalInput(3);
    	driveTrainLeftEncoder = new Encoder(leftA,leftB);
    	//LiveWindow.addSensor("driveTrain", "leftEncoder", driveTrainLeftEncoder);
     	driveTrainLeftEncoder.setDistancePerPulse(1.0/45.4);
    	
    	driveTrainRightEncoder = new Encoder(rightA, rightB);
    	//LiveWindow.addSensor("driveTrain", "rightEncoder", driveTrainRightEncoder);
    	driveTrainRightEncoder.setDistancePerPulse(1.0/484.26);//45.4);
    	
    	rightMaster	= new WPI_TalonSRX(5);
    	//LiveWindow.addActuator("driveTrain", "rightMaster", rightMaster);
    	//rightMaster.changeControlMode(TalonControlMode.PercentVbus);
    	
    	rightFollwer = new WPI_TalonSRX(4);
        //LiveWindow.addActuator("driveTrain", "rightFollwer", rightFollwer);
        rightFollwer.follow(rightMaster);
    	rightFollwer.setSafetyEnabled(false);
    	
    	leftMaster = new WPI_TalonSRX(3);
    	//LiveWindow.addActuator("driveTrain", "leftMaster", leftMaster);
    	//leftMaster.changeControlMode(TalonControlMode.PercentVbus);
    	
    	leftFollower = new WPI_TalonSRX(2);
    	//LiveWindow.addActuator("driveTrain", "leftFollower", leftFollower);
        leftFollower.follow(leftMaster);
    	leftFollower.setSafetyEnabled(false);
        
    	driveTrainDifferentialDrive = new DifferentialDrive(rightMaster, leftMaster);
        driveTrainDifferentialDrive.setSafetyEnabled(false);
        driveTrainDifferentialDrive.setExpiration(0.1);
        //driveTrainDifferentialDrive.setSensitivity(0.5);
        driveTrainDifferentialDrive.setMaxOutput(1.0);
        //driveTrainDifferentialDrive.setInvertedMotor(DifferentialDrive.MotorType.kRearRight, true);
        driveTrainDifferentialDrive.setRightSideInverted(true);
        
        gyro = new ADXRS450_Gyro();
        //LiveWindow.addActuator("driveTrain", "gyro", gyro);
        
        driveTrainShifter = new Solenoid(2);
        //LiveWindow.addActuator("driveTrain", "driveTrainShifter", driveTrainShifter);
    	
    	//GearMech Things
        gearMechDrop= new Solenoid(0);
        //LiveWindow.addActuator("gearMech", "gearMechDrop", gearMechDrop);
        
        gearMechFlap= new Solenoid(1);
        //LiveWindow.addActuator("gearMech", "gearMechFlap", gearMechFlap);
    	
    	//BallHandling Things
        elevatorEncoder = new Encoder(6,7);
        //LiveWindow.addActuator("pickUp", "elevatorEncoder", elevatorEncoder);
        
        outsideRoller = new VictorSP(0);
        //LiveWindow.addActuator("pickUp", "outsideRoller", outsideRoller);
    	
    	pickUpFlap= new Solenoid(7);
    	//LiveWindow.addActuator("pickUp", "pickUpFlap", pickUpFlap);
    	
    	elevator = new VictorSP(1);
        //LiveWindow.addActuator("pickUp", "elevator", elevator);
        
        dumperLift= new Solenoid(3);
        //LiveWindow.addActuator("gearMech", "dumperLift", dumperLift);
        
        //PeterssUnbeatableScalingMechanismWithoutpNeumatics Things
    	climberEncoder = new Encoder(4,5);
    	//LiveWindow.addActuator("peterssUnbeatableScalingMechanismWithoutpNeumatics", "climberEncoder", climberEncoder);
    	
    	climberWinch = new VictorSP(2);
    	//LiveWindow.addActuator("peterssUnbeatableScalingMechanismWithoutpNeumatics", "climberWinch", climberWinch);
    	
        //Other Things
    	climberDeployment= new Solenoid(5); //potental
    	//LiveWindow.addActuator("peterssUnbeatableScalingMechanismWithoutpNeumatics", "climberDeployment", climberDeployment);
    	
    	highGoalGate= new Solenoid(6); //potental
    	//LiveWindow.addActuator("pickUp", "highGoalGate", highGoalGate);
    	
        compressor = new Compressor();
        //LiveWindow.addActuator("Other", "compressor", compressor);
        
        pressureSwitch = new Compressor();
        //LiveWindow.addActuator("Other", "pressureSwitch", pressureSwitch);

        pdp = new PowerDistributionPanel();
        //LiveWindow.addActuator("Other", "pdp", pdp);
    }
}
