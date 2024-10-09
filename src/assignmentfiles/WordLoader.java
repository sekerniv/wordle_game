package assignmentfiles;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordLoader {

    private String[] words;

    public static String DEFAULT_WORD_FILE = "resources/wordlist.txt";

    public WordLoader() {
        this.words = loadWords(DEFAULT_WORD_FILE).toArray(new String[0]);
    }

    public WordLoader(String filename) {
        this.words = loadWords(filename).toArray(new String[0]);
    }

    public String getRandomWord() {
        Random rnd = new Random();
        int rndIdx = rnd.nextInt(words.length);
        return words[rndIdx];
    }

    public List<String> loadWords(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    words.add(line.trim()); // removing whitespaces
                } catch (Exception e) {
                    System.out.println("Error reading line");
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file");
            e.printStackTrace();
        }
        System.out.println("Loaded " + words.size() + " words");
        return words;
    }

    public static void main(String[] args) {
        WordLoader wl = new WordLoader(DEFAULT_WORD_FILE);
        System.out.println("Loaded " + wl.words.length + " words.");
    }
}
