package com.example;
import com.example.controller.GameController;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/**
 * Clase principal para la aplicación del juego de escritura.
 * Lanza la aplicación JavaFX.
 */
public class Main extends Application {
    private final GameController controller = new GameController();
    private Button restartButton;
    private Button submitButton;
    private BorderPane view;
    private Label gameOverMessage;
    private Label levelLabel;
    private Label phraseLabel;
    private Label timeLabel;
    private TextField inputField;
    private ImageView eclipseImage;
    private final String[] eclipseImagePaths = {
            "/images/eclipse_0.png",
            "/images/eclipse_25.png",
            "/images/eclipse_50.png",
            "/images/eclipse_75.png",
            "/images/eclipse_100.png"
    };

    /**
     * Método principal para iniciar la aplicación JavaFX.
     * @param primaryStage El escenario principal para la aplicación.
     */
    @Override
    public void start(Stage primaryStage) {
        createView();
        bindData();
        setupEventHandlers();
        startNewGame();

        Scene scene = new Scene(view, 800, 600);

        primaryStage.setTitle("Typing Speed Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Crea la vista principal de la aplicación.
     */
    private void createView() {
        view = new BorderPane();
        view.setPadding(new Insets(20));

        // Top Section: Level and Time
        HBox topBox = new HBox(20);
        topBox.setAlignment(Pos.TOP_RIGHT); // Align to top right
        levelLabel = new Label("Level: 1");
        timeLabel = new Label("Time: 20");

        HBox levelBox = new HBox(10, new Label("Nivel: "), levelLabel); // Nivel on top left
        levelBox.setAlignment(Pos.TOP_LEFT);

        HBox timeBox = new HBox(10, new Label("Tiempo: "), timeLabel); // Time on top right
        timeBox.setAlignment(Pos.TOP_RIGHT);

        // Eclipse Image
        eclipseImage = new ImageView();
        eclipseImage.setFitWidth(200); // Ajusta el tamaño de la imagen
        eclipseImage.setFitHeight(200);

        HBox eclipseBox = new HBox(10, eclipseImage); // Eclipse on top center
        eclipseBox.setAlignment(Pos.TOP_CENTER);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(levelBox);
        topPane.setCenter(eclipseBox);
        topPane.setRight(timeBox);

        view.setTop(topPane);

        // Center Section: Phrase and Input
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        phraseLabel = new Label("Phrase");
        phraseLabel.setFont(Font.font(48));
        inputField = new TextField();
        inputField.setMaxWidth(600);
        inputField.setAlignment(Pos.CENTER);

        submitButton = new Button("Enviar");
        submitButton.setFont(Font.font(24));
        submitButton.setPadding(new Insets(10, 20, 10, 20));

        centerBox.getChildren().addAll(phraseLabel, inputField, submitButton);
        view.setCenter(centerBox);

        // Bottom Section: Messages
        Label messageLabel = new Label();
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        gameOverMessage = new Label();
        gameOverMessage.setVisible(false); // Initially hidden

        restartButton = new Button("Re-intentar");
        restartButton.setFont(Font.font(18));
        restartButton.setOnAction(event -> startNewGame());
        restartButton.setVisible(false); // Initially hidden

        bottomBox.getChildren().addAll(gameOverMessage, restartButton, messageLabel);
        view.setBottom(bottomBox);
    }

    /**
     * Enlaza los datos del controlador con la vista.
     */
    private void bindData() {
        levelLabel.textProperty().bind(controller.levelProperty().asString("%d"));
        timeLabel.textProperty().bind(controller.timeLeftProperty().asString("%d"));
        phraseLabel.textProperty().bind(controller.currentWordProperty());
    }

    /**
     * Carga una imagen desde la ruta especificada.
     * @param path La ruta de la imagen.
     * @return La imagen cargada.
     */
    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    /**
     * Actualiza la imagen del eclipse basada en el número de errores.
     * @param errorCount El número de errores.
     */
    private void updateEclipseImage(int errorCount) {
        if (errorCount >= 0 && errorCount < eclipseImagePaths.length) {
            Image image = loadImage(eclipseImagePaths[errorCount]);
            eclipseImage.setImage(image);
        } else if (errorCount >= eclipseImagePaths.length) {
            Image image = loadImage(eclipseImagePaths[eclipseImagePaths.length - 1]);
            eclipseImage.setImage(image);
        }
    }

    /**
     * Maneja el envío de la palabra escrita por el jugador.
     */
    private void handleSubmit() {
        String typedWord = inputField.getText();
        if (!controller.submitWord(typedWord)) {
            updateEclipseImage(controller.getErrorsCount());
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
        controller.errorsProperty().set(0); // ACAAAAAAAAA
        updateEclipseImage(controller.getErrorsCount());

        controller.startNewGame();
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
        inputField.setOnAction(event -> handleSubmit()); // Enter key
        controller.errorsProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() >= 4) {
                endGame(); //Show Game over screen
            }
            updateEclipseImage(newVal.intValue()); // Always update eclipse, regardless if the user end game now
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
