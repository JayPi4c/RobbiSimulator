package com.JayPi4c.RobbiSimulator.utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class Observable {

	public CopyOnWriteArrayList<Observer> observers; // faster than vector for read access and thread save

	boolean changed;

	boolean notify = true;

	public Observable() {
		observers = new CopyOnWriteArrayList<>();
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

	public boolean removeObserver(Observer obs) {
		return observers.remove(obs);
	}

	public void activateNotification() {
		this.notify = true;
		notifyAllObservers();
	}

	public void deactivateNotification() {
		this.notify = false;
	}

}
