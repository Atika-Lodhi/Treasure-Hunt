package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

// Represents a locked door obstacle that requires a key to open.
public class OBJ_door extends Entity {

    public OBJ_door(GamePanel gp) {
        super(gp);

        name = "Door";
        type = type_obstacle; // Classified as an obstacle

        // Load the door sprite
        down1 = setup("/object/door", gp.tilesSize, gp.tilesSize);
        collision = true; // Player cannot walk through it initially

        // Define the collision area (hitbox)
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    // Defines the action that occurs when the player interacts with the door (e.g., presses the action key).
    @Override
    public void interact(){
        // Switch the game state to dialogue to display a message
        gp.gameState = gp.dialogueState;

        // Display the message indicating the door is locked
        gp.ui.currentDialogue = "This door is locked. \nYou need a Key to open it!";

        // NOTE: The actual logic for checking if the player possesses a key
        // and opening the door (e.g., changing its collision state and sprite)
        // would typically be implemented here, but the current code only shows the message.
    }
}