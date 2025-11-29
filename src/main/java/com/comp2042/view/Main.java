package com.comp2042.view;

import com.comp2042.Logic.SoundManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Title Screen FXML first
        URL location = getClass().getResource("/TitleScreen.fxml");
        if (location == null) throw new RuntimeException("TitleScreen.fxml not found");

        FXMLLoader fxmlLoader = new FXMLLoader(location);
        Parent root = fxmlLoader.load();

        primaryStage.setTitle("TetrisJFX");

        // Create scene (800x600 default, but will be maximized)
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);


        primaryStage.setMaximized(true);
        primaryStage.show();

        // Initialize background music immediately
        SoundManager.getInstance();
    }

    public static void main(String[] args) {
        launch(args);
    }
}