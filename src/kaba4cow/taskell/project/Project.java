package kaba4cow.taskell.project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Project {

	private String name;
	private String description;

	private ArrayList<Task> tasks;

	public Project(String name) {
		this.name = name;
		this.description = "";
		this.tasks = new ArrayList<>();
	}

	private Project(String name, BufferedReader reader) throws IOException {
		this(name);

		String line;
		char c;
		while ((line = reader.readLine()) != null) {
			if (line.length() <= 2)
				continue;
			c = line.charAt(0);
			line = line.substring(2);
			if (c == 'd' || c == 'D')
				description = line;
			else if (c == 't' || c == 'T') {
				c = line.charAt(0);
				Status status;
				if (c == 'n' || c == 'N')
					status = Status.NONE;
				else if (c == 'p' || c == 'P')
					status = Status.PROGRESS;
				else if (c == 'd' || c == 'D')
					status = Status.FINISHED;
				else
					status = Status.NONE;
				line = line.substring(2);
				c = line.charAt(0);
				int priority;
				try {
					priority = Integer.parseInt(c + "");
					if (priority < 0 || priority >= Task.priorities.length)
						priority = 1;
				} catch (NumberFormatException e) {
					priority = 1;
				}
				line = line.substring(2);
				addTask(line);
				Task task = tasks.get(tasks.size() - 1);
				task.setStatus(status);
				task.setPriority(priority);
			}
		}
	}

	public static Project load(String path, String name) {
		File file = new File(path + "/" + name);
		try (FileInputStream fis = new FileInputStream(file)) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
			return new Project(file.getName(), reader);
		} catch (IOException e) {
			return null;
		}
	}

	public boolean save(String path) {
		String string = "";
		if (!description.isBlank())
			string += "d " + description + "\n";
		for (Task task : tasks) {
			switch (task.getStatus()) {
			case FINISHED:
				string += "t d ";
				break;
			case NONE:
				string += "t n ";
				break;
			case PROGRESS:
				string += "t p ";
				break;
			}
			string += task.getPriority() + " " + task.getDescription() + "\n";
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path + "/" + name));
			writer.write(string);
			writer.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public boolean addTask(String description) {
		for (int i = 0; i < tasks.size(); i++)
			if (tasks.get(i).getDescription().equals(description))
				return false;
		tasks.add(new Task(description));
		return true;
	}

	public void removeTask(int index) {
		tasks.remove(index);
	}

	public boolean changeTaskDescription(int index, String description) {
		for (int i = 0; i < tasks.size(); i++)
			if (tasks.get(i).getDescription().equals(description))
				return false;
		tasks.get(index).setDescription(description);
		return true;
	}

	public void changeTaskStatus(int index, Status status) {
		tasks.get(index).setStatus(status);
	}

	public void sortByStatus() {
		Sorter.sortByStatus(tasks);
	}

	public void sortByPriority() {
		Sorter.sortByPriority(tasks);
	}

	public void sortByDescription() {
		Sorter.sortByDescription(tasks);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Task getTask(int index) {
		return tasks.get(index);
	}

	public ArrayList<Task> getTasks() {
		return tasks;
	}

	public int getNumberOfTasks() {
		return tasks.size();
	}

}
