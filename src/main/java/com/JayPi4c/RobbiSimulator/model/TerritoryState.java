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
     * Constructor to create a Territory state. Creates an independent copy of the
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
                Tile newTile = switch (tiles[i][j]) {
                    case Hollow ignored:
                        yield new Hollow();
                    case PileOfScrap ignored:
                        yield new PileOfScrap();
                    case Stockpile stockpile:
                        Stockpile s = new Stockpile();
                        for (Item item : stockpile.getAllItems())
                            s.setItem(item);
                        yield s;
                    case Tile t:
                        Tile t2 = new Tile();
                        t2.setItem(t.getItem());
                        yield t2;
                };
                this.tiles[i][j] = newTile;
            }
        }
        this.robbiState = new RobbiState(robbi.getX(), robbi.getY(), robbi.getFacing(), robbi.getItem());
    }

}
