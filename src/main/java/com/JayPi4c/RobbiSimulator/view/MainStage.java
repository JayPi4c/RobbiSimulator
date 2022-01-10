package com.JayPi4c.RobbiSimulator.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Layout;

import com.JayPi4c.RobbiSimulator.controller.ButtonState;
import com.JayPi4c.RobbiSimulator.controller.MainStageController;
import com.JayPi4c.RobbiSimulator.controller.TerritorySaveController;
import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.simulation.SimulationController;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.Messages;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class is the mainStage of the application and holds all GUI elements
 * that are visible to the user.
 * 
 * @author Jonas Pohl
 *
 */
public class MainStage extends Stage {
	private static final Logger logger = LogManager.getLogger(MainStage.class);

	private Territory territory;

	private ButtonState buttonState;

	// controllers
	private MainStageController mainStageController;
	private SimulationController simController;
	private TerritorySaveController territorySaveController;

	/**
	 * Constant for the minimum value for the speed slider.
	 */
	public static final int MIN_SPEED_VALUE = 1;
	/**
	 * Constant for the maximum value for the speed slider.
	 */
	public static final int MAX_SPEED_VALUE = 100;

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
	private MenuItem infoMenuItem;
	private MenuItem libraryMenuItem;
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

	/**
	 * Constant Image for open icon.
	 */
	public static Image openImage;
	/**
	 * Constant Image for new icon.
	 */
	public static Image newImage;
	/**
	 * Constant Image for save icon.
	 */
	public static Image saveImage;
	/**
	 * Constant Image for compile icon.
	 */
	public static Image compileImage;
	/**
	 * Constant Image for print icon.
	 */
	public static Image printImage;
	/**
	 * Constant Image for terrain icon. (Used for changeSize button)
	 */
	public static Image terrainImage;

	/**
	 * Constant Image for robbi icon.
	 */
	public static Image menuRobbiImage;
	/**
	 * Constant Image for hollow icon.
	 */
	public static Image menuHollowImage;
	/**
	 * Constant Image for pileOfScrap icon.
	 */
	public static Image menuPileOfScrapImage;
	/**
	 * Constant Image for stockpile icon.
	 */
	public static Image menuStockpileImage;
	/**
	 * Constant Image for accu icon.
	 */
	public static Image menuAccuImage;
	/**
	 * Constant Image for screw icon.
	 */
	public static Image menuScrewImage;
	/**
	 * Constant Image for nut icon.
	 */
	public static Image menuNutImage;
	/**
	 * Constant Image for delete icon.
	 */
	public static Image menuDeleteImage;

	/**
	 * Constant Image for simulation start/resume icon.
	 */
	public static Image menuStartImage;
	/**
	 * Constant Image for simulation pause icon.
	 */
	public static Image menuPauseImage;
	/**
	 * Constant Image for simulation stop icon.
	 */
	public static Image menuStopImage;

	/**
	 * Constant Image for RobbiMove icon.
	 */
	public static Image robbiMove;
	/**
	 * Constant Image for RobbiTurnLeft icon.
	 */
	public static Image robbiTurnLeft;
	/**
	 * Constant Image for RobbiPut icon.
	 */
	public static Image robbiPut;
	/**
	 * Constant Image for RobbiTake icon.
	 */
	public static Image robbiTake;

