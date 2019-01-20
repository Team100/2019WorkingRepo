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
import org.usfirst.frc100.Breadboard.Robot;
import org.usfirst.frc100.Breadboard.commands.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

import com.ctre.phoenix.motorcontrol.ControlMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Drivetrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private WPI_TalonSRX leftMaster;
    private WPI_TalonSRX rightMaster;
    private DifferentialDrive differentialDrive1;
    private WPI_VictorSPX leftFollower;
    private WPI_VictorSPX rightFollower;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Drivetrain() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        leftMaster = new WPI_TalonSRX(Constants.DRIVE_TRAIN_LEFT_MASTER_CANID);
        
        
        
        rightMaster = new WPI_TalonSRX(Constants.DRIVE_TRAIN_RIGHT_MASTER_CANID);
        
        
        
        differentialDrive1 = new DifferentialDrive(leftMaster, rightMaster);
        addChild("Differential Drive 1",differentialDrive1);
        differentialDrive1.setSafetyEnabled(true);
        differentialDrive1.setExpiration(0.1);
        differentialDrive1.setMaxOutput(Constants.DRIVE_TRAIN_MAX_MOTOR_OUTPUT);

        
        leftFollower = new WPI_VictorSPX(Constants.DRIVE_TRAIN_LEFT_FOLLOWER_CANID);
        
        
        
        rightFollower = new WPI_VictorSPX(Constants.DRIVE_TRAIN_RIGHT_FOLLOWER_CANID);
        
        
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        leftFollower.follow(leftMaster);
        rightFollower.follow(rightMaster);
    }

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        setDefaultCommand(new Drive());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
    }

    public void drive(){
        differentialDrive1.arcadeDrive(Robot.oi.getLeftStick().getY(), Robot.oi.getLeftStick().getX());
    }

    public void pivotLeft(){
        leftMaster.set(ControlMode.PercentOutput, -0.5);
        rightMaster.set(ControlMode.PercentOutput, 0.5);
    }
    public void pivotRight(){
        leftMaster.set(ControlMode.PercentOutput, 0.5);
        rightMaster.set(ControlMode.PercentOutput, -0.5);
    }
    public void stop(){
        leftMaster.set(ControlMode.PercentOutput, 0);
        rightMaster.set(ControlMode.PercentOutput, 0);
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

