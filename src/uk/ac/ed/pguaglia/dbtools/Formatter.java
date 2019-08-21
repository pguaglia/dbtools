package uk.ac.ed.pguaglia.dbtools;

import java.util.Iterator;

public class Formatter {

	private String leftDelimiter;
	private String rightDelimiter;
	private String separator;

	public Formatter (String left, String right, String sep) {
		leftDelimiter = left;
		rightDelimiter = right;
		separator = sep;
	}

	public Formatter () {
		this("{","}",";");
	}

	public Formatter delimiters(String left, String right) {
		leftDelimiter = left;
		rightDelimiter = right;
		return this;
	}

	public Formatter noDelimiters() {
		return delimiters("", "");
	}

	public Formatter leftDelimiter(String s) {
		leftDelimiter = s;
		return this;
	}

	public Formatter rightDelimiter(String s) {
		rightDelimiter = s;
		return this;
	}

	public Formatter separator(String s) {
		separator = s;
		return this;
	}


	public String toString(Iterable<?> it) {
		String s = leftDelimiter;
		Iterator<? extends Object> iter = it.iterator();
		if (iter.hasNext()) {
			s += iter.next().toString();
			while (iter.hasNext()) {
				s += separator + iter.next().toString();
			}
		}
		s += rightDelimiter;
		return s;
	}
}
