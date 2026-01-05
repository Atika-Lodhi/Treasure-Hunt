package object;

import Main.GamePanel;
import entity.Entity;

// Represents a Mana Crystal pickup item, used to restore the player's mana.
public class OBJ_ManaCrystal extends Entity {

    GamePanel gp; // Local reference to the GamePanel

    public OBJ_ManaCrystal(GamePanel gp){
        super(gp);
        this.gp = gp; // Store GamePanel reference

        type = type_pickupOnly; // Item that is automatically consumed upon collision
        name = "Mana Crystal";
        value = 1; // Amount of mana restored (in mana points)
        price = 80; // The coin value of the crystal

        // Load the sprites for the crystal (used in the world and for the UI HUD)
        down1 = setup("/object/manaCrystal_full", gp.tilesSize, gp.tilesSize); // Default world sprite
        image = setup("/object/manaCrystal_full", gp.tilesSize, gp.tilesSize);  // Full crystal icon (used by UI)
        image2 = setup("/object/manaCrystal_blank", gp.tilesSize, gp.tilesSize); // Blank crystal icon (used by UI)

    }

    /**
     * Defines the action that occurs when the player picks up this item.
     * The 'use' method is called by the collision checker when the player touches a 'pickupOnly' item.
     * @param entity The entity (player) picking up the crystal.
     */
    public void use(Entity entity){
        gp.playSE(1); // Play sound effect (index 1: coin.wav, often used for pickups)

        // Display a message showing the mana gain
        gp.ui.addMessage("Gain Mana " + value);

        // Restore mana to the player
        gp.player.mana += value;

    }
}