package uk.ac.ed.pguaglia.dbtools;

import static org.fusesource.jansi.Ansi.ansi;

public class CommandLineIO {

	public void printLineIf(String s, boolean condition) {
		if (condition) {
			System.out.println(ansi().render(s));
		}
	}
}
