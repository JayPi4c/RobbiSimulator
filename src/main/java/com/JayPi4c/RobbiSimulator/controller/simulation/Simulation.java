package com.JayPi4c.RobbiSimulator.controller.simulation;

import com.JayPi4c.RobbiSimulator.model.Robbi;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.Observable;
import com.JayPi4c.RobbiSimulator.utils.Observer;

import javafx.application.Platform;

public class Simulation extends Thread implements Observer {

	private Territory territory;
	private SimulationController simController;

	private volatile boolean stop;
	private volatile boolean pause;

	public Simulation(Territory territory, SimulationController simController) {
		this.territory = territory;
		this.simController = simController;
		stop = false;
		pause = false;
	}

	@Override
	public void run() {
		territory.addObserver(this);
		try {
			Robbi robbi = territory.getRobbi();
			robbi.main();
		} catch (StopException e) {
			// e.printStackTrace(); // TODO change to LOG
		} finally {
			territory.removeObserver(this);
			simController.finish();
		}

	}

	@Override
	public void update(Observable observable) {
		if (Platform.isFxApplicationThread()) // if observable is called by gui, ignore it
			return;
		try {
			sleep(simController.getSpeed());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (this.stop)
			throw new StopException();
		while (this.pause)
			try {
				sleep(1);// TODO change to wait. But what about Thread not owner exception?
			} catch (InterruptedException e) {

			}
		if (stop)
			throw new StopException();

	}

	public void setStop(boolean flag) {
		stop = flag;
	}

	public boolean getStop() {
		return stop;
	}

	public void setPause(boolean flag) {
		this.pause = flag;
	}

	public boolean getPause() {
		return this.pause;
	}

}
