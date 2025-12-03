# Tetris CourseWork Guide

**GIT Repository Link:** https://github.com/Abdullah-usmaniii/CW2025.git

## 1.Introduction 
This document provides a comprehensive guide to the Tetris coursework project. It outlines the structure, features, and implementation details of the Tetris game developed as part of the coursework.

## 1.2 Dependencies and Plugins 

 | Group ID    | Artifact ID     | Version | Description |
 |-------------|-----------------|---------|-------------|
 | org.openjfx | javafx-controls | 21.0.6  |Essential for creating the interactive elements of the game's graphical user interface|
 | org.openjfx | javafx-fxml     | 21.0.6  |Vital for building the application's complex views (Main Menu, Pause Menu, Game Layout) and linking them to their respective controllers.|
 | org.openjfx | javafx-media    | 21.0.6  |Crucial for implementing the game's audio system, including background music and sound effects|
 |org.junit.jupiter|junit-jupiter-api|5.12.1|Essential for test development, ensuring game logic (MatrixOperations, SoundManager), views, and controllers function correctly, which is a key part of the coursework assessment|
 |org.junit.jupiter|junit-jupiter-engine|5.12.1|Allows the test runner in your IDE or Maven build to discover and execute the unit tests defined in your project.|


|Plugin ID| Version| Description                                                                                                                                      |
|-|-|--------------------------------------------------------------------------------------------------------------------------------------------------|
|maven-compiler-plugin|3.13.0| Ensures the entire project is compiled correctly using the specified Java version, which is critical for compatibility and running the JavaFX modules. |
|javafx-maven-plugin|0.0.8| Simplifies the execution of the JavaFX application directly from the command line using ```mvn clean javafx:run```                                    |


## 2. Additional Features Implementation 
This section will cover the additional features implemented in the Tetris game, including their functionality, the set backs faced during development, and features that were not implemented.

### 2.1 Implemented Features that work properly

- **Ghost piece:** A translucent outline of the current tetromino is displayed at the position where it would land if dropped immediately. This feature helps players plan their moves more effectively.


- **Hold piece:** Players can store a tetromino for later use. This feature allows for strategic gameplay, as players can swap the current tetromino with the held one at any time by pressing the tab key.


- **Next piece preview:** The game displays the next tetromino that will appear after the current one. This feature allows players to expect and plan their moves ahead of time.


- **Sound effects and background music:** The game includes sound effects for actions such as when a tetromino is placed and when a row or multiple rows are cleared.Background music plays during the gameplay to enhance the gaming experience and the volume can be adjusted using the
settings button in the main menu.

  
- **Pause Menu:** Players can pause the game at any time by pressing the 'esc' key for the game to direct the user to the pause menu where they can choose to the see the current high score, resume back to the game by pressing the resume button or pressing esc again and they can also go back to the main menu page by clicking the main menu button.


- **High Score:** The High score feature keeps track of the highest score achieved.


- **Title Screen/Main Menu:** The game features a main menu that allows players to start a new game, access settings to adjust the background music volume. The main menu provides an intuitive instructions interface for explaining the game mechanics to the user.



## 2.2 Additional features not implemented

-


## 3 Refactoring

This Section will cover the design patterns implemented in various classes, the bugs fixed within the previous existing code, 
encapsulation implemented within the project, and the challenges faced during refactoring.


## 3.1 Packages and their classes

- **com.comp2042.app:** This package acts as the bridge or the Application layer. It connects the Logic package to the view package.
  
  + **GameController:** This is the "brain" that connects input to action. When the GuiController detects
  a key press, it tells the GameController. GameController then tells Board.java to move a piece, and tells the GuiController to redraw. It exists here to decouple the View from the Logic.
  + **Constants:** Constants class encapsulates all the constant values and links used in the project. The reason it is placed in the app package since the app package acts as the bridge package between other main packages.
  
- **com.comp2042.events:** This package implements the Observer. It defines a standard way for different parts of the app to talk to each other (specifically, sending user input from the View to the Controller).
  
  + **InputEventListener:** An interface defining what actions the user can take (move down, rotate, etc.).
  + **MoveEvent:** A wrapper class that carries details about the event(e.g., did the user press a key).
  + Reason for existence: This decoupling allows you to change the input method (e.g., adding joystick support) without rewriting the core game logic.

- **com.comp2042.Logic:** This package contains the logic and the internal state of the game. It represents the Model, since it deals with what is happening in the game (math, grid, collisions).

  + **Board and SimpleBoard:** These manage the game grid. They exist here because the state of the board (where blocks are) is the core data of the game.
  + **Score:** Tracks the player's current points.
  + **SoundManager:** Handles the audio logic in the game.
  + **ViewData and DownData:** They exist to package up the state of the board and send it to the View without giving the View direct access to the complex Board logic.
  + **Logic.bricks:** The reason for sub-packaging these brick class because these classes define the shapes and rotation states of individual bricks, and sub-packaging keeps the main Logic folder clean.

- **com.comp2042.view:** This package handles the Graphical User Interface (GUI). Classes here are responsible for drawing images, rectangles, and text to the screen. They should not know the rules of Tetris; they just display what they are told.

  + **GuiController:** This is the JavaFX controller linked to your FXML files. It creates Tetris objects and handles animations. It is the main file responsible for main visual aspects of the game.
  + **GameOverPanel and NotificationPanel:** Custom UI components that appear as overlays after the game ends.
  + **Main:** The entry point of JavaFX application. It sets up the stage and loads up the title screen which then allows the user to redirect to the game and other panels.
  +  **PauseMenuController, TitleScreenController, InstructionsController:** These classes are additional classes that are used to boost user-friendly interfaces and allow the user to access the new addtional features implemented such as the controlling the music volume.


- **com.comp2042.RotationOperations:** This is an utility package extracted to handle complex matrix math related to rotation.

  + **BrickRotator:** Contains the math to rotate a 2D matrix tetris brick.


## 3.2 Design patterns Implemented