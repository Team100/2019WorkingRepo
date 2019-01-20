/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.*;

/**
 * This is a demo program showing the use of the RobotDrive class, specifically
 * it contains the code necessary to operate a robot with tank drive.
 */
public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;
  private Joystick m_Stick;
  private WPI_TalonSRX m_leftMaster;
  private WPI_TalonSRX m_rightMaster;
  private VictorSPX m_leftFollower;
  private VictorSPX m_rightFollower;
  private Encoder m_leftEncoder;
  private Encoder m_rightEncoder;

  @Override
  public void robotInit() {
    m_leftMaster = new WPI_TalonSRX(0);
    m_leftFollower = new VictorSPX(1);
    m_rightMaster =  new WPI_TalonSRX(15);
    m_rightFollower = new VictorSPX(14);
    m_myRobot = new DifferentialDrive(m_leftMaster,m_rightMaster);
    m_rightFollower.follow(m_rightMaster);
    m_leftFollower.follow(m_leftMaster);
    m_Stick = new Joystick(0);
    m_rightEncoder = new Encoder(8,9);
    m_leftEncoder = new Encoder(0,1);
  }


  @Override
  public void teleopPeriodic() {
    m_myRobot.tankDrive(-m_Stick.getY(), -m_Stick.getThrottle());
    SmartDashboard.putNumber ("Left Encoder", m_leftEncoder.get());
    SmartDashboard.putNumber("Right Encoder", m_rightEncoder.get());
  }


}
