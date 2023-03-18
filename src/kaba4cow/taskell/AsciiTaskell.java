package kaba4cow.taskell;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.util.ArrayList;

import kaba4cow.ascii.MainProgram;
import kaba4cow.ascii.core.Display;
import kaba4cow.ascii.core.Engine;
import kaba4cow.ascii.drawing.drawers.Drawer;
import kaba4cow.ascii.input.Keyboard;
import kaba4cow.ascii.input.Mouse;
import kaba4cow.ascii.toolbox.Colors;

public class AsciiTaskell implements MainProgram {

	private static final char BACKSPACE = 0x0008;
	private static final char DELETE = 0x007F;

	private ArrayList<String> history;
	private int index;

	private StringBuilder builder;

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

		builder = new StringBuilder();
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
			else
				builder = new StringBuilder();
			String cmd = Command.getOutput();
			output += text + "\n" + cmd;
			if (history.isEmpty() || !history.get(history.size() - 1).equalsIgnoreCase(text))
				history.add(text);
			index = history.size();
		} else if (!history.isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			index--;
			if (index < 0)
				index = history.size() - 1;
			builder = new StringBuilder(history.get(index));
		} else if (!history.isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			index++;
			if (index >= history.size())
				index = 0;
			builder = new StringBuilder(history.get(index));
		} else if (Keyboard.isKey(Keyboard.KEY_CONTROL_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_V)) {
			try {
				String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard()
						.getData(DataFlavor.stringFlavor);
				data = data.replace('\t', ' ').replace('\r', ' ').replace('\n', ' ');
				builder.append(data);
			} catch (Exception e) {
			}
		} else if (Keyboard.getLastTyped() != null) {
			char c = Keyboard.getLastTyped().getKeyChar();
			if (c == BACKSPACE && builder.length() > 0)
				builder.deleteCharAt(builder.length() - 1);
			else if (c >= 32 && c < DELETE)
				builder.append(c);
			Keyboard.resetLastTyped();
		}

		text = builder.toString();
	}

	@Override
	public void render() {
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
		Display.createFullscreen();
		Display.setDrawCursor(false);
		Engine.start(new AsciiTaskell());
	}

}
