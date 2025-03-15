package com.example;
import com.example.controller.GameController;

import com.example.utils.EclipseProgress;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Clase principal para la aplicación del juego de escritura.
 * Lanza la aplicación JavaFX.
 */
public class Main extends Application {
    /**
     * Un TextField estático utilizado para la entrada del usuario en la aplicación.
     * Este campo permite a los usuarios ingresar texto, que puede ser manejado o recuperado
     * según sea necesario por la lógica de la aplicación.
     */
    public static TextField inputField;
    /**
     * Representa el controlador principal del juego para gestionar la lógica y las interacciones del juego.
     * Este controlador es responsable de coordinar el estado del juego de la aplicación,
     * incluyendo el manejo de la entrada del jugador, las reglas del juego y la gestión del progreso.
     * Sirve como la interfaz central entre la vista de la aplicación y la mecánica del juego subyacente.
     * @see GameController
     */
    private final GameController controller = new GameController();
    /**
     * Botón para reiniciar el juego, restableciendo el estado del juego y permitiendo al jugador
     * comenzar una nueva sesión. Este botón normalmente se habilita al final de una sesión de juego.
     */
    private Button restartButton;
    /**
     * Representa el botón utilizado para activar el envío de la entrada del jugador.
     */
    private Button submitButton;
    /**
     * Representa el contenedor de diseño principal de la escena de la aplicación.
     * BorderPane organiza sus hijos en cinco regiones: superior, inferior, izquierda, derecha y centro,
     * lo que permite una interfaz de usuario estructurada y adaptable.
     */
    private BorderPane view;
    /**
     * Un Label utilizado para mostrar el mensaje de fin de juego cuando el juego termina.
     * Este mensaje indica que el juego ha concluido y no es posible seguir jugando.
     */
    private Label gameOverMessage;
    /**
     * Representa la etiqueta utilizada para mostrar el nivel o etapa actual del juego en la vista de la aplicación.
     */
    private Label levelLabel;
    /**
     * Label que representa la frase o el mensaje actual que se muestra en la aplicación.
     */
    private Label phraseLabel;
    /**
     * Label utilizada para mostrar el tiempo restante o la cuenta regresiva durante la sesión de juego.
     */
    private Label timeLabel;
    /**
     * Un elemento ImageView utilizado para mostrar una imagen de eclipse en la aplicación.
     * Este campo es parte de la interfaz de usuario y puede ser manipulado o actualizado
     * a medida que avanza el juego o según la interacción del usuario.
     */
    private ImageView eclipseImage;
    /**
     * Almacena una instancia de la clase EclipseProgress que gestiona la visualización
     * del progreso basado en niveles de error dentro de la aplicación. Este objeto
     * permite rastrear el número de errores cometidos y proporciona imágenes
     * relacionadas con el estado actual del progreso del eclipse.
     * @see EclipseProgress
     */
    private final EclipseProgress eclipseProgress = new EclipseProgress();

