package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

// Represents a Heart item pickup, used to restore player life.
public class OBJ_Heart extends Entity {

    public OBJ_Heart(GamePanel gp) {
        super(gp);

        type = type_pickupOnly; // Item is consumed immediately upon collision
        name = "Heart";
        value = 2; // Amount of life restored (since 1 heart = 2 life points)

        // Load the different heart states, used both in the world and for the UI HUD
        down1 = setup("/object/heart_full",gp.tilesSize,gp.tilesSize); // Default sprite for world object
        image = setup("/object/heart_full",gp.tilesSize,gp.tilesSize);  // Full heart icon (used by UI)
        image2 = setup("/object/heart_half",gp.tilesSize,gp.tilesSize); // Half heart icon (used by UI)
        image3 = setup("/object/heart_blank",gp.tilesSize,gp.tilesSize); // Blank heart icon (used by UI)
    }

    // Defines the action that occurs when the player picks up this item.
    public void use(Entity entity){
        gp.playSE(2); // Play healing sound effect (index 2: powerup.wav)

        // Display a message showing the life gain
        gp.ui.addMessage("Gain Life + " + value);

        // Restore life to the entity
        entity.life += value;

        // Cap the entity's life so it doesn't exceed maxLife
        if (entity.life > entity.maxLife)
            entity.life = entity.maxLife;
    }
}