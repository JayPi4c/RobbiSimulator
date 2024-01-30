package com.JayPi4c.RobbiSimulator.view;

import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * The toolbar allows faster access to the most common actions.
 *
 * @author Jonas Pohl
 */
@Getter
@Slf4j
public class Toolbar extends ToolBar {

    /**
     * Constant for the minimum value for the speed slider.
     */
    public static final int MIN_SPEED_VALUE = 1;
    /**
     * Constant for the maximum value for the speed slider.
     */
    public static final int MAX_SPEED_VALUE = 100;
    private MenuBar menubar;
    // Tool bar
    private Button newButtonToolbar;
    private Button loadButtonToolbar;
    private Button saveButtonToolbar;
    private Button compileButtonToolbar;
    private Button changeSizeButtonToolbar;
    private ToggleButton placeRobbiToggleButtonToolbar;
    private ToggleButton placeHollowToggleButtonToolbar;
    private ToggleButton placePileOfScrapToggleButtonToolbar;
    private ToggleButton placeStockpileToggleButtonToolbar;
    private ToggleButton placeAccuToggleButtonToolbar;
    private ToggleButton placeScrewToggleButtonToolbar;
    private ToggleButton placeNutToggleButtonToolbar;
    private ToggleButton deleteFieldToggleButtonToolbar;
    private Button robbiTurnLeftButtonToolbar;
    private Button robbiMoveButtonToolbar;
    private Button robbiPutButtonToolbar;
    private Button robbiTakeButtonToolbar;
    private Button resetButtonToolbar;
    private ToggleButton startToggleButtonToolbar;
    private ToggleButton pauseToggleButtonToolbar;
    private ToggleButton stopToggleButtonToolbar;
    private Slider speedSliderToolbar;

    /**
     * Creates a new Toolbar
     *
     * @param menubar the menubar to link some actions from the menubar to the
     *                toolbar buttons
     */
    public Toolbar(MenuBar menubar) {
        this.menubar = menubar;
        createToolbar();
    }

    /**
     * Creates a toolbar for direct-access to the most important features.
     */
    private void createToolbar() {
        logger.debug("Create toolbar");
        newButtonToolbar = new Button(null, new ImageView(MainStage.newImage));


        loadButtonToolbar = new Button(null, new ImageView(MainStage.openImage));

        saveButtonToolbar = new Button(null, new ImageView(MainStage.saveImage));

        compileButtonToolbar = new Button(null, new ImageView(MainStage.compileImage));

        changeSizeButtonToolbar = new Button(null, new ImageView(MainStage.terrainImage));

        var placeGroupToolbar = new ToggleGroup();

        placeRobbiToggleButtonToolbar = new ToggleButton();
        placeRobbiToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuRobbiImage));
        placeRobbiToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placeRobbiToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlaceRobbiTerritoryRadioMenuItem().selectedProperty());

        placeHollowToggleButtonToolbar = new ToggleButton();
        placeHollowToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuHollowImage));
        placeHollowToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placeHollowToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlaceHollowTerritoryRadioMenuItem().selectedProperty());

        placePileOfScrapToggleButtonToolbar = new ToggleButton();
        placePileOfScrapToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuPileOfScrapImage));
        placePileOfScrapToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placePileOfScrapToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlacePileOfScrapTerritoryRadioMenuItem().selectedProperty());

        placeStockpileToggleButtonToolbar = new ToggleButton();
        placeStockpileToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuStockpileImage));
        placeStockpileToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placeStockpileToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlaceStockpileTerritoryRadioMenuItem().selectedProperty());

        placeAccuToggleButtonToolbar = new ToggleButton();
        placeAccuToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuAccuImage));
        placeAccuToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placeAccuToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlaceAccuTerritoryRadioMenuItem().selectedProperty());

        placeScrewToggleButtonToolbar = new ToggleButton();
        placeScrewToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuScrewImage));
        placeScrewToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placeScrewToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlaceScrewTerritoryRadioMenuItem().selectedProperty());

        placeNutToggleButtonToolbar = new ToggleButton();
        placeNutToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuNutImage));
        placeNutToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        placeNutToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getPlaceNutTerritoryRadioMenuItem().selectedProperty());

        deleteFieldToggleButtonToolbar = new ToggleButton();
        deleteFieldToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuDeleteImage));
        deleteFieldToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
        deleteFieldToggleButtonToolbar.selectedProperty()
                .bindBidirectional(menubar.getDeleteFieldRadioMenuItem().selectedProperty());

        robbiMoveButtonToolbar = new Button(null, new ImageView(MainStage.robbiMove));

        robbiTurnLeftButtonToolbar = new Button(null, new ImageView(MainStage.robbiTurnLeft));

        robbiPutButtonToolbar = new Button(null, new ImageView(MainStage.robbiPut));

        robbiTakeButtonToolbar = new Button(null, new ImageView(MainStage.robbiTake));

        resetButtonToolbar = new Button(null, new ImageView(MainStage.resetImage));

        var simulationGroupToolbar = new ToggleGroup();
        startToggleButtonToolbar = new ToggleButton();
        startToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuStartImage));
        startToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

        pauseToggleButtonToolbar = new ToggleButton();
        pauseToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuPauseImage));
        pauseToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

        stopToggleButtonToolbar = new ToggleButton();
        stopToggleButtonToolbar.setGraphic(new ImageView(MainStage.menuStopImage));
        stopToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

        speedSliderToolbar = new Slider(MIN_SPEED_VALUE, MAX_SPEED_VALUE, (MIN_SPEED_VALUE + MAX_SPEED_VALUE) / 2d);

        getItems().addAll(newButtonToolbar, loadButtonToolbar, new Separator(), saveButtonToolbar, compileButtonToolbar,
                new Separator(), changeSizeButtonToolbar, placeRobbiToggleButtonToolbar, placeHollowToggleButtonToolbar,
                placePileOfScrapToggleButtonToolbar, placeStockpileToggleButtonToolbar, placeAccuToggleButtonToolbar,
                placeScrewToggleButtonToolbar, placeNutToggleButtonToolbar, deleteFieldToggleButtonToolbar,
                new Separator(), robbiMoveButtonToolbar, robbiTurnLeftButtonToolbar, robbiPutButtonToolbar,
                robbiTakeButtonToolbar, new Separator(), resetButtonToolbar, startToggleButtonToolbar,
                pauseToggleButtonToolbar, stopToggleButtonToolbar, new Separator(), speedSliderToolbar);
    }
}
