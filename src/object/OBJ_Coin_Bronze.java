package object;

import Main.GamePanel;
import entity.Entity;

// Represents a bronze coin pickup item.
public class OBJ_Coin_Bronze extends Entity {

    public OBJ_Coin_Bronze(GamePanel gp) {
        super(gp);
        this.gp = gp; // Store GamePanel reference

        type = type_pickupOnly; // Item that is automatically consumed upon collision
        name = "Bronze Coin";
        value = 50; // The amount of currency this coin is worth

        // Load the coin sprite
        down1 = setup("/object/coin_bronze", gp.tilesSize, gp.tilesSize);
    }

    // Defines the action that occurs when the player picks up this item.
    // The 'use' method is called by the collision checker when the player touches a 'pickupOnly' item.
    public void use(Entity entity) {
        gp.playSE(1); // Play coin sound effect (index 1: coin.wav)

        // Display a message showing the coin gain
        gp.ui.addMessage("Gain Coin + " + value);

        // Add the coin's value to the player's total coin count
        gp.player.coin += value;
    }
}