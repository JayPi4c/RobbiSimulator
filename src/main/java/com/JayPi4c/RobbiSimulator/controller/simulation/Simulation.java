package com.JayPi4c.RobbiSimulator.controller.simulation;

import com.JayPi4c.RobbiSimulator.model.RobbiException;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.Observable;
import com.JayPi4c.RobbiSimulator.utils.Observer;
import com.JayPi4c.RobbiSimulator.utils.SoundManager;
import javafx.application.Platform;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This Simulation class is a separate thread that runs the code of robbis
 * main-Method. It can be paused, resumed and stopped by the user at any time.
 *
 * @author Jonas Pohl
 */
@Slf4j
public class Simulation extends Thread implements Observer {

    @Getter
    private final Object lock = new Object();
    private Territory territory;
    private SimulationController simController;
    private Window parent;
    @Getter
    @Setter
    private volatile boolean stopped;
    @Getter
    @Setter
    private volatile boolean paused;

    /**
     * Constructor to create a new Simulation, which can execute robbis main-Method.
     *
     * @param territory     The territory in which the simulation is happening
     * @param simController The SimulationController, which started this simulation
     * @param parent        The parent window to show alerts relative to the it
     */
    public Simulation(Territory territory, SimulationController simController, Window parent) {
        this.territory = territory;
        this.simController = simController;
        this.parent = parent;
        stopped = false;
        paused = false;
    }

    /**
     * Starts the simulation and triggers the robbis main-Method. It informs the
     * user if the execution finished in an unexpected way. In the end it does some
     * finalization.
     */
    @Override
    public void run() {
        logger.info("Simulation started");
        territory.addObserver(this);
        try {
            Method main = territory.getRobbi().getClass().getDeclaredMethod("main");
            main.setAccessible(true);
            main.invoke(territory.getRobbi());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
                 | SecurityException e) {
            if (e.getCause() instanceof StopException) {
                logger.debug("Simulation stopped");
            } else if (e.getCause() instanceof RobbiException re) {
                logger.debug("Simulation stopped with exception: {}", re.getMessage());
                SoundManager.playWarnSound();
                // TODO: change to Snackbar?
                Platform.runLater(
                        () -> AlertHelper.showAlertAndWait(AlertType.ERROR, re.getLocalizedMessage(), parent));
            } else
                e.printStackTrace();
        } finally {
            stopped = true;
            territory.removeObserver(this);
            simController.finish();
            logger.info("Simulation done.");
        }

    }

    /**
     * If the territory has been updated by a non-FXApplicationThread, this thread
     * is sleeping for the time provided by the SimulationController. Furthermore,
     * it waits while the simulation is stopped and throws an StopException if the
     * Simulation has been terminated by the user.
     *
     * @param observable The observable, which caused the update to trigger
     */
    @Override
    public void update(Observable observable) {
        if (Platform.isFxApplicationThread()) // if observable is called by gui, ignore it
            return;
        try {
            sleep(simController.getSpeed());
        } catch (InterruptedException e) {
            logger.debug("Stopping simulation during sleep");
            Thread.currentThread().interrupt();
        }
        if (this.stopped)
            throw new StopException();
        while (this.paused)
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        if (stopped)
            throw new StopException();

    }

}
