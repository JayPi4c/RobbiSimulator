package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.Serializable;

/**
 * Wrapper to hold an answer instance.
 *
 * @param code      the code which is stored in the editor
 * @param territory the territory encoded as XML string
 * @author Jonas Pohl
 */
public record Answer(String code, String territory) implements Serializable {

}
