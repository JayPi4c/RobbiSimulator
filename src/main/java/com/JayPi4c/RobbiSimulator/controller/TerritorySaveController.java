package com.JayPi4c.RobbiSimulator.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.DIRECTION;
import com.JayPi4c.RobbiSimulator.model.Item;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.stage.FileChooser;

/**
 * Controller to handle all territory save action. It handles load and save
 * actions for all supported formats.
 * 
 * @author Jonas Pohl
 *
 */
public class TerritorySaveController {

	private static final Logger logger = LogManager.getLogger(TerritorySaveController.class);

	private static final String DEFAULT_SERIALISATION_FILE_EXTENSION = ".ter";
	private static final String DEFAULT_XML_FILE_EXTENSION = ".rsxml";

	private MainStage mainStage;

	/**
	 * Constructor to create a new TerritorySaveController for the given mainStage.
	 * 
	 * @param mainStage the mainStage this controller is for
	 */
	public TerritorySaveController(MainStage mainStage) {
		this.mainStage = mainStage;
		this.mainStage.getSaveSerialTerritoryMenuItem().setOnAction(e -> serialize());
		this.mainStage.getLoadSerialTerritoryMenuItem().setOnAction(e -> deserialize());

		this.mainStage.getSaveXMLTerritoryMenuItem().setOnAction(e -> saveXMLtoFile());
		this.mainStage.getLoadXMLTerritoryMenuItem().setOnAction(e -> loadXMLfromFile());

		this.mainStage.getSaveJAXBTerritoryMenuItem().setOnAction(e -> logger.debug("Yet to implement"));// saveJAXB(ProgramController.PATH_TO_PROGRAMS
		// + "/territory.jaxb"));
		this.mainStage.getLoadJAXBTerritoryMenuItem().setOnAction(e -> logger.debug("Yet to implement"));// loadJAXB(ProgramController.PATH_TO_PROGRAMS
		// + "/territory.jaxb"));
	}

	/**
	 * Helper to serialize the territory of the mainStage into a file.
	 */
	private void serialize() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle(Messages.getString("Territory.save.dialog.title"));
		chooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.save.dialog.filter"), "*" + DEFAULT_SERIALISATION_FILE_EXTENSION));

		File file = chooser.showSaveDialog(mainStage);

		if (!file.getName().endsWith(DEFAULT_SERIALISATION_FILE_EXTENSION)) {
			File f = new File(file.getAbsolutePath() + DEFAULT_SERIALISATION_FILE_EXTENSION);
			if (!file.renameTo(f))
				logger.warn("Could not rename serializationfile from '{}' to '{}'", file.getName(), f.getName());
			file = f;
		}

		if (file != null) {
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
	}

	/**
	 * Helper to deserialize a territory from a file and update the old territory to
	 * the new values.
	 */
	private void deserialize() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Messages.getString("Territory.load.dialog.title"));
		fileChooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.load.dialog.filter"), "*" + DEFAULT_SERIALISATION_FILE_EXTENSION));

		File file = fileChooser.showOpenDialog(mainStage);
		if (file != null) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
				logger.debug("deserialize from file {}", file);
				Territory t = (Territory) ois.readObject();
				Item item = (Item) ois.readObject();
				int x = ois.readInt();
				int y = ois.readInt();
				DIRECTION facing = (DIRECTION) ois.readObject();
				synchronized (mainStage.getTerritory()) {
					mainStage.getTerritory().update(t, item, x, y, facing);
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				logger.info("finished deserialization");
			}
		}
	}

	/**
	 * Asks for a file to save the xml to and then writes the territory into this
	 * file.
	 */
	private void saveXMLtoFile() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle(Messages.getString("Territory.save.dialog.xml.title"));
		chooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.save.dialog.xml.filter"), "*" + DEFAULT_XML_FILE_EXTENSION));

		File file = chooser.showSaveDialog(mainStage);

		if (!file.getName().endsWith(DEFAULT_XML_FILE_EXTENSION)) {
			file = new File(file.getAbsolutePath() + DEFAULT_XML_FILE_EXTENSION);
		}

		if (file != null) {
			logger.debug("save as XML in file {}", file);
			try (ByteArrayOutputStream baos = mainStage.getTerritory().toXML();
					OutputStream outputStream = new FileOutputStream(file.getAbsolutePath())) {
				baos.writeTo(outputStream);
			} catch (IOException e) {
				logger.debug("failed");
			}
		}
	}

	/**
	 * Asks for an XML-File and loads the contents into the territory.
	 */
	private void loadXMLfromFile() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Messages.getString("Territory.load.dialog.xml.title"));
		fileChooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.load.dialog.xml.filter"), "*" + DEFAULT_XML_FILE_EXTENSION));
		File file = fileChooser.showOpenDialog(mainStage);
		if (file != null) {
			logger.debug("load territory from xml-file {}", file);
			try {
				mainStage.getTerritory().fromXML(new FileInputStream(file));
			} catch (FileNotFoundException e) {
				logger.debug("Could not find file {}", file.getAbsolutePath());
			}
			logger.info("finished loading from xml-file");
		}
	}

	/*
	 * /** Loads a territory by a filename using JAXB
	 * 
	 * @param filename the name of the file in which the territory is stored
	 * 
	 * @return true if the territory was loaded successfully, false otherwise
	 */
	/*
	 * private boolean loadJAXB(String filename) {
	 * 
	 * // TODO investigate JAXB
	 * 
	 * try { JAXBContext context = JAXBContext.newInstance(Territory.class);
	 * Unmarshaller um = context.createUnmarshaller(); Territory ter = (Territory)
	 * um.unmarshal(new FileReader(new File(filename))); ter.print();
	 * mainStage.getTerritory().update(ter, ter.getRobbiItem(), ter.getRobbiX(),
	 * ter.getRobbiY(), ter.getRobbiDirection()); return true; } catch (IOException
	 * | JAXBException e) { e.printStackTrace();
	 * logger.debug("failed to load JAXB"); return false; } }
	 */

	/*
	 * /** Saves the territory using JAXB.
	 * 
	 * @param filename name of the file in which the territory will be saved
	 * 
	 * @return true if the territory was saved successfully, false otherwise
	 */
	/*
	 * private boolean saveJAXB(String filename) { try (Writer w = new
	 * FileWriter(filename)) { JAXBContext context =
	 * JAXBContext.newInstance(Territory.class); Marshaller m =
	 * context.createMarshaller(); m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
	 * Boolean.TRUE); m.marshal(mainStage.getTerritory(), w); return true; } catch
	 * (IOException | JAXBException e) { e.printStackTrace();
	 * logger.debug("failed to save jaxb"); return false; } }
	 */
}
