package object;

import Main.GamePanel;
import entity.Entity;

// Represents a Key item, used to open locked doors.
public class OBJ_key extends Entity {

    public OBJ_key(GamePanel gp) {
        super(gp);

        type = type_consumable; // Item that can be used and is removed from inventory
        name = "Key";
        price = 50; // The coin value of the key
        stackable = true;

        // Load the key sprite
        down1 = setup("/object/key", gp.tilesSize, gp.tilesSize);

        // Description displayed in the inventory
        description = "[" + name + "]: \nA Golden Key to unlock \nDoors.";
    }

    /**
     * Defines the action that occurs when the player attempts to 'use' the key
     * from their inventory.
     * @param entity The entity using the key (must be gp.player in this context).
     */
    public void use(Entity entity){
        // Check if the entity is currently standing next to a "Door" object
        // NOTE: Assuming gp.obj is a 2D array [mapIndex][objectIndex]
        int obj_index = getDetected(entity, gp.obj, "Door");

        // Switch to dialogue state to communicate the result of the action
        gp.gameState = gp.dialogueState;

        if (obj_index != 999){
            // --- Door Detected: Successfully open the door and consume the key ---

            gp.ui.currentDialogue = "You used " + name + " and open the door!";
            gp.playSE(3); // Play unlock sound effect

            // 1. OPEN DOOR: Remove the door object from the game map
            gp.obj[gp.currentMap][obj_index] = null;

        } else {
            gp.ui.currentDialogue = "Keys are used to open the doors.";
        }
    }
}