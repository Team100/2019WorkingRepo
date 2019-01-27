package frc.robot.utility;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class T100TalonSRX extends WPI_TalonSRX implements T100SRXSPXInterface{

	public T100TalonSRX(int kmotorport) {
        super (kmotorport);
	}
    
}