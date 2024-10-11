# TheCramming
Game, Java Swing

# JavaPrisonEscape_Game
Team project for 2024-1

# Game Project

## Overview
This Java project is a 2D roguelite action game where the player navigates through a maze-like environment, combats enemies, and collects power-ups. The game features multiple stages, different enemy types, and power-ups that enhance the player's abilities.

## Table of Contents
- [Installation](#installation)
- [Usage](#usage)
- [Gameplay](#gameplay)
- [Classes and Methods](#classes-and-methods)
- [Assets](#assets)
- [Audio](#audio)
- [Controls](#controls)
- [Demo Video](#demo-video)
- [Known Issues](#known-issues)
- [Future Improvements](#future-improvements)
- [Contribution](#contribution)
- [Disclaimer](#disclaimer)
- [Copyright](#copyright)

## Installation

### Prerequisites
- Java Development Kit (JDK) 8
- An IDE or text editor (Eclipse, IntelliJ IDEA, VSCode)

### Steps
1. Clone the repository:
    ```bash
    git clone <>
    ```
2. Open the project in your preferred IDE.
3. Ensure that the project structure and dependencies are correctly set up.

## Usage

### Running the Game
1. Navigate to the `Main` class.
2. Run the `Main` class to start the game.

### Building the Project
1. Ensure that all dependencies and assets are included.
2. Build the project using your IDE's build tools.

## Gameplay

### Objective
Survive through various stages by defeating enemies and collecting power-ups. Progress through stages to face tougher enemies and bosses.

### Power-ups
- **Energy Drink:** Restores health.
- **Graphics Card:** Increases player speed for a limited time.
- **TalkGPT:** Enhances player attacks for a limited time.

## Classes and Methods

### Main Classes
- `Game`: The core game loop and logic.
- `PlayerAttack`: Handles player attacks.
- `Enemy`: Base class for enemies.
- `EnemyAttack`: Handles enemy attacks.
- `Wall`: Represents obstacles in the game.
- `Potion`, `Shoe`, `Sniper`: Represents different power-ups.
- `Rank`: Manages player rankings and scores.
- `Audio`: Brings audio file to be used.

### Key Methods
- `run()`: Main game loop.
- `reset()`: Resets the game state.
- `keyProcess()`: Processes player input.
- `enemyAppearProcess()`: Spawns enemies.
- `enemyMoveProcess()`: Handles enemy movement.
- `enemyAttackProcess()`: Manages enemy attacks.
- `potionProcess()`, `shoeProcess()`, `sniperProcess()`: Manages power-up appearances and effects.
- `checkGameOver()`: Checks if the game is over.
- `gameDraw(Graphics g)`: Renders the game graphics.
- `gameOverDraw(Graphics g)`: Renders the game over screen and rankings.

## Assets

### Images
- All images in `image` folder: made by Ji-uk Kim

## Audio
- Menu background music: `Spaceship Hangar by VOiD1 Gaming via Gamejolt`
- Stage 1 background music: `Good Night by Infrared Scale`
- Stage 2 background music: `Good Night by Infrared Scale`
- Stage 3 background music: `Good Night by Infrared Scale`
- Hit sound: `Power Punch by UNIVERSFIELD via Pixabay`

## Controls
- **W, A, S, D Key:** Move the player up, down, left, and right.
- **Mouse LClick / Space Key:** Shoot in the direction of the mouse pointer.
- **R Key:** Resume game when stage changes.
- **H Key:** (Only on title screen) Shows Help.
- **C Key:** (Only on title screen) Shows Credits.
- **Enter Key:** (Either on title or help screen) Starts game.

## Demo Video
[Watch the demo video](https://www.youtube.com/watch?v=ACUok3enW_A)

## Known Issues
- **Performance:** The game may lag on slower machines due to inefficient rendering and collision detection.
- **Audio:** The music for each stage sometimes doesn't load due to the audio file being too big. This frequently occurs if the loop is being played back too much.

## Future Improvements
- **Optimize Rendering:** Improve the efficiency of the rendering process to reduce lag.
- **Enhanced Collision Detection:** Make collision detection more efficient.
- **Add More Levels and Enemies:** Introduce more variety in enemies and stages.
- **Improve UI:** Enhance the user interface for a better gaming experience.
- **Bug Fixes:** Address known issues and improve overall stability.

## Contribution
Feel free to fork the repository and submit pull requests. Contributions are welcome!

## Disclaimer
### Resolution
This project was programmed, built, and tested in a 1920x1080 resolution environment. While it may run on other sized environments too, it might not provide the best experience, and the code might not function as intended.

### Input
When inputting controls in the game, please make sure your keyboard is set to English. This is because key input is made through the `keyListener` class, which recognizes input only through an English keyboard.

### Text encoding
Please set text encoding to UTF-8 to view commnents properly.

## Copyright
For assets in our project, we wanted to make sure we gave credit to respective artists for assets we did not make. For visual assets, all pixel sprites were made by Ji-uk Kim himself using Affinity Designer and Adobe Illustrator; title artwork was generated by DeepAI (deepai.org)'s pixel art generator. For audio, the menu music and hit sound effects were obtained via GameJolt and Pixabay, where both sites allow content to be used under certain conditions. For in-game music, direct contact was made with the original artist, and permission was granted (refer to respective documents for each copyright).

Happy gaming! Enjoy the adventure and challenge yourself to reach the highest score.
