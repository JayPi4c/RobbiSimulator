package com.JayPi4c.RobbiSimulator;

import com.JayPi4c.RobbiSimulator.model.Accu;
import com.JayPi4c.RobbiSimulator.model.Nut;
import com.JayPi4c.RobbiSimulator.model.Screw;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.ILanguageChangeListener;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.TerritoryPanel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

/**
 * 
 * Hauptklasse des Robbi Simulators.<br>
 * Javaversion: 17 <br>
 * JavaFX: 17<br>
 * last modified 08.11.2021
 * 
 * @author Jonas Pohl
 *
 */
public class App extends Application implements ILanguageChangeListener {

	private Stage primaryStage;

	private Territory territory;

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
	private MenuItem loadJAXBTerrotoryMenuItem;
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
	 * Erstelle den Editoreintrag für die Menubar
	 */
	private void createEditor() {
		newEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.new"));
		newEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
		newEditorMenuItem.setMnemonicParsing(true);
		newEditorMenuItem.setGraphic(new ImageView(new Image("img/New24.gif")));
		saveEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.save"));
		saveEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
		saveEditorMenuItem.setGraphic(new ImageView(new Image("img/Save24.gif")));
		openEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.open"));
		openEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
		openEditorMenuItem.setGraphic(new ImageView(new Image("img/Open24.gif")));
		compileEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.compile"));
		compileEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.K, KeyCombination.CONTROL_DOWN));
		compileEditorMenuItem.setGraphic(new ImageView(new Image("img/Compile24.gif")));
		printEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.print"));
		printEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
		printEditorMenuItem.setGraphic(new ImageView(new Image("img/Print24.gif")));
		quitEditorMenuItem = new MenuItem(Messages.getString("Menu.editor.quit"));
		// quit.setOnAction(e -> {
		// System.out.println("quit");
		// Platform.exit();
		// System.exit(0);
		// });
		quitEditorMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		editorMenu = new Menu(Messages.getString("Menu.editor"), null, newEditorMenuItem, openEditorMenuItem,
				saveEditorMenuItem, new SeparatorMenuItem(), compileEditorMenuItem, printEditorMenuItem,
				new SeparatorMenuItem(), quitEditorMenuItem);
		editorMenu.setMnemonicParsing(true);
	}

	/**
	 * Erstelle den Territoriumseintrag für die Menubar
	 */
	private void createTerritory() {
		saveXMLTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.save.xml"));
		saveJAXBTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.save.jaxb"));
		saveSerialTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.save.serialize"));

		saveTerritoryMenu = new Menu(Messages.getString("Menu.territory.save"), null, saveXMLTerritoryMenuItem,
				saveJAXBTerritoryMenuItem, saveSerialTerritoryMenuItem);
		loadXMLTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.load.xml"));
		loadJAXBTerrotoryMenuItem = new MenuItem(Messages.getString("Menu.territory.load.jaxb"));
		loadSerialTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.load.deserialize"));
		loadTerritoryMenu = new Menu(Messages.getString("Menu.territory.load"), null, loadXMLTerritoryMenuItem,
				loadJAXBTerrotoryMenuItem, loadSerialTerritoryMenuItem);
		saveAsPicMenuItem = new MenuItem(Messages.getString("Menu.territory.saveAsPic"));
		printTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.print"));
		changeSizeTerritoryMenuItem = new MenuItem(Messages.getString("Menu.territory.size"));
		var placeGroupTerritoryMenu = new ToggleGroup();
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
		startMenuItem = new MenuItem(Messages.getString("Menu.simulation.start"));
		startMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F11, KeyCombination.CONTROL_DOWN));
		startMenuItem.setGraphic(new ImageView(new Image("img/Play24.gif")));
		pauseMenuItem = new MenuItem(Messages.getString("Menu.simulation.pause"));
		pauseMenuItem.setGraphic(new ImageView(new Image("img/Pause24.gif")));
		stopMenuItem = new MenuItem(Messages.getString("Menu.simulation.stop"));
		stopMenuItem.setAccelerator(new KeyCodeCombination(KeyCode.F12, KeyCombination.CONTROL_DOWN));
		stopMenuItem.setGraphic(new ImageView(new Image("img/Stop24.gif")));
		simulationMenu = new Menu(Messages.getString("Menu.simulation"), null, startMenuItem, pauseMenuItem,
				stopMenuItem);
		simulationMenu.setMnemonicParsing(true);
	}

	/**
	 * Erstelle die gesamte Menubar mit allen Einträgen
	 */
	private void createMenuBar() {
		createEditor();
		createTerritory();
		createRobbi();
		createSimulation();
		menubar = new MenuBar(editorMenu, territoryMenu, robbiMenu, simulationMenu);
	}

	/**
	 * Erstelle die Toolbar mit den wichtigsten Funktionalitäten als direkt zu
	 * erreichende Buttons
	 */
	private void createToolbar() {
		newButtonToolbar = new Button(null, new ImageView(new Image("img/New24.gif")));
		newButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.new")));

		loadButtonToolbar = new Button(null, new ImageView(new Image("img/Open24.gif")));
		loadButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.load")));

		saveButtonToolbar = new Button(null, new ImageView(new Image("img/Save24.gif")));
		saveButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.save")));

		compileButtonToolbar = new Button(null, new ImageView(new Image("img/Compile24.gif")));
		compileButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.control.compile")));

		changeSizeButtonToolbar = new Button(null, new ImageView(new Image("img/Terrain24.gif")));
		changeSizeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.size")));

		var placeGroupToolbar = new ToggleGroup();
		placeRobbiToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Robbi24.png")));
		placeRobbiToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeRobbi")));
		placeRobbiToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		/*
		 * change cursor, when placing object
		 * 
		 * placeRobbiToggleButtonToolbar.setOnAction(e -> { if
		 * (placeRobbiToggleButtonToolbar.isSelected()) scene.setCursor(new
		 * ImageCursor(new Image("img/0Robbi32.png"))); else
		 * scene.setCursor(Cursor.DEFAULT); });
		 */

		placeHollowToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Hollow24.png")));
		placeHollowToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeHollowToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeHollow")));

		placePileOfScrapToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/PileOfScrap24.png")));
		placePileOfScrapToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placePileOfScrapToggleButtonToolbar
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placePileOfScrap")));

		placeStockpileToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Stockpile24.png")));
		placeStockpileToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeStockpileToggleButtonToolbar
				.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeStockpile")));

		placeAccuToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Accu24.png")));
		placeAccuToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeAccuToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeAccu")));

		placeScrewToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Screw24.png")));
		placeScrewToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeScrewToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeScrew")));

		placeNutToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Nut24.png")));
		placeNutToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		placeNutToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.placeNut")));

		deleteFieldToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Delete24.gif")));
		deleteFieldToggleButtonToolbar.setToggleGroup(placeGroupToolbar);
		deleteFieldToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.territory.delete")));

		robbiMoveButtonToolbar = new Button(null, new ImageView(new Image("img/RobbiMove24.png")));
		robbiMoveButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.move")));

		robbiTurnLeftButtonToolbar = new Button(null, new ImageView(new Image("img/RobbiLeft24.png")));
		robbiTurnLeftButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.turnLeft")));

		robbiPutButtonToolbar = new Button(null, new ImageView(new Image("img/RobbiPut24.png")));
		robbiPutButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.put")));

		robbiTakeButtonToolbar = new Button(null, new ImageView(new Image("img/RobbiTake24.png")));
		robbiTakeButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.robbi.take")));

		var simulationGroupToolbar = new ToggleGroup();
		startToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Play24.gif")));
		startToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);
		startToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.start")));

		pauseToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Pause24.gif")));
		pauseToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);
		pauseToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.pause")));

		stopToggleButtonToolbar = new ToggleButton(null, new ImageView(new Image("img/Stop24.gif")));
		stopToggleButtonToolbar.setToggleGroup(simulationGroupToolbar);
		stopToggleButtonToolbar.setTooltip(new Tooltip(Messages.getString("Toolbar.action.stop")));

		speedSliderToolbar = new Slider(0, 100, 50);
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
		textArea = new TextArea("""
				void main(){
					// place your code here.
				}
				""");
		textArea.setMinWidth(250);

		territoryPanel = new TerritoryPanel(this.territory);

		territoryScrollPane = new ScrollPane(territoryPanel);
		territoryScrollPane.setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
		territoryScrollPane.viewportBoundsProperty().addListener((observable, oldValue, newValue) -> {
			territoryPanel.center(newValue);
		});
		splitPane = new SplitPane(textArea, territoryScrollPane);
	}

	/**
	 * Erstelle ein label, welches dem Benutzer zusätzliches Feedback geben kann
	 */
	private void createMessageLabel() {
		messageLabel = new Label(Messages.getString("Messages.label.greeting"));
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;

		createMenuBar();
		createToolbar();
		createContentPane();
		createMessageLabel();

		// register to be notified, if the language has changed
		Messages.registerListener(this);

		VBox.setVgrow(splitPane, Priority.ALWAYS);
		var vBox = new VBox(menubar, toolbar, splitPane, messageLabel);

		scene = new Scene(vBox);
		primaryStage.setMinHeight(200);
		primaryStage.setMinWidth(500);

		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("img/Robbi24.png"));
		primaryStage.setTitle(Messages.getString("Main.title"));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void init() {
		System.out.println("init");

		territory = new Territory();
		// TODO remove debug init commands when
		territory.placeAccu(3, 3);
		territory.placeHollow(1, 0);
		territory.placePileOfScrap(0, 2);
		territory.placeHollow(0, 3);

		territory.placeStockpile(2, 1);
		territory.getTile(2, 1).setItem(new Accu());
		territory.getTile(2, 1).setItem(new Nut());
		territory.getTile(2, 1).setItem(new Screw());
		territory.getRobbi().linksUm();

		territory.changeSize(5, 7);
		// territory.changeSize(25, 35);
	}

	@Override
	public void stop() {
		// do some final serialization
		System.out.println("quitting");
	}

	@Override
	public void onLanguageChanged() {
		// Add multilanguage support
		primaryStage.setTitle(Messages.getString("Main.title"));
	}

}