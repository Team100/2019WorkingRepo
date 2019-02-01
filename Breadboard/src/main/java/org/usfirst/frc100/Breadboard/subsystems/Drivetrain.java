// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.Breadboard.subsystems;


import org.usfirst.frc100.Breadboard.Constants;
import org.usfirst.frc100.Breadboard.utils.FalconPathPlanner;
import org.usfirst.frc100.Breadboard.Robot;
import org.usfirst.frc100.Breadboard.commands.*;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;


import com.ctre.phoenix.motorcontrol.ControlMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Drivetrain extends Subsystem implements PIDOutput{

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public WPI_TalonSRX leftMaster;
    public WPI_TalonSRX rightMaster;
    private DifferentialDrive differentialDrive1;
    private WPI_VictorSPX leftFollower;
    private WPI_VictorSPX rightFollower;
    public PIDController turnPID;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private double distance;
    private double extrusion;
    private double angle;
    private double endLocationX;
    private double endLocationY;
    private double[][] waypoints;

    public Drivetrain() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        leftMaster = new WPI_TalonSRX(Constants.DRIVE_TRAIN_LEFT_MASTER_CANID);
        rightMaster = new WPI_TalonSRX(Constants.DRIVE_TRAIN_RIGHT_MASTER_CANID);
        differentialDrive1 = new DifferentialDrive(leftMaster, rightMaster);
        addChild("Differential Drive 1", differentialDrive1);
        differentialDrive1.setSafetyEnabled(false);
        differentialDrive1.setExpiration(0.7);
        differentialDrive1.setMaxOutput(Constants.DRIVE_TRAIN_MAX_MOTOR_OUTPUT);
        
        leftFollower = new WPI_VictorSPX(Constants.DRIVE_TRAIN_LEFT_FOLLOWER_CANID);
        rightFollower = new WPI_VictorSPX(Constants.DRIVE_TRAIN_RIGHT_FOLLOWER_CANID);

        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);

        leftMaster.setInverted(Constants.DRIVE_TRAIN_LEFT_MASTER_INVERT);
        leftFollower.setInverted(Constants.DRIVE_TRAIN_LEFT_FOLLOWER_INVERT);
        rightMaster.setInverted(Constants.DRIVE_TRAIN_RIGHT_MASTER_INVERT);
        rightFollower.setInverted(Constants.DRIVE_TRAIN_RIGHT_FOLLOWER_INVERT);

        turnPID = new PIDController(Constants.DT_MASTER_P, Constants.DT_MASTER_I, Constants.DT_MASTER_D, Robot.ahrs, this);
        turnPID.setInputRange(Constants.DT_TURN_MIN_ROTATION_ANGLE, Constants.DT_TURN_MAX_ROTATION_ANGLE);
        turnPID.setContinuous(true);
        turnPID.setOutputRange(Constants.DT_TURN_MIN_OUTPUT, Constants.DT_TURN_MAX_OUTPUT);
        turnPID.setAbsoluteTolerance(Constants.DT_TURN_ABSOLUTE_TOLERANCE);
    }
    
    public void turn(double leftPower, double rightPower){
        leftMaster.set(ControlMode.PercentOutput, leftPower);
        rightMaster.set(ControlMode.PercentOutput, rightPower);
    }

    public void pidTurn(){
        turn(turnPID.get(), turnPID.get());
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
    }

    public void drive(){
        /*
         * Use single stick of [LEFT] joystick for driving (X and Y axis)
         */
        // differentialDrive1.arcadeDrive(-Robot.oi.getDual().getY(), Robot.oi.getDual().getX());
        
        /*
         * Use both left and right sticks for driving (L) Up+Down, (R) Left+Right
         */
        if(Robot.getArcade()) differentialDrive1.arcadeDrive(-Robot.oi.getLeftStick().getY(), Robot.oi.getRightStick().getX());
        else differentialDrive1.tankDrive(-Robot.oi.getLeftStick().getY(), -Robot.oi.getRightStick().getY());
    }

    public void stop(){
        leftMaster.set(ControlMode.PercentOutput, 0);
        rightMaster.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
        SmartDashboard.putNumber("ENC LEFT",leftMaster.getSelectedSensorPosition());
        SmartDashboard.putNumber("ENC RIGHT", rightMaster.getSelectedSensorPosition());
    }

    @Override
    public void pidWrite(double output) {
        leftMaster.set(ControlMode.PercentOutput, output*Constants.DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
        rightMaster.set(ControlMode.PercentOutput, output*Constants.DRIVE_TRAIN_PIVOT_MOTOR_OUTPUT);
    }

    public double[][] generatePath(){
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
        angle = 40;
        distance = 120;
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
        waypoints = new double[4][2];
        waypoints[1][0] = 0.0;
        waypoints[1][1] = 0.0;
        waypoints[3][0] = endLocationX;
        waypoints[3][1] = endLocationY;
        waypoints[2][0] = endLocationX - (extrusion/3);
        waypoints[2][1] = endLocationY - (extrusion*3);
        long start = System.currentTimeMillis();

        //create waypoint path
        /*double[][] path = {}*/
            double totalTime = (((distance/12)/7)+3); //seconds
            double timeStep = 0.02; //period of control loop on Rio, seconds
            double robotTrackWidth = 2; //distance between left and right wheels, feet
            final FalconPathPlanner path = new FalconPathPlanner(waypoints);
            path.calculate(totalTime, timeStep, robotTrackWidth);

        System.out.println("Time in ms: " + (System.currentTimeMillis()-start));
        
        double[][] pathfinderPath = new double[path.smoothRightVelocity.length][3];
        for(int i = 0; i < path.smoothLeftVelocity.length; i++){
                System.out.print(path.smoothLeftVelocity[i][1] + ", ");
                System.out.print(path.smoothRightVelocity[i][1] + ", ");
                System.out.print(path.leftPath[i][1] + ", ");
                System.out.print(path.rightPath[i][1] + ", ");
        System.out.print(path.smoothLeftVelocity[i][0] + ", ");
                System.out.println(path.heading[i][1] + ", ");
        pathfinderPath[i][0] = path.smoothLeftVelocity[i][1];
        pathfinderPath[i][1] = path.smoothRightVelocity[i][1];
        pathfinderPath[i][2] = path.heading[i][1];
        }
        System.out.println("X end pos =" + (endLocationX) + ". Y end pos" + endLocationY + ". Angle: " + angle + ". Global angle" + globalAngle);
        
        // calculations for curvature https://www.desmos.com/calculator/vhrlmfzv7p
        //new PathFinder(pathfinderPath);
        //System.out.println(System.currentTimeMillis() - start);
        return pathfinderPath;
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

