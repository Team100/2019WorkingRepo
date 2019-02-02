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
  public enum LED_Mode {
    OFF,
    SOLID,
    BLINKING,
    CHASING,
    ALTERNATING
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
    port.writeString(":C1000000\n");

  }

  public void setMode(LED_Mode desiredMode) {
    String s = ":M";
    switch (desiredMode) {
      case OFF:
        s = s.concat("O\n");
        break;
      case SOLID:
        s= s.concat("S\n");
        break;
      case BLINKING:
        s= s.concat("B\n");
        break;
      case CHASING:
        s= s.concat("C\n");
        break;
      case ALTERNATING:
        s= s.concat("A\n");
        break;
      default:
        s= s.concat("\n");
        break;
    }
    port.writeString(s);
  }

  public void setBlinkTime(LED_BlinkTimerID timerID, int msTime) {
    port.writeString(":T0100\n");
    port.writeString(":T1100\n");

  }
}
