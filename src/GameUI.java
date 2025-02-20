import java.util.Scanner;

public class GameUI {
    private Scanner input = new Scanner(System.in);

    public String readUserGuess() {
        return input.next();
    }

    public void displayResult(String feedback, String guess, int attemptsLeft) {
        System.out.println("You have " + attemptsLeft + " attempts left.");
        System.out.println("Your guess: " + guess);
        System.out.println("Feedback: " + feedback);
    }

    public void displayWin() {
        System.out.println("Congratulations, YOU WIN!");
    }

    public void displayLoss(String targetWord) {
        System.out.println("Game over! The word was: " + targetWord);
    }
}