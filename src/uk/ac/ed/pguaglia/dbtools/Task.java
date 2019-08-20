package uk.ac.ed.pguaglia.dbtools;

public enum Task {
	CLOSURE 		("closure"),
	BCNF	 		("bcnf"),
	_3NF			("3nf"),
	ENTAILMENT 	("entails"),
	HELP 		("help"),
	MIN_COVER	("min-cover");

	private final String name;

	private Task (String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
