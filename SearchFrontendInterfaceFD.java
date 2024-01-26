
// --== CS400 Spring 2023 File Header Information ==--
// Name: Elizaveta Familiant
// Email: efamiliant@wisc.edu
// Team: BF
// TA: Naman Gupta
// Lecturer: Gary Dahl
// Notes to Grader: Used the code given to us by the Staff in P1.
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SearchFrontendInterfaceFD implements SearchFrontendInterface {
	private Scanner userInput = new Scanner(System.in);
	private RedBlackTree<Word> treeUsed = new RedBlackTree<Word>();
	private WordReader wordReaderUsed = new WordReader();
	private SearchBackend backend = new SearchBackend(treeUsed, wordReaderUsed);
	private String fileName = "";

	/**
	 * Helper method to display a wide row of dashes: a horizontal rule.
	 */
	private void hr() {
		System.out.println("-----------------------------------------------------------------");
	}

	/**
	 * Helper method to convert from List<String> to a single String object, with
	 * each element from the list separated by spaces
	 * 
	 * @param words List<String> of words to be converted
	 */
	public static String listStringToString(List<String> words) {
		return String.join(" ", words);
	}

	/**
	 * This loop repeated prompts the user for commands and displays appropriate
	 * feedback for each. This continues until the user enters 'q' to quit.
	 * @throws FileNotFoundException
	 */
	@Override
	public void runCommandLoop() throws FileNotFoundException {
		hr(); // display welcome message
		System.out.println("Welcome to the Dictionary Search App.");
		hr();

		List<String> words = null;

		char command = '\0';
		while (command != 'Q') { // main loop continues until user chooses to quit
			command = this.mainMenuPrompt();
			switch (command) {
				case 'L': // System.out.println(" [L]oad data from file");
					loadDataCommand();
					break;
				case 'A': // System.out.println(" Add Word [A]dd word + definition");
					// words = chooseSearchWordsPrompt();
					addWord();
					break;
				case 'R': // System.out.println("[R]emove word and definition");
					words = chooseSearchWordsPrompt();
					removeWord();
					break;
				case 'S': // System.out.println("[S]earch for word");
					words = chooseSearchWordsPrompt();
					searchWord(listStringToString(words), fileName);
					break;
				case 'I': // System.out.println("[I]nfo for stats");
					displayStatsCommand();
					break;
				case 'Q': // System.out.println(" [Q]uit");
							// do nothing, containing loop condition will fail
					break;
				default:
					System.out.println("Didn't recognize that command.  Please type one of the letters");
					break;
			}
		}

		hr(); // thank user before ending this application
		System.out.println("Thank you for using the Dictionary Search App.");
		hr();

	}

	/**
	 * Checks for the existence of a word and gives the user its definition.
	 * 
	 * @param word that user has selected
	 * @throws FileNotFoundException
	 */
	private void searchWord(String word, String filename) throws FileNotFoundException {
		if (backend.findDefinition(word, filename) != null) {
			System.out.println("Definition of " + word + ": " + backend.findDefinition(word, filename));
		} else {
			System.out.println("Word not found. Please check your spelling.");
		}
	}

	/**
	 * Prints the command options to System.out and reads user's choice through
	 * userInput scanner.
	 * 
	 * @return char user inputed
	 */
	@Override
	public char mainMenuPrompt() {
		// display menu of choices
		System.out.println("Choose a command from the list below:");
		System.out.println("    [L]oad data from file");
		System.out.println("    [A]dd word + definition");
		System.out.println("    [R]emove word + definition");
		System.out.println("    [S]earch for word");
		System.out.println("    [I]nfo for stats");
		System.out.println("    [Q]uit");

		// read in user's choice, and trim away any leading or trailing whitespace
		System.out.println("Choose command: ");
		String input = userInput.nextLine().trim();
		if (input.length() == 0) // if user's choice is blank, return null character
			return '\0';
		// otherwise, return an uppercase version of the first character in input
		return Character.toUpperCase(input.charAt(0));

	}

	/**
	 * Prompt user to enter filename, and display error message when loading fails.
	 */
	@Override
	public void loadDataCommand() {
		System.out.println("Enter the name of the file to load: ");
		String filename = userInput.nextLine().trim();
		fileName = filename;
		try {
			backend.loadData(filename);
		} catch (FileNotFoundException e) {
			System.out.println("Error: Could not find or load file: " + filename);
		}

	}

	/**
	 * This method gives user the ability to interactively add or remove individual
	 * words from their query, before performing any kind of search.
	 * 
	 * @return returns the word the user has decided to search with
	 */
	@Override
	public List<String> chooseSearchWordsPrompt() {
		List<String> words = new ArrayList<>();
		while (true) { // this loop ends when the user pressed enter (without typing any words)
			System.out.println("Word to search for: " + words.toString());
			System.out.println("Word to add-to/remove-from query, or press enter to search: ");
			String input = userInput.nextLine().replaceAll(",", "").trim();
			if (input.length() == 0) { // an empty string ends this loop and method call
				return words;
			} else {
				// otherwise the specified word's presence in the list is toggled:
				for (String word : input.split(" ")) {
					if (words.contains(word)) {
						words.remove(word); // remove any words that were already in the list
					} else {
						words.add(word); // add any words that were previously missing
					}
				}
			}
		}
	}

	/**
	 * This method gives user the ability to interactively add words to red/black
	 * tree
	 * @throws FileNotFoundException
	 */
	public void addWord() throws FileNotFoundException {
		// prompt user to input word, partofspeech and def
		System.out.println("Input word to add:");
		String newWord = userInput.nextLine();

		// check that the word isnt already in the tree
		if (backend.findDefinition(newWord, fileName) != null) {
			System.out.println("Definition of " + newWord + ": " + backend.findDefinition(newWord, fileName));
			return;
		} else {
			System.out.println("Input part of speech:");
			String partofspeech = userInput.nextLine();
			System.out.println("Input definition:");
			String definition = userInput.nextLine();
			// add to tree
			backend.loadWord(newWord, partofspeech, definition);
		}

		// 4) notify user about successful input
		System.out.println("Word " + newWord + " added.");
	}

	/**
	 * This method gives user the ability to interactively remove words to red/black
	 * tree
	 * @throws FileNotFoundException
	 */
	public void removeWord() throws FileNotFoundException {
		// prompt user
		System.out.println("Enter word you want to remove:");
		String newWord = userInput.nextLine();
		// create a outofDate array
		List<String> outOfDate = new ArrayList<>();
		// check that the word is in the tree
		if (backend.findDefinition(newWord, fileName) != null) {
			// add word to out of date array
			outOfDate.add(newWord);
			// remove word from current array
			backend.removeWord(newWord);
			// notify user about successful input
			System.out.println("Word " + newWord + " removed.");
		} else { // if word isn't in the tree notify user
			System.out.println("No such word in Dictionary.");
			return;
		}
	}

	/**
	 * Displays dataset statistics to System.out.
	 */
	public void displayStatsCommand() {
		System.out.println(backend.getStatisticsString());
	}
}
