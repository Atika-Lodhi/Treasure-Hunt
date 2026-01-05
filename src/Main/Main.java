package Main;

import javax.swing.*;
import java.io.IOException;

// Main class serves as the entry point for the game application.
public class Main {
    public static void main(String[] args) {
        // Wrap the main logic in a try-catch block to handle IOException from config loading
        try {
            // 1. Create the game window (JFrame)
            JFrame window = new JFrame();
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit app on close
            window.setResizable(false); // Prevent window resizing
            window.setTitle("Treasure Hunting"); // Set window title

            // 2. Create the game panel
            GamePanel gamePanel = new GamePanel();

            // 3. Load configuration settings before setting up the game or window
            // The loadConfig() method can throw IOException if "config.txt" is not found
            gamePanel.config.loadConfig();

            // 4. Add the game panel to the window
            window.add(gamePanel);

            // 5. Configure window size and position
            window.pack();  // Sizes the window to fit the preferred size of the GamePanel
            window.setLocationRelativeTo(null); // Center window on screen
            window.setVisible(true); // Make the window visible to the user

            // 6. Setup game objects (set entities, objects, initial state)

            gamePanel.setupGame();

            // 7. Start the game loop thread


            gamePanel.startGameThread();

        } catch (IOException e) {

            // Print error if loading config failed
            System.err.println("Error reading configuration file: " + e.getMessage());
            // Optionally, handle fatal error here (e.g., show a message to the user and exit)
            e.printStackTrace();
        }
    }
}