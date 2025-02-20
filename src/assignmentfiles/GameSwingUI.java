package assignmentfiles;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class GameSwingUI {
    private JFrame frame;
    private JPanel boardPanel;
    private JPanel keyboardPanel;
    private JLabel statusLabel;
    private JLabel[][] cells;

    private static final int TOTAL_ROWS = 6;
    private static final int TOTAL_COLS = 5;

    // Tracks which row the user is currently filling
    private int currentRow = 0;

    // The current guess (max length = 5)
    private StringBuilder currentGuess = new StringBuilder(TOTAL_COLS);

    // The guess that gets submitted to the GameEngine
    private String submittedGuess = null;

    // Keyboard color states
    private enum KeyState { DEFAULT, GRAY, YELLOW, GREEN }
    private Map<Character, JButton> keyButtons = new HashMap<>();
    private Map<Character, KeyState> keyStates = new HashMap<>();

    public GameSwingUI() {
        // Create main frame
        frame = new JFrame("Wordle Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setFocusable(true);

        // Build the board
        // Using 8 px spacing in GridLayout for clarity
        boardPanel = new JPanel(new GridLayout(TOTAL_ROWS, TOTAL_COLS, 8, 8));
        boardPanel.setBackground(new Color(211, 214, 218));

        // Give the board a preferred size so it won't shrink too small
        // Adjust as needed to make cells appear square/dense
        boardPanel.setPreferredSize(new Dimension(340, 400));

        cells = new JLabel[TOTAL_ROWS][TOTAL_COLS];
        for (int row = 0; row < TOTAL_ROWS; row++) {
            for (int col = 0; col < TOTAL_COLS; col++) {
                JLabel cell = new JLabel("", SwingConstants.CENTER);
                cell.setOpaque(true);
                cell.setBackground(Color.WHITE);
                cell.setFont(new Font("SansSerif", Font.BOLD, 28));
                cell.setBorder(new LineBorder(new Color(180, 184, 188), 2));
                cells[row][col] = cell;
                boardPanel.add(cell);
            }
        }

        // Put boardPanel in a FlowLayout container so it's centered and not stretched
        JPanel boardContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        boardContainer.setBackground(new Color(211, 214, 218));
        boardContainer.add(boardPanel);

        frame.add(boardContainer, BorderLayout.CENTER);

        // Bottom panel: status + on-screen keyboard
        JPanel bottomPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Attempts left: 6", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        bottomPanel.add(statusLabel, BorderLayout.NORTH);

        keyboardPanel = buildKeyboardPanel();
        bottomPanel.add(keyboardPanel, BorderLayout.CENTER);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Add a KeyListener for physical keyboard input
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_ENTER) {
                    handleKeyPress("ENTER");
                } else if (code == KeyEvent.VK_BACK_SPACE) {
                    handleKeyPress("BACKSPACE");
                } else {
                    char c = e.getKeyChar();
                    // Ensure it's a letter
                    if (Character.isLetter(c)) {
                        // Convert to uppercase for the UI
                        handleKeyPress(String.valueOf(Character.toUpperCase(c)));
                    }
                }
            }
        });

        // You can tweak the frame size as needed
        frame.setMinimumSize(new Dimension(600, 700));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Ensure focus so key events go to the frame
        frame.requestFocusInWindow();
    }

    /**
     * Build the QWERTY keyboard as a panel of buttons, including ENTER & BACKSPACE.
     */
    private JPanel buildKeyboardPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(211, 214, 218));

        // Row 1
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        row1.setOpaque(false);
        String row1Keys = "QWERTYUIOP";
        for (char c : row1Keys.toCharArray()) {
            row1.add(createKeyButton(c));
        }
        mainPanel.add(row1);

        // Row 2
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        row2.setOpaque(false);
        String row2Keys = "ASDFGHJKL";
        for (char c : row2Keys.toCharArray()) {
            row2.add(createKeyButton(c));
        }
        mainPanel.add(row2);

        // Row 3
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 3, 3));
        row3.setOpaque(false);

        JButton enterBtn = new JButton("ENTER");
        styleKeyButton(enterBtn);
        enterBtn.addActionListener(e -> handleKeyPress("ENTER"));
        row3.add(enterBtn);

        String row3Keys = "ZXCVBNM";
        for (char c : row3Keys.toCharArray()) {
            row3.add(createKeyButton(c));
        }

        JButton backspaceBtn = new JButton("«");
        styleKeyButton(backspaceBtn);
        backspaceBtn.addActionListener(e -> handleKeyPress("BACKSPACE"));
        row3.add(backspaceBtn);

        mainPanel.add(row3);
        return mainPanel;
    }

    /**
     * Create a single letter button for the on-screen keyboard.
     */
    private JButton createKeyButton(char c) {
        char upper = Character.toUpperCase(c);
        JButton btn = new JButton(String.valueOf(upper));
        styleKeyButton(btn);

        // Store references
        keyButtons.put(upper, btn);
        keyStates.put(upper, KeyState.DEFAULT);

        btn.addActionListener(e -> handleKeyPress(String.valueOf(upper)));
        return btn;
    }

    /**
     * Common styling for keyboard buttons
     */
    private void styleKeyButton(JButton btn) {
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setBackground(Color.LIGHT_GRAY);
        btn.setFocusPainted(false);
    }

    /**
     * Handle a pressed "key" (either from on-screen button or physical keyboard).
     */
    private void handleKeyPress(String input) {
        if ("ENTER".equals(input)) {
            if (currentGuess.length() == TOTAL_COLS) {
                // We have a full guess, so submit
                synchronized (this) {
                    submittedGuess = currentGuess.toString();
                    this.notifyAll();
                }
                currentGuess.setLength(0);
            } else {
                displayMessage("Not enough letters!");
            }
            return;
        }

        if ("BACKSPACE".equals(input)) {
            if (currentGuess.length() > 0) {
                int removeIndex = currentGuess.length() - 1;
                currentGuess.deleteCharAt(removeIndex);
                cells[currentRow][removeIndex].setText("");
            }
            return;
        }

        // Otherwise it's a letter
        if (currentGuess.length() < TOTAL_COLS) {
            currentGuess.append(input);
            int idx = currentGuess.length() - 1;
            cells[currentRow][idx].setText(input.toUpperCase());
        }
    }

    /**
     * Blocks until the user presses ENTER with a 5-letter guess.
     * Convert guess to lowercase so the GameEngine can compare easily.
     */
    public String readUserGuess() {
        synchronized (this) {
            while (submittedGuess == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String guess = submittedGuess.toLowerCase();
            submittedGuess = null;
            return guess;
        }
    }

    /**
     * Display the feedback for the guess in the current row, then increment row.
     */
    public void displayResult(String feedback, String guess, int remainingAttempts) {
        if (currentRow >= TOTAL_ROWS) {
            return;
        }
        statusLabel.setText("Attempts left: " + remainingAttempts);

        // Animate letters in the row
        CountDownLatch latch = new CountDownLatch(guess.length());
        for (int i = 0; i < guess.length() && i < TOTAL_COLS; i++) {
            char letter = guess.charAt(i);
            Color finalColor = getColorForFeedback(feedback.charAt(i));
            animateLetter(cells[currentRow][i], letter, finalColor, i * 300, latch);
        }

        // Update keyboard colors
        for (int i = 0; i < guess.length() && i < TOTAL_COLS; i++) {
            updateKeyColor(guess.charAt(i), feedback.charAt(i));
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        currentRow++;
    }

    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    public void displayWin() {
        JOptionPane.showMessageDialog(frame, "Congratulations, YOU WIN!");
        frame.dispose();
    }

    public void displayLoss(String targetWord) {
        JOptionPane.showMessageDialog(frame, "Game Over! The word was: " + targetWord);
        frame.dispose();
    }

    /**
     * Return color for each feedback character: '*' -> green, '+' -> yellow, '-' -> gray
     */
    private Color getColorForFeedback(char feedbackChar) {
        switch (feedbackChar) {
            case '*':
                return new Color(106, 170, 100); // green
            case '+':
                return new Color(201, 180, 88);  // yellow
            default:
                return new Color(120, 124, 126); // gray
        }
    }

    /**
     * Animate a single cell to simulate a flip effect, then set final color.
     */
    private void animateLetter(JLabel label, char letter, Color finalColor,
                               int initialDelay, CountDownLatch latch) {
        final int steps = 10;
        final int frameDelay = 50;
        final int normalFontSize = 28;
        final int minFontSize = 10;

        Timer timer = new Timer(frameDelay, null);
        timer.setInitialDelay(initialDelay);
        final int[] currentStep = {0};

        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int step = currentStep[0]++;
                if (step < steps) {
                    int fontSize;
                    if (step < steps / 2) {
                        // Shrink
                        fontSize = normalFontSize
                                - (normalFontSize - minFontSize) * step / (steps / 2);
                        if (step == 0) {
                            label.setText("");
                        }
                    } else {
                        if (step == steps / 2) {
                            label.setText(String.valueOf(letter).toUpperCase());
                            label.setBackground(finalColor);
                        }
                        // Expand
                        fontSize = minFontSize
                                + (normalFontSize - minFontSize) * (step - steps / 2) / (steps / 2);
                    }
                    label.setFont(new Font("SansSerif", Font.BOLD, fontSize));
                    label.repaint();
                } else {
                    timer.stop();
                    latch.countDown();
                }
            }
        });
        timer.start();
    }

    /**
     * Update the color of the on-screen keyboard button based on feedback.
     * '*' -> GREEN, '+' -> YELLOW, '-' -> GRAY.
     * Do not overwrite GREEN with a lesser color.
     */
    private void updateKeyColor(char c, char feedbackChar) {
        c = Character.toUpperCase(c);
        KeyState currentState = keyStates.getOrDefault(c, KeyState.DEFAULT);
        KeyState newState;

        switch (feedbackChar) {
            case '*':
                newState = KeyState.GREEN;
                break;
            case '+':
                // If it's already GREEN, keep it GREEN; otherwise YELLOW
                newState = (currentState == KeyState.GREEN) ? KeyState.GREEN : KeyState.YELLOW;
                break;
            default:
                // Only set GRAY if we haven't marked it GREEN before
                if (currentState == KeyState.DEFAULT || currentState == KeyState.GRAY
                        || currentState == KeyState.YELLOW) {
                    newState = KeyState.GRAY;
                } else {
                    newState = currentState;
                }
                break;
        }

        if (newState != currentState) {
            keyStates.put(c, newState);
            keyButtons.get(c).setBackground(colorForKeyState(newState));
        }
    }

    private Color colorForKeyState(KeyState state) {
        switch (state) {
            case GREEN:
                return new Color(106, 170, 100);
            case YELLOW:
                return new Color(201, 180, 88);
            case GRAY:
                return new Color(120, 124, 126);
            default:
                return Color.LIGHT_GRAY;
        }
    }
}
