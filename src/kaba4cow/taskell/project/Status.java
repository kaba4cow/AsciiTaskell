package kaba4cow.taskell.project;

public enum Status {

	PROGRESS("In progress"), NONE("None       "), FINISHED("Finished   ");

	private final String name;

	private Status(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
