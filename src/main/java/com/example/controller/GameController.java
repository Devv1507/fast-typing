package com.example.controller;

import com.example.utils.EclipseProgress;
import com.example.utils.GameState;
import com.example.utils.RandomWordGenerator;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Esta clase gestiona la lógica central de un juego de palabras,
 * incluyendo el seguimiento del estado del juego, generación de palabras aleatorias, control del tiempo, nivel, y manejo de errores.
 * @author David Esteban Valencia
 */
public class GameController implements Initializable {
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
    private final IntegerProperty errors = new SimpleIntegerProperty(0);

    /**
     * El temporizador utilizado para controlar la cuenta regresiva de la ronda actual.
     * Gestiona los eventos basados en el tiempo durante el juego, como actualizar
     * el tiempo restante y activar el final de una ronda cuando se agota el tiempo.
     */
    private Timeline timer;

    /**
     * Instancia de EclipseProgress para gestionar la visualización del progreso del eclipse.
     */
    private final EclipseProgress eclipseProgress = new EclipseProgress();

    // Componentes FXML
    @FXML private Label levelLabel;
    @FXML private Label timeLabel;
    @FXML private Label phraseLabel;
    @FXML private TextField inputField;
    @FXML private Button submitButton;
    @FXML private Button restartButton;
    @FXML private Label messageLabel;
    @FXML private ImageView eclipseImage;

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

        // Enlazar la propiedad de errores con EclipseProgress
        errors.addListener((obs, oldVal, newVal) -> {
            eclipseProgress.setErrors(newVal.intValue());
        });
    }

    /**
     * Inicializador que asegura la carga de los componentes de JavaFX.
     * Configura los bindings, listeners y prepara el estado inicial del juego.
     * @param url La ubicación utilizada para resolver rutas relativas para el objeto raíz.
     * @param resourceBundle Los recursos utilizados para localizar el objeto raíz.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Actualizar la imagen del eclipse
        updateEclipseImage();

        // Enlazar propiedades a componentes FXML
        levelLabel.textProperty().bind(level.asString());
        timeLabel.textProperty().bind(timeLeft.asString());
        phraseLabel.textProperty().bind(currentWord);

        // Configurar eventos de botones
        submitButton.setOnAction(event -> handleSubmit());
        restartButton.setOnAction(event -> resetGame());

        // Configurar evento de tecla para el campo de texto
        inputField.setOnKeyPressed(this::handleKeyPress);

        // Iniciar el juego
        startNewRound();
    }

    /**
     * Maneja el evento de teclado detectando si la tecla presionada es "Enter".
     * Si se presiona "Enter", se invoca a handleSubmit para validar la palabra.
     * @param event La información del evento de teclado, incluyendo detalles de la tecla presionada.
     */
    @FXML
    private void handleKeyPress(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleSubmit();
        }
    }

    /**
     * Gestiona el envío de la entrada del jugador durante el juego.
     * Verifica la palabra introducida en el inputField invocando a submitWord.
     * Si la palabra es correcta, muestra un mensaje de éxito.
     * Si la palabra es incorrecta, actualiza el mensaje de error según el número de intentos restantes.
     * Si el número de intentos incorrectos supera el límite, muestra un mensaje indicando que no quedan reintentos.
     */
    @FXML
    private void handleSubmit() {
        if (submitWord(inputField.getText())) {
            inputField.clear();
            messageLabel.setText("¡Correcto!");
        } else {
            inputField.clear();
            if (errors.get() < 4) {
                messageLabel.setText("¡Incorrecto! Intenta de nuevo.");
            } else {
                this.messageLabel.setText("Upps, ya no te quedan re-intentos. Intenta de nuevo!");
            }
        }
    }

    /**
     * Inicia una nueva ronda en el juego.
     * Actualiza el estado del juego generando una nueva palabra, configurando el cronómetro y preparando
     * componentes com inputField y messageLabel.
     */
    public void startNewRound() {
        generateNewWord();
        startTimer();
        inputField.clear();
        inputField.requestFocus();
        messageLabel.setText("Escribe la palabra señalada antes de que se acabe el tiempo.");
        messageLabel.setVisible(true);
    }

    /**
     * Restablece el juego a su estado inicial.
     * Prepara el juego para una nueva sesión restableciendo valores del nivel actual, del recuento de errores y del tiempo restante.
     * También actualiza los componentes de la interfaz (botones e imágenes) para reflejar el estado inicial.
     */
    private void resetGame() {
        gameState.setLevel(1);
        errors.set(0);
        gameState.setTimeLeft(20);
        submitButton.setDisable(false);
        restartButton.setVisible(false);
        String initialImage = eclipseProgress.getEclipseImageForErrors(0);
        eclipseImage.setImage(
                new Image(getClass().getResourceAsStream(initialImage))
        );
        startNewRound();
    }

    /**
     * Genera una nueva palabra aleatoria utilizando el generador de palabras.
     */
    private void generateNewWord() {
        gameState.setCurrentWord(wordGenerator.generateWord());
    }

    /**
     * Inicializa un temporizador para la ronda actual.
     * El temporizador disminuye el tiempo restante cada segundo y actualiza el estado del juego según corresponda.
     * Cuando se agota el tiempo, el juego evalúa la respuesta del jugador.
     * El tiempo de cuenta regresiva inicial se calcula según el nivel de juego actual.
     */
    private void startTimer() {
        int initialTime = calculateInitialTime();
        gameState.setTimeLeft(initialTime);

        if (timer != null) {
            timer.stop();
        }

        timer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    int currentTime = gameState.getTimeLeft();
                    if (currentTime > 0) {
                        gameState.setTimeLeft(currentTime - 1);
                    } else {
                        evaluateAnswer();
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
            handleIncorrectWord();
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
     * Si se supera el límite de errores (4), el juego se reinicia.
     * De lo contrario, genera una nueva palabra para la ronda actual e inicia el cronómetro para el siguiente intento.
     */
    private void handleIncorrectWord() {
        errors.set(errors.get() + 1);
        updateEclipseImage();
        if (errors.get() >= 4) {
            this.submitButton.setDisable(true);
            this.restartButton.setVisible(true);
            this.timer.stop();
        } else {
            generateNewWord();
            startTimer();
        }
    }

    /**
     * Evalúa la respuesta del jugador al final de la ronda actual.
     * Si el texto introducido coincide con la palabra actual del juego, el jugador avanza al siguiente nivel.
     * Si no coincide, activa el manejo de una palabra incorrecta.
     */
    private void evaluateAnswer() {
        timer.stop();
        String inputText = inputField.getText();
        if (!inputText.isEmpty() && inputText.equals(gameState.getCurrentWord())) {
            levelUp();
        } else {
            handleIncorrectWord();
        }
        handleSubmit();
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
     * Actualiza la imagen del eclipse en función del número de errores.
     */
    private void updateEclipseImage() {
        String imagePath = eclipseProgress.getEclipseImageForErrors(errors.get());
        if (eclipseImage != null && imagePath != null) {
            eclipseImage.setImage(
                    new Image(getClass().getResourceAsStream(imagePath)));
        }
    }
}