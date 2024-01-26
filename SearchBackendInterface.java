import java.io.FileNotFoundException;

public interface SearchBackendInterface {
	// public SearchBackendInterface(RedBlackTreeInterface<WordInterface> tree,
	// WordReaderInterface wordReader);
	public void loadData(String filename) throws FileNotFoundException;

	public void loadWord(String word, String partOfSpeech, String definition);

	public String findDefinition(String word);

	public String removeWord(String word);

	public String getStatisticsString();
}
