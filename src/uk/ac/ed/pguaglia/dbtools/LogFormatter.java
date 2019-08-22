package uk.ac.ed.pguaglia.dbtools;

import static org.fusesource.jansi.Ansi.ansi;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LogFormatter extends Formatter {

	@Override
	public String format(LogRecord record) {
		return ansi().render(record.getMessage() + "\n").toString();
	}

}
