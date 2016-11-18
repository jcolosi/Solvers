package jjc.solver.pudding;

public class State {
	static public final short WIDTH = 7;
	static public final short HEIGHT = 9;
	static private final char EMPTY = ' ';
	static private final Exception WRONG_SIZE = new Exception(
			"Wrong size for input");

	char[][] board;
	boolean[][] stars;
	int[][] monsters;
	int groups;

	public State() {
		this.board = new char[WIDTH][HEIGHT];
		this.stars = new boolean[WIDTH][HEIGHT];
		this.monsters = new int[WIDTH][HEIGHT];
		this.groups = 0;
	}

	public State(String board, String stars) throws Exception {
		this();
		setBoard(board);
		setStars(stars);
		buildMonsters();
	}

	public State clone() {
		State out = new State();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				out.board[i][j] = this.board[i][j];
				out.monsters[i][j] = this.monsters[i][j];
			}
		}
		out.stars = this.stars;
		out.groups = this.groups;
		return out;
	}

	public int countStars() {
		int count = 0;
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (monsters[i][j] > 0 && stars[i][j]) count++;
			}
		}
		return count;
	}

	public int getGroups() {
		return groups;
	}

	public String[] getSymmetries() {
		String[] out = new String[4];
		StringBuilder cache = new StringBuilder();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				cache.append(board[i][j]);
			}
		}
		out[0] = cache.toString();
		out[3] = cache.reverse().toString();

		cache.setLength(0);
		for (int i = WIDTH - 1; i >= 0; i--) {
			for (int j = 0; j < HEIGHT; j++) {
				cache.append(board[i][j]);
			}
		}
		out[0] = cache.toString();

		cache.setLength(0);
		for (int i = 0; i < WIDTH; i++) {
			for (int j = HEIGHT - 1; j >= 0; j--) {
				cache.append(board[i][j]);
			}
		}
		out[0] = cache.toString();

		return out;
	}

	// public State move(Move move) {
	// State out = this.clone();
	// while (true) {
	// Result result = out.canMove(move);
	// if (result == Result.FALL) return null;
	// if (result == Result.STOP_BLOCK) return out;
	// if (result == Result.STOP_ICE) {
	// out.breakIce(move);
	// return out;
	// }
	// if (result == Result.STOP_MONSTER) {
	// out.buildMonsters();
	// return out;
	// }
	// if (result == Result.OK) {
	// out.doStep(move);
	// }
	// }
	// }

	public State move(Move move) {
		State out = this.clone();
		if (!out.doMove(move)) return null;
		out.doMagnet();
		return out;
	}

	public void setBoard(String input) throws Exception {
		input = input.replaceAll("[^a-z A-Z]", "");
		// System.out.println("DEBUG: " + input + " #" + input.length());
		if (input.length() != (WIDTH * HEIGHT)) throw WRONG_SIZE;
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				board[i][j] = input.charAt((j * WIDTH) + i);
			}
		}
	}

	public void setStars(String input) throws Exception {
		input = input.replaceAll("[^a-z A-Z]", "");
		// System.out.println("DEBUG: " + input + " #" + input.length());
		if (input.length() != (WIDTH * HEIGHT)) throw WRONG_SIZE;
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				stars[i][j] = isStar(input.charAt((j * WIDTH) + i));
			}
		}
	}

	// private boolean doMove(Move move) {
	// while (true) {
	// Result result = canMove(move);
	// if (result == Result.FALL) return false;
	// if (result == Result.STOP_BLOCK) return true;
	// if (result == Result.STOP_ICE) {
	// breakIce(move);
	// return true;
	// }
	// if (result == Result.STOP_MONSTER) {
	// buildMonsters();
	// return true;
	// }
	// if (result == Result.OK) {
	// doStep(move);
	// }
	// }
	// }

	public String toString() {
		StringBuilder out = new StringBuilder();

		out.append("board:\tmnstrs:\tstars:\n");
		for (int j = 0; j < HEIGHT; j++) {
			for (int i = 0; i < WIDTH; i++) {
				out.append(board[i][j]);
			}
			out.append('\t');
			for (int i = 0; i < WIDTH; i++) {
				out.append(monsters[i][j]);
			}
			out.append('\t');
			for (int i = 0; i < WIDTH; i++) {
				out.append(stars[i][j] ? "*" : " ");
			}
			out.append("\n");
		}
		return out.toString();
	}

	private void breakIce(Move move) {
		if (move.direction == Direction.LEFT) {
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					if (monsters[i][j] == move.group) {
						if (isIce(board[i - 1][j])) board[i - 1][j] = EMPTY;
					}
				}
			}
		} else if (move.direction == Direction.UP) {
			for (int j = 0; j < HEIGHT; j++) {
				for (int i = 0; i < WIDTH; i++) {
					if (monsters[i][j] == move.group) {
						if (isIce(board[i][j - 1])) board[i][j - 1] = EMPTY;
					}
				}
			}
		} else if (move.direction == Direction.RIGHT) {
			for (int i = WIDTH - 1; i >= 0; i--) {
				for (int j = 0; j < HEIGHT; j++) {
					if (monsters[i][j] == move.group) {
						if (isIce(board[i + 1][j])) board[i + 1][j] = EMPTY;
					}
				}
			}
		} else if (move.direction == Direction.DOWN) {
			for (int j = HEIGHT - 1; j >= 0; j--) {
				for (int i = 0; i < WIDTH; i++) {
					if (monsters[i][j] == move.group) {
						if (isIce(board[i][j + 1])) board[i][j + 1] = EMPTY;
					}
				}
			}
		}
	}

	private void buildMonsters() {
		monsters = new int[WIDTH][HEIGHT];
		this.groups = 0;

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				detect(i, j, false);
			}
		}
	}

	private void detect(int i, int j, boolean adjacent) {
		if (monsters[i][j] == 0) {
			if (isMonster(board[i][j])) {
				if (!adjacent) groups++;
				monsters[i][j] = groups;
				if (i > 0) detect(i - 1, j, true);
				if (j > 0) detect(i, j - 1, true);
				if (i + 1 < WIDTH) detect(i + 1, j, true);
				if (j + 1 < HEIGHT) detect(i, j + 1, true);
			}
		}
	}

	private void mergeMonsters() {
		monsters = new int[WIDTH][HEIGHT];
		this.groups = 0;

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (isMagnet(board[i][j])) {
					if (i > 0 && isMonster(board[i - 1][j])) board[i][j] = 'm';
					if (j > 0 && isMonster(board[i][j - 1])) board[i][j] = 'm';
					if (i + 1 < WIDTH && isMonster(board[i + 1][j])) board[i][j] = 'm';
					if ((j + 1 < HEIGHT) && isMonster(board[i][j + 1])) board[i][j] = 'm';
				}
				detect(i, j, false);
			}
		}
	}

	private Result canMove(Move move) {
		if (move.direction == Direction.LEFT) {
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					if (monsters[i][j] == move.group) {
						if (i == 0) return Result.FALL;
						if (isIce(board[i - 1][j])) return Result.STOP_ICE;
						if (isMonster(board[i - 1][j]) && monsters[i - 1][j] != move.group)
							return Result.STOP_MONSTER;
						if (isBlock(board[i - 1][j])) return Result.STOP_BLOCK;
					}
				}
			}
		} else if (move.direction == Direction.UP) {
			for (int j = 0; j < HEIGHT; j++) {
				for (int i = 0; i < WIDTH; i++) {
					if (monsters[i][j] == move.group) {
						if (j == 0) return Result.FALL;
						if (isIce(board[i][j - 1])) return Result.STOP_ICE;
						if (isMonster(board[i][j - 1]) && monsters[i][j - 1] != move.group)
							return Result.STOP_MONSTER;
						if (isBlock(board[i][j - 1])) return Result.STOP_BLOCK;
					}
				}
			}
		} else if (move.direction == Direction.RIGHT) {
			for (int i = WIDTH - 1; i >= 0; i--) {
				for (int j = 0; j < HEIGHT; j++) {
					if (monsters[i][j] == move.group) {
						if (i + 1 == WIDTH) return Result.FALL;
						if (isIce(board[i + 1][j])) return Result.STOP_ICE;
						if (isMonster(board[i + 1][j]) && monsters[i + 1][j] != move.group)
							return Result.STOP_MONSTER;
						if (isBlock(board[i + 1][j])) return Result.STOP_BLOCK;
					}
				}
			}
		} else if (move.direction == Direction.DOWN) {
			for (int j = HEIGHT - 1; j >= 0; j--) {
				for (int i = 0; i < WIDTH; i++) {
					if (monsters[i][j] == move.group) {
						if (j + 1 == HEIGHT) return Result.FALL;
						if (isIce(board[i][j + 1])) return Result.STOP_ICE;
						if (isMonster(board[i][j + 1]) && monsters[i][j + 1] != move.group)
							return Result.STOP_MONSTER;
						if (isBlock(board[i][j + 1])) return Result.STOP_BLOCK;
					}
				}
			}
		}
		return Result.OK;
	}

	private void doMagnet() {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				if (isHorizontalMagnet(board[i][j])) {
					for (int q = i - 1; q >= 0; q--) {
						if (isIce(board[q][j])) break;
						if (isBlock(board[q][j])) break;
						if (isMonster(board[q][j])) {
							doMove(new Move(monsters[q][j], Direction.RIGHT));
						}
					}
					for (int q = i + 1; q < WIDTH; q++) {
						if (isIce(board[q][j])) break;
						if (isBlock(board[q][j])) break;
						if (isMonster(board[q][j])) {
							doMove(new Move(monsters[q][j], Direction.LEFT));
						}
					}
				}
				if (isVerticalMagnet(board[i][j])) {
					for (int q = j - 1; q >= 0; q--) {
						if (isIce(board[q][j])) break;
						if (isBlock(board[q][j])) break;
						if (isMonster(board[q][j])) {
							doMove(new Move(monsters[q][j], Direction.DOWN));
						}
					}
					for (int q = j + 1; q < HEIGHT; q++) {
						if (isIce(board[q][j])) break;
						if (isBlock(board[q][j])) break;
						if (isMonster(board[q][j])) {
							doMove(new Move(monsters[q][j], Direction.UP));
						}
					}
				}
			}
		}
	}

	private Direction findMagnet(int i, int j) {
		// Look LEFT
		for (int q = i - 1; q >= 0; q--) {
			if (isIce(board[q][j])) break;
			if (isBlock(board[q][j])) break;
			if (isHorizontalMagnet(board[q][j])) return Direction.LEFT;
		}

		// Look RIGHT
		for (int q = i + 1; q < WIDTH; q++) {
			if (isIce(board[q][j])) break;
			if (isBlock(board[q][j])) break;
			if (isHorizontalMagnet(board[q][j])) return Direction.RIGHT;
		}
		// Look DOWN
		for (int q = j - 1; q >= 0; q--) {
			if (isIce(board[i][q])) break;
			if (isBlock(board[i][q])) break;
			if (isVerticalMagnet(board[i][q])) return Direction.DOWN;
		}

		// Look UP
		for (int q = j + 1; q < HEIGHT; q++) {
			if (isIce(board[i][q])) break;
			if (isBlock(board[i][q])) break;
			if (isVerticalMagnet(board[i][q])) return Direction.UP;
		}

		return null;
	}

	private boolean doMove(Move move) {
		while (true) {
			Result result = canMove(move);
			if (result == Result.FALL) return false;
			if (result == Result.STOP_BLOCK || result == Result.STOP_ICE
					|| result == Result.STOP_MONSTER) {
				breakIce(move);
				buildMonsters();
				mergeMonsters();
				return true;
			}
			if (result == Result.OK) {
				Direction newDirection = doStep(move);
				if (newDirection != null) {
					move = new Move(move.group, newDirection);
				}
			}
		}
	}

	// Board:
	// i - ICE
	// b - BLOCK
	// h - horizontal magnet
	// v - vertical magnet
	// m - monster
	// - empty
	// s - Star
	//

	private Direction doStep(Move move) {
		Direction newDirection = null;
		if (move.direction == Direction.LEFT) {
			for (int i = 0; i < WIDTH; i++) {
				for (int j = 0; j < HEIGHT; j++) {
					if (monsters[i][j] == move.group) {
						board[i - 1][j] = board[i][j];
						board[i][j] = EMPTY;
						monsters[i - 1][j] = monsters[i][j];
						monsters[i][j] = 0;
						if (newDirection == null) {
							Direction tmp = findMagnet(i - 1, j);
							if (tmp != null) newDirection = tmp;
						}
					}
				}
			}
		} else if (move.direction == Direction.UP) {
			for (int j = 0; j < HEIGHT; j++) {
				for (int i = 0; i < WIDTH; i++) {
					if (monsters[i][j] == move.group) {
						board[i][j - 1] = board[i][j];
						board[i][j] = EMPTY;
						monsters[i][j - 1] = monsters[i][j];
						monsters[i][j] = 0;
						if (newDirection == null) {
							Direction tmp = findMagnet(i, j - 1);
							if (tmp != null) newDirection = tmp;
						}
					}
				}
			}
		} else if (move.direction == Direction.RIGHT) {
			for (int i = WIDTH - 1; i >= 0; i--) {
				for (int j = 0; j < HEIGHT; j++) {
					if (monsters[i][j] == move.group) {
						board[i + 1][j] = board[i][j];
						board[i][j] = EMPTY;
						monsters[i + 1][j] = monsters[i][j];
						monsters[i][j] = 0;
						if (newDirection == null) {
							Direction tmp = findMagnet(i + 1, j);
							if (tmp != null) newDirection = tmp;
						}
					}
				}
			}
		} else if (move.direction == Direction.DOWN) {
			for (int j = HEIGHT - 1; j >= 0; j--) {
				for (int i = 0; i < WIDTH; i++) {
					if (monsters[i][j] == move.group) {
						board[i][j + 1] = board[i][j];
						board[i][j] = EMPTY;
						monsters[i][j + 1] = monsters[i][j];
						monsters[i][j] = 0;
						if (newDirection == null) {
							Direction tmp = findMagnet(i, j + 1);
							if (tmp != null) newDirection = tmp;
						}
					}
				}
			}
		}
		return newDirection;
	}

	private boolean isBlock(char x) {
		return x == 'b';
	}

	private boolean isEmpty(char x) {
		return x == EMPTY;
	}

	private boolean isHorizontalMagnet(char x) {
		return x == 'h';
	}

	private boolean isIce(char x) {
		return x == 'i';
	}

	private boolean isMonster(char x) {
		return x == 'h' || x == 'v' || x == 'm';
	}

	private boolean isStar(char x) {
		return x == 's';
	}

	private boolean isVerticalMagnet(char x) {
		return x == 'v';
	}

	private boolean isMagnet(char x) {
		return x == 'h' || x == 'v';
	}

}

enum Direction {
	UP, LEFT, DOWN, RIGHT
}

class Move {
	int group;
	Direction direction;

	public Move(int group, Direction direction) {
		this.group = group;
		this.direction = direction;
	}

	public String toString() {
		return group + ":" + direction;
	}
}

enum Result {
	FALL, STOP_BLOCK, STOP_MONSTER, STOP_ICE, OK
}
