import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class WordSearchAlgorithm {

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

		// ADD check for corrupted file (dif num of rows / columns)
		
		for (int row = 0; row < numOfLines; row++) {
			chars = in.readLine().split("\\s+");
			for (int col = 0; col < chars.length; col++) {
				grid[row][col] = chars[col].charAt(0);
			}
		}

		printGrid(grid);

	}

	/**ADD THIS METHOD
	 * Checks to see if a file is corrupted
	 * 
	 * @param file
	 * @return
	 */
	static boolean fileCorrupted(File file) {
		return false;
	}

	static void printGrid(char[][] grid) {
		for (int row = 0; row < grid.length; row++) {
			for (int col = 0; col < grid[row].length; col++) {
				System.out.print(grid[row][col] + " ");
			}
			System.out.println();
		}
	}

}
