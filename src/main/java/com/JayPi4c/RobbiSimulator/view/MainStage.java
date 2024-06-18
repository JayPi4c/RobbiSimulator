package com.JayPi4c.RobbiSimulator.view;

import com.JayPi4c.RobbiSimulator.controller.*;
import com.JayPi4c.RobbiSimulator.controller.examples.ExamplesController;
import com.JayPi4c.RobbiSimulator.controller.program.Program;
import com.JayPi4c.RobbiSimulator.controller.simulation.SimulationController;
import com.JayPi4c.RobbiSimulator.controller.tutor.StudentController;
import com.JayPi4c.RobbiSimulator.controller.tutor.TutorController;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.PropertiesLoader;
import eu.mihosoft.monacofx.MonacoFX;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class is the mainStage of the application and holds all GUI elements
 * that are visible to the user.
 *
 * @author Jonas Pohl
 */
@Slf4j
@Getter
public class MainStage extends Stage {

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
        newImage = new Image(String.valueOf(MainStage.class.getResource("/img/New24.gif")));
        saveImage = new Image(String.valueOf(MainStage.class.getResource("/img/Save24.gif")));
        openImage = new Image(String.valueOf(MainStage.class.getResource("/img/Open24.gif")));
        compileImage = new Image(String.valueOf(MainStage.class.getResource("/img/Compile24.gif")));
        printImage = new Image(String.valueOf(MainStage.class.getResource("/img/Print24.gif")));
        terrainImage = new Image(String.valueOf(MainStage.class.getResource("/img/Terrain24.gif")));
        menuRobbiImage = new Image(String.valueOf(MainStage.class.getResource("/img/Robbi24.png")));
        menuHollowImage = new Image(String.valueOf(MainStage.class.getResource("/img/Hollow24.png")));
        menuPileOfScrapImage = new Image(String.valueOf(MainStage.class.getResource("/img/PileOfScrap24.png")));
        menuStockpileImage = new Image(String.valueOf(MainStage.class.getResource("/img/Stockpile24.png")));
        menuAccuImage = new Image(String.valueOf(MainStage.class.getResource("/img/Accu24.png")));
        menuScrewImage = new Image(String.valueOf(MainStage.class.getResource("/img/Screw24.png")));
        menuNutImage = new Image(String.valueOf(MainStage.class.getResource("/img/Nut24.png")));
        menuDeleteImage = new Image(String.valueOf(MainStage.class.getResource("/img/Delete24.gif")));

        resetImage = new Image(String.valueOf(MainStage.class.getResource("/img/reset24.png")));
        menuStartImage = new Image(String.valueOf(MainStage.class.getResource("/img/Play24.gif")));
        menuPauseImage = new Image(String.valueOf(MainStage.class.getResource("/img/Pause24.gif")));
        menuStopImage = new Image(String.valueOf(MainStage.class.getResource("/img/Stop24.gif")));

        robbiMove = new Image(String.valueOf(MainStage.class.getResource("/img/RobbiMove24.png")));
        robbiTurnLeft = new Image(String.valueOf(MainStage.class.getResource("/img/RobbiLeft24.png")));
        robbiPut = new Image(String.valueOf(MainStage.class.getResource("/img/RobbiPut24.png")));
        robbiTake = new Image(String.valueOf(MainStage.class.getResource("/img/RobbiTake24.png")));
    }

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
    private NotificationController notificationController;
    private Program program;
    private MenuBar menubar;
    private Toolbar toolbar;
    // Content Pane
    private MonacoFX textArea;
    private ScrollPane territoryScrollPane;
    private TerritoryPanel territoryPanel;
    private SplitPane splitPane;
    private Scene mainStageScene;

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

        menubar = new MenuBar();
        toolbar = new Toolbar(menubar);
        createContentPane();

        VBox.setVgrow(splitPane, Priority.ALWAYS);
        var vBox = new VBox(menubar, toolbar, splitPane);

        notificationController = new NotificationController(vBox);

        mainStageScene = new Scene(notificationController.getScene());
        setScene(mainStageScene);

        setMinWidth(MIN_WIDTH);
        setMinHeight(MIN_HEIGHT);
        setHeight(HEIGHT);
        getIcons().add(menuRobbiImage);


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
        notificationController.showMessage(2500, "Snackbar.message.startup");
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
