/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008-2017. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//package edu.wpi.first.wpilibj;
package org.usfirst.frc100.Robot2017.commands;

import java.util.ArrayDeque;
import java.util.Queue;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.Timer;

/**
 * Class implements a PID Control Loop.
 *
 * <p>Creates a separate thread which reads the given PIDSource and takes care of the integral
 * calculations, as well as writing the given PIDOutput.
 *
 * <p>This feedback controller runs in discrete time, so time deltas are not used in the integral
 * and derivative calculations. Therefore, the sample rate affects the controller's behavior for a
 * given set of PID constants.
 */
public class PIDControllerHenry extends PIDController {

  public static final double kDefaultPeriod = .02; //.02

 
  private int m_bufLength = 1;
  private Queue<Double> m_buf;
  private double m_bufTotal = 0.0;
  Timer m_setpointTimer;
  

  /**
   * Allocate a PID object with the given constants for P, I, D, and F
   *
   * @param Kp     the proportional coefficient
   * @param Ki     the integral coefficient
   * @param Kd     the derivative coefficient
   * @param Kf     the feed forward term
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output percentage
   * @param period the loop time for doing calculations. This particularly effects calculations of
   *               the integral and differential terms. The default is 50ms.
   */
  @SuppressWarnings("ParameterName")
  public PIDControllerHenry(double Kp, double Ki, double Kd, double Kf, PIDSource source,
                       PIDOutput output, double period) {

    super(Kp, Ki, Kd, Kf, source, output, kDefaultPeriod);
  
    
    m_buf = new ArrayDeque<Double>(m_bufLength + 1);
    m_setpointTimer = new Timer();
    m_setpointTimer.start();
  }

  /**
   * Allocate a PID object with the given constants for P, I, D and period
   *
   * @param Kp     the proportional coefficient
   * @param Ki     the integral coefficient
   * @param Kd     the derivative coefficient
   * @param source the PIDSource object that is used to get values
   * @param output the PIDOutput object that is set to the output percentage
   * @param period the loop time for doing calculations. This particularly effects calculations of
   *               the integral and differential terms. The default is 50ms.
   */
  @SuppressWarnings("ParameterName")
  public PIDControllerHenry(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output,
                       double period) {
    this(Kp, Ki, Kd, 0.0, source, output, period);
  }

  /**
   * Allocate a PID object with the given constants for P, I, D, using a 50ms period.
   *
   * @param Kp     the proportional coefficient
   * @param Ki     the integral coefficient
   * @param Kd     the derivative coefficient
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output percentage
   */
  @SuppressWarnings("ParameterName")
  public PIDControllerHenry(double Kp, double Ki, double Kd, PIDSource source, PIDOutput output) {
    this(Kp, Ki, Kd, source, output, kDefaultPeriod);
  }

  /**
   * Allocate a PID object with the given constants for P, I, D, using a 50ms period.
   *
   * @param Kp     the proportional coefficient
   * @param Ki     the integral coefficient
   * @param Kd     the derivative coefficient
   * @param Kf     the feed forward term
   * @param source The PIDSource object that is used to get values
   * @param output The PIDOutput object that is set to the output percentage
   */
  @SuppressWarnings("ParameterName")
  public PIDControllerHenry(double Kp, double Ki, double Kd, double Kf, PIDSource source,
                       PIDOutput output) {
    this(Kp, Ki, Kd, Kf, source, output, kDefaultPeriod);
  }

 

 }