	/**
	 * Constructor for the MainStage. It creates a mainStage for the given Program
	 * and loads and creates all needed Gui elements and controller. <br>
	 * This is the place, where the territory and the buttonState are created
	 * 
	 * @param program the program this mainStage is for
	 */
	public MainStage(Program program) {
		this.program = program;

		territory = new Territory();
		buttonState = new ButtonState();

		createMenuBar();
		createToolbar();
		createContentPane();
		createMessageLabel();

		VBox.setVgrow(splitPane, Priority.ALWAYS);
		var vBox = new VBox(menubar, toolbar, splitPane, messageLabel);

		scene = new Scene(vBox);
		setScene(scene);

		setMinHeight(200);
		setMinWidth(500);
		getIcons().add(menuRobbiImage);

		mainStageController = new MainStageController(this, buttonState);
		simController = new SimulationController(this, territory);
		territorySaveController = new TerritorySaveController(this);

		show();
		textArea.requestFocus();
		logger.info("Finished loading '{}'", program.getName());
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

	/**
	 * Loads all images that are presented in the toolbar. This method must be
	 * called before the first mainStage is created.
	 */
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
	 * Creates the editor-related menu-bar elements.
	 */
	private void createEditor() {
		logger.debug("Create editor entry for menubar");
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
	 * Creates the territory-related menu-bar elements.
	 */
	private void createTerritory() {
		logger.debug("Create territory entry for menubar");
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

		saveAsPNGMenuItem = new MenuItem(Messages.getString("Menu.territory.saveAsPic.png"));
		saveAsGifMenuItem = new MenuItem(Messages.getString("Menu.territory.saveAsPic.gif"));
		saveAsPicMenu = new Menu(Messages.getString("Menu.territory.saveAsPic"), null, saveAsPNGMenuItem,
				saveAsGifMenuItem);

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
				saveAsPicMenu, printTerritoryMenuItem, changeSizeTerritoryMenuItem, new SeparatorMenuItem(),
				placeRobbiTerritoryRadioMenuItem, placeHollowTerritoryRadioMenuItem,
				placePileOfScrapTerritoryRadioMenuItem, placeStockpileTerritoryRadioMenuItem,
				placeAccuTerritoryRadioMenuItem, placeScrewTerritoryRadioMenuItem, placeNutTerritoryRadioMenuItem,
				deleteFieldRadioMenuItem);
	}

	/**
	 * Creates the robbi-related menu-bar elements.
	 */
	private void createRobbi() {
		logger.debug("Create Robbi entry for menubar");
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
	 * Creates the simulation-related menu-bar elements.
	 */
	private void createSimulation() {
		logger.debug("Create simulation entry for menubar");

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

	/**
	 * Creates the window-related menu-bar elements.
	 */
	private void createWindowMenu() {
		englishLanguageMenuItem = new MenuItem(Messages.getString("Menu.window.language.english"));
		englishLanguageMenuItem.setOnAction(e -> Messages.changeBundle("lang.messages_en"));
		germanLanguageMenuItem = new MenuItem(Messages.getString("Menu.window.language.german"));
		germanLanguageMenuItem.setOnAction(e -> Messages.changeBundle("lang.messages_de"));

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

		infoMenuItem = new MenuItem(Messages.getString("Menu.window.info"));
		infoMenuItem.setOnAction(e -> {
			AlertHelper.showAlertAndWait(AlertType.INFORMATION, Messages.getString("Menu.window.info.content"), this,
					Modality.WINDOW_MODAL, Messages.getString("Menu.window.info.title"),
					Messages.getString("Menu.window.info.header"));
		});

		libraryMenuItem = new MenuItem(Messages.getString("Menu.window.libraries"));
		libraryMenuItem.setOnAction(e -> {
			String javaFxVersion = System.getProperty("javafx.version");
			String javaVersion = System.getProperty("java.version");
			String log4JVersion = Layout.class.getPackage().getImplementationVersion();
			AlertHelper.showAlertAndWait(AlertType.INFORMATION,
					String.format(Messages.getString("Menu.window.libraries.content"), javaVersion, javaFxVersion,
							log4JVersion),
					this, Modality.WINDOW_MODAL, Messages.getString("Menu.window.libraries.title"),
					Messages.getString("Menu.window.libraries.header"));
		});
		windowMenu = new Menu(Messages.getString("Menu.window"), null, languageMenu, changeCursorMenuItem,
				darkModeMenuItem, new SeparatorMenuItem(), infoMenuItem, libraryMenuItem);
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
		createWindowMenu();
		menubar = new MenuBar(editorMenu, territoryMenu, robbiMenu, simulationMenu, windowMenu);
	}

	/**
	 * Creates a toolbar for direct-access to the most important features.
	 */
	private void createToolbar() {
		logger.debug("Create toolbar");

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

		robbiTurnLeftButtonToolbar = new Button(null, new ImageView(robbiTurnLeft));
		robbiTurnLeftButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.turnLeft")));

		robbiPutButtonToolbar = new Button(null, new ImageView(robbiPut));
		robbiPutButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.put")));

		robbiTakeButtonToolbar = new Button(null, new ImageView(robbiTake));
		robbiTakeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.take")));

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

		speedSliderToolbar = new Slider(MIN_SPEED_VALUE, MAX_SPEED_VALUE, (MIN_SPEED_VALUE + MAX_SPEED_VALUE) / 2d);
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
	 * Creates the contentPane in which the text-editor and the territoryPanel take
	 * place.
	 */
	private void createContentPane() {
		logger.debug("Create content panel");

		textArea = new TextArea(program.getEditorContent());
		textArea.setMinWidth(250);

		territoryPanel = new TerritoryPanel(this.territory, this.buttonState, this);

		territoryScrollPane = new ScrollPane(territoryPanel);
		territoryScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.viewportBoundsProperty()
				.addListener((observable, oldValue, newValue) -> territoryPanel.center(newValue));// credits: Dibo

		splitPane = new SplitPane(textArea, territoryScrollPane);
	}

	/**
	 * Create a bottom label to give additional information.
	 */
	private void createMessageLabel() {
		logger.debug("Create message label");
		messageLabel = new Label(Messages.getString("Messages.label.greeting"));
	}

	// ==================================================================== //
	// ==================================================================== //
	// ============================== GETTER ============================== //
	// ==================================================================== //
	// ==================================================================== //

	/**
	 * Getter for the program
	 * 
	 * @return the program, this stage is for
	 */
	public Program getProgram() {
		return this.program;
	}

	/**
	 * Getter for the startMenuItem.
	 * 
	 * @return the startMenuItem for this stage
	 */
	public MenuItem getStartMenuItem() {
		return startMenuItem;
	}

	/**
	 * Getter for the pauseMenuItem.
	 * 
	 * @return the pauseMenuItem for this stage
	 */
	public MenuItem getPauseMenuItem() {
		return pauseMenuItem;
	}

	/**
	 * Getter for the stopMenuItem.
	 * 
	 * @return the stopMenuItem for this stage
	 */
	public MenuItem getStopMenuItem() {
		return stopMenuItem;
	}

	/**
	 * Getter for the startToggleButtonToolbar.
	 * 
	 * @return the startToggleButtonToolbar for this stage
	 */
	public ToggleButton getStartToggleButtonToolbar() {
		return startToggleButtonToolbar;
	}

	/**
	 * Getter for the pauseToggleButtonToolbar.
	 * 
	 * @return the pauseToggleButtonToolbar for this stage
	 */
	public ToggleButton getPauseToggleButtonToolbar() {
		return pauseToggleButtonToolbar;
	}

	/**
	 * Getter for the stopToggleButtonToolbar.
	 * 
	 * @return the stopToggleButtonToolbar for this stage
	 */
	public ToggleButton getStopToggleButtonToolbar() {
		return stopToggleButtonToolbar;
	}

	/**
	 * Getter for the speedSliderToolbar.
	 * 
	 * @return the speedSliderToolbar for this stage
	 */
	public Slider getSpeedSliderToolbar() {
		return speedSliderToolbar;
	}

	/**
	 * Getter for the territory.
	 * 
	 * @return the territory for this stage
	 */
	public Territory getTerritory() {
		return territory;
	}

	/**
	 * Getter for the saveEditorMenuItem.
	 * 
	 * @return the saveEditorMenuItem for this stage
	 */
	public MenuItem getSaveEditorMenuItem() {
		return saveEditorMenuItem;
	}

	/**
	 * Getter for the QuitEditorMenuItem.
	 * 
	 * @return the QuitEditorMenuItem for this stage
	 */
	public MenuItem getQuitEditorMenuItem() {
		return quitEditorMenuItem;
	}

	/**
	 * Getter for the placeRobbiTerritoryRadioMenuItem.
	 * 
	 * @return the placeRobbiTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlaceRobbiTerritoryRadioMenuItem() {
		return placeRobbiTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the placeHollowTerritoryRadioMenuItem.
	 * 
	 * @return the placeHollowTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlaceHollowTerritoryRadioMenuItem() {
		return placeHollowTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the placePileOfScrapTerritoryRadioMenuItem.
	 * 
	 * @return the placePileOFScrapTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlacePileOfScrapTerritoryRadioMenuItem() {
		return placePileOfScrapTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the placeStockpileTerritoryRadioMenuItem.
	 * 
	 * @return the placeStockpileTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlaceStockpileTerritoryRadioMenuItem() {
		return placeStockpileTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the placeAccuTerritoryRadioMenuItem.
	 * 
	 * @return the placeAccuTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlaceAccuTerritoryRadioMenuItem() {
		return placeAccuTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the placeScrewTerritoryRadioMenuItem.
	 * 
	 * @return the placeScrewTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlaceScrewTerritoryRadioMenuItem() {
		return placeScrewTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the placeNutTerritoryRadioMenuItem.
	 * 
	 * @return the placeNutTerritoryRadioMenuItem for this stage
	 */
	public RadioMenuItem getPlaceNutTerritoryRadioMenuItem() {
		return placeNutTerritoryRadioMenuItem;
	}

	/**
	 * Getter for the pileOfScrapAheadMenuItem.
	 * 
	 * @return the pileOfScrapAheadMenuItem for this stage
	 */
	public MenuItem getPileOfScrapAheadMenuItem() {
		return pileOfScrapAheadMenuItem;
	}

	/**
	 * Getter for the saveButtonToolbar.
	 * 
	 * @return the saveButtonToolbar for this stage
	 */
	public Button getSaveButtonToolbar() {
		return saveButtonToolbar;
	}

	/**
	 * Getter for the placeRobbiToggleButtonToolbar.
	 * 
	 * @return the placeRobbiToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlaceRobbiToggleButtonToolbar() {
		return placeRobbiToggleButtonToolbar;
	}

	/**
	 * Getter for the placeHollowToggleButtonToolbar.
	 * 
	 * @return the placeHollowToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlaceHollowToggleButtonToolbar() {
		return placeHollowToggleButtonToolbar;
	}

	/**
	 * Getter for the placePileOfScrapToggleButtonToolbar.
	 * 
	 * @return the placePileOfScrapToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlacePileOfScrapToggleButtonToolbar() {
		return placePileOfScrapToggleButtonToolbar;
	}

	/**
	 * Getter for the placeStockpileToggleButtonToolbar.
	 * 
	 * @return the placeStockpileToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlaceStockpileToggleButtonToolbar() {
		return placeStockpileToggleButtonToolbar;
	}

	/**
	 * Getter for the placeAccuToggleButtonToolbar.
	 * 
	 * @return the placeAccuToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlaceAccuToggleButtonToolbar() {
		return placeAccuToggleButtonToolbar;
	}

	/**
	 * Getter for the placeScrewToggleButtonToolbar.
	 * 
	 * @return the placeScrewToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlaceScrewToggleButtonToolbar() {
		return placeScrewToggleButtonToolbar;
	}

	/**
	 * Getter for the placeNutToggleButtonToolbar.
	 * 
	 * @return the placeNutToggleButtonToolbar for this stage
	 */
	public ToggleButton getPlaceNutToggleButtonToolbar() {
		return placeNutToggleButtonToolbar;
	}

	/**
	 * Getter for the MainStageScene.
	 * 
	 * @return the scene for this stage
	 */
	public Scene getMainStageScene() {
		return scene;
	}

	/**
	 * Getter for the deleteFieldRadioMenuItem.
	 * 
	 * @return the deleteFieldRadioMenuItem for this stage
	 */
	public RadioMenuItem getDeleteFieldRadioMenuItem() {
		return deleteFieldRadioMenuItem;
	}

	/**
	 * Getter for the deleteFieldToggleButtonToolbar.
	 * 
	 * @return the deleteFieldToggleButtonToolbar for this stage
	 */
	public ToggleButton getDeleteFieldToggleButtonToolbar() {
		return deleteFieldToggleButtonToolbar;
	}

	/**
	 * Getter for the itemPresentMenuItem.
	 * 
	 * @return the itemPresentMenuItem for this stage
	 */
	public MenuItem getItemPresentMenuItem() {
		return itemPresentMenuItem;
	}

	/**
	 * Getter for the isStockpileMenuItem.
	 * 
	 * @return the isStockpileMenuItem for this stage
	 */
	public MenuItem getIsStockpileMenuItem() {
		return isStockpileMenuItem;
	}

	/**
	 * Getter for the hollowAheadMenuItem.
	 * 
	 * @return the hollowAheadMenuItem for this stage
	 */
	public MenuItem getHollowAheadMenuItem() {
		return hollowAheadMenuItem;
	}

	/**
	 * Getter for the isBagFullMenuItem.
	 * 
	 * @return the isBagFullMenuItem for this stage
	 */
	public MenuItem getIsBagFullMenuItem() {
		return isBagFullMenuItem;
	}

	/**
	 * Getter for the pushPileOfScrapMenuItem.
	 * 
	 * @return the pushPileOfScrapMenuItem for this stage
	 */
	public MenuItem getPushPileOfScrapMenuItem() {
		return pushPileOfScrapMenuItem;
	}

	/**
	 * Getter for the moveMenuItem.
	 * 
	 * @return the moveMenuItem for this stage
	 */
	public MenuItem getMoveMenuItem() {
		return moveMenuItem;
	}

	/**
	 * Getter for the turnLeftMenuItem.
	 * 
	 * @return the turnLeftMenuItem for this stage
	 */
	public MenuItem getTurnLeftMenuItem() {
		return turnLeftMenuItem;
	}

	/**
	 * Getter for the putMenuItem.
	 * 
	 * @return the putMenuItem for this stage
	 */
	public MenuItem getPutMenuItem() {
		return putMenuItem;
	}

	/**
	 * Getter for the takeMenuItem.
	 * 
	 * @return the takeMenuItem for this stage
	 */
	public MenuItem getTakeMenuItem() {
		return takeMenuItem;
	}

	/**
	 * Getter for the textArea.
	 * 
	 * @return the textArea for this stage
	 */
	public TextArea getTextArea() {
		return textArea;
	}

	/**
	 * Getter for the territoryScrollPane.
	 * 
	 * @return the territoryScrollPane for this stage
	 */
	public ScrollPane getTerritoryScrollPane() {
		return territoryScrollPane;
	}

	/**
	 * Getter for the newEditorMenuItem.
	 * 
	 * @return the newEditorMenuItem for this stage
	 */
	public MenuItem getNewEditorMenuItem() {
		return newEditorMenuItem;
	}

	/**
	 * Getter for the openEditorMenuItem.
	 * 
	 * @return the openEditorMenuItem for this stage
	 */
	public MenuItem getOpenEditorMenuItem() {
		return openEditorMenuItem;
	}

	/**
	 * Getter for the compileEditorMenuItem.
	 * 
	 * @return the compileEditorMenuItem for this stage
	 */
	public MenuItem getCompileEditorMenuItem() {
		return compileEditorMenuItem;
	}

	/**
	 * Getter for the changeSizeTerritoryMenuItem.
	 * 
	 * @return the itemPresentMenuItem for this stage
	 */
	public MenuItem getChangeSizeTerritoryMenuItem() {
		return changeSizeTerritoryMenuItem;
	}

	/**
	 * Getter for the newButtonToolbar.
	 * 
	 * @return the newButtonToolbar for this stage
	 */
	public Button getNewButtonToolbar() {
		return newButtonToolbar;
	}

	/**
	 * Getter for the loadButtonToolbar.
	 * 
	 * @return the loadButtonToolbar for this stage
	 */
	public Button getLoadButtonToolbar() {
		return loadButtonToolbar;
	}

	/**
	 * Getter for the compileButtonToolbar.
	 * 
	 * @return the compileButtonToolbar for this stage
	 */
	public Button getCompileButtonToolbar() {
		return compileButtonToolbar;
	}

	/**
	 * Getter for the changeSizeButtonToolbar.
	 * 
	 * @return the changeSizeButtonToolbar for this stage
	 */
	public Button getChangeSizeButtonToolbar() {
		return changeSizeButtonToolbar;
	}

	/**
	 * Getter for the saveSerialTerritoryMenuItem.
	 * 
	 * @return the saveSerialTerritoryMenuItem for this stage
	 */
	public MenuItem getSaveSerialTerritoryMenuItem() {
		return saveSerialTerritoryMenuItem;
	}

	/**
	 * Getter for the loadSerialTerritoryMenuItem.
	 * 
	 * @return the loadSerialTerritoryMenuItem for this stage
	 */
	public MenuItem getLoadSerialTerritoryMenuItem() {
		return loadSerialTerritoryMenuItem;
	}

	/**
	 * Getter for the printEditorMenuItem.
	 * 
	 * @return the printEditorMenuItem for this stage
	 */
	public MenuItem getPrintEditorMenuItem() {
		return printEditorMenuItem;
	}

	/**
	 * Getter for the territoryPanel.
	 * 
	 * @return the territoryPanel for this stage
	 */
	public TerritoryPanel getTerritoryPanel() {
		return territoryPanel;
	}

	/**
	 * Getter for the printTerritoryMenuItem.
	 * 
	 * @return the printTerritoryMenuItem for this stage
	 */
	public MenuItem getPrintTerritoryMenuItem() {
		return printTerritoryMenuItem;
	}

	/**
	 * Getter for the saveAsPNGMenuItem.
	 * 
	 * @return the saveAsPNGMenuItem for this stage
	 */
	public MenuItem getSaveAsPNGMenuItem() {
		return saveAsPNGMenuItem;
	}

	/**
	 * Getter for the saveAsGifMenuItem.
	 * 
	 * @return the saveAsGifMenuItem for this stage
	 */
	public MenuItem getSaveAsGifMenuItem() {
		return saveAsGifMenuItem;
	}

	/**
	 * Getter for the robbiTurnLeftButtonToolbar.
	 * 
	 * @return the robbiTurnLeftButtonToolbar for this stage
	 */
	public Button getRobbiTurnLeftButtonToolbar() {
		return robbiTurnLeftButtonToolbar;
	}

	/**
	 * Getter for the robbiMoveButtonToolbar.
	 * 
	 * @return the robbiMoveButtonToolbar for this stage
	 */
	public Button getRobbiMoveButtonToolbar() {
		return robbiMoveButtonToolbar;
	}

	/**
	 * Getter for the robbiPutButtonToolbar.
	 * 
	 * @return the robbiPutButtonToolbar for this stage
	 */
	public Button getRobbiPutButtonToolbar() {
		return robbiPutButtonToolbar;
	}

	/**
	 * Getter for the robbiTakeButtonToolbar.
	 * 
	 * @return the robbiTakeButtonToolbar for this stage
	 */
	public Button getRobbiTakeButtonToolbar() {
		return robbiTakeButtonToolbar;
	}

	/**
	 * Getter for the territorySaveController.
	 * 
	 * @return the territorySaveController for this stage
	 */
	public TerritorySaveController getTerritorySaveController() {
		return territorySaveController;
	}

	/**
	 * Getter for the simulationController.
	 * 
	 * @return the simulationController for this stage
	 */
	public SimulationController getSimulationController() {
		return simController;
	}

	/**
	 * Getter for the editorMenu.
	 * 
	 * @return the editorMenu for this stage
	 */
	public Menu getEditorMenu() {
		return editorMenu;
	}

	/**
	 * Getter for the saveXMLTerritoryMenuItem.
	 * 
	 * @return the saveXMLTerritoryMenuItem for this stage
	 */
	public MenuItem getSaveXMLTerritoryMenuItem() {
		return saveXMLTerritoryMenuItem;
	}

	/**
	 * Getter for the saveJAXBTerritoryMenuItem.
	 * 
	 * @return the saveJAXBTerritoryMenuItem for this stage
	 */
	public MenuItem getSaveJAXBTerritoryMenuItem() {
		return saveJAXBTerritoryMenuItem;
	}

	/**
	 * Getter for the saveTerritoryMenu.
	 * 
	 * @return the saveTerritoryMenu for this stage
	 */
	public Menu getSaveTerritoryMenu() {
		return saveTerritoryMenu;
	}

	/**
	 * Getter for the loadXMLTerritoryMenuItem.
	 * 
	 * @return the loadXMLTerritoryMenuItem for this stage
	 */
	public MenuItem getLoadXMLTerritoryMenuItem() {
		return loadXMLTerritoryMenuItem;
	}

	/**
	 * Getter for the loadJAXBTerritoryMenuItem.
	 * 
	 * @return the loadJAXBTerritoryMenuItem for this stage
	 */
	public MenuItem getLoadJAXBTerritoryMenuItem() {
		return loadJAXBTerritoryMenuItem;
	}

	/**
	 * Getter for the loadTerritoryMenuItem.
	 * 
	 * @return the loadTerritoryMenuItem for this stage
	 */
	public Menu getLoadTerritoryMenu() {
		return loadTerritoryMenu;
	}

	/**
	 * Getter for the saveAsPicMenu.
	 * 
	 * @return the saveAsPicMenu for this stage
	 */
	public Menu getSaveAsPicMenu() {
		return saveAsPicMenu;
	}

	/**
	 * Getter for the territoryMenu.
	 * 
	 * @return the territoryMenufor this stage
	 */
	public Menu getTerritoryMenu() {
		return territoryMenu;
	}

	/**
	 * Getter for the robbiMenu.
	 * 
	 * @return the robbiMenu for this stage
	 */
	public Menu getRobbiMenu() {
		return robbiMenu;
	}

	/**
	 * Getter for the simulationMenu.
	 * 
	 * @return the simulationMenu for this stage
	 */
	public Menu getSimulationMenu() {
		return simulationMenu;
	}

	/**
	 * Getter for the languageMenu.
	 * 
	 * @return the languageMenu for this stage
	 */
	public Menu getLanguageMenu() {
		return languageMenu;
	}

	/**
	 * Getter for the englishLanguageMenuItem.
	 * 
	 * @return the englishLanguageMenuItem for this stage
	 */
	public MenuItem getEnglishLanguageMenuItem() {
		return englishLanguageMenuItem;
	}

	/**
	 * Getter for the germanLanguageMenuItem.
	 * 
	 * @return the germanLanguageMenuItem for this stage
	 */
	public MenuItem getGermanLanguageMenuItem() {
		return germanLanguageMenuItem;
	}

	/**
	 * Getter for the changeCursorMenuItem.
	 * 
	 * @return the changeCursorMenuItem for this stage
	 */
	public MenuItem getChangeCursorMenuItem() {
		return changeCursorMenuItem;
	}

	/**
	 * Getter for the windowMenu.
	 * 
	 * @return the windowMenu for this stage
	 */
	public Menu getWindowMenu() {
		return windowMenu;
	}

	/**
	 * Getter for the messageLabel.
	 * 
	 * @return the messageLabel for this stage
	 */
	public Label getMessageLabel() {
		return messageLabel;
	}

}
