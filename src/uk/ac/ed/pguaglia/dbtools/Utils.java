package uk.ac.ed.pguaglia.dbtools;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.ac.ed.pguaglia.dbtools.schema.FunctionalDependency;
import uk.ac.ed.pguaglia.dbtools.schema.Parser;

public class Utils {

	private static final Logger logger = Logger.getLogger(Utils.class.getName());

	public static Logger getLogger() {
		return logger;
	}

	public static String attributesToString(Set<String> attr) {
		Iterator<String> it = attr.iterator();
		String out = it.next();
		while (it.hasNext()) {
			out += String.format("%s%s", Parser.ATTRIBUTE_SEPARATOR, it.next());
		}
		return out;
	}

	public static <E> Set<E> singletonSet(E o) {
		Set<E> s = new HashSet<E>();
		s.add(o);
		return s;
	}

	public static int maxLengthIn(Collection<String> c) {
		int max = -1;
		for (String s : c) {
			if (s.length() > max) {
				max = s.length();
			}
		}
		return max;
	}

	public static Set<String> closure(Set<String> attributes, Set<FunctionalDependency> fds) {
		return closure(attributes, fds, false);
	}

	public static Set<String> closure(Set<String> attributes, Set<FunctionalDependency> fds, boolean verbose) {
		if (verbose == false) {
			logger.setLevel(Level.OFF);
		}
		logger.info("@|cyan <<< CLOSURE ALGORITHM (v1.0) >>>|@\n");
		logger.info("Initializing");
		HashSet<String> closure = new HashSet<String>(attributes);
		HashSet<FunctionalDependency> unused = new HashSet<FunctionalDependency>(fds);

		Formatter f = new Formatter().delimiters("{ ", " }");
		logger.info(String.format("\n\t@|blue CLOSURE|@ = %s", f.separator(", ").toString(closure)));
		logger.info(String.format("\t@|blue UNUSED |@ = %s\n", f.separator("; ").toString(unused)));

		while (unused.isEmpty() == false) {
			Iterator<FunctionalDependency> it = unused.iterator();
			boolean triggered = false;
			while (it.hasNext() && triggered == false) {
				FunctionalDependency fd = it.next();
				logger.info(String.format("Checking whether %s is applicable", fd));
				if (closure.containsAll(fd.getLeftHandSide())) {
					closure.addAll(fd.getRightHandSide());
					logger.info(String.format("@|green YES|@ ==> Adding rhs to CLOSURE and removing %s from UNUSED", fd));
					it.remove();
					triggered = true;
				} else {
					logger.info("@|red NO|@");
				}
				if (triggered) {
					logger.info(String.format("\n\t@|blue CLOSURE|@ = %s", f.separator(", ").toString(closure)));
					logger.info(String.format("\t@|blue UNUSED |@ = %s\n", f.separator("; ").toString(unused)));
				}
			}
			if (triggered == false) {
				logger.info("\nNo FDs in UNUSED are applicable: DONE");
				break;
			}
			if (unused.isEmpty()) {
				logger.info("\nNo more FDs in UNUSED: DONE");
			}
		}
		logger.info(String.format("\n\t@|blue CLOSURE|@ = %s", f.separator(", ").toString(closure)));
		return closure;
	}

	public static Set<FunctionalDependency> standardForm(Set<FunctionalDependency> fds) {
		return standardForm(fds, false);
	}

	public static Set<FunctionalDependency> standardForm(Set<FunctionalDependency> fds, boolean inPlace) {
		if (inPlace == false) {
			fds = new HashSet<FunctionalDependency>(fds);
		}
		HashSet<FunctionalDependency> newFDs = new HashSet<FunctionalDependency>();
		Iterator<FunctionalDependency> it = fds.iterator();
		while (it.hasNext()) {
			FunctionalDependency fd = it.next();
			if (fd.isInStandardForm() == false) {
				it.remove();
				Set<String> lhs = fd.getLeftHandSide();
				for (String a : fd.getRightHandSide()) {
					newFDs.add(new FunctionalDependency(lhs, Set.of(a)));
				}
			}
		}
		fds.addAll(newFDs);
		return fds;
	}

	public static Set<FunctionalDependency> minimizeLHS(Set<FunctionalDependency> fds) {
		return minimizeLHS(fds, false);
	}
	// can be simplified
	public static Set<FunctionalDependency> minimizeLHS(Set<FunctionalDependency> fds, boolean inPlace) {
		if (inPlace == false) {
			fds = new HashSet<FunctionalDependency>(fds);
		}
		FunctionalDependency newFD = null;
		boolean recurse = false;
		Iterator<FunctionalDependency> it = fds.iterator();
		while (it.hasNext() && recurse == false) {
			FunctionalDependency fd = it.next();
			Set<String> lhs = fd.getLeftHandSide();
			if (lhs.size() <= 1) {
				continue;
			}
			for (String a : lhs) {
				Set<String> diff  = new HashSet<String>(lhs);
				diff.remove(a);
				if (Utils.closure(diff, fds).contains(a)) {
					it.remove();
					newFD = new FunctionalDependency(diff, fd.getRightHandSide());
					recurse = true;
					break;
				}
			}
		}
		if (newFD != null) {
			fds.add(newFD);
			return minimizeLHS(fds, inPlace);
		}
		return fds;
	}

	public static Set<FunctionalDependency> removeRedundantFDs(Set<FunctionalDependency> fds) {
		return removeRedundantFDs(fds, false);
	}

	public static Set<FunctionalDependency> removeRedundantFDs(Set<FunctionalDependency> fds, boolean inPlace) {
		if (inPlace == false) {
			fds = new HashSet<FunctionalDependency>(fds);
		}
		Iterator<FunctionalDependency> it = fds.iterator();
		while (it.hasNext()) {
			FunctionalDependency fd = it.next();
			Set<FunctionalDependency> diff = new HashSet<FunctionalDependency>(fds);
			diff.remove(fd);
			if (fd.isEntailedBy(diff)) {
				it.remove();
			}
		}
		return fds;
	}
}
