package uk.ac.ed.pguaglia.dbtools.schema;

import java.util.Set;

import uk.ac.ed.pguaglia.dbtools.Utils;

public class FunctionalDependency {

	private Set<String> lhs;
	private Set<String> rhs;

	public FunctionalDependency (Set<String> lhs, Set<String> rhs) {
		this.lhs = Set.of(lhs.toArray(new String[lhs.size()]));
		this.rhs = Set.of(rhs.toArray(new String[rhs.size()]));
	}

	public Set<String> getLeftHandSide() {
		return lhs;
	}
	
	public Set<String> getRightHandSide() {
		return rhs;
	}
	
	public boolean isTrivial() {
		return lhs.containsAll(rhs);
	}
	
	public boolean isInStandardForm() {
		return rhs.size() == 1;
	}

	@Override
	public String toString() {
		return String.format("%s%s%s", Utils.attributesToString(lhs), Parser.LEFT_RIGHT_SEP, Utils.attributesToString(rhs));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof FunctionalDependency == false) {
			return false;
		} else {
			FunctionalDependency fd = (FunctionalDependency) o;
			return lhs.equals(fd.lhs) && rhs.equals(fd.rhs);
		}
	}
	
	public boolean isEntailedBy(Set<FunctionalDependency> sigma) {
		return Utils.closure(lhs, sigma).containsAll(rhs);
	}
	
//	public Set<FunctionalDependency> toStandardForm() {
//		Set<FunctionalDependency> fds = new HashSet<FunctionalDependency>();
//		for (String a : rhs) {
//			fds.add(new FunctionalDependency(lhs, Set.of(a)));
//		}
//		return fds;
//	}
}
