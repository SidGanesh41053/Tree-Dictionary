import java.io.FileNotFoundException;

public class test {
    public static void main(String[] args) throws FileNotFoundException {
        RedBlackTree<Word> rbt = new RedBlackTree<>();
        WordReader wr = new WordReader();
        SearchBackend sb = new SearchBackend(rbt, wr);
        sb.loadWord("chicken", "noun", "bawk bawk");
        System.out.println(rbt.size());
        sb.removeWord("chicken");
        System.out.println(sb.currentWord == null);
    }
}
