package jjc.solver.jumpinggame;

public class JumpingGame {

	private String data;

	static public void main(String[] args) {
		new JumpingGame().process(">>>> <<<<");
		// new JumpingGame().process(">>>>>> <<<<<<");
	}

	private void process(String input) {
		this.data = input;
		int i = 0;
		boolean same;
		int center = data.length() / 2;
		System.out.format("%4d: %s\n", i++, data); // DEBUG
		String type;
		while (true) {

			// Rule 1, try to "jump" in
			same = replace(">< ", " <>") && replace(" ><", "<> ");

			// Rule 2, "step" to the edges
			if (same) same = replace("^ <", "< ") && replace("> $", " >");
			type = same ? "" : "*";

			// Rule 2, "step" all other locations
			if (same) {
				// Left side of the board
				if (data.indexOf(' ') <= center) {
					same = replace("> ", " >") && replace(" <", "< ");
				}

				// Right side of the board
				else {
					same = replace(" <", "< ") && replace("> ", " >");
				}
			}
			if (same) break;
			System.out.format("%4d: %s %s\n", i++, data, type); // DEBUG
		}
	}

	private boolean replace(final String remove, final String replace) {
		String last = data;
		data = data.replace(remove, replace);
		return last.equals(data);
	}
}