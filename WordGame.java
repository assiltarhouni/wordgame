import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;  // Import Timer from java.util
import java.util.TimerTask;
import java.util.Scanner;

public class WordGame {
    private static final int TIMER_DURATION = 240000; // 4 minutes
    private static final int MAX_WORDS_PER_PLAYER = 3;
    private Timer timer;

    private PlayerWindow player1Window;
    private PlayerWindow player2Window;
    private GameWindow gameWindow;

    private List<String> dictionary;
    private int currentPlayerIndex;
    private int wordsCorrectPlayer1;
    private int wordsCorrectPlayer2;
    private long startTime;

    public WordGame() {
        initializeDictionary();
        initializeUI();
        initializeGame();
    }

    private void initializeDictionary() {
        dictionary = new ArrayList<>();
        // Use try-with-resources to read the dictionary file
        try (Scanner scanner = new Scanner(WordGame.class.getResourceAsStream("french_dictionary.txt"))) {
            while (scanner.hasNext()) {
                dictionary.add(scanner.next().toUpperCase());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeUI() {
        player1Window = new PlayerWindow("Player 1");
        player2Window = new PlayerWindow("Player 2");
        List<Character> letters = generateRandomLetters();  // Generate random letters
        gameWindow = new GameWindow("Game", letters, e -> handleButtonClick(e));
    
        player1Window.addSubmitListener(e -> handlePlayerSubmit(player1Window));
        player2Window.addSubmitListener(e -> handlePlayerSubmit(player2Window));
    
        // Set the button click listener after creating player windows
        gameWindow.setButtonClickListener(e -> handleButtonClick(e));
    }

    private List<Character> generateRandomLetters() {
        List<Character> letters = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            // Add vowels 3 times
            letters.add('A');
            letters.add('E');
            letters.add('I');
            letters.add('O');
            letters.add('U');
        }
        for (int i = 0; i < 2; i++) {
            // Add consonants 2 times
            letters.add('B');
            letters.add('C');
            // Add other consonants as needed
        }
        Collections.shuffle(letters);
        return letters;
    }

    private void initializeGame() {
        currentPlayerIndex = 0;
        wordsCorrectPlayer1 = 0;
        wordsCorrectPlayer2 = 0;
        timer = new Timer();
    }

    private void handleButtonClick(ActionEvent e) {
        char clickedLetter = gameWindow.getClickedLetter(e);
        gameWindow.disableLetterButton(clickedLetter);
        player1Window.addLetter(clickedLetter);
        player2Window.addLetter(clickedLetter);
    }

    private void handlePlayerSubmit(PlayerWindow playerWindow) {
        String playerName = playerWindow.getPlayerName();
        String word = playerWindow.proposeWord();
    
        if (!playerName.isEmpty() && !word.isEmpty()) {
            if (isValidWord(word)) {
                if (currentPlayerIndex == 0) {
                    wordsCorrectPlayer1++;
                } else {
                    wordsCorrectPlayer2++;
                }
                JOptionPane.showMessageDialog(null, "Correct word!");
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect word!");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please enter both name and word!");
            return; // Exit the method if either name or word is empty
        }
    
        // Handle end of round or game
        if (currentPlayerIndex == 0) {
            currentPlayerIndex = 1;
            player2Window.setVisible(true);
            player1Window.setVisible(false);
            player2Window.resetWordArea();
        } else {
            currentPlayerIndex = 0;
            player1Window.setVisible(true);
            player2Window.setVisible(false);
            player1Window.resetWordArea();
        }
    
        if (wordsCorrectPlayer1 + wordsCorrectPlayer2 < MAX_WORDS_PER_PLAYER * 2) {
            // Start a new round
            startRound();
        } else {
            endGame();
        }
    }
    

    private boolean isValidWord(String word) {
        return dictionary.contains(word);
    }

    private void startRound() {
        startTime = System.currentTimeMillis();
        gameWindow.enableAllLetterButtons();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Timer task: End the round after 4 minutes
                JOptionPane.showMessageDialog(null, "Time's up!");
                handlePlayerSubmit(currentPlayerIndex == 0 ? player1Window : player2Window);
            }
        }, TIMER_DURATION);
        gameWindow.setVisible(true);
    }

    private void endGame() {
        timer.cancel(); // Cancel the timer

        // Display endgame statistics
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        int seconds = (int) (elapsedTime / 1000) % 60;
        int minutes = (int) (elapsedTime / (1000 * 60));
        String resultMessage = String.format("Game over!\n\n%s: %d correct words\n%s: %d correct words\nTime: %d minutes %d seconds",
                player1Window.getPlayerName(), wordsCorrectPlayer1,
                player2Window.getPlayerName(), wordsCorrectPlayer2,
                minutes, seconds);

        JOptionPane.showMessageDialog(null, resultMessage);

        // Add more statistics as needed
        System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WordGame wordGame = new WordGame();
            wordGame.player1Window.setVisible(true);
            wordGame.startRound();
        });
    }
}
