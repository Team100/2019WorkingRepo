/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */

  File f;
	BufferedWriter bw;
  FileWriter fw;
  
  @Override
  public void robotInit() {
    /* try { 
      File f = new File("/home/lvuser/Output.txt"); 
      f.createNewFile(); 
      FileOutputStream oFile = new FileOutputStream(f, false); 
  
      String content = "content"; 
  
      oFile.write(content.getBytes()); 
      oFile.flush(); 
      oFile.close(); 
      System.out.println("success"); 
  } catch (IOException e) { 
      System.out.println("error: " + e.getMessage()); 
  }   */
    try {
      File f = new File("/home/lvuser/Output.txt");
      if(!f.exists()){
        f.createNewFile();
      }
    fw = new FileWriter(f);
  } catch (IOException e) {
    DriverStation.reportError(e.toString(), e.getStackTrace());
    e.printStackTrace();
  }
    bw = new BufferedWriter(fw);
    //System.out.println("I got to line 47");

    try {
      bw.write("Hello, I'm a text file");
      bw.close();
      fw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void teleopInit(){
    	
    try {
    bw.write("Hello, I'm a text file");
    bw.close();
    fw.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
    
  }
}


