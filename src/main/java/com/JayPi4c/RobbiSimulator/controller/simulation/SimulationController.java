package com.JayPi4c.RobbiSimulator.controller.simulation;

import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.view.MainStage;

public class SimulationController {

	// FIXME check for Deadlocks

	Simulation simulation;

	private MainStage stage;
	private Territory territory;

	private volatile int speed;

	public SimulationController(MainStage stage, Territory territory) {
		this.stage = stage;
		this.territory = territory;
		speed = (int) stage.getSpeedSliderToolbar().getValue();
		this.stage.getStartMenuItem().setOnAction(e -> start());
		this.stage.getStartToggleButtonToolbar().setOnAction(e -> {
			if (simulation == null || simulation.getStop())
				start();
			else
				resume();
		});
		this.stage.getPauseMenuItem().setOnAction(e -> pause());
		this.stage.getPauseToggleButtonToolbar().setOnAction(e -> pause());
		this.stage.getStopMenuItem().setOnAction(e -> stop());
		this.stage.getStopToggleButtonToolbar().setOnAction(e -> stop());

		this.stage.getSpeedSliderToolbar().valueProperty()
				.addListener((ov, oldVal, newVal) -> setSpeed((int) stage.getSpeedSliderToolbar().getValue()));

		// TODO set actions to buttons;

	}

	public void finish() {
		// TODO set button disable/enable
		// use Platform.runLater(); // we need to get into an fx thread to manipulate
		// gui
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	private void start() {
		simulation = new Simulation(territory, this);
		simulation.setDaemon(true); // program should exit even if simulation is running
		simulation.start();
	}

	private void resume() {
		simulation.setPause(false);
		// simulation.notify();
	}

	private void pause() {
		simulation.setPause(true);
	}

	private void stop() {
		simulation.setStop(true);
		simulation.setPause(false);
		simulation = null;
		// simulation.notify();
	}

}
