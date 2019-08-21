package uk.ac.ed.pguaglia.dbtools;
import java.util.Set;

import uk.ac.ed.pguaglia.dbtools.schema.FunctionalDependency;
import uk.ac.ed.pguaglia.dbtools.schema.Parser;
import uk.ac.ed.pguaglia.dbtools.schema.RelationSchema;

public class CommandLineApp {

	public static void main(String[] args) {
		Parser p = new Parser();
		Formatter f = new Formatter();

		if (args.length > 0) {
			String task = args[0].toLowerCase();
			if (task.equals(Task.CLOSURE.getName())) {
				//if (args[1].toLowerCase().equals("help")) {
				//System.out.println("USAGE\nclosure <attributes> <fds>\nwhere:\n\t<attributes>\tis a comma-separated list of attributes (repetitions allowed but ignored)\n\t<fds>\t\tis a semicolon (;) separated list of functional dependencies");
				//} else {
				try {
					Set<String> attributes = p.parseAttributeSet(args[1]);
					Set<FunctionalDependency> fds = p.parseFDSet((args[2]));
					f.noDelimiters().separator(",");
					System.out.println(f.toString(Utils.closure(attributes, fds)));
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
		//		System.exit(0);
		//
		//		// create the command line parser
		//		CommandLineParser cliParser = new DefaultParser();
		//
		//		// create the Options
		//		Options options = new Options();
		//		options.addOption( Option.builder("i").build() );
		//
		//		System.out.println(args[0]);
		//		System.out.println(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
		//		HelpFormatter formatter = new HelpFormatter();
		//		formatter.printHelp( "<TASK>", options );
	}
}
