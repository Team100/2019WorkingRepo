package frc.robot.utility;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

public class T100VictorSPX extends WPI_VictorSPX implements T100SRXSPXInterface{

	public T100VictorSPX(int kmotorport2) {
        super (kmotorport2);
	}
    
}