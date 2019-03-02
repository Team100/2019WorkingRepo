/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.Robot2018.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.google.gson.Gson;

import org.usfirst.frc100.Robot2018.Robot;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.command.Command;

public class VisionIntegration extends Command {
  private Gson gson = new Gson();

  public VisionIntegration() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.driveTrain);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    System.out.println("Initialized");
    //Robot.driveTrain.leftMaster.configSelectedFeedbackSensor();
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    NetworkTableEntry data = Robot.cameraData.getEntry("data");
    System.out.println(data.exists());
    System.out.println(data.getString("[]"));
    VisionTarget[] targets = gson.fromJson(data.getString("[]"), VisionTarget[].class);

    //System.out.println(targets.length);

    if (targets.length == 0) {
      return;
    }

    /*
    Double[] previous = Robot.previousEncoderGyro.get(targets[0].getTimestamp());
    if (previous == null) {
      return;
    }
    */

    double enc = (Robot.driveTrain.rightMaster.getSelectedSensorPosition() + Robot.driveTrain.leftMaster.getSelectedSensorPosition())/2.0;

    double[] petersArray = getRelativeWheelSpeed(targets[0].getPlane(), targets[0].getAngle(), targets[0].getDistance(), Robot.ahrs.getAngle(), enc);

    System.out.println(petersArray[0] + "," + petersArray[1]);

    Robot.driveTrain.leftMaster.set(ControlMode.Velocity, petersArray[0]);
    Robot.driveTrain.rightMaster.set(ControlMode.Velocity, petersArray[1]);
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    System.out.println("done");
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }

  /**
   * Get the relative wheel speeds for the robot based on orientation, angle, and distance
   * 
   * @param a1 orientation
   * @param o1 angle
   * @param d distance
   * @param g gyro
   * @param e encoder
   * @return
   */
  private double[] getRelativeWheelSpeed(double a1, double o1, double d, double g, double e) {
    a1 *= Math.PI/180;
    o1 *= Math.PI/180;
    double s = 3;

    double x = d * Math.cos(o1) - s * Math.cos(a1);
    double y = d * Math.sin(o1) - s * Math.sin(a1);

    a1 += g;
    
    x = d * Math.cos(o1);
    y = d * Math.sin(o1);

    x -= e * Math.cos(g);
    y -= e * Math.sin(g);

    d = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    o1 = Math.atan(y/x);

//peter wuz here
    // Coefficients for polynomial
    double a = Math.pow(1/Math.cos(o1), 2) * ( Math.tan(a1) - 2 * Math.tan(o1) ) / Math.pow(d, 2);
    double b = 1/Math.cos(o1) * ( 3 * Math.tan(o1) - Math.tan(a1) ) / d;

    double l = 10;

    double x1 = 0;
    double dx = 0.0001;
    double x2 = x1 + dx;

    double t1 = lx(x1, l, a, b) - lx(x2, l, a, b);
    double t2 = rx(x1, l, a, b) - rx(x2, l, a, b);

    double s1 = -Math.sqrt(Math.pow((t1 / dx), 2) + Math.pow( ( ly(x1, l, a, b) - ly(x2, l, a, b)) / dx, 2)) * Math.signum(t1);
    double s2 = -Math.sqrt(Math.pow((t2 / dx), 2) + Math.pow( ( ry(x1, l, a, b) - ry(x2, l, a, b)) / dx, 2)) * Math.signum(t2);

    double na = Math.max(Math.abs(s1), Math.abs(s2));

    double n1 = s1 / na;
    double n2 = s2 / na;

    return new double[]{n1, n2};
  }

  private double f(double x, double a, double b) {
    return a * Math.pow(x, 3) + b * Math.pow(x, 2);
  }

  private double m(double x, double a, double b) {
    double f1 = Math.atan(-1/( 3*a*Math.pow(x, 2) + 2*b*x));
    return -(Math.signum(f1) * Math.PI/2 + Math.PI/2 - f1);
  }

  private double lx(double t, double l, double a, double b) {
    return t - l * Math.cos(m(t, a, b));
  }

  private double ly(double t, double l, double a, double b) {
    return f(t, a, b) - l * Math.sin(m(t, a, b));
  }

  private double rx(double t, double l, double a, double b) {
    return t + l * Math.cos(m(t, a, b));
  }

  private double ry(double t, double l, double a, double b) {
    return f(t, a, b) + l * Math.sin(m(t, a, b));
  }
}
