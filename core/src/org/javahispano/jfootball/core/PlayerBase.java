/**
 * 
 */
package org.javahispano.jfootball.core;

/**
 * @author alfonso
 *
 */
public class PlayerBase extends MovingEntity {
	private String name;
	private int number;
	private double velocity;
	private double power;
	private double accuracy;

	public PlayerBase() {
		this.name = "No name";
		this.number = 0;
		this.velocity = 0;
		this.power = 0;
		this.accuracy = 0;
	}

	public PlayerBase(String name, int number, double velocity, double power, double accuracy) {
		this.name = name;
		this.number = number;
		this.velocity = velocity;
		this.power = power;
		this.accuracy = accuracy;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * @param number the number to set
	 */
	public void setNumber(int number) {
		this.number = number;
	}

	/**
	 * @return the velocity
	 */
	public double getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the power
	 */
	public double getPower() {
		return power;
	}

	/**
	 * @param power the power to set
	 */
	public void setPower(double power) {
		this.power = power;
	}

	/**
	 * @return the accuracy
	 */
	public double getAccuracy() {
		return accuracy;
	}

	/**
	 * @param accuracy the accuracy to set
	 */
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	
}
