package kaba4cow.taskell;

import kaba4cow.console.Command;
import kaba4cow.console.Console;
import kaba4cow.taskell.project.Project;
import kaba4cow.taskell.project.Status;
import kaba4cow.taskell.project.Task;

public final class Commands {

	private Commands() {

	}

	public static void init() {
		new Command("proj-create", "[name]", "Creates a project with specified name") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 1, output))
					return;

				output.append("Project created\n");
				AsciiTaskell.project = new Project(parameters[0]);
			}
		};

		new Command("proj-open", "[name]", "Opens a project with specified name") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 1, output))
					return;

				Project project = Project.load(Console.getDirectory().getAbsolutePath(), parameters[0]);
				if (project == null)
					output.append("File not found\n");
				else {
					output.append("Project opened\n");
					AsciiTaskell.project = project;
				}
			}
		};

		new Command("proj-save", "", "Saves current project") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else if (AsciiTaskell.project.save(Console.getDirectory().getAbsolutePath()))
					output.append("Project saved\n");
				else
					output.append("Could not save the project\n");
			}
		};

		new Command("proj-rename", "[name]", "Renames current project") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 1, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else {
					String name = parameters[0];
					AsciiTaskell.project.setName(name);
				}
			}
		};

		new Command("proj-desc", "[description]", "Adds description to the current project") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 1, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else {
					String description = parameters[0];
					AsciiTaskell.project.setDescription(description);
				}
			}
		};

		new Command("proj-info", "", "Lists all tasks in the current project") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else {
					output.append("Project: " + AsciiTaskell.project.getName() + "\n");
					output.append("Description: " + AsciiTaskell.project.getDescription() + "\n");
					if (AsciiTaskell.project.getNumberOfTasks() == 0) {
						output.append("Tasks: none\n");
						return;
					}
					output.append("Tasks (" + AsciiTaskell.project.getNumberOfTasks() + "):\n\n");
					output.append(
							String.format("%5s | %8s | %11s | %s ", "Index", "Priority", "Status", "Description"));
					output.append('\n');
					for (int i = 0; i < 45; i++)
						output.append('-');
					output.append('\n');
					for (int i = 0; i < AsciiTaskell.project.getNumberOfTasks(); i++) {
						Task task = AsciiTaskell.project.getTask(i);
						output.append(String.format("%5s | %8s | %11s | %s ", i, Task.priorities[task.getPriority()],
								task.getStatus().getName(), task.getDescription()));
						output.append('\n');
						output.append(String.format("%5s | %8s | %11s |", "", "", ""));
						output.append('\n');
					}
				}
			}
		};

		new Command("proj-sort", "[parameter]",
				"Sorts tasks in the current project (s - by status, p - by priority, d - by description)") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 1, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else if (parameters[0].equalsIgnoreCase("s"))
					AsciiTaskell.project.sortByStatus();
				else if (parameters[0].equalsIgnoreCase("p"))
					AsciiTaskell.project.sortByPriority();
				else if (parameters[0].equalsIgnoreCase("d"))
					AsciiTaskell.project.sortByDescription();
				else
					invalidParameters(output);
			}
		};

		new Command("task-add", "[name]", "Creates new task in the current project") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else if (!AsciiTaskell.project.addTask(parameters[0]))
					output.append("Task already exists\n");
			}
		};

		new Command("task-remove", "[index]", "Removes a task from project") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 1, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else
					try {
						int index = Integer.parseInt(parameters[0]);
						AsciiTaskell.project.removeTask(index);
					} catch (Exception e) {
						invalidParameters(output);
					}
			}
		};

		new Command("task-status", "[index] [status]",
				"Sets a status of specified task (none - N, in progress - P, finished - F)") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 2, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else
					try {
						int index = Integer.parseInt(parameters[0]);
						Status status;
						if (parameters[1].equalsIgnoreCase("n"))
							status = Status.NONE;
						else if (parameters[1].equalsIgnoreCase("p"))
							status = Status.PROGRESS;
						else if (parameters[1].equalsIgnoreCase("f"))
							status = Status.FINISHED;
						else {
							invalidParameters(output);
							return;
						}
						AsciiTaskell.project.getTask(index).setStatus(status);
					} catch (Exception e) {
						invalidParameters(output);
					}
			}
		};

		new Command("task-priority", "[index] [priority]", "Sets priority of specified task (LOW, NORMAL, HIGH)") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 2, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else
					try {
						int index = Integer.parseInt(parameters[0]);
						for (int priority = 0; priority < Task.priorities.length; priority++)
							if (Task.priorities[priority].equalsIgnoreCase(parameters[1]))
								AsciiTaskell.project.getTask(index).setPriority(priority);
					} catch (Exception e) {
						invalidParameters(output);
					}
			}
		};

		new Command("task-desc", "[index] [description]", "Adds description to the specified task") {
			@Override
			public void execute(String[] parameters, int numParameters, StringBuilder output) {
				if (invalidParameters(numParameters, 2, output))
					return;

				if (AsciiTaskell.project == null)
					output.append("No project selected\n");
				else
					try {
						int index = Integer.parseInt(parameters[0]);
						String description = parameters[1];
						AsciiTaskell.project.getTask(index).setDescription(description);
					} catch (Exception e) {
						invalidParameters(output);
					}
			}
		};
	}

}
