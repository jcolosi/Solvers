/**
 * Playtomo is a Blackberry app with a handful of games. A particular puzzle
 * game, FlipIt, just gets too hard too fast. Players are asked to string
 * together 5 moves or more, and there can be 9 plus colors. In particular level
 * 24 (i think) was just brutal. In other puzzle games, it feels like things
 * come together. You can be guided by what you see. Here it was just disjointed
 * until the last move or two. So I made this app to solve it and all others. It
 * works better than I could have expected. The input processing is fluid. And
 * the solving is very quick. A few seconds at most. Wow. This is what makes me
 * happy in the end.
 * 
 * TODO: I've seen it take a few seconds to process. We could add some output to
 * let you know it's working.
 * <p>
 * TODO: It's still easy to ask to solve a puzzle that can't be solved.
 * <p>
 * TODO: Even if I hit one of the other buttons, I still want to be able to
 * reset to the last solved puzzle.
 * <p>
 * TODO: Would be interesting to find puzzles with long (5 or 6) turn solutions,
 * for which that 5 or 6 turn sequence is one of only a few that will win. There
 * are thousands of those sequences. A puzzle where only one is right would be
 * hard. Could we randomly create the puzzles, and test to see if they are a
 * "narrow" solution. But this kind of processing and artful puzzle creation
 * kind of begs for it's own game.
 */
