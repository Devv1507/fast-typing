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
 * La clase GameController gestiona la lógica central de un juego de palabras,
 * incluyendo el seguimiento del estado del juego, generación de palabras aleatorias,
 * control del tiempo, nivel, y manejo de errores.
 * Esta clase actúa como el controlador que vincula la lógica del juego con
 * la interfaz de usuario, proporcionando propiedades observables para
 * interactuar con componentes de la vista en tiempo real.
 * @author David Esteban Valencia
 */
public class GameController {
    /**
     * Representa el estado actual del juego.
     * Se utiliza para realizar un seguimiento y gestionar información a nivel de juego,
     * como la palabra actual, el nivel y el tiempo restante durante la partida.
     * @see GameState
     */
    private final GameState gameState;
    /**
     * Una instancia de RandomWordGenerator responsable de generar palabras aleatorias
     * utilizadas durante las rondas del juego. Este componente es crucial para proporcionar
     * la característica central del juego de generación de palabras como parte de la lógica
     * del GameController.
     * @see RandomWordGenerator
     */
    private final RandomWordGenerator wordGenerator;
    /**
     * Representa la palabra que se muestra actualmente en el juego, que el jugador debe escribir.
     * Esta propiedad es observable y se puede enlazar a elementos de la interfaz de usuario
     * para actualizaciones en tiempo real. El valor de esta propiedad se restablece o actualiza
     * durante cada nueva ronda.
     */
    private final StringProperty currentWord = new SimpleStringProperty("");
    /**
     * Representa el nivel actual del juego.
     * Es una propiedad observable utilizada para realizar un seguimiento y actualizar el nivel
     * dinámicamente durante la progresión del juego.
     */
    private final IntegerProperty level = new SimpleIntegerProperty(1);
    /**
     * Representa el tiempo restante para la ronda de juego actual.
     * Esta propiedad se utiliza para realizar un seguimiento y actualizar el temporizador de cuenta
     * regresiva durante el juego. Comienza con un valor inicial de 20 segundos y disminuye
     * a medida que avanza el temporizador. El valor es observable, lo que permite que los
     * componentes o listeners de la interfaz de usuario reaccionen cuando cambia el tiempo.
     */
    private final IntegerProperty timeLeft = new SimpleIntegerProperty(20);
    /**
     * Representa el contador global de errores durante el juego.
     * Esta propiedad observable almacena el número actual de errores cometidos
     * por el jugador. Es utilizada para realizar un seguimiento del progreso y para
     * determinar si se ha alcanzado el límite de errores permitido en el juego.
     */
    private final IntegerProperty errors = new SimpleIntegerProperty(0); // Contador de errores (global)
    /**
     * El temporizador utilizado para controlar la cuenta regresiva de la ronda actual.
     * Gestiona los eventos basados en el tiempo durante el juego, como actualizar
     * el tiempo restante y activar el final de una ronda cuando se agota el tiempo.
     */
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
        startNewRound();
    }

    /**
     * Inicia un nuevo juego, generando una nueva palabra y comenzando el temporizador.
     */
    public void startNewRound() {
        generateNewWord();
        startTimer();
    }

    /**
     * Reinicia el estado del juego cuando el jugador pierde.
     */
    private void resetGame() {
        gameState.setLevel(1);
        this.errorsProperty().set(0);
        generateNewWord();
        gameState.setTimeLeft(20);
        startNewRound();
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
        int initialTime = calculateInitialTime();
        gameState.setTimeLeft(initialTime);

        if (timer == null) {
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
        } else {
            timer.stop();
        }
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
            this.handleIncorrectWord();
            return false;
        }
    }

    /**
     * Incrementa el nivel del juego y comienza una nueva ronda.
     */
    private void levelUp() {
        gameState.setLevel(gameState.getLevel() + 1);
        startNewRound();
    }

    /**
     * Gestiona el caso en el que el jugador introduce una palabra incorrecta.
     * Si se supera el límite de errores (4), el juego se reinicia invocando el método resetGame.
     * De lo contrario, genera una nueva palabra para la ronda actual e inicia el cronómetro para el siguiente intento.
     */
    private void handleIncorrectWord() {
        errorsProperty().set(errorsProperty().get() + 1);
        if (errorsProperty().get() >= 4) {
            resetGame();
        } else {
            gameState.setCurrentWord(wordGenerator.generateWord());
            this.startTimer();
        }
    }

    /**
     * Finaliza la ronda actual.
     * Si el tiempo se agota, se evalúa la palabra y, en caso de un error, se añade al contador.
     * Si el contador de errores llega a 4, el juego deja de reiniciarse.
     */
    private void endRound() {
        timer.stop();
        String inputText = Main.getInputText();
        if (!inputText.isEmpty() && inputText.equals(gameState.getCurrentWord())) {
            levelUp();
            return;
        }
        this.handleIncorrectWord();
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
