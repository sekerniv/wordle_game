/**
 * This task was created by Niv Seker (https://github.com/sekerniv)
 * <p>
 * For any questions or further assistance, feel free to reach out!
 */

// Leave this import as it is. You'll need it

import assignmentfiles.*;

public class GameEngine {
    private String targetWord;
    private int attemptsLeft;
    private boolean isWin;

    public GameEngine(String targetWord) {
        this.targetWord = targetWord;
        this.attemptsLeft = 6;
        this.isWin = false;
    }

    public static void main(String[] args) {
        WordLoader wordLoader = new WordLoader();
        String word = wordLoader.getRandomWord();
        GameEngine gameEngine = new GameEngine(word);
        GameUI ui = new GameUI();
        System.out.println(word);
        while (!gameEngine.isGameOver()) {
            String guess = ui.readUserGuess();
            ui.displayResult(gameEngine.playGuess(guess), guess, gameEngine.getAttemptsLeft());
        }
        if (gameEngine.isWin())
            ui.displayWin();
        else
            ui.displayLoss(word);
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
        String endString = "";
        int length = Math.min(targetWord.length(), guess.length());
        for (int i = 0; i < length; i++) {
            if (targetWord.indexOf(guess.charAt(i)) == -1)
                endString += '-';
            else if (guess.charAt(i) == targetWord.charAt(i))
                endString += "*";
            else
                endString += "+";
        }
        return endString;
    }

    public boolean isGameOver() {
        return this.attemptsLeft == 0 || this.isWin;
    }

    public String playGuess(String guess) {
        this.attemptsLeft--;
        String s = evaluateGuess(this.targetWord, guess);
        if (guess.equals(this.targetWord)) {
            this.isWin = true;
        }
        return s;
    }


    public boolean isWin() {
        return isWin;
    }

    public String getTargetWord() {
        return this.targetWord;
    }

    public int getAttemptsLeft() {
        return this.attemptsLeft;
    }
}