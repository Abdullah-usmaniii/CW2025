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


## 1.3 Compilation Instructions
To compile and run the Tetris game, ensure you have Maven installed and configured on your system. Follow these steps:
1. Clone the Repository
2. Navigate to the project directory to its appropriate folder
3. Reload/Sync the Maven dependencies and plugins within your IDE to ensure all dependencies are correctly downloaded.
4. Go to the Maven tab and javafx run section or just run:
   ```mvn clean javafx:run```

## 2. Additional Features Implementation 
This section will cover the additional features implemented in the Tetris game, including their functionality and features that were not implemented.

### 2.1 Implemented Features that work properly

- **Ghost piece:** A translucent outline of the current tetromino is displayed at the position where it would land if dropped immediately. This feature helps players plan their moves more effectively.


- **Hold piece:** Players can store a tetromino for later use. This feature allows for strategic gameplay, as players can swap the current tetromino with the held one at any time by pressing the tab key.


- **Next piece preview:** The game displays the next tetromino that will appear after the current one. This feature allows players to expect and plan their moves ahead of time.


- **Sound effects and background music:** The game includes sound effects for actions such as when a tetromino is placed and when a row or multiple rows are cleared.Background music plays during the gameplay to enhance the gaming experience and the volume can be adjusted using the
settings button in the main menu.

  
- **Pause Menu:** Players can pause the game at any time by pressing the 'esc' key for the game to direct the user to the pause menu where they can choose to the see the current high score, resume back to the game by pressing the resume button or pressing esc again and they can also go back to the main menu page by clicking the main menu button.


- **High Score:** The High score feature keeps track of the highest score achieved.


- **Title Screen/Main Menu:** The game features a main menu that allows players to start a new game, access settings to adjust the background music volume. The main menu provides an intuitive instructions interface for explaining the game mechanics to the user.

- **Dig Game Mode:** In this mode, player must aim to score the highest possible score but with a twist. The twist is when a user clears a row, instead of clearing the row a new garbage row spawns at the bottom of the board
and it has a single opening the user must aim to fill in with a brick. This challenges the user since the board gets filled faster.\

- **Levels:** As the game progresses and for every 500 points scored, the game levels up and increases the speed of the falling tetris bricks by 0.25x
    + level 1: 0-499 points - speed 1x
    + level 2: 500-999 points - speed 1.25x
    + level 3: 1000-1499 points - speed 1.5x
    + level 4: 1500-1999 points - speed 1.75x
    + level 5: 2000+ points - speed 2x


- **Bomb Squad Game Mode:** In this mode, the player has access to 4 bombs at the start of the game. The bomb can be activated by clicking the "BOOM" button on the screen and the bomb are essentially a 1x1 tetris brick which a player can use to clear a single row.

## 2.2 Additional features not implemented

- **High-score LeaderBoard:** I wanted to add a leaderboard that would save multiple user's high scores but the reason 
I could not implement this feature was since it would require active live sessions each game and it was quite hard to implement.

- **Different Game Modes:** I wanted to implement different game modes such as "Time Attack" or "Survival Mode" to add variety to the gameplay experience. 
However, due to time constraints and the complexity of balancing these modes, I was unable to include them in the final version of the game.

- **Bomb animation and bomb design in bomb squad:** Adding bomb animation and a unique design for the bomb brick in the bomb squad mode was considered to enhance visual appeal. However, due to time constraints and prioritization of core gameplay features, this enhancement was not implemented.

- **Customizable Controls:** I considered allowing players to customize the control scheme to their preferences. However, implementing a user-friendly interface for control customization proved to be more complex than anticipated, leading to its exclusion from the final product.

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
  + **MatrixOperations:** A utility class containing static methods for performing matrix manipulations, such as checking for collisions, merging bricks into the board, and detecting full rows.
  + **BombManager:** Manages the inventory and state of bombs in the game, tracking how many are left and if one is currently active.
  + **LevelManager:** Observes the player's score and automatically updates the game level and speed as the score increases.
  + **Logic.bricks:** The reason for sub-packaging these brick class because these classes define the shapes and rotation states of individual bricks, and sub-packaging keeps the main Logic folder clean.


