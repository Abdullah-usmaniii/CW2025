package com.comp2042.view;

import com.comp2042.Logic.DownData;
import com.comp2042.Logic.ViewData;
import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The main coordinator class for the UI.
 * Acts as the FXML Controller and manages the interaction between Input, Rendering, Game Loop, and Game Logic.
 */
public class GuiController implements Initializable {

    // --- FXML Injections ---
    @FXML private StackPane rootPane;
    @FXML private GridPane gamePanel;
    @FXML private GridPane brickPanel;
    @FXML private GridPane ghostPanel;
    @FXML private GridPane nextBrick;
    @FXML private GridPane holdBrick;
    @FXML private Group groupNotification;
    @FXML private Text scoreValue;
    @FXML private GameOverPanel gameOverPanel;

    // --- Dependencies (Split Classes) ---
    private GameRenderer renderer;
    private GameInputHandler inputHandler;
    private GameLoopManager loopManager;
    private InputEventListener eventListener;

    // --- State ---
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private final IntegerProperty highScore = new SimpleIntegerProperty();
    private Parent pauseOverlay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load fonts/effects
        try {
            // Ensure this resource exists or wrap in try-catch to prevent crash
            String fontPath = getClass().getClassLoader().getResource("digital.ttf").toExternalForm();
            Font.loadFont(fontPath, 38);
        } catch (Exception e) {
            System.err.println("Font not found, using default.");
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        // 1. Initialize Renderer
        this.renderer = new GameRenderer(gamePanel, brickPanel, ghostPanel, nextBrick, holdBrick);

        // Focus management
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // 2. Setup Game Over Panel
        gameOverPanel.setVisible(false);
        gameOverPanel.setNewGameAction(e -> {
            newGame();
            gameOverPanel.setVisible(false);
        });
        gameOverPanel.setExitAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        // 3. Setup Pause Logic
        isPause.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (loopManager != null) loopManager.pause();
            } else {
                if (loopManager != null && !isGameOver.getValue()) loopManager.play();
            }
        });
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;

        // Initialize Input Handler
        this.inputHandler = new GameInputHandler(gamePanel, this);

        // Initialize Game Loop Manager
        this.loopManager = new GameLoopManager(() ->
                moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        );
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        renderer.initGameView(boardMatrix, brick);
        if (loopManager != null) {
            loopManager.play();
        }
    }

    // --- Actions delegated from InputHandler ---

    public void moveLeft() {
        ViewData data = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
        renderer.refreshBrick(data);
    }

    public void moveRight() {
        ViewData data = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
        renderer.refreshBrick(data);
    }

    public void rotate() {
        ViewData data = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
        renderer.refreshBrick(data);
    }

    public void moveDownUser() {
        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
    }

    public void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
            handleScoreNotification(downData);
            renderer.refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void holdBrick() {
        ViewData data = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
        renderer.refreshBrick(data);
        renderer.refreshHoldBrick(data.getHeldBrickData());

        // CHANGED: Pass the LIST of next bricks
        renderer.refreshNextBrick(data.getNextBrickData());
    }

    // --- Core Game Logic ---

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            handleScoreNotification(downData);
            renderer.refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void handleScoreNotification(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    public void refreshGameBackground(int[][] board) {
        renderer.refreshGameBackground(board);
    }

    // CHANGED: Signature now accepts List<int[][]> to match ViewData
    public void refreshNextBrick(List<int[][]> nextBrickData) {
        renderer.refreshNextBrick(nextBrickData);
    }

    public void refreshHoldBrick(int[][] holdData) {
        renderer.refreshHoldBrick(holdData);
    }

    // --- UI State Management ---

    public boolean isPaused() {
        return isPause.get();
    }

    public boolean isGameOver() {
        return isGameOver.get();
    }

    public void togglePause() {
        if (isPause.get()) {
            closePauseMenu();
        } else {
            showPauseMenu();
        }
    }

    public void showPauseMenu() {
        if (pauseOverlay == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PauseMenu.fxml"));
                pauseOverlay = loader.load();
                PauseMenuController pauseController = loader.getController();
                pauseController.setGameController(this);
                rootPane.getChildren().add(pauseOverlay);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        isPause.set(true);
    }

    public void closePauseMenu() {
        if (rootPane != null && pauseOverlay != null) {
            rootPane.getChildren().remove(pauseOverlay);
            pauseOverlay = null;
        }
        isPause.set(false);
        gamePanel.requestFocus();
    }

    public void newGame() {
        if (loopManager != null) loopManager.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        if (loopManager != null) loopManager.play();
    }

    public void gameOver() {
        if (loopManager != null) loopManager.stop();
        gameOverPanel.setVisible(true);
        gameOverPanel.setOpacity(0);
        gameOverPanel.setTranslateY(30);

        FadeTransition ft = new FadeTransition(Duration.millis(500), gameOverPanel);
        ft.setFromValue(0);
        ft.setToValue(1);

        TranslateTransition tt = new TranslateTransition(Duration.millis(500), gameOverPanel);
        tt.setFromY(30);
        tt.setToY(0);

        ParallelTransition pt = new ParallelTransition(ft, tt);
        pt.play();

        isGameOver.setValue(Boolean.TRUE);
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreValue != null) {
            scoreValue.textProperty().bind(integerProperty.asString());
        }
    }

    /**
     * Retrieves the current high score as a String.
     * Required by PauseMenuController to display the high score.
     */
    public String getHighScoreText() {
        return Integer.toString(highScore.get());
    }

    public void bindHighScore(IntegerProperty highScoreProperty) {
        this.highScore.bind(highScoreProperty);
    }
}