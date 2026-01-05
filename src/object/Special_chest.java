package object;

import Main.GamePanel;
import entity.Entity;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

// Represents a unique chest that, upon interaction, triggers a major event
// (specifically, switching to the game's end state).
public class Special_chest extends Entity {

    GamePanel gp;
    Entity loot;       // The item the chest holds (though not currently granted in interact())
    boolean opened = false; // Flag to track if the chest has been opened

    public Special_chest(GamePanel gp, Entity loot) {
        super(gp);
        this.gp = gp;
        this.loot = loot;

        type = type_obstacle;
        name = "Special Chest";

        // Load closed and opened sprites
        image = setup("/object/chest",gp.tilesSize,gp.tilesSize);         // Closed chest sprite
        image2 = setup("/object/chest_opened",gp.tilesSize,gp.tilesSize); // Opened chest sprite

        down1 = image;   // Initial sprite is the closed chest
        collision = true; // Player cannot walk through it

        // Define the collision area (hitbox) relative to the tile
        solidArea.x = 4;
        solidArea.y = 16;
        solidArea.width = 40;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    /**
     * Overrides the base interact method.
     * Interaction with this chest immediately triggers the game's end state.
     */
    @Override
    public void interact() {
        super.interact();

        // Immediately change the game state to 'endState' (e.g., game won, credits screen)
        gp.gameState = gp.endState;
    }
}