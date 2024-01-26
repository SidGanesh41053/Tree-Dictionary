public class Word implements Comparable<Word>, WordInterface {

	String word = (String) "";
	String def = (String) "";
	String part = (String) "";

	public void setWord(String newWord) {
		word = newWord;
	}

	public void setPart(String newPart) {
		part = newPart;
	}

	public void setDefinition(String newDef) {
		def = newDef;
	}

	@Override
	public String getWord() {
		return word;
	}

	@Override
	public String getPart() {
		return part;
	}

	@Override
	public String getDefinition() {
		return def;
	}

	@Override
	public int compareTo(Word lit) {
		if (this.word.equalsIgnoreCase(lit.word)) {
			return 0;
		}
		else if (this.word.compareTo(lit.word) < 0) {
			return 1;
		}
		else {
			return -1;
		}
	}

	public String toString() {
		return this.word;
	}

}
