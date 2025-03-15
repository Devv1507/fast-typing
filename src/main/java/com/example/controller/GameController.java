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
    private Timeline timer;

    public GameController() {
        gameState = new GameState();
        wordGenerator = new RandomWordGenerator();
        currentWord.bind(gameState.currentWordProperty());
        level.bind(gameState.levelProperty());
        timeLeft.bind(gameState.timeLeftProperty());
        startNewGame();
    }

    private void startNewGame() {
        gameState.setLevel(1);
        gameState.setTimeLeft(20);
        generateNewWord();
        startTimer();
    }

    private void generateNewWord() {
        gameState.setCurrentWord(wordGenerator.generateWord());
    }

    private void startTimer() {
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

    public void submitWord(String typedWord) {
        if (typedWord != null && typedWord.equals(gameState.getCurrentWord())) {
            levelUp();
        } else {
            //TODO: Handle wrong word
        }
    }

    private void levelUp() {
        gameState.setLevel(gameState.getLevel() + 1);
        gameState.setTimeLeft(20);
        generateNewWord();
    }

    private void endRound() {
        timer.stop();
        //TODO: Handle end of round
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
}