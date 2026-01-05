package object;

import Main.GamePanel;
import entity.Entity;

// Represents a Red Potion, a consumable item used to restore the player's life.
public class OBJ_Potion_Red extends Entity {

    public OBJ_Potion_Red(GamePanel gp){
        super(gp);
        this.gp = gp; // Store GamePanel reference

        type = type_consumable; // Item is removed from inventory upon use
        name = "Red Potion";
        value = 3; // Amount of life the potion restores
        price = 40; // The coin value of the potion
        stackable = true;

        // Load the potion sprite
        down1 = setup("/object/potion_red", gp.tilesSize, gp.tilesSize);

        // Description displayed in the inventory
        description = "[" + name + "]: \nHeals your life by" + value + ".";
    }

    /**
     * Defines the action that occurs when the player 'uses' the potion from their inventory.
     * @param entity The entity consuming the potion (usually the player).
     */
    public void use(Entity entity){
        // 1. Switch to dialogue state to communicate the action
        gp.gameState = gp.dialogueState;

        // 2. Display the healing message
        gp.ui.currentDialogue = "You drink the " + name + "!\n" + "Your life has been recovered by " + value + ".";

        // 3. Apply the healing effect
        entity.life += value;

        // 4. Cap life at maxLife to prevent over-healing
        if (entity.life > entity.maxLife) {
            entity.life = entity.maxLife;
        }
    }
}