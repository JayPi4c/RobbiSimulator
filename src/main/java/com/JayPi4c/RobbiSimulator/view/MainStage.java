package com.JayPi4c.RobbiSimulator.view;

import com.JayPi4c.RobbiSimulator.controller.ButtonState;
import com.JayPi4c.RobbiSimulator.controller.LanguageController;
import com.JayPi4c.RobbiSimulator.controller.MainStageController;
import com.JayPi4c.RobbiSimulator.controller.SnackbarController;
import com.JayPi4c.RobbiSimulator.controller.TerritorySaveController;
import com.JayPi4c.RobbiSimulator.controller.examples.ExamplesController;
import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.simulation.SimulationController;
import com.JayPi4c.RobbiSimulator.controller.tutor.StudentController;
import com.JayPi4c.RobbiSimulator.controller.tutor.TutorController;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXButton.ButtonType;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleNode;

import eu.mihosoft.monacofx.MonacoFX;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
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
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

/**
 * This class is the mainStage of the application and holds all GUI elements
 * that are visible to the user.
 * 
 * @author Jonas Pohl
 *
 */
@Slf4j
@Getter
public class MainStage extends Stage {

	private Territory territory;

	private ButtonState buttonState;

	// controllers
	private MainStageController mainStageController;
	private SimulationController simulationController;
	private TerritorySaveController territorySaveController;
	private ExamplesController examplesController;
	private StudentController studenController;
	private TutorController tutorController;
	private LanguageController languageController;
	private SnackbarController snackbarController;

	/**
	 * Constant for the minimum value for the speed slider.
	 */
	public static final int MIN_SPEED_VALUE = 1;
	/**
	 * Constant for the maximum value for the speed slider.
	 */
	public static final int MAX_SPEED_VALUE = 100;

	/**
	 * Constant for the minimum width of the stage.
	 */
	public static final int MIN_WIDTH = 500;
	/**
	 * Constant for the minimum height of the stage.
	 */
	public static final int MIN_HEIGHT = 200;
	/**
	 * Constant for the default width of the stage. <br>
	 * Currently not in use.
	 */
	public static final int WIDTH = 1200;
	/**
	 * Constant for the default height of the stage.
	 */
	public static final int HEIGHT = 450;

	private Program program;

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

	private ToolBar toolbar;

	// Content Pane
	private MonacoFX textArea;
	private ScrollPane territoryScrollPane;
	private TerritoryPanel territoryPanel;
	private SplitPane splitPane;

	private Scene mainStageScene;

	/**
	 * Constant Image for open icon.
	 */
	public static final Image openImage;
	/**
	 * Constant Image for new icon.
	 */
	public static final Image newImage;
	/**
	 * Constant Image for save icon.
	 */
	public static final Image saveImage;
	/**
	 * Constant Image for compile icon.
	 */
	public static final Image compileImage;
	/**
	 * Constant Image for print icon.
	 */
	public static final Image printImage;
	/**
	 * Constant Image for terrain icon. (Used for changeSize button)
	 */
	public static final Image terrainImage;

	/**
	 * Constant Image for robbi icon.
	 */
	public static final Image menuRobbiImage;
	/**
	 * Constant Image for hollow icon.
	 */
	public static final Image menuHollowImage;
	/**
	 * Constant Image for pileOfScrap icon.
	 */
	public static final Image menuPileOfScrapImage;
	/**
	 * Constant Image for stockpile icon.
	 */
	public static final Image menuStockpileImage;
	/**
	 * Constant Image for accu icon.
	 */
	public static final Image menuAccuImage;
	/**
	 * Constant Image for screw icon.
	 */
	public static final Image menuScrewImage;
	/**
	 * Constant Image for nut icon.
	 */
	public static final Image menuNutImage;
	/**
	 * Constant Image for delete icon.
	 */
	public static final Image menuDeleteImage;

	/**
	 * Constant Image for reset icon.
	 */
	public static final Image resetImage;
	/**
	 * Constant Image for simulation start/resume icon.
	 */
	public static final Image menuStartImage;
	/**
	 * Constant Image for simulation pause icon.
	 */
	public static final Image menuPauseImage;
	/**
	 * Constant Image for simulation stop icon.
	 */
	public static final Image menuStopImage;

