/**
 * This task was created by Niv Seker (https://github.com/sekerniv)
 *
 * For any questions or further assistance, feel free to reach out!
 */

// Leave this import as it is. You'll need it
import assignmentfiles.*;

public class GameEngine {

    public GameEngine(String targetWord) {
        
    }

    public String playGuess(String guess) {
        return "-----";
    }

    public static void main(String[] args) {
        
    }

    /**
     * Compares the player's guess to the target word and returns feedback.
     * For each character in the guess:
     * - '*' if the character is in the correct position.
     * - '+' if the character is in the target word but in the wrong position.
     * - '-' if the character is not in the target word.
     *
     * The comparison is done up to the length of the shorter word, ignoring any extra characters.
     * Example:
     * targetWord = "taper", guess = "water"
     * Returns: "-*+**"
     **/
    public static String evaluateGuess(String targetWord, String guess) {
        return "-----";
    }

    public boolean isGameOver() {
        return false;
    }

    public boolean isWin() {
        return false;
    }

    public int getAttemptsLeft() {
        return 0;
    }
}