- **com.comp2042.view:** This package handles the Graphical User Interface (GUI). Classes here are responsible for drawing images, rectangles, and text to the screen. They should not know the rules of Tetris; they just display what they are told.

  + **GuiController:** This Class is the main FXML Controller. It acts as the Coordinator class. It initializes the other three classes (GameRenderer, GameInputHandler, GameLoopManager), manages high-level UI states (Score, Game Over, Pause), and bridges the Input events to the Backend Logic.
  + **(new) GameRenderer:** This class is responsible solely for the visual representation of the game. It manages the JavaFX GridPane containers and the Rectangle objects that make up the board, the current brick, and the ghost brick. It isolates the "View" details (colors, pixels, shapes) so the Controller doesn't need to manage them.
  + **(new) GameInputHandler:** This class handles user keyboard interactions. It listens for key events and translates them into semantic game actions (e.g., "Left Arrow" becomes "Move Left"). It delegates the execution of these actions to the GuiController.
  + **(new) GameLoopManager:** This class encapsulates the game loop logic using JavaFX Timeline. It manages the falling bricks logic and provides methods to start, stop, and pause the game clock.
  + **GameOverPanel and NotificationPanel:** Custom UI components that appear as overlays after the game ends.
  + **Main:** The entry point of JavaFX application. It sets up the stage and loads up the title screen which then allows the user to redirect to the game and other panels.
  + **(new) PauseMenuController, TitleScreenController, InstructionsController:** These classes are additional classes that are used to boost user-friendly interfaces and allow the user to access the new addtional features implemented such as the controlling the music volume.
  + **(new) BombPanel:** A custom UI component that displays the player's bomb inventory and handles click events to activate bombs.
  + **InstructionsController:** Controls the view that displays the game instructions and controls to the user.
  + **(new) GameModeSelectionController:** Manages the screen where the user selects between Classic, Dig, and Bomb game modes.

    
- **com.comp2042.RotationOperations:** This is an utility package extracted to handle complex matrix math related to rotation.

  + **BrickRotator:** Contains the math to rotate a 2D matrix tetris brick.
  + **NextBrickInfo:** A utility class to encapsulate the next brick's type and color information for preview purposes.


## 3.2 Design pattern implementation in classes

- **Command Pattern:** The GameInputHandler.java previously had a complex chain of if-else statements. I replaced the if-else chain with a Map that binds a KeyCode to a Runnable Command. 
This makes the code cleaner and easier to extend. For example, if you want to add 'W', 'A', 'S', 'D' support, you just add one line to the map instead of writing new else if logic. It separates the trigger key from the Action method.


- **Singleton Pattern:** The singleton Pattern used in SoundManager.java ensures the class has only one instance throughout the entire application. The SoundManager constructor is marked **Private**, so this prevents any other class from directly accessing it.
This pattern is essential for audio resources because it will ensure:
    + Avoiding multiple audio devices or starting duplicate background music tracks.
    + Volume changes made by the TitleScreenController's slider immediately affect the one music player being used.
  

- **Flyweight Pattern:** The Flyweight pattern had to be applied to brickShape classes(OBrick, JBrick, etc.). This is because everytime you say for instance **new JBrick()** the previous code creates 4 new 2D arrays int[][] and 
a new ArrayList to define the 'J' shape. When the user plays for let's say more than 10 minutes, the program would've created hundreds of identical arrays, wasting memory and CPU time.
The solution was using the Flyweight pattern as the shape of the brick is constant. Since the program creates a specific shape like J once as a static variable, and every new JBrick simply points to this single shared definition.


- **Observer Pattern:** A behavioral design pattern where one object maintains a list of its dependents and notifies them automatically of any state changes.
This pattern is implemented in GameController class, where I registered an event handler to the "BOOM" button using **btn.setOnAction(...)**. The system observes the button for when the 
user clicks it, the registered code block executes to activate the bomb in BombManager and trigger a UI update, decoupling the input trigger from the execution logic.

## 3.3 Bugs fixed

- **Rotation Bug:** Fixed an issue where rotating a tetromino near the edges of the board caused it to not rotate which lead to difficulty in playing for the user. Implemented wall-kick logic to adjust the position of the tetromino when rotated near walls.

- **Fixing the currentOffset position:** The original code had a visible buy where the bricks where falling from the middle of the board because the coordinates where set to (4,10) and I fixed them to (4,1) which fixed the position of the bricks spawning.

- **Sound Overlap Bug:** Fixed an issue where multiple sound effects would overlap and create a cacophony of sounds. Implemented a sound queue system to manage audio playback.


## 4.0 Challenges faced during Project Development

- **Implementing the board background grid:** The grid was required to improve visibility of the tetris bricks while falling, but implementing it was a challenge as it would break the existing features such as the ghost bricks would not work correctly.


- **Splitting GuiController God class:** The GuiController class was initially a "God class" that handled multiple responsibilities, making it difficult to maintain and extend. Refactoring it into smaller, focused classes (GameRenderer, GameInputHandler, GameLoopManager) was essential. I began by addressing the following issues:
    + **Entangled FXML References:** Extracting GameRenderer was messy because I had to pass five tightly coupled FXML GridPane references into its constructor.
    + **Circular Dependencies:** I had to implement a Command Pattern in GameInputHandler to map key presses to controller actions without creating circular dependencies.
    + **Callback Complexity:** Decoupling the game loop was annoying because I had to pass a Runnable callback to trigger the moveDown event back in the controller.
    + **Preserving State Access:** I was forced to expose internal flags like isPaused via getters so the new helper classes could check the game state.