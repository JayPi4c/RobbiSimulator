package com.JayPi4c.RobbiSimulator.model;

import com.JayPi4c.RobbiSimulator.utils.Observable;
import lombok.extern.slf4j.Slf4j;

import javax.xml.XMLConstants;
import javax.xml.stream.*;
import java.io.*;
import java.util.Optional;

/**
 * This class contains all datastructures and utility functions to control the
 * territory.
 *
 * @author Jonas Pohl
 */
@Slf4j
public class Territory extends Observable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_NUMBER_OF_COLUMNS = 6;
    private static final int DEFAULT_NUMBER_OF_ROWS = 6;
    private transient Robbi robbi;
    /**
     * Array-attribute to store the real territory
     */
    private Tile[][] tiles;
    /**
     * Attribute to save if the territory has changed.
     */
    private boolean sizeChanged = false;
    /**
     * Attribute to store the current number of columns of the territory.
     */
    private int numberOfColumns = DEFAULT_NUMBER_OF_COLUMNS;
    /**
     * Attribute to store the current number of rows of the territory.
     */
    private int numberOfRows = DEFAULT_NUMBER_OF_ROWS;

    /**
     * Creates a new Territory with a new robbi instance and initializes all tiles
     * to the default size.
     */
    public Territory() {
        robbi = new Robbi(this);
        tiles = new Tile[DEFAULT_NUMBER_OF_COLUMNS][DEFAULT_NUMBER_OF_ROWS];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }
    // ============ HELPER =========

    /**
     * Saves the Territory in a TerritoryState in order to allow to restore it
     * later.
     *
     * @return a Memento Object of the territory
     */
    public synchronized TerritoryState save() {
        return new TerritoryState(numberOfColumns, numberOfRows, tiles, robbi);
    }

    /**
     * Restores the territory from a memento object.
     *
     * @param state the Memento object of the state the territory should restore to.
     */
    public void restore(TerritoryState state) {
        synchronized (this) {
            numberOfColumns = state.getNumberOfColumns();
            numberOfRows = state.getNumberOfRows();
            RobbiState robbiState = state.getRobbiState();
            this.robbi.setPosition(robbiState.getX(), robbiState.getY());
            this.robbi.setItem(robbiState.getItem());
            this.robbi.setFacing(robbiState.getFacing());
            this.tiles = state.getTiles();
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * Calculates the tile for the given x and y coordinates.
     *
     * @param x tiles x ordinate
     * @param y tiles y ordinate
     * @return the tile for the corresponding coordinate
     */
    public synchronized Tile getTile(int x, int y) {
        x += numberOfColumns;
        x %= numberOfColumns;
        y += numberOfRows;
        y %= numberOfRows;
        return tiles[x][y];
    }

    /**
     * Getter for the current number of rows.
     *
     * @return the current number of rows
     */
    public synchronized int getNumRows() {
        return numberOfRows;
    }

    /**
     * Getter for the current number of columns.
     *
     * @return the current number of columns
     */
    public synchronized int getNumCols() {
        return numberOfColumns;
    }

    /**
     * Getter to check if the size of the territory has changed.
     *
     * @return true if the size has changed, false otherwise
     */
    public synchronized boolean hasSizeChanged() {
        return sizeChanged;
    }

    /**
     * Setter to update if the size has changed.
     *
     * @param flag new value for the sizeChanged attribute
     */
    public synchronized void setSizeChanged(boolean flag) {
        this.sizeChanged = flag;
    }

    /**
     * Getter for the current robbi in the territory.
     *
     * @return the current robbi
     */
    public synchronized Robbi getRobbi() {
        return robbi;
    }

    /**
     * Setter to update Robbi in the territory.
     *
     * @param robbi the new Robbi for the territory
     */
    public synchronized void setRobbi(Robbi robbi) {
        robbi.setTerritory(this);
        if (this.robbi != null) {
            robbi.setPosition(this.robbi.getX(), this.robbi.getY());
            robbi.setFacing(this.robbi.getFacing());
            robbi.setItem(this.robbi.getItem());
        }
        this.robbi = robbi;
    }

    /**
     * Getter for robbis current direction.
     *
     * @return robbis current direction
     */
    public synchronized DIRECTION getRobbiDirection() {
        return robbi.getFacing();
    }

    /**
     * Getter for robbis current item.
     *
     * @return the item robbi is currently holding. null, if robbi does not have
     * one.
     */
    public synchronized Item getRobbiItem() {
        return robbi.getItem();
    }

    /**
     * Helper to bound a value to a given bound
     *
     * @param i     the value to bound
     * @param bound the bound to limit i to
     * @return the normalized value of i
     */
    private int normalizeCoord(int i, int bound) {
        i %= bound;
        i += bound;
        i %= bound;
        return i;
    }

    /**
     * Checks if robbi is on the tile with the given coords.
     *
     * @param col the columns to check
     * @param row the row to check
     * @return true if and only if robbi is on the tile provided by col and row,
     * false otherwise
     */
    public synchronized boolean robbiOnTile(int col, int row) {
        return robbi.getX() == col && robbi.getY() == row;
    }

    /**
     * Placing the given item in the territory at the given x-y-position.
     *
     * @param item the item to place in the territory
     * @param x    x-ordinate in the territory
     * @param y    y-ordinate in the territory
     * @return true if the item has been placed in the territory, false otherwise
     */
    public boolean placeItem(Item item, int x, int y) {
        synchronized (this) {
            Tile t = tiles[normalizeCoord(x, numberOfColumns)][normalizeCoord(y, numberOfRows)];
            if (t.getItem() != null && !(t instanceof Stockpile))
                return false;
            t.setItem(item);
        }
        setChanged();
        notifyAllObservers();
        return true;
    }

    /**
     * Getter for the item in the territory at the given position.
     *
     * @param x x-ordinate in the territory
     * @param y y-ordinate in the territory
     * @return the item, which is placed at the given position
     */
    public synchronized Item getItem(int x, int y) {
        Tile t = tiles[normalizeCoord(x, numberOfColumns)][normalizeCoord(y, numberOfRows)];
        return t.getItem();
    }

    /**
     * Removes the item from the given position.
     *
     * @param x x-ordinate in the territory
     * @param y y-ordinate in the territory
     * @return the item that has been removed from the tile
     */
    public Item removeItem(int x, int y) {
        Item i = null;
        synchronized (this) {
            Tile t = tiles[normalizeCoord(x, numberOfColumns)][normalizeCoord(y, numberOfRows)];
            i = t.pickItem();
        }
        setChanged();
        notifyAllObservers();
        return i;
    }

    /**
     * Updates the territory to the given territory and updates robbi with the
     * additional information. <br>
     * This method is used to update the territory to a territory state loaded from
     * a file.
     *
     * @param territory the new territory
     * @param item      the item robbi has in his bag
     * @param x         robbis x position
     * @param y         robbis y positions
     * @param facing    robbis facing
     * @throws InvalidTerritoryException if it is impossible to create the territory
     *                                   with the given information
     */
    public void update(Territory territory, Item item, int x, int y, DIRECTION facing)
            throws InvalidTerritoryException {
        for (int i = 0; i < territory.numberOfColumns; i++) {
            if (territory.tiles[i].length != territory.numberOfRows)
                throw new InvalidTerritoryException();
        }
        synchronized (this) {
            this.numberOfColumns = territory.numberOfColumns;
            this.numberOfRows = territory.numberOfRows;
            this.tiles = territory.tiles;
            this.sizeChanged = true;
            try {
                this.robbi.setPosition(x, y);
            } catch (Exception e) {
                throw new InvalidTerritoryException();
            }
            this.robbi.setFacing(facing);
            this.robbi.setItem(item);
        }
        setChanged();
        notifyAllObservers();
    }

    // ========= GUI FUNCTIONS ===========

    /**
     * Updates the size of the territory to the new size.
     *
     * @param newCols new number of columns in the territory
     * @param newRows new number of rows in the territory
     */
    public void changeSize(int newCols, int newRows) {
        synchronized (this) {
            if (newCols <= 0 || newRows <= 0)
                throw new IllegalArgumentException("Diese Größe ist für das Territorium nicht zulässig");
            if (newCols != numberOfColumns || newRows != numberOfRows)
                sizeChanged = true;
            else
                return;
            numberOfColumns = newCols;
            numberOfRows = newRows;
            // create the new territory
            Tile[][] newTiles = new Tile[numberOfColumns][numberOfRows];
            for (int i = 0; i < numberOfColumns; i++) {
                for (int j = 0; j < numberOfRows; j++) {
                    newTiles[i][j] = new Tile();
                }
            }

            // copy the old territory into the new
            for (int i = 0; i < tiles.length && i < newCols; i++) {
                for (int j = 0; j < tiles[i].length && j < newRows; j++) {
                    newTiles[i][j] = tiles[i][j];
                }
            }

            if (robbi.getX() >= numberOfColumns || robbi.getY() > numberOfRows) {
                if (newTiles[0][0] instanceof Hollow)
                    newTiles[0][0] = new Tile();
                robbi.setPosition(0, 0);
            }
            tiles = newTiles;

            logger.debug("updated size to {}x{}", numberOfColumns, numberOfRows);
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * move robbi to the given position.
     *
     * @param x robbis new x-position
     * @param y robbis new y-position
     */
    public void placeRobbi(int x, int y) {
        synchronized (this) {
            if ((x >= 0 && x < numberOfColumns && y >= 0 && y < numberOfRows) && !(tiles[x][y] instanceof Hollow)) {
                robbi.setPosition(x, y);
            }
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * Place a new Hollow at the given position if it is in bounds and robbi is not
     * on the tile.
     *
     * @param x Hollows x-Position
     * @param y Hollows y-Position
     */
    public void placeHollow(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            if ((x < numberOfColumns && y < numberOfRows) && !(x == robbi.getX() && y == robbi.getY()))
                tiles[x][y] = new Hollow();
        }
        if ((x < numberOfColumns && y < numberOfRows) && !(x == robbi.getX() && y == robbi.getY())) {
            setChanged();
            notifyAllObservers();
        }
    }

    /**
     * Place a new PileOfScrap at the given position if it is in bounds.
     *
     * @param x PileOfScrap x-Position
     * @param y PileOfScrap y-Position
     */
    public void placePileOfScrap(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            tiles[x][y] = new PileOfScrap();
        }
        setChanged();
        notifyAllObservers();

    }

    /**
     * Place a new Stockpile at the given position if it is in bounds.
     *
     * @param x Stockpile x-Position
     * @param y Stockpile y-Position
     */
    public void placeStockpile(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            tiles[x][y] = new Stockpile();
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * Place a new Accu on the given tile if it is in bounds.
     *
     * @param x Accu x-Position
     * @param y Accu y-Position
     */
    public void placeAccu(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            tiles[x][y].setItem(new Accu());
        }
        setChanged();
        notifyAllObservers();

    }

    /**
     * Place a new Screw on the given tile if it is in bounds.
     *
     * @param x Screw x-Position
     * @param y Screw y-Position
     */
    public void placeScrew(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            tiles[x][y].setItem(new Screw());
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * Place a new Nut on the given tile if it is in bounds.
     *
     * @param x Nut x-Position
     * @param y Nut y-Position
     */
    public void placeNut(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            tiles[x][y].setItem(new Nut());
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * Removes all elements from the tile and replaces it with a new default tile.
     *
     * @param x Tile x-Position
     * @param y Tile y-Position
     */
    public void clearTile(int x, int y) {
        x = normalizeCoord(x, numberOfColumns);
        y = normalizeCoord(y, numberOfRows);
        synchronized (this) {
            tiles[x][y] = new Tile();
        }
        setChanged();
        notifyAllObservers();
    }

    /**
     * Updates this territory to the territory encoded as the XML-InputStream. <br>
     * It uses the StAX Cursor API, since it is more efficient by not creating any
     * new objects.
     *
     * @param stream InputStream of the XML-encoded territory
     * @return true if the territory was build successfully, false otherwise
     */
    public boolean fromXML(InputStream stream) {
        try {
            Territory territory = new Territory();
            int robbiX = 0;
            int robbiY = 0;
            Item robbiItem = null;
            DIRECTION robbiDirection = DIRECTION.EAST;
            int x = 0;
            int y = 0;
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setProperty(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            XMLStreamReader parser = factory.createXMLStreamReader(stream);
            while (parser.hasNext()) {
                switch (parser.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        parser.close();
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        switch (parser.getLocalName()) {
                            case "territory":
                                int cols = Integer.parseInt(parser.getAttributeValue(null, "col"));
                                int rows = Integer.parseInt(parser.getAttributeValue(null, "row"));
                                territory.changeSize(cols, rows);
                                break;
                            case "pileofscrap":
                                x = Integer.parseInt(parser.getAttributeValue(null, "col"));
                                y = Integer.parseInt(parser.getAttributeValue(null, "row"));
                                territory.tiles[x][y] = new PileOfScrap();
                                break;
                            case "hollow":
                                x = Integer.parseInt(parser.getAttributeValue(null, "col"));
                                y = Integer.parseInt(parser.getAttributeValue(null, "row"));
                                territory.tiles[x][y] = new Hollow();
                                break;
                            case "stockpile":
                                x = Integer.parseInt(parser.getAttributeValue(null, "col"));
                                y = Integer.parseInt(parser.getAttributeValue(null, "row"));
                                territory.tiles[x][y] = new Stockpile();
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
                                break;
                            default:
                                break;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        break;
                    default:
                        break;
                }
                parser.next();
            }
            update(territory, robbiItem, robbiX, robbiY, robbiDirection);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
     * Creates a ByteArrayOutputStream that contains the territory encoded as XML.
     *
     * @return ByteArrayOutputStream with XML-encoded territory.
     */
    public ByteArrayOutputStream toXML() {
        // load the dtd from resources
        String dtd;
        Optional<String> dtdOpt = getDTD();
        if (dtdOpt.isPresent()) {
            dtd = dtdOpt.get();
        } else {
            logger.warn("Could not load dtd");
            return null;
        }
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = factory.createXMLStreamWriter(baos, "utf-8");

            writer.writeStartDocument("utf-8", "1.0");
            writer.writeCharacters("\n");

            writer.writeDTD("<!DOCTYPE territory [" + dtd + "]>");
            writer.writeCharacters("\n");
            synchronized (this) {
                writer.writeStartElement("territory");
                writer.writeAttribute("col", Integer.toString(getNumCols()));
                writer.writeAttribute("row", Integer.toString(getNumRows()));
                writer.writeCharacters("\n");
                for (int i = 0; i < getNumCols(); i++) {
                    for (int j = 0; j < getNumRows(); j++) {
                        Tile t = getTile(i, j);
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
                        } else if (t instanceof Stockpile stockpile) {
                            writer.writeStartElement("stockpile");
                            writer.writeAttribute("col", Integer.toString(i));
                            writer.writeAttribute("row", Integer.toString(j));
                            writer.writeCharacters("\n");
                            for (Item item : stockpile.getAllItems()) {
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
                writer.writeAttribute("col", Integer.toString(getRobbiX()));
                writer.writeAttribute("row", Integer.toString(getRobbiY()));
                writer.writeCharacters("\n");
                writer.writeStartElement("facing");
                writer.writeAttribute("facing", getRobbiDirection().toString());
                writer.writeEndElement();
                writer.writeCharacters("\n");
                if (getRobbiItem() != null) {
                    writeItem(writer, getRobbiItem());
                }
            }
            writer.writeEndElement();
            writer.writeCharacters("\n");
            writer.writeEndElement();
            writer.writeCharacters("\n");
            writer.writeEndDocument();
            writer.close();

            return baos;
        } catch (FactoryConfigurationError | XMLStreamException e) {
            e.printStackTrace();
            logger.error("failed to save as XML.");
            return null;
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
     * Loads the dtd needed to save the territory as a xml-File from the resources
     * folder
     *
     * @return the dtd String defined in resources/xml/simulator.dtd
     */
    private Optional<String> getDTD() {
        Optional<Module> module = ModuleLayer.boot().findModule("RobbiSimulator");
        if (module.isEmpty()) {
            logger.warn("Could not load dtd");
            return Optional.empty();
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(module.get().getResourceAsStream("/xml/simulator.dtd")))) {
            StringBuilder builder = new StringBuilder();
            String s;
            while ((s = reader.readLine()) != null) {
                builder.append(s);
                builder.append(System.lineSeparator());
            }
            return Optional.of(builder.toString());
        } catch (IOException e) {
            logger.warn("Could not load dtd", e);
        }
        return Optional.empty();
    }

    /**
     * Getter for Robbis x Position.
     *
     * @return robbis x Position
     */
    public synchronized int getRobbiX() {
        return robbi.getX();
    }

    /**
     * Getter for Robbis y Position.
     *
     * @return robbis y Position
     */
    public synchronized int getRobbiY() {
        return robbi.getY();
    }

}
