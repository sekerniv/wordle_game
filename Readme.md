# Wordle Game (Java Assignment)

📄 [Hebrew Instructions](https://docs.google.com/document/d/145s9pmkx0d2R-P5xSY30v6Hus1IbrZkAXfpHIhw7Y14/edit?tab=t.0)

This project is a basic implementation of the game **[Wordle](https://www.nytimes.com/games/wordle)** (by NYT) in Java.

## 💡 Project Overview

Wordle is a word-guessing game where the player has 6 attempts to guess a secret 5-letter word. Feedback is given after each guess to help the player improve.

## 🧩 Main Components

- `GameEngine` – Manages the game logic and state.
- `GameUI` – Handles user input and output via the console.
- `WordLoader` – Loads a list of words and selects a random one.

## 🚀 Steps

1. Implement feedback logic in `evaluateGuess`.
2. Complete `GameEngine` and `GameUI` methods.
3. Add a `main` method to run the game.
4. As a last step, the student will swap the usage of the `GameUI` class (command line UI) with `GameSwingUI` for a graphical interface.
   This step introduces the students to key OOP concepts like encapsulation and separation of concerns, by showing how the game logic remains unchanged while the interface is swapped.

Have fun coding! 🎮
