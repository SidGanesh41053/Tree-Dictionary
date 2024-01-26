import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SeparateWords implements WordInterface {

	// List in which each index represents a line in the csv file
	public ArrayList<String> getAllElements = new ArrayList<>();
	// Variable used to select which word will be later related to an object
	public int currentWord = 0;

	@Override
	public String getWord() { // returns the word related to an object
		String[] temp = getAllElements.get(currentWord).split("\\/");
		return temp[0];
	}

	@Override
	public String getPart() { // returns the part of the word related to an object
		String[] temp = getAllElements.get(currentWord).split("\\/");
		return temp[1];
	}

	@Override
	public String getDefinition() { // returns the definition of the word related to an object
		String[] temp = getAllElements.get(currentWord).split("\\/");
		return temp[2];
	}

	public int compareTo(Object o) {
		return 0;
	}

//Reads the CSV file and put each line into a different index
	public void readFile(String fileName) throws FileNotFoundException {
		String csvFile = fileName;
		String line = "";
		String[] dataArray;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			// read each line of the file and store it in the list
			while ((line = br.readLine()) != null) {
				dataArray = line.split(",");
				getAllElements.add(dataArray[0]); // modify the index to store the data you need
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Helper method that returns the number of words in the dictionary
	public int numberOfWords() {
		return getAllElements.size();
	}
}
