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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * View class for the Typing Speed Game.
 */
public class GameView {
    private GameController controller;
    private BorderPane view;
    private Label levelLabel;
    private Label timeLabel;
    private Label phraseLabel;
    private TextField inputField;
    private Button submitButton;
    private Label messageLabel;
    private Circle eclipse;
    private AnchorPane errorPane; // Panel para mostrar los iconos de error
    private ImageView eclipseImage; // Imagen para representar el progreso del eclipse
    private final int MAX_ERRORS = 4;
    private Button restartButton; // Botón para reiniciar el juego
    private Label gameOverMessage; // Mensaje de fin de juego

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
        // TODO: Bind eclipse progress (Implement in Controller)
    }

    private Image loadImage(String path) {
        try {
            Image image = new Image(getClass().getResourceAsStream(path));
            if (image.isError()) {
                System.err.println("Error al cargar la imagen: " + path + " - " + image.getException().getMessage());
                return null; // Or return a default image
            }
            return image;
        } catch (Exception e) {
            System.err.println("Excepción al cargar la imagen: " + path + " - " + e.getMessage());
            return null; // Or return a default image
        }
    }

    private void initializeEclipseImage() {
        // Load the initial eclipse image (eclipse_0.png)
        Image image = loadImage(eclipseImagePaths[0]);
        if (image != null) {
            eclipseImage.setImage(image);
        } else {
            // Handle the case where the image could not be loaded
            System.err.println("No se pudo cargar la imagen inicial del eclipse.");
        }
    }

    private void updateEclipseImage(int errorCount) {
        if (errorCount >= 0 && errorCount < eclipseImagePaths.length) {
            Image image = loadImage(eclipseImagePaths[errorCount]);
            if (image != null) {
                eclipseImage.setImage(image);
            } else {
                System.err.println("No se pudo cargar la imagen del eclipse para el error count: " + errorCount);
            }
        } else if (errorCount >= eclipseImagePaths.length) {
            Image image = loadImage(eclipseImagePaths[eclipseImagePaths.length - 1]);
            if (image != null) {
                eclipseImage.setImage(image);
            } else {
                System.err.println("No se pudo cargar la imagen final del eclipse.");
            }
        }
    }

    public Parent getView() {
        return view;
    }

}