package uk.ac.ed.pguaglia.dbtools;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.fusesource.jansi.AnsiConsole;

import uk.ac.ed.pguaglia.dbtools.schema.FunctionalDependency;
import uk.ac.ed.pguaglia.dbtools.schema.Parser;
import uk.ac.ed.pguaglia.dbtools.schema.RelationSchema;

public class CommandLineApp {

	private static final String INPUT_MSG = "@|cyan %s|@ (one per line, empty line when done):";
	private static final String SUCCESS_MSG = "@|green Successfully added \"%s\"|@";
	private static final String ERROR_MSG = "@|red %s|@";

	public static void main(String[] args) {
		AnsiConsole.systemInstall();
		Parser p = new Parser();
		Formatter f = new Formatter();
		CommandLineParser parser = new DefaultParser();

		if (args.length > 0) {
			String task = args[0].toLowerCase();
			if (task.equals(Task.CLOSURE.getName())) {
				try {
					CommandLine cmd = parser.parse(buildOptions(Task.CLOSURE), Arrays.copyOfRange(args, 1, args.length));
					args = cmd.getArgs();

					Set<String> attributes;
					Set<FunctionalDependency> fds;

					if (cmd.hasOption("i")) {
						fds = getInputFDs(p);
						attributes = getInputAttributes(p);
					} else {
						if (args.length != 2) {
							throw new Exception();
						}
						attributes = p.parseAttributeSet(args[0]);
						fds = p.parseFDSet(args[1]);
					}

					boolean verbose = cmd.hasOption("v");
					boolean interactive = cmd.hasOption("i");

					if (verbose) {
						Utils.closure(attributes, fds, true, interactive);
					} else if (interactive) {
						f.delimiters("{ "," }").separator(", ");
						CommandLineIO.printLine("@|blue CLOSURE|@ = %s", f.toString(Utils.closure(attributes, fds)));
					} else {
						f.noDelimiters().separator(",");
						CommandLineIO.printLine(f.toString(Utils.closure(attributes, fds)));
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (task.equals(Task.ENTAILMENT.getName())) {
				try {
					Set<FunctionalDependency> fds = p.parseFDSet((args[1]));
					FunctionalDependency fd = p.parseFunctionalDependency(args[2]);
					System.out.println(fd.isEntailedBy(fds));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (task.equals(Task.BCNF.getName())) {
				try {
					RelationSchema sch = new RelationSchema(p.parseAttributeSet(args[1]), p.parseFDSet((args[2])));
					System.out.println(sch.isInBCNF());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (task.equals(Task._3NF.getName())) {
				try {
					RelationSchema sch = new RelationSchema(p.parseAttributeSet(args[1]), p.parseFDSet((args[2])));
					System.out.println(sch.isIn3NF());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (task.equals(Task.MIN_COVER.getName())) {
				try {
					Set<FunctionalDependency> fds = p.parseFDSet((args[1]));
					f.noDelimiters().separator("; ");
					System.out.println(f.toString(Utils.removeRedundantFDs(Utils.minimizeLHS(Utils.standardForm(fds)))));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private static Options buildOptions(Task t) {
		Options opts = new Options();
		switch (t) {
		case CLOSURE:
			opts.addOption(Option.builder("v").longOpt("verbose").build());
			opts.addOption(Option.builder("i").longOpt("interactive").build());
			break;
		case BCNF:
			opts.addOption(Option.builder("a").longOpt("all").build());
			opts.addOption(Option.builder("v").longOpt("verbose").build());
			opts.addOption(Option.builder("d").longOpt("decompose").build());
			break;
		case ENTAILMENT:
			break;
		default:	
		}
		return opts;
	}

	private static Set<FunctionalDependency> getInputFDs(Parser p) {
		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
		for (String line = CommandLineIO.readLine(INPUT_MSG + "\n", "Functional dependencies"); line.isEmpty() == false; line = CommandLineIO.readLine()) {
			try {
				FunctionalDependency fd = p.parseFunctionalDependency(line);
				fds.add(fd);
				CommandLineIO.printLine(SUCCESS_MSG, fd);
			} catch (ParseException e) {
				CommandLineIO.printLine(ERROR_MSG, e.getMessage());
			}
		}
		return fds;
	}

	private static Set<String> getInputAttributes(Parser p) {
		Set<String> attributes = new HashSet<String>();
		for (String line = CommandLineIO.readLine(INPUT_MSG + "\n", "Attributes"); line.isEmpty() == false; line = CommandLineIO.readLine()) {
			try {
				String attr = p.parseAttribute(line);
				attributes.add(attr);
				CommandLineIO.printLine(SUCCESS_MSG, attr);
			} catch (ParseException e) {
				CommandLineIO.printLine(ERROR_MSG, e.getMessage());
			}
		}
		return attributes;
	}
}
