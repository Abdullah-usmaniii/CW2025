package com.comp2042.view;

import com.comp2042.events.EventSource;
import com.comp2042.events.EventType;
import com.comp2042.events.InputEventListener;
import com.comp2042.events.MoveEvent;
import com.comp2042.Logic.DownData;
import com.comp2042.Logic.ViewData;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.effect.Reflection;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.scene.effect.DropShadow;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GuiController implements Initializable {

    private static final int BRICK_SIZE = 20;

    @FXML
    private StackPane rootPane;

    @FXML
    private GridPane holdBrick;

    private Rectangle[][] holdBrickMatrix;

    @FXML
    private GridPane gamePanel;

    @FXML
    private Group groupNotification;

    @FXML
    private Text scoreValue;

    private final IntegerProperty highScore = new SimpleIntegerProperty();

    @FXML
    private GridPane brickPanel;

    @FXML
    private GridPane ghostPanel;

    @FXML
    private GridPane nextBrick;

    private Rectangle[][] ghostRectangles;
    private Rectangle[][] nextBrickMatrix;

    @FXML
    private GameOverPanel gameOverPanel;

    private Rectangle[][] displayMatrix;
    private InputEventListener eventListener;
    private Rectangle[][] rectangles;
    private Timeline timeLine;

    private final BooleanProperty isPause = new SimpleBooleanProperty();
    private final BooleanProperty isGameOver = new SimpleBooleanProperty();

    // Track the currently open preview overlay
    private Parent currentOverlay;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Font.loadFont(getClass().getClassLoader().getResource("digital.ttf").toExternalForm(), 38);
        gamePanel.setFocusTraversable(true);
        gamePanel.requestFocus();

        // Key Handler for the Main Game
        gamePanel.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                // ESC Key: Open Preview if not already open
                if (keyEvent.getCode() == KeyCode.ESCAPE) {
                    if (currentOverlay == null) {
                        showPreview();
                    }
                    keyEvent.consume();
                    return;
                }

                if (isPause.getValue() == Boolean.FALSE && isGameOver.getValue() == Boolean.FALSE) {
                    if (keyEvent.getCode() == KeyCode.LEFT || keyEvent.getCode() == KeyCode.A) {
                        refreshBrick(eventListener.onLeftEvent(new MoveEvent(EventType.LEFT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.RIGHT || keyEvent.getCode() == KeyCode.D) {
                        refreshBrick(eventListener.onRightEvent(new MoveEvent(EventType.RIGHT, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.W) {
                        refreshBrick(eventListener.onRotateEvent(new MoveEvent(EventType.ROTATE, EventSource.USER)));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.DOWN || keyEvent.getCode() == KeyCode.S) {
                        moveDown(new MoveEvent(EventType.DOWN, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        hardDrop(new MoveEvent(EventType.HARD_DROP, EventSource.USER));
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.TAB) {
                        refreshBrick(eventListener.onHoldEvent(new MoveEvent(EventType.HOLD, EventSource.USER)));
                        keyEvent.consume();
                    }

                }
            }
        });
        gameOverPanel.setVisible(false);

        gameOverPanel.setNewGameAction(e -> {
            newGame();
            gameOverPanel.setVisible(false);
        });

        gameOverPanel.setExitAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        // Pause state listener
        isPause.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (timeLine != null) timeLine.pause();
            } else {
                if (timeLine != null && !isGameOver.getValue()) timeLine.play();
            }
        });

        final Reflection reflection = new Reflection();
        reflection.setFraction(0.8);
        reflection.setTopOpacity(0.9);
        reflection.setTopOffset(-12);
    }

    public void showPreview() {
        if (currentOverlay != null) return;

        isPause.set(true);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PreviewLayout.fxml"));
            currentOverlay = loader.load();

            GamePreview previewController = loader.getController();
            previewController.setGuiController(this);

            if (rootPane != null) {
                rootPane.getChildren().add(currentOverlay);

                currentOverlay.requestFocus();
                currentOverlay.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        closePreview();
                        event.consume();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closePreview() {
        if (rootPane != null && currentOverlay != null) {
            rootPane.getChildren().remove(currentOverlay);
            currentOverlay = null;
        }
        // Unpause game
        isPause.set(false);
        // Important: Return focus to the game panel so controls work again
        gamePanel.requestFocus();
    }

    public void newGame() {
        timeLine.stop();
        gameOverPanel.setVisible(false);
        eventListener.createNewGame();
        gamePanel.requestFocus();

        isPause.setValue(Boolean.FALSE);
        isGameOver.setValue(Boolean.FALSE);

        timeLine.play();
    }

    public String getHighScoreText() {
        return Integer.toString(highScore.get());
    }

    public void initGameView(int[][] boardMatrix, ViewData brick) {
        displayMatrix = new Rectangle[boardMatrix.length][boardMatrix[0].length];
        for (int i = 2; i < boardMatrix.length; i++) {
            for (int j = 0; j < boardMatrix[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(Color.TRANSPARENT);
                displayMatrix[i][j] = rectangle;
                gamePanel.add(rectangle, j, i - 2);
            }
        }

        rectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];
        ghostRectangles = new Rectangle[brick.getBrickData().length][brick.getBrickData()[0].length];

        for (int i = 0; i < brick.getBrickData().length; i++) {
            for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(brick.getBrickData()[i][j]));
                rectangles[i][j] = rectangle;
                brickPanel.add(rectangle, j, i);

                Rectangle ghostRect = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                ghostRect.setFill(Color.TRANSPARENT);
                ghostRectangles[i][j] = ghostRect;
                ghostPanel.add(ghostRect, j, i);
            }
        }
        brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

        ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * ghostPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
        ghostPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * ghostPanel.getHgap() + brick.getGhostYPosition() * BRICK_SIZE);

        initNextBrick(brick.getNextBrickData());
        initHoldBrick(brick.getHeldBrickData());

        timeLine = new Timeline(new KeyFrame(
                Duration.millis(400),
                ae -> moveDown(new MoveEvent(EventType.DOWN, EventSource.THREAD))
        ));
        timeLine.setCycleCount(Timeline.INDEFINITE);
        timeLine.play();
    }

    public void initNextBrick(int[][] nextBrickData) {
        nextBrick.getChildren().clear();
        nextBrickMatrix = new Rectangle[nextBrickData.length][nextBrickData[0].length];

        for (int i = 0; i < nextBrickData.length; i++) {
            for (int j = 0; j < nextBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(nextBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                nextBrickMatrix[i][j] = rectangle;
                nextBrick.add(rectangle, j, i);
            }
        }
    }


    public void initHoldBrick(int[][] holdBrickData) {
        holdBrick.getChildren().clear();
        holdBrickMatrix = new Rectangle[holdBrickData.length][holdBrickData[0].length];

        for (int i = 0; i < holdBrickData.length; i++) {
            for (int j = 0; j < holdBrickData[i].length; j++) {
                Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                rectangle.setFill(getFillColor(holdBrickData[i][j]));
                rectangle.setArcHeight(9);
                rectangle.setArcWidth(9);
                holdBrickMatrix[i][j] = rectangle;
                holdBrick.add(rectangle, j, i);
            }
        }
    }

    public void refreshNextBrick(int[][] nextBrickData) {
        if (nextBrickMatrix != null && nextBrickData.length == nextBrickMatrix.length
                && nextBrickData[0].length == nextBrickMatrix[0].length) {
            for (int i = 0; i < nextBrickData.length; i++) {
                for (int j = 0; j < nextBrickData[i].length; j++) {
                    nextBrickMatrix[i][j].setFill(getFillColor(nextBrickData[i][j]));
                }
            }
        } else {
            initNextBrick(nextBrickData);
        }
    }
    public void refreshHoldBrick(int[][] holdBrickData) {
        if (holdBrickMatrix != null && holdBrickData.length == holdBrickMatrix.length
                && holdBrickData[0].length == holdBrickMatrix[0].length) {
            for (int i = 0; i < holdBrickData.length; i++) {
                for (int j = 0; j < holdBrickData[i].length; j++) {
                    holdBrickMatrix[i][j].setFill(getFillColor(holdBrickData[i][j]));
                }
            }
        } else {
            initHoldBrick(holdBrickData);
        }
    }

    private Paint getFillColor(int i) {
        Paint returnPaint;
        switch (i) {
            case 0: returnPaint = Color.TRANSPARENT; break;
            case 1: returnPaint = Color.AQUA; break;
            case 2: returnPaint = Color.BLUEVIOLET; break;
            case 3: returnPaint = Color.DARKGREEN; break;
            case 4: returnPaint = Color.YELLOW; break;
            case 5: returnPaint = Color.RED; break;
            case 6: returnPaint = Color.BEIGE; break;
            case 7: returnPaint = Color.BURLYWOOD; break;
            default: returnPaint = Color.WHITE; break;
        }
        return returnPaint;
    }

    private void refreshBrick(ViewData brick) {
        if (isPause.getValue() == Boolean.FALSE) {
            brickPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * brickPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            brickPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getyPosition() * brickPanel.getHgap() + brick.getyPosition() * BRICK_SIZE);

            ghostPanel.setLayoutX(gamePanel.getLayoutX() + brick.getxPosition() * ghostPanel.getVgap() + brick.getxPosition() * BRICK_SIZE);
            ghostPanel.setLayoutY(-42 + gamePanel.getLayoutY() + brick.getGhostYPosition() * ghostPanel.getHgap() + brick.getGhostYPosition() * BRICK_SIZE);

            for (int i = 0; i < brick.getBrickData().length; i++) {
                for (int j = 0; j < brick.getBrickData()[i].length; j++) {
                    setRectangleData(brick.getBrickData()[i][j], rectangles[i][j]);

                    Rectangle ghostRect = ghostRectangles[i][j];
                    int val = brick.getBrickData()[i][j];

                    if (val == 0) {
                        ghostRect.setFill(Color.TRANSPARENT);
                        ghostRect.setStroke(Color.TRANSPARENT);
                        ghostRect.setEffect(null);
                    } else {
                        Color c = (Color) getFillColor(val);
                        // 1. Fill: Translucent like original logic
                        ghostRect.setFill(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0.3));

                        // 2. Stroke: Neon Edge
                        ghostRect.setStroke(c);
                        ghostRect.setStrokeWidth(2);
                        ghostRect.setStrokeType(StrokeType.INSIDE);

                        ghostRect.setArcHeight(9);
                        ghostRect.setArcWidth(9);

                        // 3. Effect: Neon Glow
                        DropShadow glow = new DropShadow();
                        glow.setColor(c);
                        glow.setRadius(10);
                        glow.setSpread(0.6);
                        ghostRect.setEffect(glow);
                    }
                }
            }
        }
    }

    public void refreshGameBackground(int[][] board) {
        for (int i = 2; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setRectangleData(board[i][j], displayMatrix[i][j]);
            }
        }
    }

    private void setRectangleData(int color, Rectangle rectangle) {
        rectangle.setFill(getFillColor(color));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
    }

    private void moveDown(MoveEvent event) {
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onDownEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    private void hardDrop(MoveEvent event){
        if (isPause.getValue() == Boolean.FALSE) {
            DownData downData = eventListener.onHardDropEvent(event);
            if (downData.getClearRow() != null && downData.getClearRow().getLinesRemoved() > 0) {
                NotificationPanel notificationPanel = new NotificationPanel("+" + downData.getClearRow().getScoreBonus());
                groupNotification.getChildren().add(notificationPanel);
                notificationPanel.showScore(groupNotification.getChildren());
            }
            refreshBrick(downData.getViewData());
        }
        gamePanel.requestFocus();
    }

    public void setEventListener(InputEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void bindScore(IntegerProperty integerProperty) {
        if (scoreValue != null) {
            scoreValue.setFill(Color.RED);
            scoreValue.setStyle("-fx-font-weight: bold; -fx-font-size: 20px;");
            scoreValue.textProperty().bind(integerProperty.asString());
        }
    }

    public void bindHighScore(IntegerProperty highScoreProperty) {
        this.highScore.bind(highScoreProperty);
    }

    public void gameOver() {
        timeLine.stop();

        // 1. Make visible and prepare for animation
        gameOverPanel.setVisible(true);
        gameOverPanel.setOpacity(0);
        gameOverPanel.setTranslateY(30); // Start slightly below final position

        // 2. Fade In
        FadeTransition ft = new FadeTransition(Duration.millis(500), gameOverPanel);
        ft.setFromValue(0);
        ft.setToValue(1);

        // 3. Slide Up (Pop up)
        TranslateTransition tt = new TranslateTransition(Duration.millis(500), gameOverPanel);
        tt.setFromY(30);
        tt.setToY(0);

        // 4. Play Together
        ParallelTransition pt = new ParallelTransition(ft, tt);
        pt.play();

        isGameOver.setValue(Boolean.TRUE);
    }

}