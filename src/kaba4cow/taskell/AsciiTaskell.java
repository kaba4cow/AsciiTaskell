package kaba4cow.taskell;

import java.util.ArrayList;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Input;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.Colors;

public class AsciiTaskell implements MainProgram {

	private ArrayList<String> history;
	private int index;

	private String output;
	private String text;

	private int scroll;
	private int maxScroll;

	private int color = 0x000FFF;

	public AsciiTaskell() {

	}

	@Override
	public void init() {
		scroll = 0;
		maxScroll = 0;

		text = "";

		history = new ArrayList<>();
		index = 0;

		Command.processCommand("");
		output = "TASKELL by kaba4cow" + Command.getOutput();
	}

	@Override
	public void update(float dt) {
		scroll -= 2 * Mouse.getScroll();
		if (scroll < 0)
			scroll = 0;
		if (scroll > maxScroll)
			scroll = maxScroll;

		if (Keyboard.isKeyDown(Keyboard.KEY_ENTER)) {
			scroll = Integer.MAX_VALUE;
			maxScroll = Integer.MAX_VALUE;
			if (Command.processCommand(text))
				Engine.requestClose();
			String cmd = Command.getOutput();
			output += text + "\n" + cmd;
			if (history.isEmpty() || !history.get(history.size() - 1).equalsIgnoreCase(text))
				history.add(text);
			text = "";
			index = history.size();
		} else if (!history.isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			index--;
			if (index < 0)
				index = history.size() - 1;
			text = history.get(index);
		} else if (!history.isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			index++;
			if (index >= history.size())
				index = 0;
			text = history.get(index);
		} else
			text = Input.typeString(text);
	}

	@Override
	public void render() {
		int width = Command.getWindowWidth();
		int height = Command.getWindowHeight();
		if (Display.getWidth() != width || Display.getHeight() != height) {
			if (width < 0 || height < 0) {
				if (!Display.isFullscreen())
					Display.createFullscreen();
			} else
				Display.createWindowed(width, height);
		}

		color = Colors.combine(Command.getBackgroundColor(), Command.getForegroundColor());
		Display.setBackground(' ', color);

		int x = 0;
		int y = -scroll;
		for (int i = 0; i < output.length(); i++) {
			char c = output.charAt(i);
			if (c == '\n') {
				x = 0;
				y++;
			} else if (c == '\t')
				x += 4;
			else
				Drawer.drawChar(x++, y, c, color);
			if (x >= Display.getWidth()) {
				x = 0;
				y++;
			}
		}

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			Drawer.drawChar(x++, y, c, color);
			if (x >= Display.getWidth()) {
				x = 0;
				y++;
			}
		}

		y += scroll;
		if (y < Display.getHeight())
			maxScroll = 0;
		else
			maxScroll = y + 5 - Display.getHeight();
	}

	public static void main(String[] args) {
		Engine.init("Taskell", 30);

		int width = Command.getWindowWidth();
		int height = Command.getWindowHeight();
		if (width < 0 || height < 0)
			Display.createFullscreen();
		else
			Display.createWindowed(width, height);
		Display.setDrawCursor(false);
		Engine.start(new AsciiTaskell());
	}

}
