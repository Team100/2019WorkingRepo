/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc100.WRRF_Demo_Aug_2015.commands;
import org.usfirst.frc100.WRRF_Demo_Aug_2015.Robot;

import org.usfirst.frc100.WRRF_Demo_Aug_2015.RobotMap;
import org.usfirst.frc100.WRRF_Demo_Aug_2015.subsystems.RobotArm;


import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class SelfTest extends Command {
 
  int itars = 0;
  public SelfTest() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.robotArm);

  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Threader t = new Threader();
    t.tester();
  
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    
  }
  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
class Threader extends Thread{
  NetworkTableInstance netTable = NetworkTableInstance.getDefault();
    NetworkTable nt = netTable.getTable("selftest");
  public void tester(){
    nt.getEntry("running").setBoolean(true);

    nt.getEntry("test1").setString("");
    nt.getEntry("test2").setString("");

    nt.getEntry("stage").setNumber(0);
    transitionTo1();
    test1();
    transitionTo2();
    test2();
    transitionTo1();
    test3();
    
  }
  private void test1(){
    nt.getEntry("stage").setNumber(0);

    double start = Robot.robotArm.getPotValue();
    Robot.robotArm.raise();
    boolean run = true;
    while(run){
      if(Robot.robotArm.isAtHighLimit()==true && Robot.robotArm.getPotValue() < start){

        nt.getEntry("test1").setString("PASS");
        run = false;
  
      }
      else if(Robot.robotArm.isAtHighLimit()){
        nt.getEntry("test1").setString("FAIL: Bad Encoder");
      }
      else if(Robot.robotArm.isAtLowLimit() == true){
        nt.getEntry("test1").setString("FAIL: Reached Low");
        System.out.println("FAILED");
        run = false;
      } 
    }
    
    Robot.robotArm.stop();
    SmartDashboard.putBoolean("DONE", true);
    
    
    
    
  }
  private void transitionTo1(){
    Robot.robotArm.raise();
    
    while(Robot.robotArm.isAtLowLimit() == true){
      
    }
    Robot.robotArm.stop();
  }
  private void transitionTo2(){
    Robot.robotArm.lower();
    
    while(Robot.robotArm.isAtHighLimit() == true){
      
    }
    Robot.robotArm.stop();
  }
  private void test2(){
    nt.getEntry("stage").setNumber(1);

    double start = Robot.robotArm.getPotValue();
    Robot.robotArm.lower();
    boolean run = true;
    while(run){
      
      if(Robot.robotArm.isAtLowLimit()==true && Robot.robotArm.getPotValue() > start){

        nt.getEntry("test2").setString("PASS");
        nt.getEntry("running").setBoolean(false);
        run = false;
  
      }
      else if(Robot.robotArm.isAtLowLimit()){
        nt.getEntry("test2").setString("FAIL: Bad Encoder");
      }
      else if(Robot.robotArm.isAtHighLimit() == true){
        nt.getEntry("test2").setString("FAIL: Reached High");
        System.out.println("FAILED");
        run = false;
      } 
    }
    
    Robot.robotArm.stop();
    SmartDashboard.putBoolean("DONE", true);
    
    
    
    
  }
  private void test3(){

    double start = Robot.robotArm.getPotValue();
    Robot.robotArm.raise();
    boolean run = true;
    while(run){
      if(Robot.robotArm.isAtHighLimit()==true && Robot.robotArm.getPotValue() < start){

        nt.getEntry("test3").setString("PASS");
        run = false;
  
      }
      else if(Robot.robotArm.isAtHighLimit()){
        nt.getEntry("test3").setString("FAIL: Bad Encoder");
      }
      else if(Robot.robotArm.isAtLowLimit() == true){
        nt.getEntry("test3").setString("FAIL: Reached Low");
        System.out.println("FAILED");
        run = false;
      } 
    }
    
    Robot.robotArm.stop();
    SmartDashboard.putBoolean("DONE", true);
    
    
    
    
  }
}

  
