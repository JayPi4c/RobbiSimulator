package com.JayPi4c.RobbiSimulator.view;

import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;

import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * The menuBar holds all possible actions for the application in one menuBar.
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
@Getter
public class MenuBar extends javafx.scene.control.MenuBar {

	// Menu Bar
	// editor Menu
	private MenuItem newEditorMenuItem;
	private MenuItem saveEditorMenuItem;
	private MenuItem openEditorMenuItem;
	private MenuItem formatSourceCodeMenuItem;
	private MenuItem compileEditorMenuItem;
	private MenuItem printEditorMenuItem;

	private MenuItem quitEditorMenuItem;
	private Menu editorMenu;

	// territory Menu
	private MenuItem saveXMLTerritoryMenuItem;
	private MenuItem saveJAXBTerritoryMenuItem;
	private MenuItem saveSerialTerritoryMenuItem;

	private Menu saveTerritoryMenu;
	private MenuItem loadXMLTerritoryMenuItem;
	private MenuItem loadJAXBTerritoryMenuItem;
	private MenuItem loadSerialTerritoryMenuItem;

	private Menu loadTerritoryMenu;
	private Menu saveAsPicMenu;
	private MenuItem saveAsPNGMenuItem;
	private MenuItem saveAsGifMenuItem;
	private MenuItem printTerritoryMenuItem;
	private MenuItem changeSizeTerritoryMenuItem;
	private RadioMenuItem placeRobbiTerritoryRadioMenuItem;
	private RadioMenuItem placeHollowTerritoryRadioMenuItem;
	private RadioMenuItem placePileOfScrapTerritoryRadioMenuItem;
	private RadioMenuItem placeStockpileTerritoryRadioMenuItem;
	private RadioMenuItem placeAccuTerritoryRadioMenuItem;
	private RadioMenuItem placeScrewTerritoryRadioMenuItem;
	private RadioMenuItem placeNutTerritoryRadioMenuItem;
	private RadioMenuItem deleteFieldRadioMenuItem;
	private Menu territoryMenu;
	private ToggleGroup placeGroupTerritoryMenu;

	// robbi Menu
	private MenuItem itemPresentMenuItem;
	private MenuItem isStockpileMenuItem;
	private MenuItem hollowAheadMenuItem;
	private MenuItem pileOfScrapAheadMenuItem;
	private MenuItem isBagFullMenuItem;
	private MenuItem pushPileOfScrapMenuItem;
	private MenuItem moveMenuItem;
	private MenuItem turnLeftMenuItem;
	private MenuItem putMenuItem;
	private MenuItem takeMenuItem;
	private Menu robbiMenu;

	// simulation Menu
	private MenuItem resetMenuItem;
	private MenuItem startMenuItem;
	private MenuItem pauseMenuItem;
	private MenuItem stopMenuItem;
	private Menu simulationMenu;

	// examples Menu
	private MenuItem loadExampleMenuItem;
	private MenuItem saveExampleMenuItem;
	private Menu examplesMenu;

	// window Menu
	private Menu languageMenu;
	private MenuItem englishLanguageMenuItem;
	private MenuItem germanLanguageMenuItem;
	private CheckMenuItem changeCursorMenuItem;
	private CheckMenuItem darkModeMenuItem;
	private CheckMenuItem enableSoundsMenuItem;
	private MenuItem infoMenuItem;
	private MenuItem libraryMenuItem;
	private Menu windowMenu;

	// tutor Menu
	private Menu tutorMenu;
	private MenuItem sendRequestMenuItem;
	private MenuItem receiveAnswerMenuItem;
	private MenuItem loadRequestMenuItem;
	private MenuItem saveAnswerMenuItem;

	/**
	 * Creates a new MenuBar
	 */
	public MenuBar() {
		createMenuBar();
		getMenus().addAll(editorMenu, territoryMenu, robbiMenu, simulationMenu, examplesMenu, tutorMenu, windowMenu);
	}

