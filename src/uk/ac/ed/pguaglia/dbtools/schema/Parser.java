package uk.ac.ed.pguaglia.dbtools.schema;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Parser {

	public static final String ATTRIBUTE_SEPARATOR = ",";
	public static final String FD_SEPARATOR = ";";
	public static final String ATTRIBUTE_REGEX = "[a-zA-Z]\\w*";
	public static final String LEFT_RIGHT_SEP = "->";

	private String attrListRegex = String.format("%s(%s%s)*", Parser.ATTRIBUTE_REGEX, Parser.ATTRIBUTE_SEPARATOR, Parser.ATTRIBUTE_REGEX);
	private String fdRegex = String.format("%s%s%s", attrListRegex, Parser.LEFT_RIGHT_SEP, attrListRegex);
	private String fdListRegex = String.format("%s(%s%s)*", fdRegex, Parser.FD_SEPARATOR, fdRegex);

	public Parser () {
	}

	public List<String> parseAttributeList(String input) throws Exception {
		input = input.replaceAll("\\s+", "");
		if (input.matches(attrListRegex) == false) {
			throw new Exception();
		}
		ArrayList<String> attributes = new ArrayList<String>();
		for (String a : Arrays.asList(input.split(Parser.ATTRIBUTE_SEPARATOR))) {
			attributes.add(a);
		}
		return attributes;
	}

	public Set<String> parseAttributeSet(String input) throws Exception {
		return new HashSet<String>(parseAttributeList(input));
	}

	public Set<FunctionalDependency> parseFDSet(String input) throws Exception {
		input = input.replaceAll("\\s+", "");
		if (input.matches(fdListRegex) == false) {
			throw new Exception();
		}
		String[] fdsList = input.split(Parser.FD_SEPARATOR);
		HashSet<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		for (String fd : fdsList) {
			fds.add(parseFunctionalDependency(fd));
		}
		return fds;
	}

	public FunctionalDependency parseFunctionalDependency(String input) throws Exception {
		input = input.replaceAll("\\s+", "");
		if (input.matches(fdRegex) == false) {
			throw new Exception();
		}
		String[] attrLists = input.split(Parser.LEFT_RIGHT_SEP);
		return new FunctionalDependency(parseAttributeSet(attrLists[0]),parseAttributeSet(attrLists[1]));
	}
}
