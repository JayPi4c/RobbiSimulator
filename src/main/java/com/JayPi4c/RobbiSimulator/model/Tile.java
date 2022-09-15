package com.JayPi4c.RobbiSimulator.model;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * The default Tile for the territory.
 * 
 * @author Jonas Pohl
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Tile implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Attribute to store the item whoch is placed on the tile.
	 */
	@Getter
	@Setter
	@XmlAnyElement(lax = true)
	private Item item = null;

	/**
	 * Removes the item from the tile and returns it.
	 * 
	 * @return the item stored on the tile
	 */
	public Item pickItem() {
		Item it = item;
		item = null;
		return it;
	}

}
