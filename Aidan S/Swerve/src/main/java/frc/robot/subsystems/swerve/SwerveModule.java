package frc.robot.subsystems.swerve;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class SwerveModule {

    public double turnSetpoint;
    public double currentPos;
    public double offset;
    public BaseMotorController drive;
    public TalonSRX turn;

    public SwerveModule(boolean vic, int driveID, int turnID, double zero) {
        if(vic) drive = new VictorSPX(driveID);
        else drive = new TalonSRX(driveID);
        turn = new TalonSRX(turnID);
        offset = zero;
        turnSetpoint = 0;
    }

    public void periodic() {
        retrieveCurrentPos();
    }


    //__________TURN____________

    /**
     * Set turn motor to absolute position in degrees
     * @param degrees
     */
    public void setDegrees(double degrees) {
        pivotAbs(degrees*45.0/128.0);
    }

    /**
     * Pivot to absolute position (0 to 1024)
     * @param position
     */
    public void pivotAbs(double position) {
        turnToSetpoint(position);
    }

    /**
     * Pivot left or right a specified amount
     * @param amount
     */
    public void pivotRel(double amount) {
        turnToSetpoint((turnSetpoint += amount) % 1024);
    }

    private void turnToSetpoint(double in) {
        turnSetpoint = in;
        turn.set(ControlMode.Position, turnSetpoint);
    }

    public void retrieveCurrentPos() {
        currentPos = turn.getSelectedSensorPosition() % 1024;
    }

    public void zero() {
        turn.set(ControlMode.Position, offset);
    }


    //___________DRIVE____________

    public void setPower(double pow) {
        drive.set(ControlMode.PercentOutput, pow);
    }

}