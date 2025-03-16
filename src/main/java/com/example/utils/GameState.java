package com.example.utils;

import com.example.interfaces.IGameState;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Representa el estado de un juego,
 * incluyendo propiedades para la palabra actual, el nivel del juego y el tiempo restante.
 * Esta clase proporciona métodos getter y setter, así como propiedades observables para estos atributos.
 * @author David Esteban Valencia
 */
public class GameState implements IGameState {
    /**
     * Representa la palabra actual en el estado del juego.
     * Esta propiedad es observable y se puede actualizar o monitorizar para detectar cambios.
     * @see StringProperty
     */
    private final StringProperty currentWord = new SimpleStringProperty("");

    /**
     * Representa el nivel actual del estado del juego.
     * Es una propiedad observable que permite monitorear cambios en el nivel.
     * @see IntegerProperty
     */
    private final IntegerProperty level = new SimpleIntegerProperty(1);

    /**
     * Representa el tiempo restante en el estado actual del juego.
     * Inicializado con un valor predeterminado de 20 segundos.
     * Es una propiedad observable que permite detectar cambios en el tiempo restante.
     * @see StringProperty
     */
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(20);

    /**
     * Obtiene la palabra actual de la ronda.
     * Representa la palabra activa en uso en la sesión de juego actual.
     * @return La palabra actual como una cadena de texto.
     */
    @Override
    public String getCurrentWord() {
        return currentWord.get();
    }

    /**
     * Obtiene la propiedad observable de la palabra actual.
     * @return La propiedad asociada a la palabra actual.
     * @see StringProperty
     */
    @Override
    public StringProperty currentWordProperty() {
        return currentWord;
    }

    /**
     * Establece una nueva palabra para la ronda actual.
     * @param currentWord La nueva palabra a asignar.
     */
    @Override
    public void setCurrentWord(String currentWord) {
        this.currentWord.set(currentWord);
    }

    /**
     * Obtiene el nivel actual del juego.
     * @return El nivel actual como un entero.
     */
    @Override
    public int getLevel() {
        return level.get();
    }

    /**
     * Obtiene la propiedad observable del nivel del juego.
     * @return La propiedad asociada al nivel del juego.
     * @see IntegerProperty
     */
    @Override
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Establece un nuevo nivel para el juego.
     * @param level El nuevo nivel a asignar.
     */
    @Override
    public void setLevel(int level) {
        this.level.set(level);
    }

    /**
     * Obtiene el tiempo restante en la ronda actual.
     * @return El tiempo restante en segundos.
     */
    @Override
    public int getTimeLeft() {
        return timeLeft.get();
    }

    /**
     * Obtiene la propiedad observable del tiempo restante.
     * @return La propiedad asociada al tiempo restante.
     * @see IntegerProperty
     */
    @Override
    public IntegerProperty timeLeftProperty() {
        return timeLeft;
    }

    /**
     * Establece un nuevo valor para el tiempo restante en la ronda actual.
     * @param timeLeft El nuevo tiempo restante en segundos.
     */
    @Override
    public void setTimeLeft(int timeLeft) {
        this.timeLeft.set(timeLeft);
    }
}