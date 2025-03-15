package com.example.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import com.example.mainClasses.GameState;
import com.example.utils.RandomWordGenerator;

/**
 * Controller class for the Typing Speed Game.
 */
public class GameController {

    private final GameState gameState;
    private final RandomWordGenerator wordGenerator;
    private final StringProperty currentWord = new SimpleStringProperty("");
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(20);
    private final IntegerProperty errors = new SimpleIntegerProperty(0); // Error counter (global)
    private Timeline timer;

    public GameController() {
        gameState = new GameState();
        wordGenerator = new RandomWordGenerator();
        currentWord.bind(gameState.currentWordProperty());
        level.bind(gameState.levelProperty());
        timeLeft.bindBidirectional(gameState.timeLeftProperty());
        startNewGame();
    }

    public void startNewGame() {
        generateNewWord();
        startTimer();
    }

    private void generateNewWord() {
        gameState.setCurrentWord(wordGenerator.generateWord());
    }

    private void startTimer() {
        if (timer != null) {
            timer.stop(); // Stop the existing timer if it's running
        }
        int initialTime = calculateInitialTime();
        gameState.setTimeLeft(initialTime); // Establecer el valor a través de la propiedad en GameState
        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    int currentTime = gameState.getTimeLeft();
                    if (currentTime > 0) {
                        gameState.setTimeLeft(currentTime - 1);
                    } else {
                        endRound();
                    }
                })
        );
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    public boolean submitWord(String typedWord) {
        if (typedWord != null && typedWord.equals(gameState.getCurrentWord())) {
            levelUp();
            return true;
        } else {
            errorsProperty().set(errorsProperty().get() + 1);
            endRound(); // endRound will be called even with submit
            return false;
        }
    }

    private void levelUp() {
        gameState.setLevel(gameState.getLevel() + 1);
        startNewGame();
    }

    private void endRound() {
        timer.stop();
        if (timeLeft.get() == 0) {
            errorsProperty().set(errorsProperty().get() + 1);
            System.out.println("Tiempo agotado! Errores: " + errorsProperty().get());
        }
        if (errorsProperty().get() >= 4) {
            System.out.println("Game over in GameController!");
            // No iniciar un nuevo juego aquí, ya que se acabaron los intentos
        } else {
            startNewGame();
        }
    }

    private int calculateInitialTime() {
        int currentLevel = gameState.getLevel();
        int time = 20;

        // Decrease time every 5 levels
        for (int i = 5; i <= currentLevel; i += 5) {
            time -= 2;
        }

        // Ensure the minimum time is 2
        return Math.max(2, time);
    }

    public StringProperty currentWordProperty() {
        return currentWord;
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public IntegerProperty timeLeftProperty() {
        return timeLeft;
    }

    public IntegerProperty errorsProperty() {
        return errors;
    }

    public int getErrors() {
        return errors.get();
    }

}