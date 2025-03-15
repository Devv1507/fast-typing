package com.example.utils;

import com.example.interfaces.IGameState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa el estado del juego.
 */
public class GameState implements IGameState {
    private final StringProperty currentWord = new SimpleStringProperty("");
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(20);

    /**
     * Obtiene la palabra actual.
     * @return La palabra actual.
     */
    @Override
    public String getCurrentWord() {
        return currentWord.get();
    }

    /**
     * Obtiene la propiedad de la palabra actual.
     * @return La propiedad de la palabra actual.
     */
    @Override
    public StringProperty currentWordProperty() {
        return currentWord;
    }

    @Override
    public void setCurrentWord(String currentWord) {
        this.currentWord.set(currentWord);
    }

    @Override
    public int getLevel() {
        return level.get();
    }

    /**
     * Obtiene la propiedad del nivel.
     * @return La propiedad del nivel.
     */
    @Override
    public IntegerProperty levelProperty() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level.set(level);
    }

    @Override
    public int getTimeLeft() {
        return timeLeft.get();
    }

    /**
     * Obtiene la propiedad del tiempo restante.
     * @return La propiedad del tiempo restante.
     */
    @Override
    public IntegerProperty timeLeftProperty() {
        return timeLeft;
    }

    @Override
    public void setTimeLeft(int timeLeft) {
        this.timeLeft.set(timeLeft);
    }
}