package com.example.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

/**
 * Define la estructura para el estado del juego, proporcionando métodos para gestionar
 * la palabra actual, el nivel y el tiempo restante, junto con sus propiedades observables.
 * @author David Esteban Valencia
 */
public interface IGameState {
    /**
     * Obtiene la palabra actual de la ronda.
     * @return La palabra actual como una cadena de texto.
     */
    String getCurrentWord();

    /**
     * Obtiene la palabra de la ronda como una propiedad observable.
     * @return Propiedad observable de la palabra actual.
     */
    StringProperty currentWordProperty();

    /**
     * Establece la palabra actual de la ronda.
     * @param currentWord Nueva palabra a establecer.
     */
    void setCurrentWord(String currentWord);

    /**
     * Obtiene el nivel actual del juego.
     * @return Nivel actual como un número entero.
     */
    int getLevel();

    /**
     * Obtiene la propiedad observable del nivel del juego.
     * @return Propiedad observable del nivel.
     */
    IntegerProperty levelProperty();

    /**
     * Establece el nivel del juego.
     * @param level Nuevo nivel a establecer.
     */
    void setLevel(int level);

    /**
     * Obtiene el tiempo restante en la ronda actual.
     * @return Tiempo restante en segundos.
     */
    int getTimeLeft();

    /**
     * Obtiene la propiedad observable del tiempo restante.
     * @return Propiedad observable del tiempo restante.
     */
    IntegerProperty timeLeftProperty();

    /**
     * Establece el tiempo restante en la ronda actual.
     * @param timeLeft Nuevo tiempo restante en segundos.
     */
    void setTimeLeft(int timeLeft);
}
