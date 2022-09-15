package com.JayPi4c.RobbiSimulator.model;

import jakarta.xml.bind.annotation.XmlAnyElement;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Robbi State to store all state relevant attributes in order to apply the
 * Memento-Pattern.
 * 
 * @author Jonas Pohl
 *
 */
@XmlRootElement
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RobbiState {

	@XmlElement
	private int x;

	@XmlElement
	private int y;

	@XmlElement
	private DIRECTION facing;

	@XmlAnyElement
	private Item item;

}
