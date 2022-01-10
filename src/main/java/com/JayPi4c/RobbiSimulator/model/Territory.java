package com.JayPi4c.RobbiSimulator.model;

/**
 * 
 * This class contains all datastructures and utility functions to control the
 * territory.
 * 
 * last modified 08.11.2021
 * 
 * @author Jonas Pohl
 *
 */
public class Territory {
	private Robbi robbi;

	private Tile tiles[][];

	private static final int DEFAULT_NUMBER_OF_COLUMNS = 10;
	private static final int DEFAULT_NUMBER_OF_ROWS = 10;

	private int NUMBER_OF_COLUMNS = DEFAULT_NUMBER_OF_COLUMNS;
	private int NUMBER_OF_ROWS = DEFAULT_NUMBER_OF_ROWS;

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

	public Tile getTile(int x, int y) {
		x += NUMBER_OF_COLUMNS;
		x %= NUMBER_OF_COLUMNS;
		y += NUMBER_OF_ROWS;
		y %= NUMBER_OF_ROWS;
		return tiles[x][y];
	}

	public int getNumRows() {
		return NUMBER_OF_ROWS;
	}

	public int getNumCols() {
		return NUMBER_OF_COLUMNS;
	}

	public Robbi getRobbi() {
		return robbi;
	}

	public DIRECTION getRobbiDirection() {
		return robbi.getFacing();
	}

	private int normalizeCoord(int i, int bound) {
		i += bound;
		i %= bound;
		return i;
	}

	public boolean placeItem(Item item, int x, int y) {
		Tile t = tiles[normalizeCoord(x, NUMBER_OF_COLUMNS)][normalizeCoord(y, NUMBER_OF_ROWS)];
		if (t.getItem() != null && !(t instanceof Stockpile))
			return false;

		t.setItem(item);
		return true;
	}

	public Item getItem(int x, int y) {
		Tile t = tiles[normalizeCoord(x, NUMBER_OF_COLUMNS)][normalizeCoord(y, NUMBER_OF_ROWS)];
		return t.getItem();
	}

	public Item removeItem(int x, int y) {
		Tile t = tiles[normalizeCoord(x, NUMBER_OF_COLUMNS)][normalizeCoord(y, NUMBER_OF_ROWS)];
		return t.pickItem();
	}

	// ========= GUI FUNCTIONS ===========

	public void changeSize(int newCols, int newRows) {
		if (newCols <= 0 || newRows <= 0)
			throw new IllegalArgumentException("Diese Größe ist für das Territorium nicht zulässig");
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
	}

	public void placeRobbi(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if ((x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS) && !(tiles[x][y] instanceof Hollow)) {
			robbi.setPosition(x, y);
		}
	}

	public void placeHollow(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if ((x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS) && !(x == robbi.getX() && y == robbi.getY())) {
			tiles[x][y] = new Hollow();
		}
	}

	public void placePileOfScrap(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if (x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS)
			tiles[x][y] = new PileOfScrap();
	}

	public void placeStockpile(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if (x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS)
			tiles[x][y] = new Stockpile();
	}

	public void placeAccu(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if ((x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS))
			tiles[x][y].setItem(new Accu());

	}

	public void placeScrew(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if ((x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS))
			tiles[x][y].setItem(new Screw());
	}

	public void placeNut(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if ((x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS))
			tiles[x][y].setItem(new Nut());
	}

	public void clearTile(int x, int y) {
		x = normalizeCoord(x, NUMBER_OF_ROWS);
		y = normalizeCoord(y, NUMBER_OF_COLUMNS);
		if ((x < NUMBER_OF_ROWS && y <= NUMBER_OF_COLUMNS))
			tiles[x][y] = new Tile();
	}

	// ================== DEBUG =======

	protected void print() {

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

	public int getRobbiX() {
		return robbi.getX();
	}

	public int getRobbiY() {
		return robbi.getY();
	}

}
