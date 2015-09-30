import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.JOptionPane;

/**
 * Program that solves word search files and outputs the completed word search
 * in a text file of your choice
 * 
 * @author Bimesh De Silva
 * @version September 2015
 *
 */
public class WordSearchAlgorithm {

	// Turn on and off status messages (SEVERELY decreases efficiency)
	private static final boolean DISPLAY_STATUS = true;

	// Static variables to represent directions words could be found in
	private static final int LEFT = 1;
	private static final int RIGHT = 2;
	private static final int UP = 3;
	private static final int DOWN = 4;
	private static final int UP_LEFT = 5;
	private static final int UP_RIGHT = 6;
	private static final int DOWN_LEFT = 7;
	private static final int DOWN_RIGHT = 8;
	private static final int NOT_FOUND = 0;

	// Static variables to keep track of the number of words found and not found
	private static int wordsFound = 0;
	private static int wordsNotFound = 0;

	public static void main(String[] args) throws IOException {

		// Allow the user to enter the file name
		File file = new File(JOptionPane.showInputDialog("Enter the file name: (include '.txt'): "));
		while (file == null || !file.isFile()) {
			file = new File(JOptionPane
					.showInputDialog("The file entered was invalid!\nEnter the file name: (include '.txt'): "));
		}
		long timeStart = System.currentTimeMillis();
		String output = JOptionPane.showInputDialog("Enter the name of the desired output file (ex. 'output.txt'): ");
		while (output == null || output.equals("")) {
			output = JOptionPane.showInputDialog(
					"Please enter a valid file name! Enter the name of the desired output file (ex. 'output.txt'): ");
		}
		BufferedReader in = new BufferedReader(new FileReader(file));

		// Count the number of words and find the width of the array
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

		// Store the words to find in an array
		char[][] grid = new char[numOfLines][numOfLines];
		String[] chars = null;
		in.close();
		in = new BufferedReader(new FileReader(file));
		String[] words = new String[numOfWords];
		for (int word = 0; word < words.length; word++) {
			words[word] = in.readLine().toLowerCase();
		}

		// Store the word search grid in a 2-D array
		for (int row = 0; row < numOfLines; row++) {
			chars = in.readLine().split("\\s+");
			for (int col = 0; col < chars.length; col++) {
				grid[row][col] = chars[col].charAt(0);
			}
		}

		// Pass the grid and the words to the word search solving method
		grid = solveWordSearch(grid, words);
		saveGrid(output, grid);
		JOptionPane.showMessageDialog(null, "Word Search Solved: \n" + (System.currentTimeMillis() - timeStart) / 1000.0
				+ " secs\n" + wordsFound + " words were found!\n" + wordsNotFound + " words were NOT found!");

	}

	/**
	 * Prints out grid, very useful for debugging purposes
	 * 
	 * @param grid
	 *            The char grid to print out
	 */
	public static void printGrid(char[][] grid) {
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				System.out.print(grid[row][col] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Saves the grid into a specified output file
	 * 
	 * @param grid
	 *            The char grid to print out
	 * @throws IOException
	 */
	public static void saveGrid(String fileName, char[][] grid) throws IOException {
		File file = new File(fileName);
		PrintWriter writer = new PrintWriter(new FileWriter(file));
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				writer.print(grid[row][col] + " ");
			}
			writer.println();
		}
		writer.close();
	}

	/**
	 * Solve word search by looping through each of the provided words
	 * 
	 * @param grid
	 *            The word search grid
	 * @param words
	 *            The words to find
	 * @return The completed word search with capitalized words
	 */
	public static char[][] solveWordSearch(char[][] grid, String[] words) {
		char[][] originalGrid = Arrays.copyOf(grid, grid.length);
		for (int array = 0; array < grid.length; array++) {
			originalGrid[array] = Arrays.copyOf(grid[array], grid[array].length);
		}
		for (String word : words) {
			grid = findWord(word, grid, originalGrid);
		}
		return grid;
	}

