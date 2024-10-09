import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameEngineTest {

    @Test
    public void testEvaluateGuessExactMatch() {
        String targetWord = "taper";
        String guess = "taper";
        String result = GameEngine.evaluateGuess(targetWord, guess);
        // making one test fail
        assertEquals("*****", result, "The guess should match the target word exactly.");
    }

    @Test
    public void testEvaluateGuessNoMatch() {
        String targetWord = "apple";
        String guess = "wrong";
        String result = GameEngine.evaluateGuess(targetWord, guess);

        assertEquals("-----", result, "There should be no matches between guess and target.");
    }

    @Test
    public void testEvaluateGuessPartialMatch() {
        String targetWord = "taper";
        String guess = "water";
        String result = GameEngine.evaluateGuess(targetWord, guess);

        assertEquals("-*+**", result, "The result doesn't match the expected");
    }

    @Test
    public void testEvaluateGuessAllLettersInWrongPosition() {
        String targetWord = "abcdef";
        String guess = "fedcba";
        String result = GameEngine.evaluateGuess(targetWord, guess);

        assertEquals("++++++", result, "All letters exist in the word but are in the wrong position.");
    }

    @Test
    public void testIncorrectGuess() {
        // Create a game with the target word "apple"
        GameEngine gameEngine = new GameEngine("apple");

        // Make an incorrect guess
        String feedback = gameEngine.playGuess("brick");

        // Check that the number of attempts has gone down to 5
        assertEquals(5, gameEngine.getAttemptsLeft(), "Number of attempts should decrease after one incorrect guess");

        // Check that the game is not over
        assertFalse(gameEngine.isGameOver(), "Game should not be over after one incorrect guess");

        // Check the feedback
        assertEquals("-----", feedback, "Feedback should indicate no correct letters");
    }

    @Test
    public void testCorrectGuess() {
        // Create a game with the target word "apple"
        GameEngine gameEngine = new GameEngine("apple");

        // Make a correct guess
        String feedback = gameEngine.playGuess("apple");

        // Check that the game is over
        assertTrue(gameEngine.isGameOver(), "Game should be over after one correct guess");

        // Check that the player has won
        assertTrue(gameEngine.isWin(), "Player should win after guessing the correct word");

        // Check the feedback
        assertEquals("*****", feedback, "Feedback should indicate all correct letters");
    }

    @Test
    public void testCorrectGuessOnLastAttempt() {
        // Testing Game Over on Correct Guess in Last Attempt
        GameEngine game = new GameEngine("apple");
        int numOfAttempts = game.getAttemptsLeft();
        for(int i = 0; i < numOfAttempts - 1; i++) {
            game.playGuess("wrong");
        }

        assertFalse(game.isGameOver(), "Made " + numOfAttempts + " guess. The game shouldn't be over");
        // Make correct guess on last attempt
        String feedback = game.playGuess("apple");

        // Game should be over, and the player should have won
        assertTrue(game.isGameOver());
        assertTrue(game.isWin());  // Ensure the game was won
        assertEquals("*****", feedback);  // Check correct feedback
    }

    @Test
    public void testAttemptsExhaustionBeforeWin() {
        // Testing Attempts Exhaustion Before Win
        GameEngine game = new GameEngine("apple");
        int numOfAttempts = game.getAttemptsLeft();
        for(int i = 0; i < numOfAttempts; i++) {
            game.playGuess("wrong");
        }

        // Game should be over and the player should have lost
        assertTrue(game.isGameOver());
        assertFalse(game.isWin(), "Made " + numOfAttempts +" attempts. The game should be over.");  // Ensure the game was lost
    }
}


