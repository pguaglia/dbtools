package uk.ac.ed.pguaglia.dbtools.schema;

import java.util.HashSet;
import java.util.Set;

import uk.ac.ed.pguaglia.dbtools.Utils;

public class RelationSchema {

	private Set<String> attributes;
	private Set<FunctionalDependency> fds;

	public RelationSchema(Set<String> attributes, Set<FunctionalDependency> fds) throws Exception {
		if (attributes.isEmpty()) {
			throw new Exception("A relation schema cannot have an empty set of attributes");
		} else {
			this.attributes = Set.of(attributes.toArray(new String[attributes.size()]));
		}
		for (FunctionalDependency fd : fds) {
			if ((attributes.containsAll(fd.getLeftHandSide()) && attributes.containsAll(fd.getRightHandSide())) == false) {
				throw new Exception("The FDs must mention only attributes in the relation schema");
			}
		}
		this.fds = Set.of(fds.toArray(new FunctionalDependency[fds.size()]));
	}

	public Set<String> getAttributes() {
		return attributes;
	}

	public Set<FunctionalDependency> getFunctionalDependencies() {
		return fds;
	}

	public boolean hasKey(Set<String> x) {
		return Utils.closure(x, fds).containsAll(attributes);
	}

	public Set<Set<String>> candidateKeys() {
		HashSet<String> rhsAttrs = new HashSet<String>();
		for (FunctionalDependency fd : fds) {
			rhsAttrs.addAll(fd.getRightHandSide());
		}
		HashSet<String> nonDerivableAttrs = new HashSet<String>(attributes);
		nonDerivableAttrs.removeAll(rhsAttrs);

		HashSet<Set<String>> toExpand = new HashSet<Set<String>>();
		HashSet<Set<String>> toCheck = new HashSet<Set<String>>();
		HashSet<Set<String>> ck = new HashSet<Set<String>>();

		if (nonDerivableAttrs.isEmpty() == false) {
			toCheck.add(nonDerivableAttrs);
		} else {
			for (String a : attributes) {
				toCheck.add(Utils.singletonSet(a));
			}
		}

		while (toCheck.isEmpty() == false) {
			//			System.out.println("To check: "+toCheck);
			for (Set<String> x : toCheck) {
				if (this.hasKey(x)) {
					ck.add(x);
					//					System.out.println("Added CK: " + x);
				} else {
					toExpand.add(x);
				}
			}
			toCheck.clear();
			//			System.out.println("To expand: "+toExpand);
			for (Set<String> s : toExpand) {
				for (String a : attributes) {
					if (s.contains(a)) {
						continue;
					}
					Set<String> s1 = new HashSet<String>(s);
					s1.add(a);
					boolean add = true;
					for (Set<String> k : ck) {
						if (s1.containsAll(k)) {
							add = false;
							break;
						}
					}
					if (add == true) {
						toCheck.add(s1);
					}
				}
			}
			toExpand.clear();
		}
		return ck;
	}

	public Set<String> primeAttributes() {
		HashSet<String> prime = new HashSet<String>();
		for (Set<String> ck : candidateKeys()) {
			prime.addAll(ck);
		}
		return prime;
	}

	public Set<FunctionalDependency> violationsBCNF() {
		HashSet<FunctionalDependency> violations = new HashSet<FunctionalDependency>();
		for (FunctionalDependency fd : fds) {
			if (fd.isTrivial() == false && this.hasKey(fd.getLeftHandSide()) == false) {
				violations.add(fd);
			}
		}
		return violations;
	}

	public boolean isInBCNF() {
		for (FunctionalDependency fd : fds) {
			if (fd.isTrivial() == false && this.hasKey(fd.getLeftHandSide()) == false) {
				return false;
			}
		}
		return true;
	}

	public Set<FunctionalDependency> violations3NF() {
		HashSet<FunctionalDependency> violations = new HashSet<FunctionalDependency>();
		Set<String> prime = primeAttributes();
		for (FunctionalDependency fd : violationsBCNF()) {
			if (prime.containsAll(fd.getRightHandSide()) == false) {
				violations.add(fd);
			}
		}
		return violations;
	}

	public boolean isIn3NF() {
		Set<String> prime = primeAttributes();
		for (FunctionalDependency fd : violationsBCNF()) {
			if (prime.containsAll(fd.getRightHandSide()) == false) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("(%s;%s)", attributes, fds);
	}
}
