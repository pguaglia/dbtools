package uk.ac.ed.pguaglia.dbtools.schema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.cli.ParseException;

public class Parser {

	public static final String ATTRIBUTE_SEPARATOR = ",";
	public static final String FD_SEPARATOR = ";";
	public static final String ATTRIBUTE_REGEX = "[a-zA-Z]\\w*";
	public static final String LEFT_RIGHT_SEP = "->";

	private String attrListRegex = String.format( "\\s*%s\\s*(\\s*%s\\s*%s\\s*)*",
			Parser.ATTRIBUTE_REGEX, Parser.ATTRIBUTE_SEPARATOR, Parser.ATTRIBUTE_REGEX );
	private String fdRegex = String.format("%s%s%s", attrListRegex, Parser.LEFT_RIGHT_SEP, attrListRegex);


	public String parseAttribute(String input) throws ParseException {
		input = input.trim();
		if (input.matches(ATTRIBUTE_REGEX) == false) {
			throw new ParseException(String.format("Invalid attribute name: \"%s\"", input));
		}
		return input;
	}

	public List<String> parseAttributeList(String input) throws ParseException {
		ArrayList<String> attributes = new ArrayList<String>();
		for (String a : Arrays.asList(input.split(Parser.ATTRIBUTE_SEPARATOR, -1))) {
			attributes.add(parseAttribute(a));
		}
		return attributes;
	}

	public Set<String> parseAttributeSet(String input) throws ParseException {
		return new HashSet<String>(parseAttributeList(input));
	}

	public Set<FunctionalDependency> parseFDSet(String input) throws ParseException {
		return parseFDSet(input.split(Parser.FD_SEPARATOR, -1));
	}

	public Set<FunctionalDependency> parseFDSet(String[] input) throws ParseException {
		HashSet<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		for (String fd : input) {
			fds.add(parseFunctionalDependency(fd));
		}
		return fds;
	}

	public FunctionalDependency parseFunctionalDependency(String input) throws ParseException {
		if (input.matches(fdRegex) == false) {
			throw new ParseException(String.format("Cannot parse functional dependency: \"%s\"", input));
		}
		String[] attrLists = input.split(Parser.LEFT_RIGHT_SEP, -1);
		return new FunctionalDependency(parseAttributeSet(attrLists[0]),parseAttributeSet(attrLists[1]));
	}
}
