package com.JayPi4c.RobbiSimulator.controller.program;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This class contains a list of Diagnostics, which are created during the
 * post-compile annotation check.
 * 
 * @author Jonas Pohl
 *
 */
public class Diagnostics {

	private List<Diagnostic> diags;

	/**
	 * Constructor for the Diagnostics-class. It creates the list to hold the
	 * diagnostics.
	 */
	public Diagnostics() {
		diags = new ArrayList<>();
	}

	/**
	 * Adds the given diagnostic to the list of diagnostics
	 * 
	 * @param d The Diagnostic to be added to the list of diagnostics
	 */
	public void add(Diagnostic d) {
		diags.add(d);
	}

	/**
	 * Returns a list of all collected Diagnostics.
	 * 
	 * @return the list of all diagnostics
	 */
	public List<Diagnostic> getDiagnosis() {
		return diags;
	}

	/**
	 * Container to hold a single Diagnostic, which consists of a type and the
	 * faulty value provided for this type.
	 * 
	 * @author Jonas Pohl
	 *
	 */
	public record Diagnostic(String type, String value) {

	}

}
