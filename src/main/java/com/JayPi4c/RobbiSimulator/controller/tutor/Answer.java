package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.Serializable;

/**
 * Wrapper to hold an answer instance.
 * 
 * @author Jonas Pohl
 *
 */
public record Answer(String code, String territory) implements Serializable {

}
