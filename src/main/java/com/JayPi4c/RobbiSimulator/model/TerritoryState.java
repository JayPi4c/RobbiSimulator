package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Territory State to store all state relevant attributes in order to apply the
 * Memento-Pattern.
 * 
 * @author Jonas Pohl
 *
 */
@XmlRootElement
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TerritoryState {

	@XmlElement
	private int numberOfColumns;
	@XmlElement
	private int numberOfRows;

	@XmlElement
	private Tile[][] tiles;
	@XmlElement
	private RobbiState robbiState;

	/**
	 * Constructor to create a Territory state. Creates an independet copy of the
	 * tiles array and the robbi.
	 * 
	 * @param numberOfColumns number of columns in the territory
	 * @param numberOfRows    number of rows in the territory
	 * @param tiles           the tiles array representing the territory
	 * @param robbi           the robbi in the territory
	 */
	public TerritoryState(int numberOfColumns, int numberOfRows, Tile[][] tiles, Robbi robbi) {
		this.numberOfColumns = numberOfColumns;
		this.numberOfRows = numberOfRows;
		this.tiles = new Tile[tiles.length][tiles[0].length];
		for (int i = 0; i < tiles.length; i++) {
			for (int j = 0; j < tiles[i].length; j++) {
				Tile t = tiles[i][j];
				Tile newTile;
				if (t instanceof Hollow) {
					newTile = new Hollow();
				} else if (t instanceof PileOfScrap) {
					newTile = new PileOfScrap();
				} else if (t instanceof Stockpile stockpile) {
					newTile = new Stockpile();
					for (Item item : stockpile.getAllItems())
						newTile.setItem(item);
				} else {
					newTile = new Tile();
					newTile.setItem(t.getItem());
				}
				this.tiles[i][j] = newTile;

			}
		}
		this.robbiState = new RobbiState(robbi.getX(), robbi.getY(), robbi.getFacing(), robbi.getItem());
	}

}
