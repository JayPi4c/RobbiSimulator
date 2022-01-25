package com.JayPi4c.RobbiSimulator.utils;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Abstract class to allow the Observer-Pattern. Classes implementing the
 * Observer Interface can register to classes extending this Observable class.
 * 
 * @author Jonas Pohl
 *
 */
public abstract class Observable {

	private CopyOnWriteArrayList<Observer> observers; // faster than vector for read access and thread save

	private boolean changed;

	private boolean notify = true;

	/**
	 * Constructor for the Observable class which initializes the observer array.
	 */
	protected Observable() {
		observers = new CopyOnWriteArrayList<>();
		changed = false;
	}

	/**
	 * Sets the changed flag to true, in order to allow the notifyAllObservers
	 * method to inform all observers.
	 */
	public void setChanged() {
		changed = true;
	}

	/**
	 * Notifies all observers that the Observable Object has been updated.
	 */
	public void notifyAllObservers() {
		if (notify) {
			if (changed) {
				for (Observer s : observers)
					s.update(this);
			}
			changed = false;
		}
	}

	/**
	 * Register a new Observer to the list of Observers.
	 * 
	 * @param obs the new Observer to add to the list of observers.
	 */
	public void addObserver(Observer obs) {
		observers.add(obs);
	}

	/**
	 * Removes the given Observer from the list of observers. The Observer will no
	 * longer be notified about any updates.
	 * 
	 * @param obs the observer to remove from the list of observers
	 * @return true if the observer could be removed, false otherwise
	 */
	public boolean removeObserver(Observer obs) {
		return observers.remove(obs);
	}

	/**
	 * Allows to send Notifications to all observers and notifies all Observers if
	 * changes have been made while the notifications had been deactivated.
	 */
	public void activateNotification() {
		this.notify = true;
		notifyAllObservers();
	}

	/**
	 * Prevents the Observable class from notifying any observers even if
	 * notifyAllObservers is called. To activate notifications again, call
	 * activateNotification.
	 */
	public void deactivateNotification() {
		this.notify = false;
	}

}
