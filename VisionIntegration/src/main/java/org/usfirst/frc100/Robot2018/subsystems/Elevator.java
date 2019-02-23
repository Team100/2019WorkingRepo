// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.Robot2018.subsystems;

import org.usfirst.frc100.Robot2018.OI;
import org.usfirst.frc100.Robot2018.RobotMap;
import org.usfirst.frc100.Robot2018.commands.*;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DigitalInput;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class Elevator extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final WPI_TalonSRX elevatorTalon = RobotMap.elevatorElevatorTalon;
    private final WPI_VictorSPX elevatorVictor = RobotMap.elevatorElevatorVictor;
    private final WPI_VictorSPX elevatorVictor2 = RobotMap.elevatorElevatorVictor2;
    private final DigitalInput elevatorLim1 = RobotMap.elevatorElevatorLim1;
    private final DigitalInput elevatorLim2 = RobotMap.elevatorElevatorLim2;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    @Override
    public void initDefaultCommand() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND


        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DEFAULT_COMMAND

        // Set the default command for a subsystem here.

        setDefaultCommand(new ElevatorAdjust());

    }

    @Override
    public void periodic() {
        // Put code here to be run every loop
    	SmartDashboard.putString("ELEVCommand", this.getCurrentCommandName());
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

