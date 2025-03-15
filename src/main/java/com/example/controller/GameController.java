package com.example.controller;

import com.example.Main;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import com.example.utils.GameState;
import com.example.utils.RandomWordGenerator;

/**
 * Controlador para el juego de escritura rápida (fast typing).
 */
public class GameController {

    private final GameState gameState;
    private final RandomWordGenerator wordGenerator;
    private final StringProperty currentWord = new SimpleStringProperty("");
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(20);
    private final IntegerProperty errors = new SimpleIntegerProperty(0); // Contador de errores (global)
    private Timeline timer;

    /**
     * Constructor de la clase GameController.
     * Inicializa el estado del juego, el generador de palabras aleatorias y las propiedades observables.
     */
    public GameController() {
        gameState = new GameState();
        wordGenerator = new RandomWordGenerator();
        currentWord.bind(gameState.currentWordProperty());
        level.bind(gameState.levelProperty());
        timeLeft.bindBidirectional(gameState.timeLeftProperty());
        startNewGame();
    }

    /**
     * Inicia un nuevo juego, generando una nueva palabra y comenzando el temporizador.
     */
    public void startNewGame() {
        generateNewWord();
        startTimer();
    }

    /**
     * Genera una nueva palabra aleatoria utilizando el generador de palabras.
     */
    private void generateNewWord() {
        gameState.setCurrentWord(wordGenerator.generateWord());
    }

    /**
     * Inicia el temporizador para la ronda actual.
     */
    private void startTimer() {
        if (timer != null) {
            timer.stop(); // Detiene el temporizador existente si está en ejecución
        }
        int initialTime = calculateInitialTime();
        gameState.setTimeLeft(initialTime);
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

    /**
     * Valida la palabra escrita por el jugador.
     * @param typedWord La palabra escrita por el jugador en el campo de texto.
     * @return true si la palabra es correcta, false si es incorrecta.
     */
    public boolean submitWord(String typedWord) {
        if (typedWord != null && typedWord.equals(gameState.getCurrentWord())) {
            levelUp();
            return true;
        } else {
            errorsProperty().set(errorsProperty().get() + 1);
            endRound(); // endRound se llamará incluso con submit
            return false;
        }
    }

    /**
     * Incrementa el nivel del juego y comienza una nueva ronda.
     */
    private void levelUp() {
        gameState.setLevel(gameState.getLevel() + 1);
        startNewGame();
    }

    /**
     * Finaliza la ronda actual.
     * Si el tiempo se agota, se evalúa la palabra y, en caso de un error, se añade al contador.
     * Si el contador de errores llega a 4, el juego deja de reiniciarse.
     */
    private void endRound() {
        timer.stop();
        if (timeLeft.get() == 0 && !submitWord(Main.getInputText())) {
            errorsProperty().set(errorsProperty().get() + 1);
        }
        if (errorsProperty().get() >= 4) {
            System.out.println("¡Juego terminado en GameController!");
        } else {
            startNewGame();
        }
    }

    /**
     * Calcula el tiempo inicial basado en el nivel actual.
     * @return El tiempo inicial calculado.
     */
    private int calculateInitialTime() {
        int currentLevel = gameState.getLevel();
        int time = 20;

        // Disminuye el tiempo cada 5 niveles
        for (int i = 5; i <= currentLevel; i += 5) {
            time -= 2;
        }

        // Asegura que el tiempo mínimo sea 2
        return Math.max(2, time);
    }

    /**
     * Obtiene la propiedad de la palabra actual.
     * @return La propiedad de la palabra actual.
     */
    public StringProperty currentWordProperty() {
        return currentWord;
    }

    /**
     * Obtiene la propiedad del nivel.
     * @return La propiedad del nivel.
     */
    public IntegerProperty levelProperty() {
        return level;
    }

    /**
     * Obtiene la propiedad del tiempo restante.
     * @return La propiedad del tiempo restante.
     */
    public IntegerProperty timeLeftProperty() {
        return timeLeft;
    }

    /**
     * Obtiene la propiedad de los errores.
     * @return La propiedad de los errores.
     */
    public IntegerProperty errorsProperty() {
        return errors;
    }

    /**
     * Obtiene el número de errores.
     * @return El número de errores.
     */
    public int getErrorsCount() {
        return errors.get();
    }

}
