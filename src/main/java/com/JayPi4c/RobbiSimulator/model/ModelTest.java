package com.JayPi4c.RobbiSimulator.model;

import java.util.Scanner;

/**
 * Class to test the model in the console.
 * 
 * @author Jonas Pohl
 *
 */
public class ModelTest {

	/**
	 * Starts a test program, which allows a basic robbi movement in the console.
	 * 
	 * @param args does nothing
	 */
	public static void main(String... args) {
		Territory t = new Territory();
		Robbi robbi = t.getRobbi();

		Scanner scanner = new Scanner(System.in);

		t.placeAccu(3, 3);
		t.placeHollow(1, 0);
		t.placePileOfScrap(0, 2);
		t.placeHollow(0, 3);

		boolean running = true;
		do {
			t.print();
			System.out.println("Choose Command: v, t, l, n, s | b, h, p and q");
			char c = scanner.next().charAt(0);
			switch (c) {
				case 't':
					robbi.linksUm();
					break;
				case 'v':
					robbi.vor();
					break;
				case 'l':
					robbi.legeAb();
					break;
				case 'n':
					robbi.nehmeAuf();
					break;
				case 's':
					robbi.schiebeSchrotthaufen();
					break;
				case 'b':
					System.out.println("bag full: " + robbi.istTascheVoll());
					break;
				case 'h':
					System.out.println("is hollow ahead: " + robbi.vornKuhle());
					break;
				case 'p':
					System.out.println("is pile of scrap ahead: " + robbi.vornSchrotthaufen());
					break;
				case 'q':
					running = false;
					break;
				default:
					System.out.println("please enter a command!");
			}
		} while (running);

		scanner.close();
		System.out.println("done");
	}

}
