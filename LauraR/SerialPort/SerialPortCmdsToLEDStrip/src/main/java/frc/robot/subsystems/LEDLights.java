/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * Add your docs here.
 */
public class LEDLights extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  public final static char BEGINNING_OF_MESSAGE = ':';
  public final static char END_OF_MESSAGE = '\n';
  
  public enum LED_Mode {
    OFF ('O'),
    SOLID ('S'),
    BLINKING ('B'),
    CHASING ('C'),
    ALTERNATING ('A');

    private char mModeCode;
    LED_Mode(char kModeCode){
      mModeCode = kModeCode;
    }

    public char getModeCode () {
      return mModeCode;
    }
  } 

  public enum LED_ColorID {
    COLOR1, 
    COLOR2
  }

  public enum LED_BlinkTimerID {
    BLINK_ON_TIME,
    BLINK_OFF_TIME
  }

  SerialPort port = new SerialPort(115200, SerialPort.Port.kOnboard);
  

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  public void setColor(LED_ColorID slot, int red, int blue, int green) {
    writeStringToArduino(":C1000000\n");

  }

  public void callWriteStringToArduino(String s) {
    writeStringToArduino(s);

  }

  public void setMode(LED_Mode desiredMode) {
    
    switch (desiredMode) {
      case OFF:
      case SOLID:  
      case BLINKING:
      case CHASING:
      case ALTERNATING:
        String s = ":M";
        //s.concat(Character.toString(desiredMode.getModeCode());
        s.concat(Character.toString(END_OF_MESSAGE));
        writeStringToArduino(s);
        break;
      default:
        
        break;
    }

  }

  public void setBlinkTime(LED_BlinkTimerID timerID, int msTime) {
    String s = "";
    switch (timerID) {
      case BLINK_ON_TIME:
        break;
      case BLINK_OFF_TIME:
        break;
      default:
        break;
    }
    writeStringToArduino(":T0100\n");
    writeStringToArduino(":T1100\n");

  }

  private void writeStringToArduino(String s) {
    port.writeString(s);
  }
}
