package com.example.utils;

import com.example.controller.GameController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    public GameView() {
        controller = new GameController();
        createView();
        bindData();
        setupEventHandlers();
    }

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

        eclipse = new Circle(30); // Represent the eclipse
        eclipse.setStyle("-fx-fill: yellow; -fx-stroke: black; -fx-stroke-width: 2;");

        HBox eclipseBox = new HBox(10, eclipse); // Eclipse on top center
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
        VBox bottomBox = new VBox(20);
        bottomBox.setAlignment(Pos.CENTER);
        messageLabel = new Label();

        bottomBox.getChildren().addAll(messageLabel);
        view.setBottom(bottomBox);
    }

    private void bindData() {
        levelLabel.textProperty().bind(controller.levelProperty().asString("%d"));
        timeLabel.textProperty().bind(controller.timeLeftProperty().asString("%d"));
        phraseLabel.textProperty().bind(controller.currentWordProperty());
        // TODO: Bind eclipse progress (Implement in Controller)
    }

    private void setupEventHandlers() {
        submitButton.setOnAction(event -> controller.submitWord(inputField.getText()));
        inputField.setOnAction(event -> controller.submitWord(inputField.getText())); // Enter key
    }

    public Parent getView() {
        return view;
    }
}