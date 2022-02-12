package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.Serializable;

/**
 * Wrapper to hold an answer instance.
 * 
 * @author Jonas Pohl
 * @param code      the code which is stored in the editor
 * @param territory the territory encoded as XML string
 */
public record Answer(String code, String territory) implements Serializable {

}