	/**
	 * Creates the editor-related menu-bar elements.
	 */
	private void createEditor() {
		logger.debug("Create editor entry for menubar");
		newEditorMenuItem = new MenuItem();
		newEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newEditorMenuItem.setMnemonicParsing(true);
		newEditorMenuItem.setGraphic(new ImageView(MainStage.newImage));
		saveEditorMenuItem = new MenuItem();
		saveEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		saveEditorMenuItem.setGraphic(new ImageView(MainStage.saveImage));
		openEditorMenuItem = new MenuItem();
		openEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openEditorMenuItem.setGraphic(new ImageView(MainStage.openImage));

		formatSourceCodeMenuItem = new MenuItem();
		formatSourceCodeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN)); // T
																													// for
																													// tidy

		compileEditorMenuItem = new MenuItem();
		compileEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
		compileEditorMenuItem.setGraphic(new ImageView(MainStage.compileImage));

		printEditorMenuItem = new MenuItem();
		printEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		printEditorMenuItem.setGraphic(new ImageView(MainStage.printImage));
		quitEditorMenuItem = new MenuItem();
		quitEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		editorMenu = new Menu("", null, newEditorMenuItem, openEditorMenuItem, saveEditorMenuItem,
				new SeparatorMenuItem(), formatSourceCodeMenuItem, compileEditorMenuItem, printEditorMenuItem,
				new SeparatorMenuItem(), quitEditorMenuItem);
		editorMenu.setMnemonicParsing(true);
	}

	/**
	 * Creates the territory-related menu-bar elements.
	 */
	private void createTerritory() {
		logger.debug("Create territory entry for menubar");
		saveXMLTerritoryMenuItem = new MenuItem();
		saveJAXBTerritoryMenuItem = new MenuItem();
		saveSerialTerritoryMenuItem = new MenuItem();

		saveTerritoryMenu = new Menu("", null, saveXMLTerritoryMenuItem, saveJAXBTerritoryMenuItem,
				saveSerialTerritoryMenuItem);
		loadXMLTerritoryMenuItem = new MenuItem();
		loadJAXBTerritoryMenuItem = new MenuItem();
		loadSerialTerritoryMenuItem = new MenuItem();
		loadTerritoryMenu = new Menu("", null, loadXMLTerritoryMenuItem, loadJAXBTerritoryMenuItem,
				loadSerialTerritoryMenuItem);

		saveAsPNGMenuItem = new MenuItem();
		saveAsGifMenuItem = new MenuItem();
		saveAsPicMenu = new Menu("", null, saveAsPNGMenuItem, saveAsGifMenuItem);

		printTerritoryMenuItem = new MenuItem();
		changeSizeTerritoryMenuItem = new MenuItem();

		placeGroupTerritoryMenu = new ToggleGroup();

		placeRobbiTerritoryRadioMenuItem = new RadioMenuItem();
		placeRobbiTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeHollowTerritoryRadioMenuItem = new RadioMenuItem();
		placeHollowTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placePileOfScrapTerritoryRadioMenuItem = new RadioMenuItem();
		placePileOfScrapTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeStockpileTerritoryRadioMenuItem = new RadioMenuItem();
		placeStockpileTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeAccuTerritoryRadioMenuItem = new RadioMenuItem();
		placeAccuTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeScrewTerritoryRadioMenuItem = new RadioMenuItem();
		placeScrewTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeNutTerritoryRadioMenuItem = new RadioMenuItem();
		placeNutTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		deleteFieldRadioMenuItem = new RadioMenuItem();
		deleteFieldRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		territoryMenu = new Menu("", null, saveTerritoryMenu, loadTerritoryMenu, saveAsPicMenu, printTerritoryMenuItem,
				changeSizeTerritoryMenuItem, new SeparatorMenuItem(), placeRobbiTerritoryRadioMenuItem,
				placeHollowTerritoryRadioMenuItem, placePileOfScrapTerritoryRadioMenuItem,
				placeStockpileTerritoryRadioMenuItem, placeAccuTerritoryRadioMenuItem, placeScrewTerritoryRadioMenuItem,
				placeNutTerritoryRadioMenuItem, deleteFieldRadioMenuItem);
	}

	/**
	 * Creates the robbi-related menu-bar elements.
	 */
	private void createRobbi() {
		logger.debug("Create Robbi entry for menubar");
		territoryMenu.setMnemonicParsing(true);
		itemPresentMenuItem = new MenuItem();
		isStockpileMenuItem = new MenuItem();
		hollowAheadMenuItem = new MenuItem();
		pileOfScrapAheadMenuItem = new MenuItem();
		isBagFullMenuItem = new MenuItem();
		pushPileOfScrapMenuItem = new MenuItem();
		moveMenuItem = new MenuItem();
		moveMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		turnLeftMenuItem = new MenuItem();
		turnLeftMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

		putMenuItem = new MenuItem();
		putMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		takeMenuItem = new MenuItem();
		takeMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		robbiMenu = new Menu("", null, moveMenuItem, turnLeftMenuItem, putMenuItem, takeMenuItem,
				pushPileOfScrapMenuItem, itemPresentMenuItem, isStockpileMenuItem, hollowAheadMenuItem,
				pileOfScrapAheadMenuItem, isBagFullMenuItem);
		robbiMenu.setMnemonicParsing(true);
	}

	/**
	 * Creates the simulation-related menu-bar elements.
	 */
	private void createSimulation() {
		logger.debug("Create simulation entry for menubar");

		resetMenuItem = new MenuItem();
		resetMenuItem.setGraphic(new ImageView(MainStage.resetImage));
		startMenuItem = new MenuItem();
		startMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11, KeyCombination.CONTROL_DOWN));
		startMenuItem.setGraphic(new ImageView(MainStage.menuStartImage));
		pauseMenuItem = new MenuItem();
		pauseMenuItem.setGraphic(new ImageView(MainStage.menuPauseImage));
		stopMenuItem = new MenuItem();
		stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12, KeyCombination.CONTROL_DOWN));
		stopMenuItem.setGraphic(new ImageView(MainStage.menuStopImage));
		simulationMenu = new Menu("", null, resetMenuItem, startMenuItem, pauseMenuItem, stopMenuItem);
		simulationMenu.setMnemonicParsing(true);
	}

	/**
	 * Creates the examples-related menu-bar elements
	 */
	private void createExamplesMenu() {
		saveExampleMenuItem = new MenuItem();
		loadExampleMenuItem = new MenuItem();
		examplesMenu = new Menu("", null, saveExampleMenuItem, loadExampleMenuItem);
	}

	/**
	 * Creates the tutor-related menu-bar elements
	 */
	private void createTutorMenu() {
		if (PropertiesLoader.isTutor()) {
			loadRequestMenuItem = new MenuItem();
			saveAnswerMenuItem = new MenuItem();

			tutorMenu = new Menu("", null, loadRequestMenuItem, saveAnswerMenuItem);
		} else {
			sendRequestMenuItem = new MenuItem();
			receiveAnswerMenuItem = new MenuItem();

			tutorMenu = new Menu("", null, sendRequestMenuItem, receiveAnswerMenuItem);
		}
	}

	/**
	 * Creates the window-related menu-bar elements.
	 */
	private void createWindowMenu() {
		englishLanguageMenuItem = new MenuItem();
		germanLanguageMenuItem = new MenuItem();
		languageMenu = new Menu("", null, englishLanguageMenuItem, germanLanguageMenuItem);

		changeCursorMenuItem = new CheckMenuItem();
		// https://stackoverflow.com/a/49159612/13670629
		darkModeMenuItem = new CheckMenuItem();
		enableSoundsMenuItem = new CheckMenuItem();

		infoMenuItem = new MenuItem();
		libraryMenuItem = new MenuItem();

		windowMenu = new Menu("", null, languageMenu, changeCursorMenuItem, darkModeMenuItem, enableSoundsMenuItem,
				new SeparatorMenuItem(), infoMenuItem, libraryMenuItem);
	}

	/**
	 * Creates the entire menuBar.
	 */
	private void createMenuBar() {
		logger.debug("Create menubar");

		createEditor();
		createTerritory();
		createRobbi();
		createSimulation();
		createExamplesMenu();
		createTutorMenu();
		createWindowMenu();

	}

}
