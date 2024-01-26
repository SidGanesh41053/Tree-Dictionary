import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class WordReader implements WordReaderInterface {
	public RedBlackTree<Word> temp = new RedBlackTree<Word>(); // redblacktree with the dictionary data

	// Creates one object for each word in the dictionary and returns a list with
	// all the objects
	public List<Word> readWordsFromFile(String filename) throws FileNotFoundException {
		//This method creates a new separateWords object and add it to the result list
		//SeparateWords object contains getPart, getWord, and getDefinition methods,which do not
		//have to be called in other classes; instead, callinng getInformationByWord is more than sufficient
		List<Word> result = new ArrayList<>();
		SeparateWords foo = new SeparateWords();
		foo.readFile(filename);
		for (int i = 0; i < foo.numberOfWords(); i++) {

			SeparateWords fooAdd = new SeparateWords();
			fooAdd.readFile(filename);
		    fooAdd.currentWord = i;
			Word tempWord = new Word();
			tempWord.setWord(fooAdd.getWord());
			tempWord.setPart(fooAdd.getPart());
			tempWord.setDefinition(fooAdd.getDefinition());
		    result.add(tempWord);
			//((List<Word>) result).add(fooAdd);
		}

		return result;
	}

	// Helper method that returns the part of the word associated to the object to
	// be user in getInformationByWord()
	public String getPartByWord(String word, String filename) throws FileNotFoundException {

		for (int i = 0; i < readWordsFromFile(filename).size(); i++) {
			if (readWordsFromFile(filename).get(i).getWord().equals(word)) {
				return (String) readWordsFromFile(filename).get(i).getPart();
			}
		}
		return null;
	}

	// Helper method that returns the definition of the word associated to the
	// object to be user in getInformationByWord()
	public String getDefinitionByWord(String word, String filename) throws FileNotFoundException {

		for (int i = 0; i < readWordsFromFile(filename).size(); i++) {
			if (readWordsFromFile(filename).get(i).getWord().equals(word)) {
				return (String) readWordsFromFile(filename).get(i).getDefinition();
			}
		}
		return null;
	}

	// Returns the information of a word
	public String getInformationByWord(String word, String filename) throws FileNotFoundException {
		Word tempWord = new Word();
		tempWord.setWord(word);

		if (temp.findNodeWithData(tempWord) != null) {
			return "The word chosen is [" + word.toLowerCase() + "]\n" + "The part is [" + getPartByWord(word, filename)
					+ "] \nThe definition is: " + getDefinitionByWord(word, filename);
		} else {
			return "The word does not exist in the dictionary\n" + possibleWords(word, filename);
		}
	}

	// Loads all the words into the Red-Black Tree
	public void loadRBT(String fileName) throws FileNotFoundException {

		int size = readWordsFromFile(fileName).size();
		//Runs through all the words in the dictionary and add it to the RBT
		for (int i = 0; i < size; i++) {
			String currentWord = (String) readWordsFromFile(fileName).get(i).getWord();
			Word curr = new Word();
			curr.setWord(currentWord);
			temp.insert(curr);
		}
	}

	// Provides possible words by getting the first 2 characters of a word if the
	// word is bigger than 3 character or
	// getting the whole word if it is smaller than 3 words and using it as a prefix
	// to find other matching words
	public String possibleWords(String word, String filename) throws FileNotFoundException {
		ArrayList<String> result = new ArrayList<>();
		String prefix = "";
		if (word.length() > 3) {
			prefix = word.substring(0, 2);
		} else {
			prefix = word;
		}
		int size = readWordsFromFile(filename).size();
		for (int i = 0; i < size; i++) {
			String currentWord = (String) readWordsFromFile(filename).get(i).getWord();
			if (currentWord.substring(0, prefix.length()).equals(prefix)) {
				result.add(currentWord);
			}
		}
		return "Possible words are: " + result;
	}

}
