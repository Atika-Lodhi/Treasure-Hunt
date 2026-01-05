package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

// Represents an interactable Chest obstacle that holds loot.
public class OBJ_chest extends Entity {

    GamePanel gp;
    Entity loot; // The item contained inside the chest
    boolean opened = false; // Flag to track if the chest has already been looted

    public OBJ_chest(GamePanel gp, Entity loot) {
        super(gp);
        this.gp = gp;
        this.loot = loot;

        type = type_obstacle; // Classifies this entity as an obstacle/interactable object
        name = "Chest";

        // Load closed and opened sprites
        image = setup("/object/chest",gp.tilesSize,gp.tilesSize);         // Closed image
        image2 = setup("/object/chest_opened",gp.tilesSize,gp.tilesSize); // Opened image

        down1 = image; // Initial sprite is the closed chest
        collision = true; // Player cannot walk through it

        // Define the collision area (hitbox)
        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    // Interaction logic when the player attempts to interact (e.g., presses the action key).
    @Override
    public void interact() {
        super.interact(); // Call base Entity interact method (if any)

        // Switch to dialogue state to display the outcome
        gp.gameState = gp.dialogueState;

        if (opened == false) {
            // --- Chest is not yet opened ---

            gp.playSE(3); // Play the unlock/powerup sound effect (index 3: unlock.wav)

            StringBuilder sb = new StringBuilder();
            sb.append("You have opened a chest \nand find " + loot.name + "!");

            // Check if the player has space in their inventory
            if (gp.player.canObtainItem(loot) == false) {
                // Inventory is full
                sb.append("\n...But you cannot carry more.");
            } else {
                down1 = image2; // Change sprite to the opened chest image
                opened = true; // Mark chest as opened
            }
            gp.ui.currentDialogue = sb.toString();
        } else {
            // --- Chest is already opened ---
            gp.ui.currentDialogue = "Its Empty!";
        }
    }
}