import java.io.FileNotFoundException;
import java.util.List;

public interface SearchFrontendInterface {
	// public SearchFrontendXX(Scanner userInput, SearchBackendInterface backend);
	public void runCommandLoop() throws FileNotFoundException;

	public char mainMenuPrompt();

	public void loadDataCommand();

	public List<String> chooseSearchWordsPrompt();

	public void displayStatsCommand();
}
