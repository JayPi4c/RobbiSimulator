package com.JayPi4c.RobbiSimulator.controller.tutor;

import java.io.Serializable;

public record Request(int id, String code, String territory) implements Serializable {

}
