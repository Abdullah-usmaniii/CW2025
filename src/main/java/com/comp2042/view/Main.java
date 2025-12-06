package com.comp2042.view;

import com.comp2042.app.Constants;
import com.comp2042.Logic.SoundManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.net.URL;

/**
 * The entry point for the Tetris application.
 * Extends the JavaFX Application class to set up the primary stage and load the initial scene.
 * @author Abdullah Usmani
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application.
     * Loads the Title Screen FXML, sets up the main window (Stage), and initializes background music.
     *
     * @param primaryStage The primary window for this application, onto which the application scene can be set.
     * @throws Exception If the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Title Screen FXML
        URL location = getClass().getResource(Constants.FXML_TITLE_SCREEN);
        if (location == null) throw new RuntimeException("TitleScreen.fxml not found");

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle(Constants.WINDOW_TITLE);


        Scene scene = new Scene(root, Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        primaryStage.setScene(scene);


        primaryStage.setMaximized(true);
        primaryStage.show();

        // Initialize background music
        SoundManager.getInstance();
    }

    /**
     * The main method, serving as the entry point for the Java application.
     * Launches the JavaFX runtime.
     *
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}