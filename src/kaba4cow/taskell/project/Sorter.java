package kaba4cow.taskell.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sorter implements Comparator<Task> {

	private static final Sorter instance = new Sorter();

	private static int parameter = 0;

	private Sorter() {

	}

	public static void sortByStatus(ArrayList<Task> tasks) {
		parameter = 0;
		Collections.sort(tasks, instance);
	}

	public static void sortByPriority(ArrayList<Task> tasks) {
		parameter = 1;
		Collections.sort(tasks, instance);
	}

	public static void sortByDescription(ArrayList<Task> tasks) {
		parameter = 2;
		Collections.sort(tasks, instance);
	}

	@Override
	public int compare(Task o1, Task o2) {
		if (parameter == 0)
			return Integer.compare(o1.getStatus().ordinal(), o2.getStatus().ordinal());
		if (parameter == 1)
			return Integer.compare(o2.getPriority(), o1.getPriority());
		return o1.getDescription().compareToIgnoreCase(o2.getDescription());
	}

}
