package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;

/**
 * Class representing a PileOfScrap Tile.
 *
 * @author Jonas Pohl
 */
@XmlRootElement
public class PileOfScrap extends Tile {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Setter to place an item on a PileOfScrap. Since the item placed on a pile of
     * scrap, the item just vanishes.
     *
     * @param item the item to vanish
     */
    @Override
    public void setItem(Item item) {
        // Item vanishes
    }

    /**
     * Getter for the item placed on the tile.
     *
     * @return the item placed on this tile. All items placed on a pile of scrap
     * vanish, therefore the item returned will be null.
     */
    @Override
    public Item getItem() {
        return null;
    }

}