    /**
     * Iniciar la aplicación JavaFX.
     * @param primaryStage El escenario principal para la aplicación.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        createView();
        bindData();
        setupEventHandlers();
        startNewGame();

        Scene scene = new Scene(view, 800, 600);

        primaryStage.setTitle("Fast Typing Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Crea la vista principal de la aplicación.
     */
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));

        // Sección superior: Etiquetas de Nivel y Tiempo
        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.TOP_RIGHT);
        levelLabel = new Label("Nivel: 1");
        timeLabel = new Label("Tiempo: 20");

        HBox levelBox = new HBox(10, new Label("Nivel: "), levelLabel);
        levelBox.setAlignment(Pos.TOP_LEFT);

        HBox timeBox = new HBox(10, new Label("Tiempo: "), timeLabel);
        timeBox.setAlignment(Pos.TOP_RIGHT);

        // Imagen del eclipse dentro de un AnchorPane
        eclipseImage = new ImageView();
        eclipseImage.setFitWidth(200);
        eclipseImage.setFitHeight(200);
        eclipseImage.setImage(eclipseProgress.getEclipseImage());

        AnchorPane eclipsePane = new AnchorPane();
        eclipsePane.setPrefSize(200, 200); // Tamaño definido del AnchorPane
        AnchorPane.setTopAnchor(eclipseImage, 0.0);
        AnchorPane.setLeftAnchor(eclipseImage, 0.0);
        AnchorPane.setRightAnchor(eclipseImage, 0.0);
        AnchorPane.setBottomAnchor(eclipseImage, 0.0);
        eclipsePane.getChildren().add(eclipseImage);

        HBox eclipseBox = new HBox(eclipsePane);
        eclipseBox.setAlignment(Pos.TOP_CENTER);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(levelBox);
        topPane.setCenter(eclipseBox);
        topPane.setRight(timeBox);

        view.setTop(topPane);

        // Sección central: Frase e ingreso de texto
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        phraseLabel = new Label("Frase");
        phraseLabel.setFont(Font.font(48));
        inputField = new TextField();
        inputField.setMaxWidth(600);
        inputField.setAlignment(Pos.CENTER);

        submitButton = new Button("Enviar");
        submitButton.setFont(Font.font(24));
        submitButton.setPadding(new Insets(10, 20, 10, 20));

        centerBox.getChildren().addAll(phraseLabel, inputField, submitButton);
        view.setCenter(centerBox);

        // Sección inferior: Mensajes
        Label messageLabel = new Label();
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        gameOverMessage = new Label();
        gameOverMessage.setVisible(false);

        restartButton = new Button("Reintentar");
        restartButton.setFont(Font.font(18));
        restartButton.setOnAction(event -> startNewGame());
        restartButton.setVisible(false);

        bottomBox.getChildren().addAll(gameOverMessage, restartButton, messageLabel);
        view.setBottom(bottomBox);
    }

    /**
     * Obtiene el texto contenido en el campo de entrada.
     * @return El texto actual del campo de entrada.
     */
    public static String getInputText() {
        return inputField.getText();
    }

    /**
     * Enlaza los datos del controlador con la vista.
     */
    private void bindData() {
        levelLabel.textProperty().bind(controller.levelProperty().asString("%d"));
        timeLabel.textProperty().bind(controller.timeLeftProperty().asString("%d"));
        phraseLabel.textProperty().bind(controller.currentWordProperty());
        eclipseProgress.errorsProperty().addListener((obs, oldVal, newVal) -> {
            eclipseImage.setImage(eclipseProgress.getEclipseImage());
        });
    }

    /**
     * Maneja el envío de la palabra escrita por el jugador.
     */
    private void handleSubmit() {
        String typedWord = inputField.getText();
        if (!controller.submitWord(typedWord)) {
            eclipseProgress.setErrors(controller.getErrorsCount());
        }
        inputField.clear();
    }

    /**
     * Inicia un nuevo juego, restableciendo el estado y habilitando la entrada.
     */
    private void startNewGame() {
        inputField.setDisable(false);
        submitButton.setDisable(false);

        // Oculta el mensaje de fin de juego y el botón de reintento
        gameOverMessage.setVisible(false);
        restartButton.setVisible(false);

        // Establece los valores iniciales para el nuevo juego
        controller.errorsProperty().set(0);
        eclipseProgress.setErrors(controller.getErrorsCount());

        controller.startNewRound();
    }

    /**
     * Muestra el mensaje de fin de juego y deshabilita la entrada.
     */
    private void endGame() {
        gameOverMessage.setText("¡Ups, se te acabaron los intentos, trata de nuevo!");
        gameOverMessage.setFont(Font.font(20));
        gameOverMessage.setVisible(true);
        restartButton.setVisible(true);

        inputField.setDisable(true);
        submitButton.setDisable(true);
    }

    /**
     * Configura los controladores de eventos para la vista.
     */
    private void setupEventHandlers() {
        submitButton.setOnAction(event -> handleSubmit());
        inputField.setOnAction(event -> handleSubmit()); // Tecla Enter
        controller.errorsProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 4) {
                endGame();
            }
            eclipseProgress.setErrors(newVal.intValue());
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
