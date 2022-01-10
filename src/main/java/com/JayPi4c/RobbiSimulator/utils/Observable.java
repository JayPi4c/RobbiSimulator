package com.JayPi4c.RobbiSimulator.utils;

import java.util.ArrayList;

public class Observable {
	public ArrayList<Observer> observers;

	boolean changed;

	boolean notify = true;

	public Observable() {
		observers = new ArrayList<Observer>();
		changed = false;
	}

	public void setChanged() {
		changed = true;
	}

	public void notifyAllObservers() {
		if (notify) {
			if (changed) {
				for (Observer s : observers)
					s.update(this);
			}
			changed = false;
		}
	}

	public void addObserver(Observer obs) {
		observers.add(obs);
	}

	public void activateNotification() {
		this.notify = true;
		notifyAllObservers();
	}

	public void deactivateNotification() {
		this.notify = false;
	}

}
