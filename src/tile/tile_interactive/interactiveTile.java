package tile.tile_interactive;

import Main.GamePanel;
import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

// Base class for all interactive tiles in the game (e.g., breakable rocks, chopped trees).
// It extends Entity to utilize movement, collision, and drawing methods.
public class interactiveTile extends Entity {

    // Flag indicating if this tile can be destroyed (e.g., a rock or a tree).
    public boolean destructible = false;

    /**
     * Constructor for interactive tiles.
     * @param gp The GamePanel instance.
     * @param col The map column (x-coordinate on the tile grid).
     * @param row The map row (y-coordinate on the tile grid).
     */
    public interactiveTile(GamePanel gp, int col, int row) {
        super(gp);
        this.gp = gp;

        // Calculate world coordinates from the tile grid position
        this.worldX = col * gp.tilesSize;
        this.worldY = row * gp.tilesSize;
    }

    /**
     * Checks if the entity (usually the player) is using the correct item to interact
     * with or destroy this tile. Must be overridden by specific interactive tiles.
     * @param entity The entity attempting the interaction.
     * @return true if the item is correct, false otherwise.
     */
    public boolean isCorrectItem(Entity entity) {
        boolean isCorrectItem = false;
        return isCorrectItem;
    }

    /**
     * Plays the specific sound effect related to hitting or interacting with this tile.
     * Must be overridden by specific interactive tiles.
     */
    public void playSE() {
        // Implementation will be in subclasses (e.g., play chopping sound)
    }

    /**
     * Returns the destroyed state of the tile (e.g., a tree stump after a tree is cut).
     * Must be overridden by specific interactive tiles.
     * @return A new interactiveTile object representing the destroyed form, or null.
     */
    public interactiveTile getDestroyedForm() {
        interactiveTile tile = null;
        return tile;
    }

    /**
     * Handles timed events like invincibility cooldown.
     */
    @Override
    public void update() {
        // Handles the invincibility frame cooldown after being hit.
        if (invincible == true) {
            invincibleCounter++;
            if (invincibleCounter > 20) {   // 20-frame small cooldown
                invincible = false;
                invincibleCounter = 0;
            }
        }
    }

    /**
     * Draws the interactive tile on the screen, optimizing rendering by only drawing
     * tiles that are currently within the camera view.
     * @param g2 The Graphics2D context.
     */
    @Override
    public void draw(Graphics2D g2) {
        // Calculate the tile's position relative to the center of the player's screen
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;


        // Draw only if the tile is within the visible screen area (plus a one-tile buffer)
        if (worldX + gp.tilesSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tilesSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tilesSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tilesSize < gp.player.worldY + gp.player.screenY) {

            // The 'down1' sprite is used for the static visual representation of the tile
            g2.drawImage(down1, screenX, screenY, null);
        }
    }
}