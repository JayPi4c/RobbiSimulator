package com.JayPi4c.RobbiSimulator.controller.simulation;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.JayPi4c.RobbiSimulator.model.Robbi;
import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.Observable;
import com.JayPi4c.RobbiSimulator.utils.Observer;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Simulation extends Thread implements Observer {

	private static Logger logger = Logger.getLogger(Simulation.class.getName());

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
		logger.log(Level.WARNING, "simulation started");
		territory.addObserver(this);
		try {
			Robbi robbi = territory.getRobbi();
			robbi.main();
		} catch (StopException e) {
			logger.log(Level.INFO, "Simulation stopped");
		} catch (RobbiException re) {
			logger.log(Level.WARNING, "Simulation stopped with exception: " + re.getMessage());
			// simulation does not properly stop
			Platform.runLater(() -> {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setContentText(re.getMessage());
				alert.showAndWait();
			});
		} finally {
			stop = true;
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
			logger.log(Level.INFO, "Stopping simulation during sleep");
		}
		if (this.stop)
			throw new StopException();
		while (this.pause)
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {

				}
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
