package com.JayPi4c.RobbiSimulator.model;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.JayPi4c.RobbiSimulator.utils.Observable;

/**
 * 
 * This class contains all datastructures and utility functions to control the
 * territory.
 * 
 * @author Jonas Pohl
 *
 */
//@XmlRootElement
public class Territory extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(Territory.class);

	private transient Robbi robbi;

	/**
	 * Array-attribute to store the real territory
	 */
	private Tile tiles[][];
	/**
	 * Attribute to save if the territory has changed.
	 */
	private boolean sizeChanged = false;

	private static final int DEFAULT_NUMBER_OF_COLUMNS = 6;
	private static final int DEFAULT_NUMBER_OF_ROWS = 6;

	/**
	 * Attribute to store the current number of columns of the territory.
	 */
	private int NUMBER_OF_COLUMNS = DEFAULT_NUMBER_OF_COLUMNS;
	/**
	 * Attribute to store the current number of rows of the territory.
	 */
	private int NUMBER_OF_ROWS = DEFAULT_NUMBER_OF_ROWS;

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
		return new TerritoryState(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS, tiles, robbi);
	}

	/**
	 * Restores the territory from a memento object.
	 * 
	 * @param state the Memento object of the state the territory should restore to.
	 */
	public synchronized void restore(TerritoryState state) {
		NUMBER_OF_COLUMNS = state.getNumberOfColumns();
		NUMBER_OF_ROWS = state.getNumberOfRows();
		RobbiState robbiState = state.getRobbiState();
		this.robbi.setPosition(robbiState.getX(), robbiState.getY());
		this.robbi.setItem(robbiState.getItem());
		this.robbi.setFacing(robbiState.getFacing());
		this.tiles = state.getTiles();
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
		x += NUMBER_OF_COLUMNS;
		x %= NUMBER_OF_COLUMNS;
		y += NUMBER_OF_ROWS;
		y %= NUMBER_OF_ROWS;
		return tiles[x][y];
	}

	/**
	 * Getter for the current number of rows.
	 * 
	 * @return the current number of rows
	 */
	public synchronized int getNumRows() {
		return NUMBER_OF_ROWS;
	}

	/**
	 * Getter for the current number of columns.
	 * 
	 * @return the current number of columns
	 */
	public synchronized int getNumCols() {
		return NUMBER_OF_COLUMNS;
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
	 * Getter for the current robbi in the territory.
	 * 
	 * @return the current robbi
	 */
	public synchronized Robbi getRobbi() {
		return robbi;
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
	 *         one.
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
	 *         false otherwise
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
	public synchronized boolean placeItem(Item item, int x, int y) {
		Tile t = tiles[normalizeCoord(x, NUMBER_OF_COLUMNS)][normalizeCoord(y, NUMBER_OF_ROWS)];
		if (t.getItem() != null && !(t instanceof Stockpile))
			return false;

		t.setItem(item);
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
		Tile t = tiles[normalizeCoord(x, NUMBER_OF_COLUMNS)][normalizeCoord(y, NUMBER_OF_ROWS)];
		return t.getItem();
	}

	/**
	 * Removes the item from the given position.
	 * 
	 * @param x x-ordinate in the territory
	 * @param y y-ordinate in the territory
	 * @return the item that has been removed from the tile
	 */
	public synchronized Item removeItem(int x, int y) {
		Tile t = tiles[normalizeCoord(x, NUMBER_OF_COLUMNS)][normalizeCoord(y, NUMBER_OF_ROWS)];
		setChanged();
		notifyAllObservers();
		return t.pickItem();
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
	 */
	public synchronized void update(Territory territory, Item item, int x, int y, DIRECTION facing) {
		this.tiles = territory.tiles;
		this.sizeChanged = territory.sizeChanged;
		this.NUMBER_OF_COLUMNS = territory.NUMBER_OF_COLUMNS;
		this.NUMBER_OF_ROWS = territory.NUMBER_OF_ROWS;
		this.robbi.setPosition(x, y);
		this.robbi.setFacing(facing);
		this.robbi.setItem(item);
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
	public synchronized void changeSize(int newCols, int newRows) {
		if (newCols <= 0 || newRows <= 0)
			throw new IllegalArgumentException("Diese Größe ist für das Territorium nicht zulässig");
		if (newCols != NUMBER_OF_COLUMNS || newRows != NUMBER_OF_ROWS)
			sizeChanged = true;
		else
			return;
		NUMBER_OF_COLUMNS = newCols;
		NUMBER_OF_ROWS = newRows;
		// create the new territory
		Tile newTiles[][] = new Tile[NUMBER_OF_COLUMNS][NUMBER_OF_ROWS];
		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				newTiles[i][j] = new Tile();
			}
		}

		// copy the old territory into the new
		for (int i = 0; i < tiles.length && i < newCols; i++) {
			for (int j = 0; j < tiles[i].length && j < newRows; j++) {
				newTiles[i][j] = tiles[i][j];
			}
		}

		if (robbi.getX() >= NUMBER_OF_COLUMNS || robbi.getY() > NUMBER_OF_ROWS) {
			if (newTiles[0][0] instanceof Hollow)
				newTiles[0][0] = new Tile();
			robbi.setPosition(0, 0);
		}
		tiles = newTiles;

		logger.debug("updated size to {}x{}", NUMBER_OF_COLUMNS, NUMBER_OF_ROWS);

		setChanged();
		notifyAllObservers();
	}

	/**
	 * move robbi to the given position.
	 * 
	 * @param x robbis new x-position
	 * @param y robbis new y-position
	 */
	public synchronized void placeRobbi(int x, int y) {
		if ((x >= 0 && x < NUMBER_OF_COLUMNS && y >= 0 && y < NUMBER_OF_ROWS) && !(tiles[x][y] instanceof Hollow)) {
			robbi.setPosition(x, y);
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
	public synchronized void placeHollow(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if ((x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) && !(x == robbi.getX() && y == robbi.getY())) {
			tiles[x][y] = new Hollow();
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
	public synchronized void placePileOfScrap(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if (x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) {
			tiles[x][y] = new PileOfScrap();
			setChanged();
			notifyAllObservers();
		}
	}

	/**
	 * Place a new Stockpile at the given position if it is in bounds.
	 * 
	 * @param x Stockpile x-Position
	 * @param y Stockpile y-Position
	 */
	public synchronized void placeStockpile(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if (x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) {
			tiles[x][y] = new Stockpile();
			setChanged();
			notifyAllObservers();
		}
	}

	/**
	 * Place a new Accu on the given tile if it is in bounds.
	 * 
	 * @param x Accu x-Position
	 * @param y Accu y-Position
	 */
	public synchronized void placeAccu(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if (x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) {
			tiles[x][y].setItem(new Accu());
			setChanged();
			notifyAllObservers();
		}

	}

	/**
	 * Place a new Screw on the given tile if it is in bounds.
	 * 
	 * @param x Screw x-Position
	 * @param y Screw y-Position
	 */
	public synchronized void placeScrew(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if (x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) {
			tiles[x][y].setItem(new Screw());
			setChanged();
			notifyAllObservers();
		}
	}

	/**
	 * Place a new Nut on the given tile if it is in bounds.
	 * 
	 * @param x Nut x-Position
	 * @param y Nut y-Position
	 */
	public synchronized void placeNut(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if (x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) {
			tiles[x][y].setItem(new Nut());
			setChanged();
			notifyAllObservers();
		}
	}

	/**
	 * Removes all elements from the tile and replaces it with a new default tile.
	 * 
	 * @param x Tile x-Position
	 * @param y Tile y-Position
	 */
	public synchronized void clearTile(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_COLUMNS);
		y = normalizeCoord(y, NUMBER_OF_ROWS);
		if (x < NUMBER_OF_COLUMNS && y < NUMBER_OF_ROWS) {
			tiles[x][y] = new Tile();
			setChanged();
			notifyAllObservers();
		}
	}

	// ================== DEBUG =======
	/**
	 * Debug method to print the territory in the console.
	 */
	public void print() {

		for (int i = 0; i < NUMBER_OF_COLUMNS; i++) {
			for (int j = 0; j < NUMBER_OF_ROWS; j++) {
				if (j == robbi.getX() && i == robbi.getY()) {
					robbi.print();
					continue;
				}
				Tile t = tiles[j][i];
				if (t instanceof Hollow)
					System.out.print("H");
				else if (t instanceof Stockpile)
					System.out.print("k");
				else if (t instanceof PileOfScrap)
					System.out.print("P");
				else if (t.getItem() == null)
					System.out.print("+");
				else if (t.getItem() instanceof Screw)
					System.out.print("s");
				else if (t.getItem() instanceof Accu)
					System.out.print("A");
				else if (t.getItem() instanceof Nut)
					System.out.print("N");
			}
			System.out.println();
		}
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
