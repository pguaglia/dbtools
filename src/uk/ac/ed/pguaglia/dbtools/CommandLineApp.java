package uk.ac.ed.pguaglia.dbtools;
import java.util.Set;

import uk.ac.ed.pguaglia.dbtools.schema.FunctionalDependency;
import uk.ac.ed.pguaglia.dbtools.schema.Parser;
import uk.ac.ed.pguaglia.dbtools.schema.RelationSchema;

public class CommandLineApp {

	public static void main(String[] args) {
		Parser p = new Parser();
		Formatter f = new Formatter();
				
		CommandLineIO io = new CommandLineIO();

		if (args.length > 0) {
			String task = args[0].toLowerCase();
			if (task.equals(Task.CLOSURE.getName())) {
				try {
					Set<String> attributes = p.parseAttributeSet(args[1]);
					Set<FunctionalDependency> fds = p.parseFDSet((args[2]));
					f.noDelimiters().separator(",");
					f.toString(Utils.closure(attributes, fds, true, io));
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
}
