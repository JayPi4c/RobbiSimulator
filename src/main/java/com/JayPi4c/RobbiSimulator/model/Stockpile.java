package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Stockpile tile.
 *
 * @author Jonas Pohl
 */
@XmlRootElement
public class Stockpile extends Tile {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Attribute to store all items placed on the tile
     */
    @XmlElementWrapper
    @XmlAnyElement(lax = true)
    private ArrayList<Item> items;
    // TODO Queue more efficient?
    // check if queue would be better, allowing an easier iteration over the
    // elements.

    /**
     * Constructor to create a new Stockpile and initialize the list of items on the
     * stockpile.
     */
    public Stockpile() {
        items = new ArrayList<>();
    }

    /**
     * Adds the given item to the list of items on the stockpile.
     *
     * @param item the item to add to the stockpile
     */
    @Override
    public void setItem(Item item) {
        items.add(item);
    }

    /**
     * Iterates through the list of items in the stockpile and returns one.
     *
     * @return the last item added to the stockpile, null if no item is placed on
     * the stockpile
     */
    @Override
    public Item getItem() {
        return (items.isEmpty()) ? null : items.get(items.size() - 1);
    }

    /**
     * Removes and returns the last item from the stockpile.
     *
     * @return the last item added to the stockpile, null if no item was added yet
     */
    @Override
    public Item pickItem() {
        return items.remove(items.size() - 1);
    }

    /**
     * Returns the full list of items placed on the stockpile.
     *
     * @return the arrayList containing all items on this stockpile
     */
    public List<Item> getAllItems() {
        return items;
    }

}
