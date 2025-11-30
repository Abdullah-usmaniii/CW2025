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


