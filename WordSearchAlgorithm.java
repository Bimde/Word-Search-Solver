import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Program that solves word search files
 * 
 * @author Bimesh De Silva
 * @version September 2015
 *
 */
public class WordSearchAlgorithm {

	private static final int LEFT = 76;
	private static final int RIGHT = 77;
	private static final int UP = 78;
	private static final int DOWN = 79;

	public static void main(String[] args) throws IOException {
		File file = new File("test.txt");
		BufferedReader in = new BufferedReader(new FileReader(file));

		String[] read = null;
		try {
			read = in.readLine().split("\\s+");
		} catch (IOException e) {
			e.printStackTrace();
		}

		int numOfWords = 0;

		while (read.length == 1) {
			numOfWords++;
			read = in.readLine().split("\\s+");
		}
		int numOfLines = read.length;

		char[][] grid = new char[numOfLines][numOfLines];
		String[] chars = null;
		in.close();
		in = new BufferedReader(new FileReader(file));
		String[] words = new String[numOfWords];
		for (int word = 0; word < words.length; word++) {
			words[word] = in.readLine().toLowerCase();
		}

		// ADD check for corrupted file (dif num of rows / columns) using
		// fileCorrupted method
		for (int row = 0; row < numOfLines; row++) {
			chars = in.readLine().split("\\s+");
			for (int col = 0; col < chars.length; col++) {
				grid[row][col] = chars[col].charAt(0);
			}
		}

		grid = solveWordSearch(grid, words);
		printGrid(grid);

	}

	/**
	 * ADD THIS METHOD Checks to see if a file is corrupted
	 * 
	 * @param file
	 * @return
	 */
	static boolean fileCorrupted(File file) {
		return false;
	}

	/**
	 * Prints out grid, very useful for debugging purposes
	 * 
	 * @param grid
	 *            The char grid to print out
	 */
	static void printGrid(char[][] grid) {
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				System.out.print(grid[row][col] + " ");
			}
			System.out.println();
		}
	}

	static char[][] solveWordSearch(char[][] grid, String[] words) {
		for (String word : words) {
			grid = findWord(word, grid);
		}
		return grid;
	}

	private static char[][] findWord(String word, char[][] grid) {
		char firstChar = word.charAt(0);

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (grid[row][col] == firstChar) {
					if (searchLeft(word, grid, row, col)) {
						grid = updateGrid(grid, word.length(), row, col, LEFT);
					} else if (searchRight(word, grid, row, col)) {
						grid = updateGrid(grid, word.length(), row, col, RIGHT);
					} else if (searchUp(word, grid, row, col)) {
						grid = updateGrid(grid, word.length(), row, col, UP);
					} else if (searchDown(word, grid, row, col)) {
						grid = updateGrid(grid, word.length(), row, col, DOWN);
					}
				}
			}
		}
		return grid;
	}

	private static char[][] updateGrid(char[][] grid, int length, int row, int col, int direction) {
		if (direction == LEFT) {
			for (int pos = 0; pos < length; pos++) {
				grid[row][col - pos] = (char) (grid[row][col - pos] - 32);
			}
		} else if (direction == RIGHT) {
			for (int pos = 0; pos < length; pos++) {
				grid[row][col + pos] = (char) (grid[row][col + pos] - 32);
			}
		} else if (direction == UP) {
			for (int pos = 0; pos < length; pos++) {
				grid[row - pos][col] = (char) (grid[row - pos][col] - 32);
			}
		} else if (direction == DOWN) {
			for (int pos = 0; pos < length; pos++) {
				grid[row + pos][col] = (char) (grid[row + pos][col] - 32);
			}
		}
		return grid;
	}

	private static boolean searchLeft(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col - (length - 1) < 0)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row][col - i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean searchRight(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col + (length - 1) >= grid[row].length)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row][col + i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean searchUp(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (row - (length - 1) < 0)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row - i][col]) {
				return false;
			}
		}
		return true;
	}

	private static boolean searchDown(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (row + (length - 1) >= grid.length)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row + i][col]) {
				return false;
			}
		}
		return true;
	}

}
