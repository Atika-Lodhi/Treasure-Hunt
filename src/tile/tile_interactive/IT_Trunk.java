package tile.tile_interactive;

import Main.GamePanel;
import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

// Represents a Tree Trunk, the destroyed form of a dry tree, acting as a non-destructible obstacle.
public class IT_Trunk extends interactiveTile{

    public IT_Trunk(GamePanel gp, int col, int row){
        super(gp,col,row); // Initialize common interactive tile properties

        // Redundant initialization, as it's handled by super(gp, col, row), but kept for fidelity:
        this.gp = gp;
        this.worldX = gp.tilesSize*col;
        this.worldY = gp.tilesSize*row;

        // Load the tree trunk (stump) sprite
        down1 = setup("/tiles_interactive/trunk", gp.tilesSize, gp.tilesSize);

        destructible = false; // Cannot be destroyed further
        life = 999;           // Effectively infinite life
        type = type_obstacle; // Classified as a simple obstacle (not interactive in the same way)

        // Define the collision area for the trunk
        solidArea.x = 0;
        solidArea.y = 0;
        solidArea.width = 0; // Trunk might be non-collidable or have a small hitbox
        solidArea.height = 0;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    // As a simple stump, it requires no specific item to interact with/destroy.
    @Override
    public boolean isCorrectItem(Entity entity){
        return false;
    }
}