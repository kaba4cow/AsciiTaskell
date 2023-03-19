package kaba4cow.taskell.project;

public class Task {

	public static final String[] priorities = { "Low", "Medium", "High" };

	private String description;
	private Status status;
	private int priority;

	public Task(String description) {
		this.description = description;
		this.status = Status.NONE;
		this.priority = 1;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

}
