/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants;
import frc.robot.commands.Drive;
import frc.robot.subsystems.swerve.SwerveModule;

/**
 * Drivetrain
 */
public class Drivetrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public SwerveModule fletcher, frederick, blake, brian;

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new Drive());
        fletcher = new SwerveModule(true, Constants.FL_DRIVE_CANID, Constants.FL_TURN_CANID, Constants.FL_TURN_ABS_ZERO);
        frederick = new SwerveModule(false, Constants.FR_DRIVE_CANID, Constants.FR_TURN_CANID, Constants.FR_TURN_ABS_ZERO);
        blake = new SwerveModule(true, Constants.BL_DRIVE_CANID, Constants.BL_TURN_CANID, Constants.BL_TURN_ABS_ZERO);
        brian = new SwerveModule(false, Constants.BR_DRIVE_CANID, Constants.BR_TURN_CANID, Constants.BR_TURN_ABS_ZERO);
    }

    public void rotation(double deg) {
        fletcher.setDegrees(deg);
        frederick.setDegrees(deg);
        blake.setDegrees(deg);
        brian.setDegrees(deg);
    }

    public void updatePeriodic() {
        fletcher.periodic();
        frederick.periodic();
        blake.periodic();
        brian.periodic();
    }

    public void drive() {

    }
}
