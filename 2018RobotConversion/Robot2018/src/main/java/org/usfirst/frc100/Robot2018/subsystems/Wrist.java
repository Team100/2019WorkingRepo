package org.usfirst.frc100.Robot2018.subsystems;


import org.usfirst.frc100.Robot2018.Robot;
import org.usfirst.frc100.Robot2018.commands.DoubleSolenoidControl;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Wrist extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DoubleSolenoidControl());
    }

}

