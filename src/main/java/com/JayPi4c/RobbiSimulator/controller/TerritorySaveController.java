package com.JayPi4c.RobbiSimulator.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Optional;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.controller.program.ProgramController;
import com.JayPi4c.RobbiSimulator.model.Accu;
import com.JayPi4c.RobbiSimulator.model.DIRECTION;
import com.JayPi4c.RobbiSimulator.model.Hollow;
import com.JayPi4c.RobbiSimulator.model.Item;
import com.JayPi4c.RobbiSimulator.model.Nut;
import com.JayPi4c.RobbiSimulator.model.PileOfScrap;
import com.JayPi4c.RobbiSimulator.model.Screw;
import com.JayPi4c.RobbiSimulator.model.Stockpile;
import com.JayPi4c.RobbiSimulator.model.Territory;
import com.JayPi4c.RobbiSimulator.model.Tile;
import com.JayPi4c.RobbiSimulator.utils.AlertHelper;
import com.JayPi4c.RobbiSimulator.utils.Messages;
import com.JayPi4c.RobbiSimulator.view.MainStage;

import javafx.scene.control.Alert.AlertType;
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

		this.mainStage.getSaveXMLTerritoryMenuItem().setOnAction(e -> saveXML());
		this.mainStage.getLoadXMLTerritoryMenuItem().setOnAction(e -> loadXML());

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
	private void saveXML() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle(Messages.getString("Territory.save.dialog.xml.title"));
		chooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.save.dialog.xml.filter"), "*" + DEFAULT_XML_FILE_EXTENSION));

		File file = chooser.showSaveDialog(mainStage);

		if (!file.getName().endsWith(DEFAULT_XML_FILE_EXTENSION)) {
			file = new File(file.getAbsolutePath() + DEFAULT_XML_FILE_EXTENSION);
		}

		// load the dtd from resources
		String dtd;
		Optional<String> dtdOpt = getDTD();
		if (dtdOpt.isPresent()) {
			dtd = dtdOpt.get();
		} else {
			logger.warn("Could not load dtd");
			AlertHelper.showAlertAndWait(AlertType.ERROR, Messages.getString("Territory.xml.error.dtd"), mainStage);
			return;
		}

		if (file != null) {
			logger.debug("save as XML in file {}", file);
			try {
				XMLOutputFactory factory = XMLOutputFactory.newInstance();
				XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(file.getAbsolutePath()),
						"utf-8");

				writer.writeStartDocument("utf-8", "1.0");
				writer.writeCharacters("\n");

				writer.writeDTD("<!DOCTYPE territory [" + dtd + "]>");
				writer.writeCharacters("\n");
				// writer.writeDTD("<!DOCTYPE territory SYSTEM \"xml/simulator.dtd\">");
				synchronized (mainStage.getTerritory()) {
					Territory territory = mainStage.getTerritory();
					writer.writeStartElement("territory");
					writer.writeAttribute("col", Integer.toString(territory.getNumCols()));
					writer.writeAttribute("row", Integer.toString(territory.getNumRows()));
					writer.writeCharacters("\n");
					for (int i = 0; i < territory.getNumCols(); i++) {
						for (int j = 0; j < territory.getNumRows(); j++) {
							Tile t = territory.getTile(i, j);
							if (t instanceof Hollow) {
								writer.writeStartElement("hollow");
								writer.writeAttribute("col", Integer.toString(i));
								writer.writeAttribute("row", Integer.toString(j));
								writer.writeCharacters("\n");
							} else if (t instanceof PileOfScrap) {
								writer.writeStartElement("pileofscrap");
								writer.writeAttribute("col", Integer.toString(i));
								writer.writeAttribute("row", Integer.toString(j));
								writer.writeCharacters("\n");
							} else if (t instanceof Stockpile) {
								writer.writeStartElement("stockpile");
								writer.writeAttribute("col", Integer.toString(i));
								writer.writeAttribute("row", Integer.toString(j));
								writer.writeCharacters("\n");
								for (Item item : ((Stockpile) t).getAllItems()) {
									writeItem(writer, item);
								}
							} else {
								writer.writeStartElement("tile");
								writer.writeAttribute("col", Integer.toString(i));
								writer.writeAttribute("row", Integer.toString(j));
								writer.writeCharacters("\n");
								if (t.getItem() != null) {
									writeItem(writer, t.getItem());
								}
							}
							writer.writeEndElement();
							writer.writeCharacters("\n");
						}
					}
					writer.writeStartElement("robbi");
					writer.writeAttribute("col", Integer.toString(territory.getRobbiY()));
					writer.writeAttribute("row", Integer.toString(territory.getRobbiX()));
					writer.writeCharacters("\n");
					writer.writeStartElement("facing");
					writer.writeAttribute("facing", territory.getRobbiDirection().toString());
					writer.writeEndElement();
					writer.writeCharacters("\n");
					if (territory.getRobbiItem() != null) {
						writeItem(writer, territory.getRobbiItem());
					}
				}
				writer.writeEndElement();
				writer.writeCharacters("\n");
				writer.writeEndElement();
				writer.writeCharacters("\n");
				writer.writeEndDocument();
				writer.close();

			} catch (FactoryConfigurationError | FileNotFoundException | XMLStreamException e) {
				e.printStackTrace();
				logger.error("failed to save as XML.");
				AlertHelper.showAlertAndWait(AlertType.ERROR, Messages.getString("Territory.save.dialog.xml.error"),
						mainStage);
			} finally {
				logger.info("finished saving as XML.");
			}
		}

	}

	/**
	 * Helper to store an item in an xml-file.
	 * 
	 * @param writer the writer, the item needs to be written to
	 * @param item   the item to write
	 * @throws XMLStreamException if the item cannot be written
	 */
	private void writeItem(XMLStreamWriter writer, Item item) throws XMLStreamException {
		writer.writeStartElement("item");
		writer.writeAttribute("type", item.getClass().getSimpleName());
		writer.writeEndElement();
		writer.writeCharacters("\n");
	}

	/**
	 * Asks for an XML-File and loads the contents into the territory.
	 */
	private void loadXML() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(Messages.getString("Territory.load.dialog.xml.title"));
		fileChooser.setInitialDirectory(new File(ProgramController.PATH_TO_PROGRAMS));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(
				Messages.getString("Territory.load.dialog.xml.filter"), "*" + DEFAULT_XML_FILE_EXTENSION));
		File file = fileChooser.showOpenDialog(mainStage);
		if (file != null) {
			logger.debug("load territory from xml-file {}", file);
			try {
				Territory territory = new Territory();
				int robbiX = 0;
				int robbiY = 0;
				Item robbiItem = null;
				DIRECTION robbiDirection = DIRECTION.EAST;
				int x = 0;
				int y = 0;
				XMLInputFactory factory = XMLInputFactory.newInstance();
				XMLStreamReader parser = factory.createXMLStreamReader(new FileInputStream(file));
				while (parser.hasNext()) {
					switch (parser.getEventType()) {
					case XMLStreamConstants.START_DOCUMENT:
						break;
					case XMLStreamConstants.END_DOCUMENT:
						parser.close();
						break;
					case XMLStreamConstants.START_ELEMENT:
						switch (parser.getLocalName()) {
						case "pileofscrap":
							x = Integer.parseInt(parser.getAttributeValue(null, "col"));
							y = Integer.parseInt(parser.getAttributeValue(null, "row"));
							territory.placePileOfScrap(x, y);
							break;
						case "hollow":
							x = Integer.parseInt(parser.getAttributeValue(null, "col"));
							y = Integer.parseInt(parser.getAttributeValue(null, "row"));
							territory.placeHollow(x, y);
							break;
						case "stockpile":
							x = Integer.parseInt(parser.getAttributeValue(null, "col"));
							y = Integer.parseInt(parser.getAttributeValue(null, "row"));
							territory.placeStockpile(x, y);
							break;
						case "tile":
							x = Integer.parseInt(parser.getAttributeValue(null, "col"));
							y = Integer.parseInt(parser.getAttributeValue(null, "row"));
							break;
						case "item":
							Item item = getItemFromParser(parser);
							if (x < 0 || y < 0) {
								robbiItem = item;
							} else {
								territory.placeItem(item, x, y);
							}
							break;
						case "facing":
							DIRECTION facing = DIRECTION.valueOf(parser.getAttributeValue(null, "facing"));
							robbiDirection = facing;
							break;
						case "robbi":
							robbiX = Integer.parseInt(parser.getAttributeValue(null, "col"));
							robbiY = Integer.parseInt(parser.getAttributeValue(null, "row"));
							x = -1;
							y = -1;
						default:
							break;
						}
					case XMLStreamConstants.CHARACTERS:
						break;
					case XMLStreamConstants.END_ELEMENT:
						break;
					default:
						break;
					}
					parser.next();
				}
				mainStage.getTerritory().update(territory, robbiItem, robbiX, robbiY, robbiDirection);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Failed to load territory from XML-file");
			}
			logger.info("finished loading from xml-file");
		}
	}

	/**
	 * Helper to get the correct item from the parser. <br>
	 * Only use this method if you know for sure, that the parsers EventType is
	 * Item.
	 * 
	 * @param parser the parser to read the type from
	 * @return the correct item class for this element
	 */
	private Item getItemFromParser(XMLStreamReader parser) {
		String type = parser.getAttributeValue(null, "type");
		if (type.equals("Nut")) {
			return new Nut();
		} else if (type.equals("Accu")) {
			return new Accu();
		} else if (type.equals("Screw")) {
			return new Screw();
		}
		return null;
	}

	/**
	 * Loads the dtd needed to save the territory as a xml-File from the resources
	 * folder
	 * 
	 * @return the dtd String defined in resources/xml/simulator.dtd
	 */
	private Optional<String> getDTD() {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("xml/simulator.dtd")))) {

			StringBuilder builder = new StringBuilder();
			String s;
			while ((s = reader.readLine()) != null) {
				builder.append(s);
				builder.append(System.lineSeparator());
			}
			return Optional.of(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Optional.empty();
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
