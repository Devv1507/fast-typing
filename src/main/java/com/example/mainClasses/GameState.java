package com.example.mainClasses;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Represents the state of the game.
 */
public class GameState {

    private final StringProperty currentWord = new SimpleStringProperty("");
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(20);
    private final IntegerProperty eclipseProgress = new SimpleIntegerProperty(100);

    public String getCurrentWord() {
        return currentWord.get();
    }

    public StringProperty currentWordProperty() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord.set(currentWord);
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    public int getTimeLeft() {
        return timeLeft.get();
    }

    public IntegerProperty timeLeftProperty() {
        return timeLeft;
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft.set(timeLeft);
    }

    public int getEclipseProgress() {
        return eclipseProgress.get();
    }

    public IntegerProperty eclipseProgressProperty() {
        return eclipseProgress;
    }

    public void setEclipseProgress(int eclipseProgress) {
        this.eclipseProgress.set(eclipseProgress);
    }
}