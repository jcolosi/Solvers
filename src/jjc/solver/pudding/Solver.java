package jjc.solver.pudding;

import java.util.HashSet;

// Board:
// i - ICE
// b - BLOCK
// h - horizontal magnet
// v - vertical magnet
// m - monster
//
// s - Star
//

/**
 * TODO: If the cache has a state already there, this thread should continue if
 * it arrived at that state in a shorter set of moves.
 * 
 * @author John
 *
 */
public class Solver {

	static class Config {
		static public final boolean debug = true;
	}

	static private final String game424 = "       :m  i  m:   b   :   b   :ibbbbbi:   b   :   b   :m  i  h:       :";
	static private final String star424 = "       :       :       :       :       :       :  s    :  s s  :       :";

	@SuppressWarnings("unused")
	static private final String game0 = "      m:       :       :       :       :       :       :       :m     m:";
	@SuppressWarnings("unused")
	static private final String star0 = " s     :ss     :       :       :       :       :       :       :       :";

	@SuppressWarnings("unused")
	static private final String game1 = "bbbbbbb:      h:       :       :       :       :       :       :m     m:";
	@SuppressWarnings("unused")
	static private final String star1 = "       : s     :ss     :       :       :       :       :       :       :";

	private HashSet<String> cache;
	private int[] CountByStars = new int[4];

	public Solver() {
		cache = new HashSet<String>();
	}

	static public void main(String[] args) {
		Solver solver = new Solver();

		try {
			// State state = new State(game0, star0);
			// State state = new State(game1, star1);
			State state = new State(game424, star424);
			System.out.println("BEGIN\n" + state); // DEBUG
			solver.solve(state);
			// state = solver.step(state, new Move(1, Direction.DOWN));
			// state = solver.step(state, new Move(1, Direction.DOWN));
			// state = solver.step(state, new Move(3, Direction.LEFT));
			// state = solver.step(state, new Move(3, Direction.DOWN));
			// state = solver.step(state, new Move(3, Direction.DOWN));
			// state = solver.step(state, new Move(2, Direction.LEFT));

			for (int stars = 0; stars < solver.CountByStars.length; stars++) {
				System.out.format("%d: %d\n", stars, solver.CountByStars[stars]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	private State step(State state, Move move) {
		State newState = state.move(move);
		if (newState != null) {
			show(state, newState);
			return newState;
		} else {
			return state;
		}
	}

	public void solve(State state) {
		solve(state, "");
	}

	public void solve(State state, String moves) {
		debug("SOLVING\n" + state); // DEBUG
		for (int group = 1; group <= state.groups; group++) {
			for (Direction direction : Direction.values()) {
				Move move = new Move(group, direction);
				debug(move + "..."); // DEBUG
				State newState = state.move(move);
				String myMoves = moves + " -> " + move;
				if (newState != null) {
					String[] symmetries = newState.getSymmetries();
					if (!any(symmetries)) {
						debug("MOVED\n"); // DEBUG
						show(state, newState); // DEBUG
						cache.add(symmetries[0]);
						if (newState.groups > 1) solve(newState, myMoves);
						else {
							System.out.println("FINAL\n"); // DEBUG
							int stars = newState.countStars();
							CountByStars[stars]++;
							System.out.println(moves);
							System.out.println(newState + "\n\n");
						}
					} else {
						debug("FOUND\n"); // DEBUG
						show(state, newState); // DEBUG
					}
				} else {
					debug("FALL\n"); // DEBUG
				}
			}
		}
	}

	// public void solve(State state) {
	// debug("SOLVING\n" + state); // DEBUG
	// for (int group = 1; group <= state.groups; group++) {
	// for (Direction direction : Direction.values()) {
	// Move move = new Move(group, direction);
	// debug(move + "..."); // DEBUG
	// State newState = state.move(move);
	// if (newState != null) {
	// String[] symmetries = newState.getSymmetries();
	// if (!any(symmetries)) {
	// debug("MOVED\n"); // DEBUG
	// show(state, newState); // DEBUG
	// cache.add(symmetries[0]);
	// if (newState.groups > 1) solve(newState);
	// else {
	// System.out.println("FINAL\n"); // DEBUG
	// int stars = newState.countStars();
	// CountByStars[stars]++;
	// System.out.println(newState + "\n\n");
	// }
	// } else {
	// debug("FOUND\n"); // DEBUG
	// show(state, newState); // DEBUG
	// }
	// } else {
	// debug("FALL\n"); // DEBUG
	// }
	// }
	// }
	// }

	public void show(State a, State b) {
		StringBuilder out = new StringBuilder();

		for (int j = 0; j < State.HEIGHT; j++) {
			for (int i = 0; i < State.WIDTH; i++) {
				out.append(a.board[i][j]);
			}
			out.append("\t");
			for (int i = 0; i < State.WIDTH; i++) {
				out.append(a.monsters[i][j]);
			}
			if (j == State.HEIGHT / 2) out.append("\t=>\t");
			else out.append("\t\t");
			for (int i = 0; i < State.WIDTH; i++) {
				out.append(b.board[i][j]);
			}
			out.append("\t");
			for (int i = 0; i < State.WIDTH; i++) {
				out.append(b.monsters[i][j]);
			}
			out.append("\n");
		}

		debug(out.toString() + "\n");
	}

	public boolean any(String[] symmetries) {
		for (String symmetry : symmetries) {
			if (cache.contains(symmetry)) return true;
		}
		return false;
	}

	static private void debug(String format, Object... args) {
		if (Config.debug) System.out.format(format, args);
	}

}
