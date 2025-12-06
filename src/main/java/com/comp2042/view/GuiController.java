package com.comp2042.view;

import com.comp2042.Logic.DownData;
import com.comp2042.Logic.ViewData;
import com.comp2042.app.Constants;
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
import javafx.scene.control.Button;
import javafx.scene.effect.Reflection;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import com.comp2042.app.GameMode;

/**
 * The main coordinator class for the UI.
 * Acts as the FXML Controller and manages the interaction between Input, Rendering, Game Loop, and Game Logic.
 * @author Abdullah Usmani
 */
public class GuiController implements Initializable {

    // FXML Injections
    @FXML private StackPane rootPane;
    @FXML private GridPane gamePanel;
    @FXML private GridPane brickPanel;
    @FXML private GridPane ghostPanel;
    @FXML private GridPane nextBrick;
    @FXML private GridPane holdBrick;
    @FXML private Group groupNotification;
    @FXML private Text scoreValue;
    @FXML private Text levelValue;
    @FXML private Text speedValue;
    @FXML private GameOverPanel gameOverPanel;
    @FXML private Text countdownText;


    // Dependencies
    private GameRenderer renderer;
    private GameInputHandler inputHandler;
    private GameLoopManager loopManager;
    private InputEventListener eventListener;
    private Button bombButton;
    private GameMode currentMode;
    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();
    private final IntegerProperty highScore = new SimpleIntegerProperty();
    private Parent pauseOverlay;
    private final BooleanProperty isCountingDown = new SimpleBooleanProperty(false);

    /**
     * Initializes the controller after its root element has been completely processed.
     * Sets up the game renderer, focus management, game-over panel logic, and pause logic.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load fonts/effects
        try {
            String fontPath = getClass().getClassLoader().getResource(Constants.RESOURCE_FONT_DIGITAL).toExternalForm();
            Font.loadFont(fontPath, 38);
        } catch (Exception e) {
            System.err.println("Font not found, using default.");
        }

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);

        // Initialize Renderer
        this.renderer = new GameRenderer(gamePanel, brickPanel, ghostPanel, nextBrick, holdBrick);

        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();
        gameOverPanel.setVisible(false);
        gameOverPanel.setNewGameAction(e -> {
            newGame();
            gameOverPanel.setVisible(false);
        });
        gameOverPanel.setExitAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        gameOverPanel.setMainMenuAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_TITLE_SCREEN));
                Parent root = loader.load();

                // Get current stage and swap scene
                Stage stage = (Stage) rootPane.getScene().getWindow();
                stage.getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        // Setup Pause Logic
        isPause.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (loopManager != null) loopManager.pause();
            } else {
                if (loopManager != null && !isGameOver.getValue()) loopManager.play();
            }
        });
    }

    /**
     * Sets the game mode and initializes mode-specific UI elements.
     * Should be called immediately after loading the controller.
     * @param mode The selected GameMode.
     */
    public void setGameMode(GameMode mode) {
        this.currentMode = mode;
        if (mode == GameMode.BOMB) {
            initBombButton();
        }
    }
    /**
     * Creates and adds the Bomb Button to the UI dynamically.
     */
    private void initBombButton() {
        bombButton = new Button("BOOM");
        bombButton.getStyleClass().add("ipad-dark-grey");
        bombButton.setPrefWidth(120);
        bombButton.setPrefHeight(60);
        // Larger font for effect
        bombButton.setStyle("-fx-font-size: 24px; -fx-text-fill: red; -fx-font-family: 'Let\\'s go Digital';");

        bombButton.setLayoutX(470);
        bombButton.setLayoutY(550);

        if (rootPane.getChildren().get(0) instanceof Pane) {
            ((Pane) rootPane.getChildren().get(0)).getChildren().add(bombButton);
        }
    }

    /**
     * Initializes the Bomb Panel if the current game mode is BOMB.
     */
    public Button getBombButton() {
        return bombButton;
    }

    /**
     * Displays a temporary notification message on screen.
     * @param text The message to display.
     */
    public void showNotification(String text) {
        NotificationPanel panel = new NotificationPanel(text);
        groupNotification.getChildren().add(panel);
        panel.showScore(groupNotification.getChildren());
    }
    /**
     * Updates the bomb button state.
     * @param count Remaining bombs.
     */
    public void updateBombButtonState(int count) {
        if (bombButton != null) {
            if (count <= 0) {
                bombButton.setDisable(true);
                bombButton.setText("EMPTY");
            } else {
                bombButton.setDisable(false);
                bombButton.setText("BOOM");
            }
        }
    }

