package com.JayPi4c.RobbiSimulator.controller.simulation;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.Robbi;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.model.TerritoryState;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import com.JayPi4c.RobbiSimulator.view.Toolbar;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Controller to handle all actions belonging to the simulation.
 *
 * @author Jonas Pohl
 */
@Slf4j
public class SimulationController {

    private static final int MIN_SPEED = 100;
    private static final int MAX_SPEED = 2500;

    private Simulation simulation;

    private final MainStage stage;
    private final Territory territory;

    @Getter
    private volatile int speed;

    private final MenuItem resetMenuItem;
    private final Button resetToolbar;

    private final MenuItem pauseMenuItem;
    private final ToggleButton pauseToolbar;
    private final MenuItem startMenuItem;
    private final ToggleButton startToolbar;
    private final MenuItem stopMenuItem;
    private final ToggleButton stopToolbar;

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
        Toolbar toolbar = stage.getToolbar();
        speed = (int) toolbar.getSpeedSliderToolbar().getValue();

        resetToolbar = toolbar.getResetButtonToolbar();
        resetToolbar.setOnAction(e -> reset());
        resetMenuItem = this.stage.getMenubar().getResetMenuItem();
        resetMenuItem.onActionProperty().bind(resetToolbar.onActionProperty());

        startToolbar = toolbar.getStartToggleButtonToolbar();
        startToolbar.setOnAction(e -> {
            if (!isSimulationRunning())
                start();
            else
                resume();
        });
        startMenuItem = stage.getMenubar().getStartMenuItem();
        startMenuItem.onActionProperty().bind(startToolbar.onActionProperty());
        startMenuItem.disableProperty().bind(startToolbar.disableProperty());

        pauseToolbar = toolbar.getPauseToggleButtonToolbar();
        pauseToolbar.setOnAction(e -> pause());
        pauseMenuItem = this.stage.getMenubar().getPauseMenuItem();
        pauseMenuItem.onActionProperty().bind(pauseToolbar.onActionProperty());
        pauseMenuItem.disableProperty().bind(pauseToolbar.disableProperty());

        stopToolbar = toolbar.getStopToggleButtonToolbar();
        stopToolbar.setOnAction(e -> stop());
        stopMenuItem = this.stage.getMenubar().getStopMenuItem();
        stopMenuItem.onActionProperty().bind(stopToolbar.onActionProperty());
        stopMenuItem.disableProperty().bind(stopToolbar.disableProperty());

        toolbar.getSpeedSliderToolbar().valueProperty().addListener((ov, oldVal, newVal) -> setSpeed((Double) newVal));
        setSpeed(toolbar.getSpeedSliderToolbar().getValue());
        disableButtonStates(false, true, true);

    }

    /**
     * Helper to start e new simulation.
     */
    private void start() {
        logger.debug("Starting new simulation");
        Optional<Robbi> r = ProgramController.getNewRobbi(stage.getProgram().getName());
        if (r.isPresent())
            territory.setRobbi(r.get());
        else
            logger.debug("Could not initialize Robbi");
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
        simulation.setPaused(true);
        disableButtonStates(false, true, false);
    }

    /**
     * Helper to resume the current simulation.
     */
    private void resume() {
        logger.debug("Resuming simulation");
        simulation.setPaused(false);
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
        simulation.setStopped(true);
        simulation.setPaused(false);
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
        return !(simulation == null || simulation.isStopped());
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
        this.speed = (int) map(speed, Toolbar.MIN_SPEED_VALUE, Toolbar.MAX_SPEED_VALUE, MAX_SPEED, MIN_SPEED);
    }

    /**
     * Maps the given value, which ranges between inStart and inStop, on a value which
     * ranges between outStart and outStop.
     *
     * @param value    value to map
     * @param inStart  input start
     * @param inStop   input stop
     * @param outStart output start
     * @param outStop  output stop
     * @return the mapped value
     * @see <a href=
     * "https://stackoverflow.com/a/17135426/13670629">Stackoverflow</a>
     */
    private double map(double value, double inStart, double inStop, double outStart, double outStop) {
        return outStart + (outStop - outStart) * ((value - inStart) / (inStop - inStart));
    }

}