	/**
	 * Constant Image for RobbiMove icon.
	 */
	public static final Image robbiMove;
	/**
	 * Constant Image for RobbiTurnLeft icon.
	 */
	public static final Image robbiTurnLeft;
	/**
	 * Constant Image for RobbiPut icon.
	 */
	public static final Image robbiPut;
	/**
	 * Constant Image for RobbiTake icon.
	 */
	public static final Image robbiTake;

	/**
	 * loading images
	 */
	static {
		logger.debug("Loading stage images");

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

		resetImage = new Image("img/reset24.png");
		menuStartImage = new Image("img/Play24.gif");
		menuPauseImage = new Image("img/Pause24.gif");
		menuStopImage = new Image("img/Stop24.gif");

		robbiMove = new Image("img/RobbiMove24.png");
		robbiTurnLeft = new Image("img/RobbiLeft24.png");
		robbiPut = new Image("img/RobbiPut24.png");
		robbiTake = new Image("img/RobbiTake24.png");
	}

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

		VBox.setVgrow(splitPane, Priority.ALWAYS);
		var vBox = new VBox(menubar, toolbar, splitPane);

		mainStageScene = new Scene(vBox);
		setScene(mainStageScene);

		setMinWidth(MIN_WIDTH);
		setMinHeight(MIN_HEIGHT);
		setHeight(HEIGHT);
		getIcons().add(menuRobbiImage);

		snackbarController = new SnackbarController(vBox);
		mainStageController = new MainStageController(this, buttonState);
		simulationController = new SimulationController(this, territory);
		territorySaveController = new TerritorySaveController(this);
		examplesController = new ExamplesController(this);
		languageController = new LanguageController(this);

		if (PropertiesLoader.isTutor())
			tutorController = new TutorController(this);
		else
			studenController = new StudentController(this);

