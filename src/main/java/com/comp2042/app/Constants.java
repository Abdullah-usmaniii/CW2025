package com.comp2042.app;

public class Constants {
    // Window Settings
    public static final String WINDOW_TITLE = "TetrisJFX";
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    // Game Configuration
    public static final int BOARD_WIDTH = 25;
    public static final int BOARD_HEIGHT = 10;
    public static final int BRICK_SIZE = 20;
    public static final double GAME_SPEED_MILLIS = 400;
    public static final int SCORE_PER_LINE_MULTIPLIER = 50;
    public static final int SCORE_PER_LEVEL = 500;
    public static final int MAX_LEVEL = 5;
    public static final double SPEED_INCREASE_PER_LEVEL = 0.25;

    // Spawn Configuration
    public static final int SPAWN_X = 4;
    public static final int SPAWN_Y = 1;

    // Audio Settings
    public static final double DEFAULT_MUSIC_VOLUME = 0.5;
    public static final double SFX_VOLUME = 0.7;

    // Resource Paths - Audio
    public static final String RESOURCE_MUSIC = "music.mp3";
    public static final String RESOURCE_SOUND_PLACE = "place.mp3";
    public static final String RESOURCE_SOUND_CLEAR = "clear.mp3";

    // Resource Paths - UI
    public static final String RESOURCE_FONT_DIGITAL = "digital.ttf";

    // Resource Paths - FXML
    public static final String FXML_TITLE_SCREEN = "/TitleScreen.fxml";
    public static final String FXML_GAME_LAYOUT = "/gameLayout.fxml";
    public static final String FXML_PAUSE_MENU = "/PauseMenu.fxml";
    public static final String FXML_INSTRUCTIONS = "/Instructions.fxml";
}
