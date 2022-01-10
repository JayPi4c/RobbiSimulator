package com.JayPi4c.RobbiSimulator.view;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.JayPi4c.RobbiSimulator.controller.ButtonState;
import com.JayPi4c.RobbiSimulator.controller.MainStageController;
import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.controller.simulation.SimulationController;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.ILanguageChangeListener;
import com.JayPi4c.RobbiSimulator.utils.Messages;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainStage extends Stage implements ILanguageChangeListener {
	Logger logger = Logger.getLogger(MainStage.class.getName());

	private Territory territory;

	private ButtonState buttonState;
	private MainStageController mainStageController;

	// private SimulationController simController;

	private static final int MIN_SPEED_VALUE = 100;
	private static final int MAX_SPEED_VALUE = 2500;

	private Program program;

	// Menu Bar
	// editor Menu
	private MenuItem newEditorMenuItem;
	private MenuItem saveEditorMenuItem;
	private MenuItem openEditorMenuItem;
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
	private MenuItem saveAsPicMenuItem;
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
	private MenuItem startMenuItem;
	private MenuItem pauseMenuItem;
	private MenuItem stopMenuItem;
	private Menu simulationMenu;

	// window Meun
	private Menu languageMenu;
	private MenuItem englishLanguageMenuItem;
	private MenuItem germanLanguageMenuItem;
	private CheckMenuItem changeCursorMenuItem;
	private CheckMenuItem darkModeMenuItem;
	private Menu windowMenu;

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

	private ToggleButton startToggleButtonToolbar;
	private ToggleButton pauseToggleButtonToolbar;
	private ToggleButton stopToggleButtonToolbar;

	private Slider speedSliderToolbar;

	private ToolBar toolbar;

	// Content Pane
	private TextArea textArea;
	private ScrollPane territoryScrollPane;
	private TerritoryPanel territoryPanel;
	private SplitPane splitPane;

	// Message Label
	private Label messageLabel;

	private Scene scene;

	public static Image openImage;
	public static Image newImage;
	public static Image saveImage;
	public static Image compileImage;
	public static Image printImage;
	public static Image terrainImage;

	public static Image menuRobbiImage;
	public static Image menuHollowImage;
	public static Image menuPileOfScrapImage;
	public static Image menuStockpileImage;
	public static Image menuAccuImage;
	public static Image menuScrewImage;
	public static Image menuNutImage;
	public static Image menuDeleteImage;

	public static Image menuStartImage;
	public static Image menuPauseImage;
	public static Image menuStopImage;

	public static Image robbiMove;
	public static Image robbiTurnLeft;
	public static Image robbiPut;
	public static Image robbiTake;

	public MainStage(Program program) {
		this.program = program;
		Messages.registerListener(this);

		territory = new Territory();
		buttonState = new ButtonState();

		createMenuBar();
		createToolbar();
		createContentPane();
		createMessageLabel();

		VBox.setVgrow(splitPane, Priority.ALWAYS);
		var vBox = new VBox(menubar, toolbar, splitPane, messageLabel);

		mainStageController = new MainStageController(this, buttonState);
		// var simController =
		new SimulationController(this, territory);

		scene = new Scene(vBox);

		setMinHeight(200);
		setMinWidth(500);

		setScene(scene);
		getIcons().add(menuRobbiImage);
		setTitle(Messages.getString("Main.title") + ": " + program.getName());
		setOnCloseRequest(e -> {
			program.save(textArea.getText());
			ProgramController.close(program.getName());
		});
		// initJavaFX();

		show();
		textArea.requestFocus();
		logger.log(Level.INFO, "Finished loading '" + program.getName() + "'");
	}

	// https://stackoverflow.com/questions/31219169/javafx-application-name-on-gnome
	// does not work yet
	/*
	 * private void initJavaFX() { if
	 * (System.getProperty("os.name").toLowerCase().contains("linux")) { try {
	 * Toolkit xToolkit = Toolkit.getDefaultToolkit(); Field awtAppClassNameField =
	 * xToolkit.getClass().getDeclaredField("com.JayPi4c.RobbiSimulator.App");
	 * awtAppClassNameField.setAccessible(true); awtAppClassNameField.set(xToolkit,
	 * "MyApp"); } catch (Exception ignored) { } } }
	 */

	public Program getProgram() {
		return this.program;
	}

	public MenuItem getStartMenuItem() {
		return startMenuItem;
	}

	public MenuItem getPauseMenuItem() {
		return pauseMenuItem;
	}

	public MenuItem getStopMenuItem() {
		return stopMenuItem;
	}

	public ToggleButton getStartToggleButtonToolbar() {
		return startToggleButtonToolbar;
	}

	public ToggleButton getPauseToggleButtonToolbar() {
		return pauseToggleButtonToolbar;
	}

	public ToggleButton getStopToggleButtonToolbar() {
		return stopToggleButtonToolbar;
	}

	public Slider getSpeedSliderToolbar() {
		return speedSliderToolbar;
	}

	public Territory getTerritory() {
		return territory;
	}

	public MenuItem getSaveEditorMenuItem() {
		return saveEditorMenuItem;
	}

	public MenuItem getQuitEditorMenuItem() {
		return quitEditorMenuItem;
	}

	public RadioMenuItem getPlaceRobbiTerritoryRadioMenuItem() {
		return placeRobbiTerritoryRadioMenuItem;
	}

	public RadioMenuItem getPlaceHollowTerritoryRadioMenuItem() {
		return placeHollowTerritoryRadioMenuItem;
	}

	public RadioMenuItem getPlacePileOfScrapTerritoryRadioMenuItem() {
		return placePileOfScrapTerritoryRadioMenuItem;
	}

	public RadioMenuItem getPlaceStockpileTerritoryRadioMenuItem() {
		return placeStockpileTerritoryRadioMenuItem;
	}

	public RadioMenuItem getPlaceAccuTerritoryRadioMenuItem() {
		return placeAccuTerritoryRadioMenuItem;
	}

	public RadioMenuItem getPlaceScrewTerritoryRadioMenuItem() {
		return placeScrewTerritoryRadioMenuItem;
	}

	public RadioMenuItem getPlaceNutTerritoryRadioMenuItem() {
		return placeNutTerritoryRadioMenuItem;
	}

	public MenuItem getPileOfScrapAheadMenuItem() {
		return pileOfScrapAheadMenuItem;
	}

	public Button getSaveButtonToolbar() {
		return saveButtonToolbar;
	}

	public ToggleButton getPlaceRobbiToggleButtonToolbar() {
		return placeRobbiToggleButtonToolbar;
	}

	public ToggleButton getPlaceHollowToggleButtonToolbar() {
		return placeHollowToggleButtonToolbar;
	}

	public ToggleButton getPlacePileOfScrapToggleButtonToolbar() {
		return placePileOfScrapToggleButtonToolbar;
	}

	public ToggleButton getPlaceStockpileToggleButtonToolbar() {
		return placeStockpileToggleButtonToolbar;
	}

	public ToggleButton getPlaceAccuToggleButtonToolbar() {
		return placeAccuToggleButtonToolbar;
	}

	public ToggleButton getPlaceScrewToggleButtonToolbar() {
		return placeScrewToggleButtonToolbar;
	}

	public ToggleButton getPlaceNutToggleButtonToolbar() {
		return placeNutToggleButtonToolbar;
	}

	public Scene getMainStageScene() {
		return scene;
	}

	public RadioMenuItem getDeleteFieldRadioMenuItem() {
		return deleteFieldRadioMenuItem;
	}

	public ToggleButton getDeleteFieldToggleButtonToolbar() {
		return deleteFieldToggleButtonToolbar;
	}

	public MenuItem getItemPresentMenuItem() {
		return itemPresentMenuItem;
	}

	public MenuItem getIsStockpileMenuItem() {
		return isStockpileMenuItem;
	}

	public MenuItem getHollowAheadMenuItem() {
		return hollowAheadMenuItem;
	}

	public MenuItem getIsBagFullMenuItem() {
		return isBagFullMenuItem;
	}

	public MenuItem getPushPileOfScrapMenuItem() {
		return pushPileOfScrapMenuItem;
	}

	public MenuItem getMoveMenuItem() {
		return moveMenuItem;
	}

	public MenuItem getTurnLeftMenuItem() {
		return turnLeftMenuItem;
	}

	public MenuItem getPutMenuItem() {
		return putMenuItem;
	}

	public MenuItem getTakeMenuItem() {
		return takeMenuItem;
	}

	public TextArea getTextArea() {
		return textArea;
	}

	public ScrollPane getTerritoryScrollPane() {
		return territoryScrollPane;
	}

	public MenuItem getNewEditorMenuItem() {
		return newEditorMenuItem;
	}

	public MenuItem getOpenEditorMenuItem() {
		return openEditorMenuItem;
	}

	public MenuItem getCompileEditorMenuItem() {
		return compileEditorMenuItem;
	}

	public MenuItem getChangeSizeTerritoryMenuItem() {
		return changeSizeTerritoryMenuItem;
	}

	public Button getNewButtonToolbar() {
		return newButtonToolbar;
	}

	public Button getLoadButtonToolbar() {
		return loadButtonToolbar;
	}

	public Button getCompileButtonToolbar() {
		return compileButtonToolbar;
	}

	public Button getChangeSizeButtonToolbar() {
		return changeSizeButtonToolbar;
	}

	public static void loadImages() {
		newImage = new Image("img/New24.gif");
		saveImage = new Image("img/Save24.gif");
		openImage = new Image("img/Open24.gif");
		compileImage = new Image("img/Compile24.gif");
		printImage = new Image("img/Print24.gif");
		terrainImage = new Image("img/Terrain24.gif");
		menuRobbiImage = new Image("img/Robbi24.png");
		menuHollowImage = new Image("img/Hollow24.png");
		menuPileOfScrapImage = new Image("img/PileOfScrap24.png");
		menuStockpileImage = new Image("img/Stockpile24.png");
		menuAccuImage = new Image("img/Accu24.png");
		menuScrewImage = new Image("img/Screw24.png");
		menuNutImage = new Image("img/Nut24.png");
		menuDeleteImage = new Image("img/Delete24.gif");

		menuStartImage = new Image("img/Play24.gif");
		menuPauseImage = new Image("img/Pause24.gif");
		menuStopImage = new Image("img/Stop24.gif");

		robbiMove = new Image("img/RobbiMove24.png");
		robbiTurnLeft = new Image("img/RobbiLeft24.png");
		robbiPut = new Image("img/RobbiPut24.png");
		robbiTake = new Image("img/RobbiTake24.png");
	}

	/**
	 * Erstelle den Editoreintrag für die Menubar
	 */
	private void createEditor() {
		logger.log(Level.INFO, "Create editor entry for menubar");
		newEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.new"));
		newEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newEditorMenuItem.setMnemonicParsing(true);
		newEditorMenuItem.setGraphic(new ImageView(newImage));
		saveEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.save"));
		saveEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		saveEditorMenuItem.setGraphic(new ImageView(saveImage));
		openEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.open"));
		openEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openEditorMenuItem.setGraphic(new ImageView(openImage));
		compileEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.compile"));
		compileEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
		compileEditorMenuItem.setGraphic(new ImageView(compileImage));

		printEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.print"));
		printEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		printEditorMenuItem.setGraphic(new ImageView(printImage));
		quitEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.quit"));
		quitEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		editorMenu = new Menu(Messages.getString("Menu.editor"), null, newEditorMenuItem, openEditorMenuItem,
				saveEditorMenuItem, new SeparatorMenuItem(), compileEditorMenuItem, printEditorMenuItem,
				new SeparatorMenuItem(), quitEditorMenuItem);
		editorMenu.setMnemonicParsing(true);
	}

	/**
	 * 
	 * Erstelle den Territoriumseintrag für die Menubar
	 */
	private void createTerritory() {
		logger.log(Level.INFO, "Create territory entry for menubar");
		saveXMLTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.save.xml"));
		saveJAXBTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.save.jaxb"));
		saveSerialTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.save.serialize"));

		saveTerritoryMenu = new Menu(Messages.getString("Menu.territory.save"), null, saveXMLTerritoryMenuItem,
				saveJAXBTerritoryMenuItem, saveSerialTerritoryMenuItem);
		loadXMLTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.load.xml"));
		loadJAXBTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.load.jaxb"));
		loadSerialTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.load.deserialize"));
		loadTerritoryMenu = new Menu(Messages.getString("Menu.territory.load"), null, loadXMLTerritoryMenuItem,
				loadJAXBTerritoryMenuItem, loadSerialTerritoryMenuItem);
		saveAsPicMenuItem = new MenuItem(Messages.getString("Menu.territory.saveAsPic"));
		printTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.print"));
		changeSizeTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.size"));

		placeGroupTerritoryMenu = new ToggleGroup();

		placeRobbiTerritoryRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.place.robbi"));
		placeRobbiTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeHollowTerritoryRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.place.hollow"));
		placeHollowTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placePileOfScrapTerritoryRadioMenuItem = new RadioMenuItem(
				Messages.getString("Menu.territory.place.pileOfScrap"));
		placePileOfScrapTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeStockpileTerritoryRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.place.stockpile"));
		placeStockpileTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeAccuTerritoryRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.place.accu"));
		placeAccuTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeScrewTerritoryRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.place.screw"));
		placeScrewTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeNutTerritoryRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.place.nut"));
		placeNutTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		deleteFieldRadioMenuItem = new RadioMenuItem(Messages.getString("Menu.territory.delete"));
		deleteFieldRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		territoryMenu = new Menu(Messages.getString("Menu.territory"), null, saveTerritoryMenu, loadTerritoryMenu,
				saveAsPicMenuItem, printTerritoryMenuItem, changeSizeTerritoryMenuItem, new SeparatorMenuItem(),
				placeRobbiTerritoryRadioMenuItem, placeHollowTerritoryRadioMenuItem,
				placePileOfScrapTerritoryRadioMenuItem, placeStockpileTerritoryRadioMenuItem,
				placeAccuTerritoryRadioMenuItem, placeScrewTerritoryRadioMenuItem, placeNutTerritoryRadioMenuItem,
				deleteFieldRadioMenuItem);
	}

	/**
	 * Erstelle den Robbieintrag für die Menubar
	 */
	private void createRobbi() {
		logger.log(Level.INFO, "Create Robbi entry for menubar");
		territoryMenu.setMnemonicParsing(true);
		itemPresentMenuItem = new MenuItem(Messages.getString("Menu.robbi.itemPresent"));
		isStockpileMenuItem = new MenuItem(Messages.getString("Menu.robbi.isStockpile"));
		hollowAheadMenuItem = new MenuItem(Messages.getString("Menu.robbi.hollowAhead"));
		pileOfScrapAheadMenuItem = new MenuItem(Messages.getString("Menu.robbi.pileOfScrapAhead"));
		isBagFullMenuItem = new MenuItem(Messages.getString("Menu.robbi.isBagFull"));
		pushPileOfScrapMenuItem = new MenuItem(Messages.getString("Menu.robbi.pushPileOfScrap"));
		moveMenuItem = new MenuItem(Messages.getString("Menu.robbi.move"));
		moveMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		turnLeftMenuItem = new MenuItem(Messages.getString("Menu.robbi.turnLeft"));
		turnLeftMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

		putMenuItem = new MenuItem(Messages.getString("Menu.robbi.put"));
		putMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		takeMenuItem = new MenuItem(Messages.getString("Menu.robbi.take"));
		takeMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		robbiMenu = new Menu(Messages.getString("Menu.robbi"), null, moveMenuItem, turnLeftMenuItem, putMenuItem,
				takeMenuItem, pushPileOfScrapMenuItem, itemPresentMenuItem, isStockpileMenuItem, hollowAheadMenuItem,
				pileOfScrapAheadMenuItem, isBagFullMenuItem);
		robbiMenu.setMnemonicParsing(true);
	}

	/**
	 * Erstelle den Simulationseintrag für die Menubar
	 */
	private void createSimulation() {
		logger.log(Level.INFO, "Create simulation entry for menubar");

		startMenuItem = new MenuItem(Messages.getString("Menu.simulation.start"));
		startMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11, KeyCombination.CONTROL_DOWN));
		startMenuItem.setGraphic(new ImageView(menuStartImage));
		pauseMenuItem = new MenuItem(Messages.getString("Menu.simulation.pause"));
		pauseMenuItem.setGraphic(new ImageView(menuPauseImage));
		stopMenuItem = new MenuItem(Messages.getString("Menu.simulation.stop"));
		stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12, KeyCombination.CONTROL_DOWN));
		stopMenuItem.setGraphic(new ImageView(menuStopImage));
		simulationMenu = new Menu(Messages.getString("Menu.simulation"), null, startMenuItem, pauseMenuItem,
				stopMenuItem);
		simulationMenu.setMnemonicParsing(true);
	}

	private void createWindowMenu() {
		englishLanguageMenuItem = new MenuItem(Messages.getString("Menu.window.language.english"));
		englishLanguageMenuItem.setOnAction(e -> Messages.changeBundle("lang.messages_en_US"));
		germanLanguageMenuItem = new MenuItem(Messages.getString("Menu.window.language.german"));
		germanLanguageMenuItem.setOnAction(e -> Messages.changeBundle("lang.messages_de_DE"));

		languageMenu = new Menu(Messages.getString("Menu.window.language"), null, englishLanguageMenuItem,
				germanLanguageMenuItem);
		changeCursorMenuItem = new CheckMenuItem(Messages.getString("Menu.window.changeCursor"));
		changeCursorMenuItem.setOnAction(e -> {
			mainStageController.setChangeCursor(changeCursorMenuItem.isSelected());
			if (!changeCursorMenuItem.isSelected())
				scene.setCursor(Cursor.DEFAULT);
		});

		// https://stackoverflow.com/a/49159612/13670629
		darkModeMenuItem = new CheckMenuItem("Darkmode");
		darkModeMenuItem.selectedProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal) {
				scene.getStylesheets().add("css/dark-theme.css");
			} else
				scene.getStylesheets().remove("css/dark-theme.css");
		});
		windowMenu = new Menu(Messages.getString("Menu.window"), null, languageMenu, changeCursorMenuItem,
				darkModeMenuItem);
	}

	/**
	 * Erstelle die gesamte Menubar mit allen Einträgen
	 */
	private void createMenuBar() {
		logger.log(Level.INFO, "Create menubar");

		createEditor();
		createTerritory();
		createRobbi();
		createSimulation();
		createWindowMenu();
		menubar = new MenuBar(editorMenu, territoryMenu, robbiMenu, simulationMenu, windowMenu);
	}

	/**
	 * Erstelle die Toolbar mit den wichtigsten Funktionalitäten als direkt zu
	 * erreichende Buttons
	 */
	private void createToolbar() {
		logger.log(Level.INFO, "Create toolbar");

		newButtonToolbar = new Button(null, new ImageView(newImage));
		newButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.new")));

		loadButtonToolbar = new Button(null, new ImageView(openImage));
		loadButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.load")));

		saveButtonToolbar = new Button(null, new ImageView(saveImage));
		saveButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.save")));

		compileButtonToolbar = new Button(null, new ImageView(compileImage));
		compileButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.compile")));

		changeSizeButtonToolbar = new Button(null, new ImageView(terrainImage));
		changeSizeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.size")));

		var placeGroupToolbar = new ToggleGroup();

		placeRobbiToggleButtonToolbar = new ToggleButton(null, new ImageView(menuRobbiImage));
		placeRobbiToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeRobbi")));
		placeRobbiToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeRobbiToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeRobbiTerritoryRadioMenuItem.selectedProperty());

		placeHollowToggleButtonToolbar = new ToggleButton(null, new ImageView(menuHollowImage));
		placeHollowToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeHollowToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeHollow")));
		placeHollowToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeHollowTerritoryRadioMenuItem.selectedProperty());

		placePileOfScrapToggleButtonToolbar = new ToggleButton(null, new ImageView(menuPileOfScrapImage));
		placePileOfScrapToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placePileOfScrapToggleButtonToolbar
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placePileOfScrap")));
		placePileOfScrapToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placePileOfScrapTerritoryRadioMenuItem.selectedProperty());

		placeStockpileToggleButtonToolbar = new ToggleButton(null, new ImageView(menuStockpileImage));
		placeStockpileToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeStockpileToggleButtonToolbar
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeStockpile")));
		placeStockpileToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeStockpileTerritoryRadioMenuItem.selectedProperty());

		placeAccuToggleButtonToolbar = new ToggleButton(null, new ImageView(menuAccuImage));
		placeAccuToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeAccuToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeAccu")));
		placeAccuToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeAccuTerritoryRadioMenuItem.selectedProperty());

		placeScrewToggleButtonToolbar = new ToggleButton(null, new ImageView(menuScrewImage));
		placeScrewToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeScrewToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeScrew")));
		placeScrewToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeScrewTerritoryRadioMenuItem.selectedProperty());

		placeNutToggleButtonToolbar = new ToggleButton(null, new ImageView(menuNutImage));
		placeNutToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeNutToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeNut")));
		placeNutToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeNutTerritoryRadioMenuItem.selectedProperty());

		deleteFieldToggleButtonToolbar = new ToggleButton(null, new ImageView(menuDeleteImage));
		deleteFieldToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		deleteFieldToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.delete")));
		deleteFieldToggleButtonToolbar.selectedProperty()
				.bindBidirectional(deleteFieldRadioMenuItem.selectedProperty());

		robbiMoveButtonToolbar = new Button(null, new ImageView(robbiMove));
		robbiMoveButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.move")));
		robbiMoveButtonToolbar.setOnAction(e -> territory.getRobbi().vor());

		robbiTurnLeftButtonToolbar = new Button(null, new ImageView(robbiTurnLeft));
		robbiTurnLeftButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.turnLeft")));
		robbiTurnLeftButtonToolbar.setOnAction(e -> territory.getRobbi().linksUm());

		robbiPutButtonToolbar = new Button(null, new ImageView(robbiPut));
		robbiPutButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.put")));
		robbiPutButtonToolbar.setOnAction(e -> territory.getRobbi().legeAb());

		robbiTakeButtonToolbar = new Button(null, new ImageView(robbiTake));
		robbiTakeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.take")));
		robbiTakeButtonToolbar.setOnAction(e -> territory.getRobbi().nehmeAuf());

		var simulationGroupToolbar = new ToggleGroup();
		startToggleButtonToolbar = new ToggleButton(null, new ImageView(menuStartImage));
		startToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);
		startToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.start")));

		pauseToggleButtonToolbar = new ToggleButton(null, new ImageView(menuPauseImage));
		pauseToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);
		pauseToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.pause")));

		stopToggleButtonToolbar = new ToggleButton(null, new ImageView(menuStopImage));
		stopToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);
		stopToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.stop")));

		speedSliderToolbar = new Slider(MIN_SPEED_VALUE, MAX_SPEED_VALUE, (MIN_SPEED_VALUE + MAX_SPEED_VALUE) / 2);
		speedSliderToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.speed")));

		toolbar = new ToolBar(newButtonToolbar, loadButtonToolbar, new Separator(), saveButtonToolbar,
				compileButtonToolbar, new Separator(), changeSizeButtonToolbar, placeRobbiToggleButtonToolbar,
				placeHollowToggleButtonToolbar, placePileOfScrapToggleButtonToolbar, placeStockpileToggleButtonToolbar,
				placeAccuToggleButtonToolbar, placeScrewToggleButtonToolbar, placeNutToggleButtonToolbar,
				deleteFieldToggleButtonToolbar, new Separator(), robbiMoveButtonToolbar, robbiTurnLeftButtonToolbar,
				robbiPutButtonToolbar, robbiTakeButtonToolbar, new Separator(), startToggleButtonToolbar,
				pauseToggleButtonToolbar, stopToggleButtonToolbar, new Separator(), speedSliderToolbar);
	}

	/**
	 * Erstelle eine ContentPane, in der ein Texteditor und die Bühne für die MPW zu
	 * finden sind.
	 */
	private void createContentPane() {
		logger.log(Level.INFO, "Create content panel");

		textArea = new TextArea(program.getEditorContent());
		textArea.setMinWidth(250);

		territoryPanel = new TerritoryPanel(this.territory, this.buttonState);

		territoryScrollPane = new ScrollPane(territoryPanel);
		territoryScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.viewportBoundsProperty()
				.addListener((observable, oldValue, newValue) -> territoryPanel.center(newValue));// credits: Dibo

		splitPane = new SplitPane(textArea, territoryScrollPane);
	}

	/**
	 * Erstelle ein label, welches dem Benutzer zusätzliches Feedback geben kann
	 */
	private void createMessageLabel() {
		logger.log(Level.INFO, "Create message label");
		messageLabel = new Label(Messages.getString("Messages.label.greeting"));
	}

	@Override
	public void onLanguageChanged() {
		// Add multilanguage support
		setTitle(Messages.getString("Main.title") + ": " + program.getName() + (program.isEdited() ? "*" : ""));

		newEditorMenuItem.setText(Messages.getString("Menu.editor.new"));
		saveEditorMenuItem.setText(Messages.getString("Menu.editor.save"));
		openEditorMenuItem.setText(Messages.getString("Menu.editor.open"));
		compileEditorMenuItem.setText(Messages.getString("Menu.editor.compile"));
		printEditorMenuItem.setText(Messages.getString("Menu.editor.print"));
		quitEditorMenuItem.setText(Messages.getString("Menu.editor.quit"));
		editorMenu.setText(Messages.getString("Menu.editor"));

		// territory Menu
		saveXMLTerritoryMenuItem.setText(Messages.getString("Menu.territory.save.xml"));
		saveJAXBTerritoryMenuItem.setText(Messages.getString("Menu.territory.save.jaxb"));
		saveSerialTerritoryMenuItem.setText(Messages.getString("Menu.territory.save.serialize"));
		saveTerritoryMenu.setText(Messages.getString("Menu.territory.save"));
		loadXMLTerritoryMenuItem.setText(Messages.getString("Menu.territory.load.xml"));
		loadJAXBTerritoryMenuItem.setText(Messages.getString("Menu.territory.load.jaxb"));
		loadSerialTerritoryMenuItem.setText(Messages.getString("Menu.territory.load.deserialize"));
		loadTerritoryMenu.setText(Messages.getString("Menu.territory.load"));
		saveAsPicMenuItem.setText(Messages.getString("Menu.territory.saveAsPic"));
		printTerritoryMenuItem.setText(Messages.getString("Menu.territory.print"));

		changeSizeTerritoryMenuItem.setText(Messages.getString("Menu.territory.size"));
		placeRobbiTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.robbi"));
		placeHollowTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.hollow"));
		placePileOfScrapTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.pileOfScrap"));
		placeStockpileTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.stockpile"));
		placeAccuTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.accu"));
		placeScrewTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.screw"));
		placeNutTerritoryRadioMenuItem.setText(Messages.getString("Menu.territory.place.nut"));
		deleteFieldRadioMenuItem.setText(Messages.getString("Menu.territory.delete"));
		territoryMenu.setText(Messages.getString("Menu.territory"));
		// robbi Menu
		itemPresentMenuItem.setText(Messages.getString("Menu.robbi.itemPresent"));
		isStockpileMenuItem.setText(Messages.getString("Menu.robbi.isStockpile"));
		hollowAheadMenuItem.setText(Messages.getString("Menu.robbi.hollowAhead"));
		pileOfScrapAheadMenuItem.setText(Messages.getString("Menu.robbi.pileOfScrapAhead"));
		isBagFullMenuItem.setText(Messages.getString("Menu.robbi.isBagFull"));
		pushPileOfScrapMenuItem.setText(Messages.getString("Menu.robbi.pushPileOfScrap"));
		moveMenuItem.setText(Messages.getString("Menu.robbi.move"));
		turnLeftMenuItem.setText(Messages.getString("Menu.robbi.turnLeft"));
		putMenuItem.setText(Messages.getString("Menu.robbi.put"));
		takeMenuItem.setText(Messages.getString("Menu.robbi.take"));
		robbiMenu.setText(Messages.getString("Menu.robbi"));
		// simulation Menu
		startMenuItem.setText(Messages.getString("Menu.simulation.start"));
		pauseMenuItem.setText(Messages.getString("Menu.simulation.pause"));
		stopMenuItem.setText(Messages.getString("Menu.simulation.stop"));
		simulationMenu.setText(Messages.getString("Menu.simulation"));
		// window Meun
		languageMenu.setText(Messages.getString("Menu.window.language"));
		englishLanguageMenuItem.setText(Messages.getString("Menu.window.language.english"));
		germanLanguageMenuItem.setText(Messages.getString("Menu.window.language.german"));
		changeCursorMenuItem.setText(Messages.getString("Menu.window.changeCursor"));
		windowMenu.setText(Messages.getString("Menu.window"));

		// Tool bar
		newButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.new")));
		loadButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.load")));
		saveButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.save")));
		compileButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.compile")));

		changeSizeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.size")));
		placeRobbiToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeRobbi")));
		placeHollowToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeHollow")));
		placePileOfScrapToggleButtonToolbar
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placePileOfScrap")));
		placeStockpileToggleButtonToolbar
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeStockpile")));
		placeAccuToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeAccu")));
		placeScrewToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeScrew")));
		placeNutToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeNut")));
		deleteFieldToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.delete")));

		robbiTurnLeftButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.turnLeft")));
		robbiMoveButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.move")));
		robbiPutButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.put")));
		robbiTakeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.take")));

		startToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.start")));
		pauseToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.pause")));
		stopToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.stop")));
		speedSliderToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.speed")));

		messageLabel.setText(Messages.getString("Messages.label.greeting"));
	}

}