    /**
     * Returns focus to the game panel so keyboard controls work immediately.
     */
    public void returnFocusToGame() {
        gamePanel.requestFocus();
    }

    /**
     * Registers the InputEventListener which handles the game logic for input events.
     * Also initializes the InputHandler and GameLoopManager.
     *
     * @param eventListener The listener that processes input events (usually the GameController).
     */
    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;

        this.inputHandler = new GameInputHandler(gamePanel, this);
        this.loopManager = new GameLoopManager(() ->
                moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        );
    }

    /**
     * Initializes the visual state of the game board.
     * Delegates initial drawing to the GameRenderer and starts the game loop.
     *
     * @param boardMatrix The initial state of the board grid.
     * @param brick       The initial data for the current falling brick.
     */
    public void initGameView(int[][] boardMatrix, ViewData brick) {
        renderer.initGameView(boardMatrix, brick);
        if (loopManager != null) {
            loopManager.play();
        }
    }
    /**
     * Initiates a move to the left.
     * Called by the InputHandler when the move left key is pressed.
     */
    public void moveLeft() {
        if (!isCountingDown.get()) {
            ViewData data = eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER));
            renderer.refreshBrick(data);
        }
    }
    /**
     * Initiates a move to the right.
     * Called by the InputHandler when the move right key is pressed.
     */
    public void moveRight() {
        if (!isCountingDown.get()) {
            ViewData data = eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER));
            renderer.refreshBrick(data);
        }
    }
    /**
     * Initiates a rotation of the brick.
     * Called by the InputHandler when the rotate key is pressed.
     */
    public void rotate() {
        if (!isCountingDown.get()) {
            ViewData data = eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER));
            renderer.refreshBrick(data);
        }
    }
    /**
     * Initiates a downward movement triggered by the user (soft drop).
     */
    public void moveDownUser() {
        if (!isCountingDown.get()) {
            moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
        }
    }

    /**
     * Initiates a hard drop (instant fall).
     * Called by the InputHandler when the hard drop key is pressed.
     */
    public void hardDrop() {
        if (isPause.getValue() == Boolean.FALSE && !isCountingDown.get()) {
            DownData downData = eventListener.onHardDropEvent(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
            handleScoreNotification(downData);
            renderer.refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Triggers the "Hold Brick" action.
     * Swaps the current brick with the held brick and updates the UI accordingly.
     */
    public void holdBrick() {
        if (!isCountingDown.get()) {
            ViewData data = eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER));
            renderer.refreshBrick(data);
            renderer.refreshHoldBrick(data.getHeldBrickData());
            renderer.refreshNextBrick(data.getNextBrickData());
        }
    }


    /**
     * Processes a downward movement event (either from gravity or user input).
     * Updates the UI with new brick positions and handles score notifications if lines are cleared.
     *
     * @param event The MoveEvent containing the source (User vs Thread).
     */
    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE && !isCountingDown.get()) {
            DownData downData = eventListener.onDownEvent(event);
            handleScoreNotification(downData);
            renderer.refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    /**
     * Checks if any lines were cleared and displays a floating score notification if so.
     *
     * @param downData The result of a downward movement, containing info about cleared rows.
     */
    private void handleScoreNotification(DownData downData) {
        if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
            NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
            groupNotification.getChildren().add(notificationPanel);
            notificationPanel.showScore(groupNotification.getChildren());
        }
    }

    /**
     * Refreshes the visual representation of the game board background (static bricks).
     *
     * @param board The current state of the board grid.
     */
    public void refreshGameBackground(int[][] board) {
        renderer.refreshGameBackground(board);
    }

    /**
     * Updates the "Next Brick" preview panel.
     *
     * @param nextBrickData A list of matrices representing the upcoming bricks.
     */    public void refreshNextBrick(List<int[][]> nextBrickData) {
        renderer.refreshNextBrick(nextBrickData);
    }

    /**
     * Updates the "Hold Brick" preview panel.
     *
     * @param holdData A matrix representing the currently held brick.
     */
    public void refreshHoldBrick(int[][] holdData) {
        renderer.refreshHoldBrick(holdData);
    }
    /**
     * Refreshes the display of the currently falling brick.
     * Delegates the drawing to the GameRenderer.
     * @param data The view data containing the brick's position and shape.
     */
    public void refreshBrick(ViewData data) {
        renderer.refreshBrick(data);
    }

    /**
     * Checks if the game is currently paused.
     *
     * @return true if the game is paused, false otherwise.
     */
    public boolean isPaused() {
        return isPause.get();
    }

    /**
     * Checks if the game is in a "Game Over" state.
     *
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return isGameOver.get();
    }

    public boolean isCountingDown() {
        return isCountingDown.get();
    }

    /**
     * Toggles the pause state of the game.
     * Opens or closes the pause menu overlay accordingly.
     */
    public void togglePause() {
        if (isCountingDown.get()) return;
        if (isPause.get()) {
            closePauseMenu();
        } else {
            showPauseMenu();
        }
    }

    /**
     * Displays the pause menu overlay and pauses the game logic.
     */
    public void showPauseMenu() {
        if (pauseOverlay == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.FXML_PAUSE_MENU));
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

    /**
     * Removes the pause menu overlay and resumes the game logic.
     */
    public void closePauseMenu() {
        if (rootPane != null && pauseOverlay != null) {
            rootPane.getChildren().remove(pauseOverlay);
            pauseOverlay = null;
        }

        gamePanel.requestFocus();

        // Start countdown before resuming game loop
        startCountdown(() -> {
            isPause.set(false);
            if (loopManager != null && !isGameOver.getValue() && !isPause.getValue()) {
                loopManager.play();
            }
        });
    }

    /**
     * Resets the game UI and Logic to start a fresh game.
     */
    public void newGame() {
        if (loopManager != null) loopManager.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();
        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);
        startCountdown(() -> {
            if (loopManager != null) loopManager.play();
        });
    }

    public void setGameSpeed(double rate) {
        if (loopManager != null) {
            loopManager.setRate(rate);
        }
    }

    public void updateLevel(int level) {
        if (levelValue != null) {
            levelValue.setText(String.valueOf(level));
        }
    }

    public void updateSpeed(double speed) {
        if (speedValue != null) {
            speedValue.setText(speed + "x");
        }
    }

    // Countdown Logic
    private void startCountdown(Runnable onFinished) {
        isCountingDown.set(true);
        countdownText.setVisible(true);

        SequentialTransition sequence = new SequentialTransition();

        sequence.getChildren().addAll(
                createPulseAnimation("3"),
                createPulseAnimation("2"),
                createPulseAnimation("1"),
                createPulseAnimation("GO!")
        );

        sequence.setOnFinished(e -> {
            countdownText.setVisible(false);
            isCountingDown.set(false);
            onFinished.run();
        });

        sequence.play();
    }

    private Transition createPulseAnimation(String text) {
        PauseTransition setContent = new PauseTransition(Duration.ZERO);
        setContent.setOnFinished(e -> countdownText.setText(text));

        ScaleTransition scale = new ScaleTransition(Duration.millis(500), countdownText);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1.5);
        scale.setToY(1.5);

        FadeTransition fade = new FadeTransition(Duration.millis(500), countdownText);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);

        ParallelTransition parallel = new ParallelTransition(scale, fade);

        return new SequentialTransition(setContent, parallel);
    }

    /**
     * Triggers the Game Over sequence.
     * Stops the game loop and displays the Game Over panel with an animation.
     */
    public void gameOver() {
        if (loopManager != null) loopManager.stop();
        gameOverPanel.setHighScore(highScore.get());
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

    /**
     * Binds the score text display to the logic's score property.
     *
     * @param integerProperty The observable integer property representing the score.
     */
    public void bindScore(IntegerProperty integerProperty) {
        if (scoreValue != null) {
            scoreValue.textProperty().bind(integerProperty.asString());
        }
    }

    /**
     * Retrieves the current high score as a String.
     * Required by PauseMenuController to display the high score.
     *
     * @return The high score as a string.
     */
    public String getHighScoreText() {
        return Integer.toString(highScore.get());
    }

    /**
     * Binds the local high score property to the logic's high score property.
     *
     * @param highScoreProperty The observable integer property representing the high score.
     */
    public void bindHighScore(IntegerProperty highScoreProperty) {
        this.highScore.bind(highScoreProperty);
    }
}