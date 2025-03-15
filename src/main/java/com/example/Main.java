package com.example;
import com.example.utils.GameView;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 * Main class for the Typing Game application.
 * Launches the JavaFX application.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        GameView gameView = new GameView();
        Scene scene = new Scene(gameView.getView(), 800, 600);

        primaryStage.setTitle("Typing Speed Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
