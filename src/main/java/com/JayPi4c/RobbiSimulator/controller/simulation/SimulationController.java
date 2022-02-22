package com.JayPi4c.RobbiSimulator.controller.simulation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.model.TerritoryState;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;

/**
 * Controller to handle all actions belonging to the simulation.
 * 
 * @author Jonas Pohl
 *
 */
public class SimulationController {

	private static final Logger logger = LoggerFactory.getLogger(SimulationController.class);

	private static final int MIN_SPEED = 100;
	private static final int MAX_SPEED = 2500;

	private Simulation simulation;

	private MainStage stage;
	private Territory territory;

	private volatile int speed;

	private MenuItem resetMenuItem;
	private Button resetToolbar;

	private MenuItem pauseMenuItem;
	private ToggleButton pauseToolbar;
	private MenuItem startMenuItem;
	private ToggleButton startToolbar;
	private MenuItem stopMenuItem;
	private ToggleButton stopToolbar;

	private TerritoryState territoryState;

	/**
	 * Constructor to create a new SimulationController, which adds all actions to
	 * the corresponding gui elements.
	 * 
	 * @param stage     The stage, this controller is for
	 * @param territory the territory in which the simulation takes place
	 */
	public SimulationController(MainStage stage, Territory territory) {
		this.stage = stage;
		this.territory = territory;
		speed = (int) stage.getSpeedSliderToolbar().getValue();

		resetToolbar = this.stage.getResetButtonToolbar();
		resetToolbar.setOnAction(e -> reset());
		resetMenuItem = this.stage.getResetMenuItem();
		resetMenuItem.onActionProperty().bind(resetToolbar.onActionProperty());

		startToolbar = this.stage.getStartToggleButtonToolbar();
		startToolbar.setOnAction(e -> {
			if (!isSimulationRunning())
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
				.addListener((ov, oldVal, newVal) -> setSpeed((Double) newVal));
		setSpeed(this.stage.getSpeedSliderToolbar().getValue());
		disableButtonStates(false, true, true);

	}

	/**
	 * Helper to start e new simulation.
	 */
	private void start() {
		logger.debug("Starting new simulation");
		territoryState = territory.save();
		simulation = new Simulation(territory, this, stage);
		simulation.setDaemon(true); // program should exit even if simulation is running
		simulation.start();
		disableButtonStates(true, false, false);
	}

	/**
	 * Helper to pause the current simulation.
	 */
	private void pause() {
		logger.debug("Pausing simulation");
		simulation.setPause(true);
		disableButtonStates(false, true, false);
	}

	/**
	 * Helper to resume the current simulation.
	 */
	private void resume() {
		logger.debug("Resuming simulation");
		simulation.setPause(false);
		synchronized (simulation.getLock()) {
			simulation.getLock().notifyAll();
		}
		disableButtonStates(true, false, false);
	}

	/**
	 * Helper to stop the current simulation.
	 */
	private void stop() {
		logger.debug("Stopping simulation");
		simulation.setStop(true);
		simulation.setPause(false);
		simulation.interrupt();
		synchronized (simulation.getLock()) {
			simulation.getLock().notifyAll();
		}
	}

	/**
	 * Helper to reset the territory to the state at the beginning of the
	 * simulation.
	 */
	private void reset() {
		logger.debug("Resetting the simulation");
		if (isSimulationRunning())
			stop();

		if (territoryState != null)
			territory.restore(territoryState);
	}

	/**
	 * Helper to check if the simulation is currently running
	 * 
	 * @return true if the simulation is not null or not stopped
	 */
	private boolean isSimulationRunning() {
		return !(simulation == null || simulation.getStop());
	}

	/**
	 * Stops the simulation if one exists.
	 */
	public void stopSimulation() {
		if (simulation != null) {
			stop();
		}
	}

	/**
	 * Helper to finish up a simulation if it is finished or has been stopped by the
	 * user.
	 */
	public void finish() {
		Platform.runLater(() -> {
			disableButtonStates(false, true, true);
			startToolbar.setSelected(false);
		});
	}

	/**
	 * Helper to set the buttonstates according the the given parameters.
	 * 
	 * @param start true, to disable start simulation button
	 * @param pause true, to disable pause simulation button
	 * @param stop  true, to disable stop simulation button
	 */
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

	/**
	 * Updates the speed attribute to the given parameter.
	 * 
	 * @param speed the new speed value
	 */
	public void setSpeed(double speed) {
		this.speed = (int) map(speed, MainStage.MIN_SPEED_VALUE, MainStage.MAX_SPEED_VALUE, MAX_SPEED, MIN_SPEED);
	}

	/**
	 * Getter for the speed attribute.
	 * 
	 * @return the speed attribute
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * Maps the given value, which ranges between istart and istop on a value which
	 * ranges between ostart and ostop.
	 * 
	 * @see <a href=
	 *      "https://stackoverflow.com/a/17135426/13670629">Stackoverflow</a>
	 * @param value  value to map
	 * @param istart input start
	 * @param istop  input stop
	 * @param ostart output start
	 * @param ostop  output stop
	 * @return the mapped value
	 */
	private final double map(double value, double istart, double istop, double ostart, double ostop) {
		return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
	}

}
