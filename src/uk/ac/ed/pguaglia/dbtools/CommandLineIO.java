package uk.ac.ed.pguaglia.dbtools;

import static org.fusesource.jansi.Ansi.ansi;

public class CommandLineIO {

	public static void printLine(String format, Object ... args) {
		format += "\n";
		System.console().printf(ansi().render(format, args).toString());
	}

	public static void printLineIf(boolean condition, String format, Object ... args) {
		if (condition) {
			printLine(format, args);
		}
	}

	public static void printLinesIf(boolean condition, String ... lines) {
		if (condition == false) {
			return;
		}
		for (String line : lines) {
			printLine(line);
		}
	}

	public static String readLine() {
		return System.console().readLine();
	}

	public static String readLine(String format, Object ... args) {
		return System.console().readLine(ansi().render(format, args).toString());
	}

	public static String readLineIf(boolean condition) {
		if (condition) {
			return readLine();
		}
		return null;
	}

	public static String readLineIf(boolean condition, String format, Object ... args) {
		if (condition) {
			readLine(format, args);
		}
		return null;
	}
}
