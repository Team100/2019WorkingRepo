package org.usfirst.frc100.Robot2018.commands;
/**
 * All constants made after and including the 3 of November
 * @author Team100
 *
 */
public class Constants {

	// For Pathfinding
	
	
	/**
	 * Refresh rate in milliseconds
	 */
	public static final int REFRESH_RATE = 100;
	
	
	/**
	 * This is the ratio of the encoder revolution (in ticks per revolution) to the the wheel revolution ratio (in meters)
	 */
	public static final double DRIVETRAIN_ENCODER_TO_WHEEL_RATIO = 7.5;
	
	
	/**
	 * The number of ticks per one encoder revolution
	 */
	public static final int ENCODER_PULSES_PER_REVOLUTION = 256;
	
	
	/**
	 * The ratio of encoder ticks per one pulse of the encoder
	 */
	public static final int ENCODER_TICKS_PER_PULSE = 4;
	
	
	/**
	 * This is the diameter in meters
	 */
	public static final double WHEEL_DIAMETER_IN_METERS = 0.15494;
	
	
	/**
	 * pi constant
	 */
	public static final double PI = Math.PI;
	
	
	/**
	 * P value for the drivetrain
	 */
	public static final double DRIVETRAIN_P = 0.6;
	
	
	/**
	 * I value for the drivetrain
	 */
	public static final double DRIVETRAIN_I = 0.004;
	
	
	/**
	 * D value for the drivetrain
	 */
	public static final double DRIVETRAIN_D = 0.005;
	
	
	/**
	 * F value for the drivetrain
	 */
	public static final double DRIVETRAIN_F = 1.0;
	
	
	/**
	 * The modifier for the left side of the drivetrain. 
	 * Can be set to 1 or negative 1
	 */
	public static final int LEFT_DRIVETRAIN_MODIFIER = 1;
	
	
	/**
	 * The modifier for the right side of the drivetrain.
	 * Can be set to 1 or negative 1
	 */
	public static final int RIGHT_DRIVETRAIN_MODIFIER = -1;
	
	
	/**
	 * The closed loop ramp rate for the drivetrain
	 */
	public static final double RAMP_RATE_DRIVETRAIN = 0.25;
	
	/**
	 * The interval for each loop of an execution of the Pathfinding processor
	 */
	public static final int EXECUTION_LOOP_INTERVAL = 25;
	
	
	// Modes
	
	/**
	 * The encoder ticks per meter as defined from a formula based on the other constants
	 */
	public static final double DRIVETRAIN_TICKS_PER_METER = (ENCODER_PULSES_PER_REVOLUTION)*(ENCODER_TICKS_PER_PULSE)*(DRIVETRAIN_ENCODER_TO_WHEEL_RATIO) * (1/(WHEEL_DIAMETER_IN_METERS * PI));
	
	
	/**
	 * The possible modes for autonomous in <code>{start} to {side} {object}</code> 
	 */
	public static final String[] POSSIBLE_MODES = {"cross line","center to left switch","center to right switch","left to left scale"};
}
