package com.JayPi4c.RobbiSimulator.controller;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.*;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.view.MainStage;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.Optional;

import static com.JayPi4c.RobbiSimulator.utils.I18nUtils.i18n;

/**
 * Controller to handle all territory save action. It handles load and save
 * actions for all supported formats.
 *
 * @author Jonas Pohl
 */
@Slf4j
public class TerritorySaveController {

    private static final String DEFAULT_SERIALISATION_FILE_EXTENSION = ".ter";
    private static final String DEFAULT_XML_FILE_EXTENSION = ".rsxml";
    private static final String DEFAULT_JAXB_FILE_EXTENSION = ".rsjaxb";

    private MainStage mainStage;

    /**
     * Constructor to create a new TerritorySaveController for the given mainStage.
     *
     * @param mainStage the mainStage this controller is for
     */
    public TerritorySaveController(MainStage mainStage) {
        this.mainStage = mainStage;
        this.mainStage.getMenubar().getSaveSerialTerritoryMenuItem().setOnAction(e -> serialize());
        this.mainStage.getMenubar().getLoadSerialTerritoryMenuItem().setOnAction(e -> deserialize());

        this.mainStage.getMenubar().getSaveXMLTerritoryMenuItem().setOnAction(e -> saveXMLtoFile());
        this.mainStage.getMenubar().getLoadXMLTerritoryMenuItem().setOnAction(e -> loadXMLfromFile());

        this.mainStage.getMenubar().getSaveJAXBTerritoryMenuItem().setOnAction(e -> saveJAXB());
        this.mainStage.getMenubar().getLoadJAXBTerritoryMenuItem().setOnAction(e -> loadJAXB());
    }

    /**
     * Helper to serialize the territory of the mainStage into a file.
     */
    private void serialize() {
        Optional<File> fileOpt = getSaveFile(i18n("Territory.save.dialog.title"),
                i18n("Territory.save.dialog.filter.serial"), DEFAULT_SERIALISATION_FILE_EXTENSION);

        if (fileOpt.isEmpty()) {
            logger.debug("No file was selected to serialze in.");
            return;
        }

        File file = fileOpt.get();
        if (!file.getName().endsWith(DEFAULT_SERIALISATION_FILE_EXTENSION)) {
            file = new File(file.getAbsolutePath() + DEFAULT_SERIALISATION_FILE_EXTENSION);
        }

        logger.debug("serialize in file {}", file);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            synchronized (mainStage.getTerritory()) {
                oos.writeObject(mainStage.getTerritory());
                oos.writeObject(mainStage.getTerritory().getRobbiItem());
                oos.writeInt(mainStage.getTerritory().getRobbiX());
                oos.writeInt(mainStage.getTerritory().getRobbiY());
                oos.writeObject(mainStage.getTerritory().getRobbiDirection());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logger.info("finished serialization");
        }

    }

