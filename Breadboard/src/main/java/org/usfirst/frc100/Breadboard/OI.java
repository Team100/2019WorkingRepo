// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc100.Breadboard;

import org.usfirst.frc100.Breadboard.commands.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;



/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);

    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.

    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:

    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());

    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());

    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());


    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    public Joystick leftStick;
    public Joystick rightStick;
    public Joystick dual;

    public JoystickButton turnButton;
    public JoystickButton pickupButton;
    public JoystickButton pivotButton;
    public JoystickButton pivotHalfButton;
    public JoystickButton driveButton;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    public OI() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

        leftStick = new Joystick(0);
        rightStick = new Joystick(1);
        dual = new Joystick(2);
        
        pickupButton = new JoystickButton(dual, 1);
        pickupButton.whileHeld(new Pickup());

        turnButton = new JoystickButton(dual, 2);
        turnButton.whenPressed(new TurnAbsolute(90));

        pivotButton = new JoystickButton(dual, 3);
        pivotButton.whenPressed(new TurnRelative(90));

        pivotHalfButton = new JoystickButton(dual, 4);
        pivotHalfButton.whenPressed(new Pathfinding());

        driveButton = new JoystickButton(leftStick, 1);


        // SmartDashboard Buttons
        SmartDashboard.putData("Autonomous Command", new AutonomousCommand());
        SmartDashboard.putData("Drive", new Drive());
        SmartDashboard.putData("Pickup", new Pickup());
        SmartDashboard.putData("Turn to 90", new TurnAbsolute(90));
        SmartDashboard.putData("Pivot 90 ->", new TurnRelative(90));
        SmartDashboard.putData("Pivot 90 <-", new TurnRelative(-90));
        SmartDashboard.putData("Run Path", new Pathfinding());

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
    }

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS
    public Joystick getLeftStick() {
        return leftStick;
    }

    public Joystick getRightStick() {
        return rightStick;
    }

    public Joystick getDual() {
        return dual;
    }


    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=FUNCTIONS

}

