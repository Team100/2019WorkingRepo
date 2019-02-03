// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.RobotArmWithEncoder.subsystems;

import org.usfirst.frc100.RobotArmWithEncoder.Constants;
import org.usfirst.frc100.RobotArmWithEncoder.Robot;
import org.usfirst.frc100.RobotArmWithEncoder.commands.*;
import org.usfirst.frc100.RobotArmWithEncoder.commands.Homing.HomingInit;

import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder.IndexingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DigitalInput;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS

/**
 *
 */
public class RobotArm extends PIDSubsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public Encoder robotArmEncoder;
    public VictorSP robotArmMotor;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public Timer t = new Timer();

    public double setpoint = 1000;

    public States state;
    public enum States{
        HOMING,AT_SETPOINT,MOVE_TO_SETPOINT
    }




    public HomeStates homeState;
    public enum HomeStates{
        NOT_STARTED,ELEV_LOWERING_TO_LIMIT_SWITCH,ELEV_AT_LIMIT_SWITCH,ELEV_RAISING_TO_ABOVE_LIMIT_SWITCH,COMPLETE,FATAL
    }


    public DigitalInput lowerLimitSwitch = new DigitalInput(0);
    public DigitalInput upperLimitSwitch = new DigitalInput(1);

    // Initialize your subsystem here
    public RobotArm() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        super("RobotArm", 1.0, 0.0, 0.0);
        setAbsoluteTolerance(0.2);
        getPIDController().setContinuous(false);
        getPIDController().setName("RobotArm", "PIDSubsystem Controller");
        getPIDController().setPID(Constants.ELEVATOR_P, Constants.ELEVATOR_I, Constants.ELEVATOR_D, Constants.ELEVATOR_F);
        getPIDController().setAbsoluteTolerance(500);

        LiveWindow.add(getPIDController());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=PID
        

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        robotArmEncoder = new Encoder(2, 3, false,EncodingType.k4X);
        addChild("RobotArmEncoder",robotArmEncoder);
        robotArmEncoder.setDistancePerPulse(1.0);
        robotArmEncoder.setPIDSourceType(PIDSourceType.kDisplacement);
        
        
        robotArmMotor = new VictorSP(0);
    
        addChild("RobotArmMotor",robotArmMotor);
        robotArmMotor.setInverted(true);
        this.state = States.HOMING;
        this.t.start();
        

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS


        // Use these to get going:
        // setSetpoint() -  Sets where the PID controller should move the system
        //                  to
        // enable() - Enables the PID controller.
    }


    public void armButtonHandler(){
        if(this.state == States.HOMING){
            new HomingInit().start();
          }
          else if(Robot.oi.teleop.get()){
            new MoveToSetpoint(4096).start();
          }
          else if(Robot.oi.home.get()){
            new MoveToSetpoint(32767).start();
          }
          else if(Robot.oi.down.get()){
      
            new MoveToSetpoint(Constants.ARM_ENCODER_LOWER_BUFFER).start();
          }
          else if(Robot.oi.up.get()){
            new MoveToSetpoint(Constants.ARM_ENCODER_MAX_VALUE-Constants.ARM_ENCODER_TOP_BUFFER).start();
          }
          else if (Math.abs(Robot.oi.teleopJoystick.getY())>Constants.ELEVATOR_OI_INTERRUPT_DEADBAND){
            if(Robot.robotArm.lowerLimitSwitch.get() && Robot.oi.teleopJoystick.getY() > 0){
                Robot.robotArm.robotArmMotor.set(0);
              }
              else if(Robot.robotArm.upperLimitSwitch.get() && Robot.oi.teleopJoystick.getY() < 0){
                Robot.robotArm.robotArmMotor.set(0);
              }
              else if(Robot.robotArm.robotArmEncoder.get() < Constants.ARM_ENCODER_LOWER_BUFFER && Robot.oi.teleopJoystick.getY() > 0){
                Robot.robotArm.robotArmMotor.set(0);
              }
              else if(Robot.robotArm.robotArmEncoder.get()>Constants.ARM_ENCODER_MAX_VALUE-Constants.ARM_ENCODER_TOP_BUFFER && Robot.oi.teleopJoystick.getY() < 0){
                Robot.robotArm.robotArmMotor.set(0);
              }
              else{
                Robot.robotArm.robotArmMotor.set(-Robot.oi.teleopJoystick.getY());
                Robot.robotArm.setpoint = Robot.robotArm.robotArmEncoder.get();
                Robot.robotArm.updateSetpoint();
              }
          }
    }
    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new AtSetpoint());
     
    }

    @Override
    protected double returnPIDInput() {
        // Return your input value for the PID loop
        // e.g. a sensor, like a potentiometer:
        // yourPot.getAverageVoltage() / kYourMaxVoltage;

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
        return robotArmEncoder.pidGet();

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=SOURCE
    }

    @Override
    protected void usePIDOutput(double output) {
        // Use output to drive your system, like a motor
        // e.g. yourMotor.set(output);

        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
        
        robotArmMotor.pidWrite(output);

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=OUTPUT
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CMDPIDGETTERS
    public void updateSetpoint(){
        this.getPIDController().setSetpoint(this.setpoint);
    }
    public void lower(){
        robotArmMotor.set(-1);
    }
    public void raise(){
        robotArmMotor.set(1);
    }
    public void stop(){
        robotArmMotor.set(0);
    }

}
