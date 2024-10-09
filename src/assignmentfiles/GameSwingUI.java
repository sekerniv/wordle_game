package assignmentfiles;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameSwingUI {
    private JFrame frame;
    private JTextField inputField;
    private JTextPane messageArea;
    private JButton submitButton;
    private String guess;

    public GameSwingUI() {
        // Initialize the frame and components
        frame = new JFrame("Wordle Game");
        inputField = new JTextField(10);
        messageArea = new JTextPane();
        submitButton = new JButton("Submit");

        // Set the layout to BorderLayout
        frame.setLayout(new BorderLayout(10, 10));  // Add padding of 10 pixels

        // Create a panel for input and submit button
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        inputPanel.add(new JLabel("Enter your guess: "));
        inputPanel.add(inputField);
        inputPanel.add(submitButton);

        // Set properties for the message area
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        // Add components to the frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Set minimum size for the frame
        frame.setMinimumSize(new Dimension(400, 400));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();  // Resize frame to fit contents
        frame.setVisible(true);

        // Action listener for submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guess = inputField.getText();
                inputField.setText("");  // Clear the input field after submitting
            }
        });
    }

    public String readUserGuess() {
        // Wait for user input
        while (guess == null) {
            try {
                Thread.sleep(100);  // Waiting for user input
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String temp = guess;
        guess = null;  // Reset guess for the next input
        return temp;
    }

    public void displayMessage(String message) {
        // Display a general message
        appendToPane(messageArea, message + "\n", Color.BLACK);
    }

    public void displayResult(String feedback, String guess, int remainingAttempts) {
        appendToPane(messageArea, "Your Guess: " + guess + "\n", Color.BLACK);
        appendFeedbackWithColors(feedback);
        appendToPane(messageArea, "Remaining Attempts: " + remainingAttempts + "\n\n", Color.BLACK);
    }

    private void appendFeedbackWithColors(String feedback) {
        for (int i = 0; i < feedback.length(); i++) {
            char ch = feedback.charAt(i);
            Color color;
            if (ch == '*') {
                color = Color.GREEN;  // Correct letter, correct place
            } else if (ch == '+') {
                color = Color.YELLOW;  // Correct letter, wrong place
            } else {
                color = Color.GRAY;  // Incorrect letter
            }
            appendToPane(messageArea, String.valueOf(ch), color);
        }
        appendToPane(messageArea, "\n", Color.BLACK);  // Newline after feedback
    }

    public void displayLoss(String targetWord) {
        // Game loss message
        JOptionPane.showMessageDialog(frame, "Game over! The word was: " + targetWord);
        appendToPane(messageArea, "Game over! The word was: " + targetWord + "\n", Color.RED);
        frame.dispose();
    }

    public void displayWin() {
        // Game win message
        JOptionPane.showMessageDialog(frame, "Congratulations, YOU WIN!");
        appendToPane(messageArea, "Congratulations, YOU WIN!\n", Color.GREEN);
        frame.dispose();
    }

    private String formatFeedback(String feedback) {
        // Return the feedback string for further formatting
        return feedback;
    }

    private void appendToPane(JTextPane tp, String msg, Color c) {
        // Helper method to append colored text
        StyledDocument doc = tp.getStyledDocument();

        // Create a new style for the text
        Style style = tp.addStyle("Color Style", null);
        StyleConstants.setForeground(style, c);
        StyleConstants.setFontFamily(style, "Monospaced");
        StyleConstants.setAlignment(style, StyleConstants.ALIGN_JUSTIFIED);

        try {
            // Insert the text into the document with the given style
            doc.insertString(doc.getLength(), msg, style);
            // Scroll to the end of the text
            tp.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

}
