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
    try {
      f = new File("C:/Users/Team100/Documents/GitHub/2019WorkingRepo/Noel S/LoadingFilesRoboRIO/src/main/deploy/Output.txt");
      if(!f.exists()){
        f.createNewFile();
      }
    fw = new FileWriter(f);
  } catch (IOException e) {
    e.printStackTrace();
  }
    bw = new BufferedWriter(fw);
  }

  public void teleopInit(){
    	
    try {
    bw.write("Hellow, I'm a text file");
    bw.close();
    fw.close();
  } catch (IOException e) {
    e.printStackTrace();
  }
    
  }
}


