package object;

import Main.GamePanel;
import entity.Entity;

// Represents a special door that requires high resource costs and status checks to open.
public class Special_door extends Entity {

    public Special_door (GamePanel gp) {
        super(gp);

        name = "Special Door";
        type = type_obstacle;

        // Load the door sprite
        down1 = setup("/object/door",gp.tilesSize,gp.tilesSize);
        collision = true; // Player cannot walk through it initially

        // Define the collision area (hitbox)
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    /**
     * Handles the interaction logic, checking multiple player requirements before opening.
     */
    public void interact() {

        // Get the object's index on the current map for later removal
        int objIndex = getDetected(gp.player, gp.obj, "Special Door");

        // --- 1. Check Requirements ---
        boolean hasKeys   = gp.player.inventoryCount("Key") >= 1;
        boolean hasCoins  = gp.player.coin >= 300;
        boolean hasLevel  = gp.player.level >= 3;

        if(hasKeys && hasCoins && hasLevel) {
            // --- Success: Open Door and Consume Resources ---

            gp.gameState = gp.dialogueState;
            gp.ui.currentDialogue = "Door opened!";
            gp.playSE(3); // Play unlock sound
            gp.player.coin -= 300;

            // Consume required resources
            gp.player.removeItem("Key", 1);
            gp.player.coin -= 300;

            // Remove the door from the map array
            if(objIndex != 999) {
                gp.obj[gp.currentMap][objIndex] = null;
            }

        } else {
            // --- Failure: Show Requirement Screen ---

            // Set a special game state to inform the player of the missing requirements
            gp.gameState = gp.requirementState;
        }
    }
}