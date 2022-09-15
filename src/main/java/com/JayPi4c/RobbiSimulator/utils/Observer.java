package com.JayPi4c.RobbiSimulator.utils;

/**
 * Interface to allow to register to Observable classes. The update function
 * will be called whenever the observable class decides to notify all Observers.
 * 
 * @author Jonas Pohl
 *
 */
public interface Observer {

	/**
	 * Notifies the observer whenever the Observable class is notifying all
	 * observers about changes.
	 * 
	 * @param observable the Observable to call the update function.
	 */
	public void update(Observable observable);

}
