package com.example.utils;

import com.example.controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

/**
 * View class for the Typing Speed Game.
 */
public class GameView {
    private Button restartButton; // Botón para reiniciar el juego
    private Button submitButton;
    private BorderPane view;
    private GameController controller;
    private Label gameOverMessage; // Mensaje de fin de juego
    private Label messageLabel;
    private Label levelLabel;
    private Label phraseLabel;
    private Label timeLabel;
    private TextField inputField;
    private ImageView eclipseImage; // Imagen para representar el progreso del eclipse

    public GameView() {
        controller = new GameController();
        createView();
        bindData();
        setupEventHandlers();
        startNewGame(); // This will trigger the first eclipse update
    }

    private final String[] eclipseImagePaths = {
            "/images/eclipse_0.png",
            "/images/eclipse_25.png",
            "/images/eclipse_50.png",
            "/images/eclipse_75.png",
            "/images/eclipse_100.png"
    };

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
        eclipseImage.setFitWidth(100); // Ajusta el tamaño de la imagen
        eclipseImage.setFitHeight(100);

        HBox eclipseBox = new HBox(10, eclipseImage); // Eclipse on top center
        eclipseBox.setAlignment(Pos.TOP_CENTER);

        BorderPane topPane = new BorderPane();
        topPane.setLeft(levelBox);
        topPane.setCenter(eclipseBox);
        topPane.setRight(timeBox);

        view.setTop(topPane);

        // Center Section: Phrase and Input
        VBox centerBox = new VBox(10); // 10px spacing
        centerBox.setAlignment(Pos.CENTER);
        phraseLabel = new Label("Phrase");
        phraseLabel.setFont(Font.font(48)); // Make the phrase bigger
        inputField = new TextField();
        inputField.setMaxWidth(600); // Wider input
        inputField.setAlignment(Pos.CENTER); // Center the text inside the input

        submitButton = new Button("Submit");
        submitButton.setFont(Font.font(24)); // Enlarge submit button
        submitButton.setPadding(new Insets(10, 20, 10, 20)); // Add some padding for visual appeal

        centerBox.getChildren().addAll(phraseLabel, inputField, submitButton);
        view.setCenter(centerBox);

        // Bottom Section: Messages
        messageLabel = new Label();
        VBox bottomBox = new VBox(10);
        bottomBox.setAlignment(Pos.CENTER);

        gameOverMessage = new Label();
        gameOverMessage.setVisible(false); // Initially hidden

        restartButton = new Button("Re-intentar");
        restartButton.setFont(Font.font(18));
        restartButton.setOnAction(event -> restartGame());
        restartButton.setVisible(false); // Initially hidden

        bottomBox.getChildren().addAll(gameOverMessage, restartButton, messageLabel);
        view.setBottom(bottomBox);
    }


    private void bindData() {
        levelLabel.textProperty().bind(controller.levelProperty().asString("%d"));
        timeLabel.textProperty().bind(controller.timeLeftProperty().asString("%d"));
        phraseLabel.textProperty().bind(controller.currentWordProperty());
    }

    private Image loadImage(String path) {
        return new Image(getClass().getResourceAsStream(path));
    }

    private void updateEclipseImage(int errorCount) {
        if (errorCount >= 0 && errorCount < eclipseImagePaths.length) {
            Image image = loadImage(eclipseImagePaths[errorCount]);
            eclipseImage.setImage(image);
        } else if (errorCount >= eclipseImagePaths.length) {
            Image image = loadImage(eclipseImagePaths[eclipseImagePaths.length - 1]);
            eclipseImage.setImage(image);
        }
    }

    public Parent getView() {
        return view;
    }


    private void handleSubmit() {
        String typedWord = inputField.getText();
        if (!controller.submitWord(typedWord)) {
            updateEclipseImage(controller.getErrors());
        }
    }
    private void startNewGame() {
        // Re-enable input field and submit button
        inputField.setDisable(false);
        submitButton.setDisable(false);

        // Hide game over message and restart button
        gameOverMessage.setVisible(false);
        restartButton.setVisible(false);

        // Set initial values for start new Game
        controller.errorsProperty().set(0);
        updateEclipseImage(controller.getErrors());

        // Set initial time and then start new game
        controller.startNewGame(); // Add method to reset the game

        inputField.clear();
    }
    private void endGame() {
        // Show game over message and restart button
        gameOverMessage.setText("¡Ups, se te acabaron los intentos, trata de nuevo!");
        gameOverMessage.setFont(Font.font(20));
        gameOverMessage.setVisible(true);
        restartButton.setVisible(true);

        // Disable input field and submit button
        inputField.setDisable(true);
        submitButton.setDisable(true);
    }

    private void restartGame() {
        //Start new game
        startNewGame();
    }

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
}