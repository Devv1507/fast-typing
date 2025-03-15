package com.example.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Interface para el estado del juego.
 */
public interface IGameState {
    String getCurrentWord();

    StringProperty currentWordProperty();

    void setCurrentWord(String currentWord);

    int getLevel();

    IntegerProperty levelProperty();

    void setLevel(int level);

    int getTimeLeft();

    IntegerProperty timeLeftProperty();

    void setTimeLeft(int timeLeft);
}