import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JOptionPane;

/**
 * Program that solves word search files
 * 
 * @author Bimesh De Silva
 * @version September 2015
 *
 */
public class WordSearchAlgorithm {

	private static final int LEFT = 1;
	private static final int RIGHT = 3;
	private static final int UP = 6;
	private static final int DOWN = 10;
	private static final int FAIL = 0;
	private static int wordsFound = 0;
	private static int wordsNotFound = 0;

	public static void main(String[] args) throws IOException {
		File file = new File(JOptionPane.showInputDialog("Enter the file name: (include '.txt'): "));
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

		long timeStart = System.currentTimeMillis();
		grid = solveWordSearch(grid, words);
		printGrid(grid);
		JOptionPane.showMessageDialog(null, "Word Search Solved: \n" + (System.currentTimeMillis() - timeStart) / 1000.0
				+ " secs\n" + wordsFound + " words were found!\n" + wordsNotFound + " words were NOT found!");

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
		char[][] originalGrid = Arrays.copyOf(grid, grid.length);
		for (int array = 0; array < grid.length; array++) {
			originalGrid[array] = Arrays.copyOf(grid[array], grid[array].length);
		}
		for (String word : words) {
			grid = findWord(word, grid, originalGrid);
		}
		return grid;
	}

	private static char[][] findWord(String word, char[][] grid, char[][] originalGrid) {
		char firstChar = word.charAt(0);

		int direction = FAIL;
		for (int row = 0; direction == FAIL && row < grid.length; row++) {
			for (int col = 0; direction == FAIL && col < grid[row].length; col++) {
				if (originalGrid[row][col] == firstChar) {
					direction = search(word, grid, originalGrid, row, col);
					if (direction != FAIL)
						grid = capitalizeWord(grid, originalGrid, word.length(), row, col, direction);
				}
			}
		}
		return grid;
	}

	private static char[][] capitalizeWord(char[][] grid, char[][] originalGrid, int length, int row, int col,
			int direction) {
		if (direction == FAIL) {
			wordsNotFound++;
			return grid;
		}
		wordsFound++;
		if (direction == LEFT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row][col - pos] == grid[row][col - pos])
					grid[row][col - pos] = (char) (grid[row][col - pos] - 32);
			}
		} else if (direction == RIGHT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row][col + pos] == grid[row][col + pos])
					grid[row][col + pos] = (char) (grid[row][col + pos] - 32);
			}
		} else if (direction == UP) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row - pos][col] == grid[row - pos][col])
					grid[row - pos][col] = (char) (grid[row - pos][col] - 32);
			}
		} else if (direction == DOWN) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row + pos][col] == grid[row + pos][col])
					grid[row + pos][col] = (char) (grid[row + pos][col] - 32);
			}
		} else if (direction == UP + LEFT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row - pos][col - pos] == grid[row - pos][col - pos])
					grid[row - pos][col - pos] = (char) (grid[row - pos][col - pos] - 32);
			}
		} else if (direction == UP + RIGHT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row - pos][col + pos] == grid[row - pos][col + pos])
					grid[row - pos][col + pos] = (char) (grid[row - pos][col + pos] - 32);
			}
		} else if (direction == DOWN + LEFT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row + pos][col - pos] == grid[row + pos][col - pos])
					grid[row + pos][col - pos] = (char) (grid[row + pos][col - pos] - 32);
			}
		} else if (direction == DOWN + RIGHT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row + pos][col + pos] == grid[row + pos][col + pos])
					grid[row + pos][col + pos] = (char) (grid[row + pos][col + pos] - 32);
			}
		}
		return grid;
	}

	private static int search(String word, char[][] grid, char[][] originalGrid, int row, int col) {
		if (searchLeft(word, originalGrid, row, col)) {
			return LEFT;
		} else if (searchRight(word, originalGrid, row, col)) {
			return RIGHT;
		} else if (searchUp(word, originalGrid, row, col)) {
			return UP;
		} else if (searchDown(word, originalGrid, row, col)) {
			return DOWN;
		} else if (searchUpLeft(word, originalGrid, row, col)) {
			return UP + LEFT;
		} else if (searchUpRight(word, originalGrid, row, col)) {
			return UP + RIGHT;
		} else if (searchDownLeft(word, originalGrid, row, col)) {
			return DOWN + LEFT;
		} else if (searchDownRight(word, originalGrid, row, col)) {
			return DOWN + RIGHT;
		}

		return FAIL;

	}

	private static boolean searchUpLeft(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col - (length - 1) < 0 || row - (length - 1) < 0)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row - i][col - i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean searchDownRight(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col + (length - 1) >= grid[row].length || row + (length - 1) >= grid.length)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row + i][col + i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean searchUpRight(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col + (length - 1) >= grid[row].length || row - (length - 1) < 0)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row - i][col + i]) {
				return false;
			}
		}
		return true;
	}

	private static boolean searchDownLeft(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col - (length - 1) < 0 || row + (length - 1) >= grid.length)
			return false;
		for (int i = 1; i < length; i++) {
			if (word.charAt(i) != grid[row + i][col - i]) {
				return false;
			}
		}
		return true;
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