    /**
     * Helper to deserialize a territory from a file and update the old territory to
     * the new values.
     */
    private void deserialize() {
        Optional<File> fileOpt = getLoadFile(i18n("Territory.load.dialog.title"),
                i18n("Territory.load.dialog.filter.deserial"), DEFAULT_SERIALISATION_FILE_EXTENSION);

        if (fileOpt.isEmpty()) {
            logger.debug("No file was selected to deserialize from.");
            return;
        }
        File file = fileOpt.get();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            logger.debug("deserialize from file {}", file);
            Territory t = (Territory) ois.readObject();
            Item item = (Item) ois.readObject();
            int x = ois.readInt();
            int y = ois.readInt();
            DIRECTION facing = (DIRECTION) ois.readObject();
            mainStage.getTerritory().update(t, item, x, y, facing);
        } catch (InvalidTerritoryException e) {
            AlertHelper.showAlertAndWait(AlertType.WARNING, i18n("Territory.load.failure"), mainStage);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            logger.info("finished deserialization");
        }
    }

    /**
     * Asks for a file to save the xml to and then writes the territory into this
     * file.
     */
    private void saveXMLtoFile() {
        Optional<File> fileOpt = getSaveFile(i18n("Territory.save.dialog.title"),
                i18n("Territory.save.dialog.filter.xml"), DEFAULT_XML_FILE_EXTENSION);

        if (fileOpt.isEmpty()) {
            logger.debug("No file selected to save territory in.");
            return;
        }
        File file = fileOpt.get();

        if (!file.getName().endsWith(DEFAULT_XML_FILE_EXTENSION)) {
            file = new File(file.getAbsolutePath() + DEFAULT_XML_FILE_EXTENSION);
        }

        logger.debug("save as XML in file {}", file);
        try (ByteArrayOutputStream baos = mainStage.getTerritory().toXML();
             OutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
            baos.writeTo(outputStream);
        } catch (IOException e) {
            logger.debug("failed");
        }

    }

    /**
     * Asks for an XML-File and loads the contents into the territory.
     */
    private void loadXMLfromFile() {
        Optional<File> fileOpt = getLoadFile(i18n("Territory.load.dialog.title"),
                i18n("Territory.load.dialog.filter.xml"), DEFAULT_XML_FILE_EXTENSION);

        if (fileOpt.isEmpty()) {
            logger.debug("No file selected to load XML from.");
            return;
        }

        File file = fileOpt.get();
        logger.debug("load territory from xml-file {}", file);
        try {
            if (mainStage.getTerritory().fromXML(new FileInputStream(file)))
                logger.info("finished loading from xml-file");
            else
                logger.info("Failed loading from xml-file");
        } catch (FileNotFoundException e) {
            logger.debug("Could not find file {}", file.getAbsolutePath());
        }

    }

    /**
     * Loads a territory by a filename using JAXB
     *
     * @return true if the territory was loaded successfully, false otherwise
     */
    private void loadJAXB() {
        Optional<File> fileOpt = getLoadFile(i18n("Territory.load.dialog.title"),
                i18n("Territory.load.dialog.filter.jaxb"), DEFAULT_JAXB_FILE_EXTENSION);

        if (fileOpt.isEmpty()) {
            logger.debug("No file selected");
            return;
        }
        File file = fileOpt.get();
        logger.debug("load territory from jaxb-file {}", file);
        try {
            JAXBContext context = JAXBContext.newInstance(TerritoryState.class, Nut.class, Screw.class, Accu.class,
                    Stockpile.class, PileOfScrap.class, Hollow.class);
            Unmarshaller um = context.createUnmarshaller();
            TerritoryState ter = (TerritoryState) um.unmarshal(new FileReader(file));
            mainStage.getTerritory().restore(ter);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
            logger.debug("failed to load JAXB");
        }
    }

    /**
     * Saves the territory using JAXB.
     */
    private void saveJAXB() {
        Optional<File> fileOpt = getSaveFile(i18n("Territory.save.dialog.title"),
                i18n("Territory.save.dialog.filter.jaxb"), DEFAULT_JAXB_FILE_EXTENSION);

        if (fileOpt.isEmpty()) {
            logger.debug("No file selected to save territory in.");
            return;
        }
        File file = fileOpt.get();
        if (!file.getName().endsWith(DEFAULT_JAXB_FILE_EXTENSION)) {
            file = new File(file.getAbsolutePath() + DEFAULT_JAXB_FILE_EXTENSION);
        }
        logger.debug("save territory from jaxb-file {}", file);
        try (Writer w = new FileWriter(file)) {
            JAXBContext context = JAXBContext.newInstance(TerritoryState.class, Nut.class, Screw.class, Accu.class,
                    Stockpile.class, PileOfScrap.class, Hollow.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(mainStage.getTerritory().save(), w);
        } catch (IOException | JAXBException e) {
            e.printStackTrace();
            logger.debug("failed to save jaxb");
        }
    }

    /**
     * Opens a dialog to ask for a file to save the territory in.
     *
     * @param title         the title of the dialog
     * @param description   file description for the allowed files
     * @param fileExtension allowed file-extension
     * @return a file to save the territory in
     */
    private Optional<File> getSaveFile(String title, String description, String fileExtension) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle(title);
        chooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, "*" + fileExtension));
        return Optional.ofNullable(chooser.showSaveDialog(mainStage));
    }

    /**
     * Opens a dialog to ask for a file to load a territory from.
     *
     * @param title         the title of the dialog
     * @param description   file description for allowed files
     * @param fileExtension allowed file-extension
     * @return a file to load a territory from
     */
    public Optional<File> getLoadFile(String title, String description, String fileExtension) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(description, "*" + fileExtension));
        return Optional.ofNullable(fileChooser.showOpenDialog(mainStage));
    }

}