package jjc.solver.flipit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class Main extends PApplet {

	static private final long serialVersionUID = 9197934498371711514L;
	static private final int WIDTH = 650;
	static private final int HEIGHT = 550;
	static private final int GRIDS = 9;
	static private final float UNIT = (float) (HEIGHT - 1) / GRIDS;
	static private final float BUTTON = ((float) (WIDTH - HEIGHT) / 2) - 1;

	private Cell[][] cells;
	private Cell[][] original;
	@SuppressWarnings("unused")
	private long frame = 0;
	private boolean drawFlag = true;

	private List<Operation> solution = null;
	private int solutionIndex = 0;
	private SolutionMode mode = SolutionMode.NONE;

	private PImage solve = loadImage("Solve.png");
	private PImage beginning = loadImage("Beginning.png");
	private PImage next = loadImage("Next.png");
	private PImage flipup = loadImage("FlipUp.png");
	private PImage reset = loadImage("Reset.png");
	private PImage shiftleft = loadImage("ShiftLeft.png");
	private PImage shiftright = loadImage("ShiftRight.png");
	private PImage turnleft = loadImage("TurnLeft.png");
	private PImage turnright = loadImage("TurnRight.png");

	public void setup() {
		size(WIDTH, HEIGHT, P2D);
		noStroke();
		colorMode(RGB, 256);
		cells = new Cell[GRIDS][GRIDS];
		reset();
	}

	public void solve() {
		settle();

		// Save original cells
		original = new Cell[GRIDS][GRIDS];
		copyCells(cells, original);

		boolean solved = false;
		resetSolution();

		// Create an initial list
		List<List<Operation>> sequenceList = new ArrayList<List<Operation>>();
		for (Operation op : Operation.values()) {
			List<Operation> sequence = new ArrayList<Operation>();
			sequence.add(op);
			sequenceList.add(sequence);
		}

		while (!solved) {
			for (List<Operation> sequence : sequenceList) {
				copyCells(original, cells);
				for (Operation op : sequence) {
					execute(op);
					settle();
				}
				if (isClear()) {
					displaySolution(original, sequence);
					solved = true;
					solution = sequence;
					mode = SolutionMode.NEXTONLY;
					break;
				}
			}

			if (!solved) {
				List<List<Operation>> expandedSequenceList = new ArrayList<List<Operation>>();

				for (List<Operation> sequence : sequenceList) {
					for (Operation op : Operation.values()) {
						List<Operation> newSequence = new ArrayList<Operation>(sequence);
						newSequence.add(op);
						expandedSequenceList.add(newSequence);
					}
				}

				sequenceList = expandedSequenceList;
			}
		}

		copyCells(original, cells);
		drawFlag = true;
	}

	public void displaySolution(Cell[][] grid, List<Operation> sequence) {
		StringBuilder out = new StringBuilder();
		out.append("\n\n    1 2 3 4 5 6 7 8 9\n");
		out.append("   ___________________\n");
		out.append("  |                   |\n");
		for (int j = 0; j < GRIDS; j++) {
			out.append("  |");
			for (int i = 0; i < GRIDS; i++) {
				out.append(grid[i][j]);
			}
			out.append(" |  " + j + "\n");
		}

		out.append("  |___________________|\n\n");
		out.append("  Allowed    " + sequence.size() + "\n");
		out.append("  Solution  ");

		for (Operation op : sequence) {
			out.append(op);
		}
		System.out.println(out);
	}

	public void copyCells(Cell[][] from, Cell[][] into) {
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				into[i][j] = from[i][j];
			}
		}
	}

	public void execute(Operation op) {
		if (op == Operation.TURNLEFT) _turnLeft();
		else if (op == Operation.TURNRIGHT) _turnRight();
		else if (op == Operation.FLIPUP) _flipUp();
		else if (op == Operation.SHIFTLEFT) _shiftLeft();
		else if (op == Operation.SHIFTRIGHT) _shiftRight();
		settle();
	}

	public void reset() {
		resetSolution();
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				cells[i][j] = Cell.EMPTY;
			}
		}
	}

	public void resetSolution() {
		solution = null;
		solutionIndex = 0;
		mode = SolutionMode.NONE;
	}

	public boolean isClear() {
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				if (cells[i][j].isColor()) return false;
			}
		}
		return true;
	}

	public boolean isClear(Cell toMatch) {
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				if (cells[i][j].matches(toMatch)) return false;
			}
		}
		return true;
	}

	public boolean clear() {
		boolean any = false;
		int count = 0;
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				if (cells[i][j].isColor()) {
					count = 0;
					if (i > 0 && cells[i][j].matches(cells[i - 1][j])) count++;
					if (j > 0 && cells[i][j].color == cells[i][j - 1].color) count++;
					if (i < GRIDS - 1 && cells[i][j].color == cells[i + 1][j].color) count++;
					if (j < GRIDS - 1 && cells[i][j].color == cells[i][j + 1].color) count++;
					if (count > 1) {
						cascadeClear(i, j, cells[i][j]);
						any = true;
					}
				}
			}
		}
		return any;
	}

	public void cascadeClear(int i, int j, Cell toClear) {
		cells[i][j] = Cell.EMPTY;
		if (i > 0 && cells[i - 1][j].matches(toClear)) cascadeClear(i - 1, j, toClear);
		if (j > 0 && cells[i][j - 1].matches(toClear)) cascadeClear(i, j - 1, toClear);
		if (i < GRIDS - 1 && cells[i + 1][j].matches(toClear)) cascadeClear(i + 1, j, toClear);
		if (j < GRIDS - 1 && cells[i][j + 1].matches(toClear)) cascadeClear(i, j + 1, toClear);
	}

	public void settle() {
		gravity();
		while (clear()) {
			gravity();
		}
	}

	private void _turnRight() {
		Cell[][] tmp = new Cell[GRIDS][GRIDS];
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				tmp[i][j] = cells[j][GRIDS - 1 - i];
			}
		}
		cells = tmp;
	}

	private void _turnLeft() {
		Cell[][] tmp = new Cell[GRIDS][GRIDS];
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				tmp[j][GRIDS - 1 - i] = cells[i][j];
			}
		}
		cells = tmp;
	}

	private void _flipUp() {
		Cell[][] tmp = new Cell[GRIDS][GRIDS];
		for (int i = 0; i < GRIDS; i++) {
			for (int j = 0; j < GRIDS; j++) {
				tmp[i][j] = cells[i][GRIDS - 1 - j];
			}
		}
		cells = tmp;
	}

	private void _shiftRight() {
		for (int i = GRIDS - 1; i > 0; i--) {
			for (int j = 0; j < GRIDS; j++) {
				if (cells[i][j] == Cell.EMPTY && cells[i - 1][j].isColor()) {
					cells[i][j] = cells[i - 1][j];
					cells[i - 1][j] = Cell.EMPTY;
				}
			}
		}
	}

	private void _shiftLeft() {
		for (int i = 0; i < GRIDS - 1; i++) {
			for (int j = 0; j < GRIDS; j++) {
				if (cells[i][j] == Cell.EMPTY && cells[i + 1][j].isColor()) {
					cells[i][j] = cells[i + 1][j];
					cells[i + 1][j] = Cell.EMPTY;
				}
			}
		}
	}

	public void gravity() {
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 0; i < GRIDS; i++) {
				for (int j = GRIDS - 1; j > 0; j--) {
					if (cells[i][j] == Cell.EMPTY && cells[i][j - 1].isColor()) {
						cells[i][j] = cells[i][j - 1];
						cells[i][j - 1] = Cell.EMPTY;
						changed = true;
					}
				}
			}
		}
	}

	public void draw() {
		Cell cell;

		if (drawFlag) {
			background(240);
			for (int i = 0; i < GRIDS; i++) {
				for (int j = 0; j < GRIDS; j++) {
					cell = cells[i][j];
					fill(cell.color.getRed(), cell.color.getGreen(), cell.color.getBlue());
					if (cell.shape == Shape.SQUARE) {
						rect((i * UNIT) + 1, (j * UNIT) + 1, UNIT - 1, UNIT - 1);
					} else {
						ellipse((i * UNIT) + (UNIT / 2), (j * UNIT) + (UNIT / 2), UNIT - 1, UNIT - 1);
					}
				}
			}

			int index = 0;
			for (Cell btn : Cell.values()) {
				fill(btn.color.getRed(), btn.color.getGreen(), btn.color.getBlue());
				if (btn.shape == Shape.SQUARE) {
					rect((BUTTON * (index / 6)) + 1 + HEIGHT, (BUTTON * (index % 6)) + 1, BUTTON - 1, BUTTON - 1);
				} else {
					ellipse((BUTTON * (index / 6)) + 1 + HEIGHT + (BUTTON / 2),
							(BUTTON * (index % 6)) + 1 + (BUTTON / 2), BUTTON - 1, BUTTON - 1);
				}
				index++;
			}

			if (mode.hasBack()) image(beginning, HEIGHT, BUTTON * 6, BUTTON, BUTTON);
			if (mode.hasNext()) image(next, HEIGHT + BUTTON, BUTTON * 6, BUTTON, BUTTON);

			image(flipup, HEIGHT, BUTTON * 7, BUTTON, BUTTON);
			image(reset, HEIGHT + BUTTON, BUTTON * 7, BUTTON, BUTTON);
			image(shiftleft, HEIGHT, BUTTON * 8, BUTTON, BUTTON);
			image(shiftright, HEIGHT + BUTTON, BUTTON * 8, BUTTON, BUTTON);
			image(turnleft, HEIGHT, BUTTON * 9, BUTTON, BUTTON);
			image(turnright, HEIGHT + BUTTON, BUTTON * 9, BUTTON, BUTTON);
			image(solve, HEIGHT + BUTTON, BUTTON * 10, BUTTON, BUTTON);

			drawFlag = false;
		}

		if (mousePressed) processMousePressed();

		frame++;
	}

	private Cell toDraw = Cell.SOLID;

	public void processMousePressed() {
		if (mouseY > 0 && mouseY < HEIGHT - 1 && mouseX > 0 && mouseX < WIDTH - 1) {
			if (mouseX < HEIGHT) {
				int i = (int) (mouseX / UNIT);
				int j = (int) (mouseY / UNIT);
				cells[i][j] = toDraw;
				drawFlag = true;
				resetSolution();
			}
		}
	}

	public void mouseReleased() {
		if (mouseY > 0 && mouseY < HEIGHT - 1 && mouseX > 0 && mouseX < WIDTH - 1) {
			if (mouseX > HEIGHT) {
				int i = (int) ((mouseX - HEIGHT) / BUTTON);
				int j = (int) (mouseY / BUTTON);
				if (mouseY < (BUTTON * 6)) {
					int ordinal = (i * 6) + j;
					toDraw = Cell.values()[ordinal];
				} else {
					if (j == 6) {
						if (i == 0 && mode.hasBack()) {
							copyCells(original, cells);
							solutionIndex = 0;
							mode = SolutionMode.NEXTONLY;
						} else if (i == 1 && mode.hasNext()) {
							execute(solution.get(solutionIndex++));
							if (solutionIndex < solution.size()) mode = SolutionMode.BOTH;
							else mode = SolutionMode.BACKONLY;
						}
					} else if (j == 7) {
						if (i == 0) {
							execute(Operation.FLIPUP);
							resetSolution();
						} else reset();
					} else if (j == 8) {
						if (i == 0) {
							execute(Operation.SHIFTLEFT);
							resetSolution();
						} else {
							execute(Operation.SHIFTRIGHT);
							resetSolution();
						}
					} else if (j == 9) {
						if (i == 0) {
							execute(Operation.TURNLEFT);
							resetSolution();
						} else {
							execute(Operation.TURNRIGHT);
							resetSolution();
						}
					} else if (j == 10) {
						if (i == 0) {} //
						else solve();
					}
				}
			}
			drawFlag = true;
		}
	}
}