	/**
	 * Find the specified word in the provided grid of characters
	 * 
	 * @param word
	 *            The word to find
	 * @param grid
	 *            The grid of characters to change once the word is found
	 * @param originalGrid
	 *            The grid of characters to find the word in
	 * @return The grid after capitalizing the specified word
	 */
	public static char[][] findWord(String word, char[][] grid, char[][] originalGrid) {
		char firstChar = word.charAt(0);
		if (DISPLAY_STATUS)
			System.out.println(word);

		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				if (originalGrid[row][col] == firstChar) {
					int direction = search(word, originalGrid, row, col);
					if (direction != NOT_FOUND) {
						grid = capitalizeWord(grid, originalGrid, word.length(), row, col, direction);
						return grid;
					}
				}
			}
		}
		return grid;
	}

	/**
	 * Capitalize text in the grid using provided starting point, direction, and
	 * distance
	 * 
	 * @param grid
	 *            The grid to capitalize the word in
	 * @param originalGrid
	 *            The unchanged grid to compare the mutable grid to
	 * @param length
	 *            The length of the word (or text) to capitalize
	 * @param row
	 *            The starting row of the text
	 * @param col
	 *            The starting row of the text
	 * @param direction
	 *            The direction that the text continues from the starting row
	 *            and column
	 * @return The grid with the capitalized word
	 */
	public static char[][] capitalizeWord(char[][] grid, char[][] originalGrid, int length, int row, int col,
			int direction) {
		if (direction == NOT_FOUND) {
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
		} else if (direction == UP_LEFT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row - pos][col - pos] == grid[row - pos][col - pos])
					grid[row - pos][col - pos] = (char) (grid[row - pos][col - pos] - 32);
			}
		} else if (direction == UP_RIGHT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row - pos][col + pos] == grid[row - pos][col + pos])
					grid[row - pos][col + pos] = (char) (grid[row - pos][col + pos] - 32);
			}
		} else if (direction == DOWN_LEFT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row + pos][col - pos] == grid[row + pos][col - pos])
					grid[row + pos][col - pos] = (char) (grid[row + pos][col - pos] - 32);
			}
		} else if (direction == DOWN_RIGHT) {
			for (int pos = 0; pos < length; pos++) {
				if (originalGrid[row + pos][col + pos] == grid[row + pos][col + pos])
					grid[row + pos][col + pos] = (char) (grid[row + pos][col + pos] - 32);
			}
		}
		return grid;
	}

	/**
	 * Search the provided grid of characters for the specified word
	 * 
	 * @param word
	 *            The word to search for
	 * @param originalGrid
	 *            The grid of characters to search for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return The direction which the word for found (in the form of an
	 *         integer)
	 */
	public static int search(String word, char[][] originalGrid, int row, int col) {
		if (searchLeft(word, originalGrid, row, col)) {
			return LEFT;
		} else if (searchRight(word, originalGrid, row, col)) {
			return RIGHT;
		} else if (searchUp(word, originalGrid, row, col)) {
			return UP;
		} else if (searchDown(word, originalGrid, row, col)) {
			return DOWN;
		} else if (searchUpLeft(word, originalGrid, row, col)) {
			return UP_LEFT;
		} else if (searchUpRight(word, originalGrid, row, col)) {
			return UP_RIGHT;
		} else if (searchDownLeft(word, originalGrid, row, col)) {
			return DOWN_LEFT;
		} else if (searchDownRight(word, originalGrid, row, col)) {
			return DOWN_RIGHT;
		}

		return NOT_FOUND;

	}

	/**
	 * Search for the word in the specified direction: Up Left
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchUpLeft(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col - (length - 1) < 0 || row - (length - 1) < 0)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row - pos][col - pos]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Down Right
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchDownRight(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col + (length - 1) >= grid[row].length || row + (length - 1) >= grid.length)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row + pos][col + pos]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Up Right
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchUpRight(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col + (length - 1) >= grid[row].length || row - (length - 1) < 0)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row - pos][col + pos]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Down Left
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchDownLeft(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col - (length - 1) < 0 || row + (length - 1) >= grid.length)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row + pos][col - pos]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Left
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchLeft(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col - (length - 1) < 0)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row][col - pos]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Right
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchRight(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (col + (length - 1) >= grid[row].length)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row][col + pos]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Up
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchUp(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (row - (length - 1) < 0)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row - pos][col]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Search for the word in the specified direction: Down
	 * 
	 * @param word
	 *            The word to search for in the grid
	 * @param grid
	 *            The grid of characters to look for the word in
	 * @param row
	 *            The row to start looking for the word in
	 * @param col
	 *            The column to start looking for the word in
	 * @return Whether or not the specified word was found
	 */
	public static boolean searchDown(String word, char[][] grid, int row, int col) {
		int length = word.length();
		if (row + (length - 1) >= grid.length)
			return false;
		for (int pos = 1; pos < length; pos++) {
			if (word.charAt(pos) != grid[row + pos][col]) {
				return false;
			}
		}
		return true;
	}

}