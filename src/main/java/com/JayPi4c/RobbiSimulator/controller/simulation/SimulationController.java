package com.JayPi4c.RobbiSimulator.controller.simulation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;

public class SimulationController {

	private static final Logger logger = LogManager.getLogger(SimulationController.class);

	private Simulation simulation;

	private MainStage stage;
	private Territory territory;

	private volatile int speed;

	private MenuItem pauseMenuItem;
	private ToggleButton pauseToolbar;
	private MenuItem startMenuItem;
	private ToggleButton startToolbar;
	private MenuItem stopMenuItem;
	private ToggleButton stopToolbar;

	public SimulationController(MainStage stage, Territory territory) {
		this.stage = stage;
		this.territory = territory;
		speed = (int) stage.getSpeedSliderToolbar().getValue();
		startToolbar = this.stage.getStartToggleButtonToolbar();
		startToolbar.setOnAction(e -> {
			if (simulation == null || simulation.getStop())
				start();
			else
				resume();
		});
		startMenuItem = stage.getStartMenuItem();
		startMenuItem.onActionProperty().bind(startToolbar.onActionProperty());
		startMenuItem.disableProperty().bind(startToolbar.disableProperty());

		pauseToolbar = this.stage.getPauseToggleButtonToolbar();
		pauseToolbar.setOnAction(e -> pause());
		pauseMenuItem = this.stage.getPauseMenuItem();
		pauseMenuItem.onActionProperty().bind(pauseToolbar.onActionProperty());
		pauseMenuItem.disableProperty().bind(pauseToolbar.disableProperty());

		stopToolbar = this.stage.getStopToggleButtonToolbar();
		stopToolbar.setOnAction(e -> stop());
		stopMenuItem = this.stage.getStopMenuItem();
		stopMenuItem.onActionProperty().bind(stopToolbar.onActionProperty());
		stopMenuItem.disableProperty().bind(stopToolbar.disableProperty());

		this.stage.getSpeedSliderToolbar().valueProperty()
				.addListener((ov, oldVal, newVal) -> setSpeed((int) stage.getSpeedSliderToolbar().getValue()));
		disableButtonStates(false, true, true);

	}

	public void finish() {
		disableButtonStates(false, true, true);
		startToolbar.setSelected(false);
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	private void start() {
		logger.debug("Starting new simulation");
		simulation = new Simulation(territory, this);
		simulation.setDaemon(true); // program should exit even if simulation is running
		simulation.start();
		disableButtonStates(true, false, false);
	}

	private void resume() {
		logger.debug("Resuming simulation");
		simulation.setPause(false);
		synchronized (simulation) {
			simulation.notify();
		}
		disableButtonStates(true, false, false);
	}

	private void pause() {
		logger.debug("Pausing simulation");
		simulation.setPause(true);
		disableButtonStates(false, true, false);
	}

	private void stop() {
		logger.debug("Stopping simulation");
		simulation.setStop(true);
		simulation.setPause(false);
		simulation.interrupt();
		synchronized (simulation) {
			simulation.notify();
		}
	}

	private void disableButtonStates(boolean start, boolean pause, boolean stop) {
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> {
				startToolbar.setDisable(start);
				pauseToolbar.setDisable(pause);
				stopToolbar.setDisable(stop);
			});
		} else {
			startToolbar.setDisable(start);
			pauseToolbar.setDisable(pause);
			stopToolbar.setDisable(stop);
		}
	}

}