enum Cell {
	EMPTY(Shape.SQUARE, Color.white, ' '), //
	SOLID(Shape.SQUARE, Color.gray, '#'), //
	BLUE(Shape.CIRCLE, Color.blue, 'B'), //
	GREEN(Shape.CIRCLE, Color.green, 'G'), //
	YELLOW(Shape.CIRCLE, Color.yellow, 'Y'), //
	ORANGE(Shape.CIRCLE, Color.orange, 'O'), //
	RED(Shape.CIRCLE, new Color(0xcc, 0, 0), 'R'), //
	VIOLET(Shape.CIRCLE, new Color(192, 0, 192), 'V'), //
	PINK(Shape.CIRCLE, Color.pink, 'P'), //
	BROWN(Shape.CIRCLE, new Color(160, 128, 0), 'N'), //
	LIGHTBLUE(Shape.CIRCLE, new Color(0xaa, 0xcc, 0xff), 'L'), //
	LIGHTGREEN(Shape.CIRCLE, new Color(0x00, 0xcc, 0xcc), 'T');//

	static private Cell[] cells = Cell.values();
	static private final int SIZE = cells.length;

	public final Shape shape;
	public final Color color;
	public final char symbol;

	Cell(Shape shape, Color color, char symbol) {
		this.shape = shape;
		this.color = color;
		this.symbol = symbol;
	}

	public boolean matches(Cell other) {
		return this.ordinal() == other.ordinal();
	}

	public Cell next() {
		return cells[(this.ordinal() + 1) % SIZE];
	}

	public Cell previous() {
		return cells[(this.ordinal() + SIZE - 1) % SIZE];
	}

	public boolean isColor() {
		return this != EMPTY && this != SOLID;
	}

	static public Cell getRandom() {
		return cells[(int) (Math.random() * SIZE)];
	}

	public String toString() {
		return " " + symbol;
	}
}

enum Shape {
	SQUARE, CIRCLE
}

enum Operation {
	TURNLEFT('L'), SHIFTLEFT('<'), FLIPUP('^'), SHIFTRIGHT('>'), TURNRIGHT('R');

	private final char symbol;

	Operation(char symbol) {
		this.symbol = symbol;
	}

	public String toString() {
		return " " + symbol;
	}
}

enum SolutionMode {
	NONE, BACKONLY, BOTH, NEXTONLY;

	public boolean hasBack() {
		return this == BACKONLY || this == BOTH;
	}

	public boolean hasNext() {
		return this == NEXTONLY || this == BOTH;
	}
}