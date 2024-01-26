import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class SearchBackend {

	protected Word currentWord;
	protected RedBlackTree<Word> tree;
	protected WordReader wordReader;
	List<Integer> alphabetIndex = new ArrayList<Integer>();
	int currentExtraCharacters = 0;
	String alphabet = "abcdefghijklmnopqrstuvwxyz";

	SearchBackend(RedBlackTree<Word> tree2, WordReader newWordReader) {
		tree = tree2;
		wordReader = newWordReader;
		alphabetIndex.add(0);
	}

	public void loadData(String filename) throws FileNotFoundException {
		List<Word> listOfWords = wordReader.readWordsFromFile(filename);
		for (Word word : listOfWords) {
			tree.insert(word);
		}
	}

	public void loadWord(String word, String partOfSpeech, String definition) {
		Word newWord = new Word();
		newWord.setWord(word);
		newWord.setPart(partOfSpeech);
		newWord.setDefinition(definition);
		tree.insert(newWord);
	}

	public String findDefinition(String word, String filename) throws FileNotFoundException {
		Word newWord = new Word();
		newWord.setWord(word);
		try {
			currentWord = tree.search(newWord);
		} catch (IllegalArgumentException e) {
			currentWord = null;
		}
		//currentWord = tree.search(newWord);
		if (currentWord == null) {
			return wordReader.possibleWords(word, filename);
		} else {
			return currentWord.getDefinition();
		}
	}

	public String removeWord(String word) {
		Word newWord = new Word();
		newWord.setWord(word);
		if (tree.remove(newWord)) {
			return "Removed " + word;
		}

		return "Did not remove " + word;
	}

	public String getStatisticsString() {
		if (tree.isEmpty()) {
			return "The dictionary is empty";
		}
		String stats = "Number of Words in Dictionary: " + tree.size() + "\n" + "Words in order: " + tree.toString();
		return stats;
	}
}
