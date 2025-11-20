package com.comp2042.view;

import com.comp2042.app.GameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getClassLoader().getResource("gameLayout.fxml");
        if (location == null) {
            // try class-relative
            location = getClass().getResource("gameLayout.fxml");
        }
        if (location == null) {
            throw new RuntimeException("FXML not found: ensure gameLayout.fxml is on the classpath under resources");
        }
        ResourceBundle resources = null;
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);
        Parent root = fxmlLoader.load();
        GuiController c = fxmlLoader.getController();

        primaryStage.setTitle("TetrisJFX");
        Scene scene = new Scene(root, 350, 510);
        primaryStage.setScene(scene);
        primaryStage.show();

        GameController gameController = new GameController(c);
        // bind the score from the game controller to the GUI
        c.bindScore(gameController.getScore().scoreProperty());
    }


    public static void main(String[] args) {
        launch(args);
    }
}