		show();
		textArea.requestFocus();
		logger.info("Finished loading '{}'", program.getName());
	}

	/**
	 * Creates the editor-related menu-bar elements.
	 */
	private void createEditor() {
		logger.debug("Create editor entry for menubar");
		newEditorMenuItem = new MenuItem(i18n("Menu.editor.new"));
		newEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newEditorMenuItem.setMnemonicParsing(true);
		newEditorMenuItem.setGraphic(new ImageView(newImage));
		saveEditorMenuItem = new MenuItem(i18n("Menu.editor.save"));
		saveEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		saveEditorMenuItem.setGraphic(new ImageView(saveImage));
		openEditorMenuItem = new MenuItem(i18n("Menu.editor.open"));
		openEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openEditorMenuItem.setGraphic(new ImageView(openImage));

		formatSourceCodeMenuItem = new MenuItem(i18n("Menu.editor.format"));
		formatSourceCodeMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.T,KeyCombination.CONTROL_DOWN)); // T for tidy
		
		compileEditorMenuItem = new MenuItem(i18n("Menu.editor.compile"));
		compileEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
		compileEditorMenuItem.setGraphic(new ImageView(compileImage));

		printEditorMenuItem = new MenuItem(i18n("Menu.editor.print"));
		printEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		printEditorMenuItem.setGraphic(new ImageView(printImage));
		quitEditorMenuItem = new MenuItem(i18n("Menu.editor.quit"));
		quitEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		editorMenu = new Menu(i18n("Menu.editor"), null, newEditorMenuItem, openEditorMenuItem,
				saveEditorMenuItem, new SeparatorMenuItem(), formatSourceCodeMenuItem, compileEditorMenuItem,
				printEditorMenuItem, new SeparatorMenuItem(), quitEditorMenuItem);
		editorMenu.setMnemonicParsing(true);
	}

	/**
	 * Creates the territory-related menu-bar elements.
	 */
	private void createTerritory() {
		logger.debug("Create territory entry for menubar");
		saveXMLTerritoryMenuItem = new MenuItem(i18n("Menu.territory.save.xml"));
		saveJAXBTerritoryMenuItem = new MenuItem(i18n("Menu.territory.save.jaxb"));
		saveSerialTerritoryMenuItem = new MenuItem(i18n("Menu.territory.save.serialize"));

		saveTerritoryMenu = new Menu(i18n("Menu.territory.save"), null, saveXMLTerritoryMenuItem,
				saveJAXBTerritoryMenuItem, saveSerialTerritoryMenuItem);
		loadXMLTerritoryMenuItem = new MenuItem(i18n("Menu.territory.load.xml"));
		loadJAXBTerritoryMenuItem = new MenuItem(i18n("Menu.territory.load.jaxb"));
		loadSerialTerritoryMenuItem = new MenuItem(i18n("Menu.territory.load.deserialize"));
		loadTerritoryMenu = new Menu(i18n("Menu.territory.load"), null, loadXMLTerritoryMenuItem,
				loadJAXBTerritoryMenuItem, loadSerialTerritoryMenuItem);

		saveAsPNGMenuItem = new MenuItem(i18n("Menu.territory.saveAsPic.png"));
		saveAsGifMenuItem = new MenuItem(i18n("Menu.territory.saveAsPic.gif"));
		saveAsPicMenu = new Menu(i18n("Menu.territory.saveAsPic"), null, saveAsPNGMenuItem,
				saveAsGifMenuItem);

		printTerritoryMenuItem = new MenuItem(i18n("Menu.territory.print"));
		changeSizeTerritoryMenuItem = new MenuItem(i18n("Menu.territory.size"));

		placeGroupTerritoryMenu = new ToggleGroup();

		placeRobbiTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.robbi"));
		placeRobbiTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeHollowTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.hollow"));
		placeHollowTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placePileOfScrapTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.pileOfScrap"));
		placePileOfScrapTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeStockpileTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.stockpile"));
		placeStockpileTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeAccuTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.accu"));
		placeAccuTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeScrewTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.screw"));
		placeScrewTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		placeNutTerritoryRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.place.nut"));
		placeNutTerritoryRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		deleteFieldRadioMenuItem = new RadioMenuItem(i18n("Menu.territory.delete"));
		deleteFieldRadioMenuItem.setToggleGroup(placeGroupTerritoryMenu);

		territoryMenu = new Menu(i18n("Menu.territory"), null, saveTerritoryMenu, loadTerritoryMenu,
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
		itemPresentMenuItem = new MenuItem(i18n("Menu.robbi.itemPresent"));
		isStockpileMenuItem = new MenuItem(i18n("Menu.robbi.isStockpile"));
		hollowAheadMenuItem = new MenuItem(i18n("Menu.robbi.hollowAhead"));
		pileOfScrapAheadMenuItem = new MenuItem(i18n("Menu.robbi.pileOfScrapAhead"));
		isBagFullMenuItem = new MenuItem(i18n("Menu.robbi.isBagFull"));
		pushPileOfScrapMenuItem = new MenuItem(i18n("Menu.robbi.pushPileOfScrap"));
		moveMenuItem = new MenuItem(i18n("Menu.robbi.move"));
		moveMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		turnLeftMenuItem = new MenuItem(i18n("Menu.robbi.turnLeft"));
		turnLeftMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));

		putMenuItem = new MenuItem(i18n("Menu.robbi.put"));
		putMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		takeMenuItem = new MenuItem(i18n("Menu.robbi.take"));
		takeMenuItem.setAccelerator(
				new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
		robbiMenu = new Menu(i18n("Menu.robbi"), null, moveMenuItem, turnLeftMenuItem, putMenuItem,
				takeMenuItem, pushPileOfScrapMenuItem, itemPresentMenuItem, isStockpileMenuItem, hollowAheadMenuItem,
				pileOfScrapAheadMenuItem, isBagFullMenuItem);
		robbiMenu.setMnemonicParsing(true);
	}

	/**
	 * Creates the simulation-related menu-bar elements.
	 */
	private void createSimulation() {
		logger.debug("Create simulation entry for menubar");

		resetMenuItem = new MenuItem(i18n("Menu.simulation.reset"));
		resetMenuItem.setGraphic(new ImageView(resetImage));
		startMenuItem = new MenuItem(i18n("Menu.simulation.start"));
		startMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11, KeyCombination.CONTROL_DOWN));
		startMenuItem.setGraphic(new ImageView(menuStartImage));
		pauseMenuItem = new MenuItem(i18n("Menu.simulation.pause"));
		pauseMenuItem.setGraphic(new ImageView(menuPauseImage));
		stopMenuItem = new MenuItem(i18n("Menu.simulation.stop"));
		stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12, KeyCombination.CONTROL_DOWN));
		stopMenuItem.setGraphic(new ImageView(menuStopImage));
		simulationMenu = new Menu(i18n("Menu.simulation"), null, resetMenuItem, startMenuItem, pauseMenuItem,
				stopMenuItem);
		simulationMenu.setMnemonicParsing(true);
	}

	/**
	 * Creates the examples-related menu-bar elements
	 */
	private void createExamplesMenu() {
		saveExampleMenuItem = new MenuItem(i18n("Menu.examples.save"));
		loadExampleMenuItem = new MenuItem(i18n("Menu.examples.load"));
		examplesMenu = new Menu(i18n("Menu.examples"), null, saveExampleMenuItem, loadExampleMenuItem);
	}

	/**
	 * Creates the tutor-related menu-bar elements
	 */
	private void createTutorMenu() {
		if (PropertiesLoader.isTutor()) {
			loadRequestMenuItem = new MenuItem(i18n("Menu.tutor.loadRequest"));
			saveAnswerMenuItem = new MenuItem(i18n("Menu.tutor.saveAnswer"));

			tutorMenu = new Menu(i18n("Menu.tutor"), null, loadRequestMenuItem, saveAnswerMenuItem);
		} else {
			sendRequestMenuItem = new MenuItem(i18n("Menu.tutor.sendRequest"));
			receiveAnswerMenuItem = new MenuItem(i18n("Menu.tutor.receiveAnswer"));

			tutorMenu = new Menu(i18n("Menu.tutor"), null, sendRequestMenuItem, receiveAnswerMenuItem);
		}
	}

	/**
	 * Creates the window-related menu-bar elements.
	 */
	private void createWindowMenu() {
		englishLanguageMenuItem = new MenuItem(i18n("Menu.window.language.english"));
		germanLanguageMenuItem = new MenuItem(i18n("Menu.window.language.german"));
		languageMenu = new Menu(i18n("Menu.window.language"), null, englishLanguageMenuItem,
				germanLanguageMenuItem);

		changeCursorMenuItem = new CheckMenuItem(i18n("Menu.window.changeCursor"));
		// https://stackoverflow.com/a/49159612/13670629
		darkModeMenuItem = new CheckMenuItem(i18n("Menu.window.darkmode"));
		enableSoundsMenuItem = new CheckMenuItem(i18n("Menu.window.enableSounds"));

		infoMenuItem = new MenuItem(i18n("Menu.window.info"));
		libraryMenuItem = new MenuItem(i18n("Menu.window.libraries"));

		windowMenu = new Menu(i18n("Menu.window"), null, languageMenu, changeCursorMenuItem, darkModeMenuItem,
				enableSoundsMenuItem, new SeparatorMenuItem(), infoMenuItem, libraryMenuItem);
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
		menubar = new MenuBar(editorMenu, territoryMenu, robbiMenu, simulationMenu, examplesMenu, tutorMenu,
				windowMenu);
	}

	/**
	 * Creates a toolbar for direct-access to the most important features.
	 */
	private void createToolbar() {
		logger.debug("Create toolbar");

		newButtonToolbar = new JFXButton(null, new ImageView(newImage));
		((JFXButton) newButtonToolbar).setButtonType(ButtonType.RAISED);

		loadButtonToolbar = new JFXButton(null, new ImageView(openImage));

		saveButtonToolbar = new JFXButton(null, new ImageView(saveImage));

		compileButtonToolbar = new JFXButton(null, new ImageView(compileImage));

		changeSizeButtonToolbar = new JFXButton(null, new ImageView(terrainImage));

		var placeGroupToolbar = new ToggleGroup();

		placeRobbiToggleButtonToolbar = new JFXToggleNode();
		placeRobbiToggleButtonToolbar.setGraphic(new ImageView(menuRobbiImage));
		placeRobbiToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeRobbiToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeRobbiTerritoryRadioMenuItem.selectedProperty());

		placeHollowToggleButtonToolbar = new JFXToggleNode();
		placeHollowToggleButtonToolbar.setGraphic(new ImageView(menuHollowImage));
		placeHollowToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeHollowToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeHollowTerritoryRadioMenuItem.selectedProperty());

		placePileOfScrapToggleButtonToolbar = new JFXToggleNode();
		placePileOfScrapToggleButtonToolbar.setGraphic(new ImageView(menuPileOfScrapImage));
		placePileOfScrapToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placePileOfScrapToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placePileOfScrapTerritoryRadioMenuItem.selectedProperty());

		placeStockpileToggleButtonToolbar = new JFXToggleNode();
		placeStockpileToggleButtonToolbar.setGraphic(new ImageView(menuStockpileImage));
		placeStockpileToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeStockpileToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeStockpileTerritoryRadioMenuItem.selectedProperty());

		placeAccuToggleButtonToolbar = new JFXToggleNode();
		placeAccuToggleButtonToolbar.setGraphic(new ImageView(menuAccuImage));
		placeAccuToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeAccuToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeAccuTerritoryRadioMenuItem.selectedProperty());

		placeScrewToggleButtonToolbar = new JFXToggleNode();
		placeScrewToggleButtonToolbar.setGraphic(new ImageView(menuScrewImage));
		placeScrewToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeScrewToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeScrewTerritoryRadioMenuItem.selectedProperty());

		placeNutToggleButtonToolbar = new JFXToggleNode();
		placeNutToggleButtonToolbar.setGraphic(new ImageView(menuNutImage));
		placeNutToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeNutToggleButtonToolbar.selectedProperty()
				.bindBidirectional(placeNutTerritoryRadioMenuItem.selectedProperty());

		deleteFieldToggleButtonToolbar = new JFXToggleNode();
		deleteFieldToggleButtonToolbar.setGraphic(new ImageView(menuDeleteImage));
		deleteFieldToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		deleteFieldToggleButtonToolbar.selectedProperty()
				.bindBidirectional(deleteFieldRadioMenuItem.selectedProperty());

		robbiMoveButtonToolbar = new JFXButton(null, new ImageView(robbiMove));

		robbiTurnLeftButtonToolbar = new JFXButton(null, new ImageView(robbiTurnLeft));

		robbiPutButtonToolbar = new JFXButton(null, new ImageView(robbiPut));

		robbiTakeButtonToolbar = new JFXButton(null, new ImageView(robbiTake));

		resetButtonToolbar = new JFXButton(null, new ImageView(resetImage));

		var simulationGroupToolbar = new ToggleGroup();
		startToggleButtonToolbar = new JFXToggleNode();
		startToggleButtonToolbar.setGraphic(new ImageView(menuStartImage));
		startToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

		pauseToggleButtonToolbar = new JFXToggleNode();
		pauseToggleButtonToolbar.setGraphic(new ImageView(menuPauseImage));
		pauseToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

		stopToggleButtonToolbar = new JFXToggleNode();
		stopToggleButtonToolbar.setGraphic(new ImageView(menuStopImage));
		stopToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);

		speedSliderToolbar = new JFXSlider(MIN_SPEED_VALUE, MAX_SPEED_VALUE, (MIN_SPEED_VALUE + MAX_SPEED_VALUE) / 2d);

		toolbar = new ToolBar(newButtonToolbar, loadButtonToolbar, new Separator(), saveButtonToolbar,
				compileButtonToolbar, new Separator(), changeSizeButtonToolbar, placeRobbiToggleButtonToolbar,
				placeHollowToggleButtonToolbar, placePileOfScrapToggleButtonToolbar, placeStockpileToggleButtonToolbar,
				placeAccuToggleButtonToolbar, placeScrewToggleButtonToolbar, placeNutToggleButtonToolbar,
				deleteFieldToggleButtonToolbar, new Separator(), robbiMoveButtonToolbar, robbiTurnLeftButtonToolbar,
				robbiPutButtonToolbar, robbiTakeButtonToolbar, new Separator(), resetButtonToolbar,
				startToggleButtonToolbar, pauseToggleButtonToolbar, stopToggleButtonToolbar, new Separator(),
				speedSliderToolbar);
	}

	/**
	 * Creates the contentPane in which the text-editor and the territoryPanel take
	 * place.
	 */
	private void createContentPane() {
		logger.debug("Create content panel");

		textArea = new MonacoFX();
		textArea.getEditor().getDocument().setText(program.getEditorContent());
		textArea.getEditor().setCurrentLanguage("java");
		textArea.setMinWidth(250);

		territoryPanel = new TerritoryPanel(this.territory, this.buttonState, this);

		territoryScrollPane = new ScrollPane(territoryPanel);
		territoryScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.viewportBoundsProperty()
				.addListener((observable, oldValue, newValue) -> territoryPanel.center(newValue));// credits: Dibo

		splitPane = new SplitPane(textArea, territoryScrollPane);
	}

}
