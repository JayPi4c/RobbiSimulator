package com.JayPi4c.RobbiSimulator.utils;

import java.util.ArrayList;

public class Observable {
	public ArrayList<Observer> observers;

	boolean changed;

	public Observable() {
		observers = new ArrayList<Observer>();
		changed = false;
	}

	public void setChanged() {
		changed = true;
	}

	public void notifyAllObservers() {
		if (changed) {
			for (Observer s : observers)
				s.update(this);
		}
		changed = false;
	}

	public void addObserver(Observer obs) {
		observers.add(obs);
	}

}
