package kaba4cow.taskell;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Engine;
import kaba4cow.console.ConsoleProgram;
import kaba4cow.taskell.project.Project;

public class AsciiTaskell extends ConsoleProgram implements MainProgram {

	public static Project project = null;

	public AsciiTaskell() {
		super(AsciiTaskell.class, "TASKELL by kaba4cow");
		Commands.init();
	}

	@Override
	public void init() {

	}

	@Override
	public void update(float dt) {
		updateConsole(project == null ? null : project.getName());
	}

	@Override
	public void render() {
		updateWindow();
		renderConsole();
	}

	public static void main(String[] args) {
		Engine.init("Taskell", 60);
		Engine.start(new AsciiTaskell());
	}

}
