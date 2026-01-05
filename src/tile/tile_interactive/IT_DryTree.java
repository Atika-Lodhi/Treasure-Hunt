package tile.tile_interactive;

import Main.GamePanel;
import entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage; // Note: Imported implicitly via setup method in Entity

// Represents a Dry Tree, a destructible interactive tile that requires an axe to be chopped down.
public class IT_DryTree extends interactiveTile{

    public IT_DryTree(GamePanel gp, int col, int row){
        super(gp,col,row); // Initialize common interactive tile properties (world position)

        // Redundant initialization, as it's handled by super(gp, col, row), but kept for fidelity:
        this.gp = gp;
        this.worldX = gp.tilesSize*col;
        this.worldY = gp.tilesSize*row;

        // Load the dry tree sprite
        down1 = setup("/tiles_interactive/drytree", gp.tilesSize, gp.tilesSize);

        destructible = true; // Flag the tile as breakable
        life = 3;           // Requires 3 hits (life points) to be destroyed
        type = type_interactiveTile; // Categorize the entity type
    }

    /**
     * Checks if the entity is wielding an Axe (type_axe), which is the correct tool
     * for destroying the Dry Tree.
     * @param entity The entity attempting to interact (e.g., player).
     * @return true if the entity's current weapon is an axe.
     */
    @Override
    public boolean isCorrectItem(Entity entity){
        boolean isCorrectItem = false;
        // Check if the current weapon type matches the axe type constant
        if (entity.currentWeapon.type == type_axe){
            isCorrectItem = true;
        }
        return isCorrectItem ;
    }

    /**
     * Plays the sound effect when the tree is hit.
     */
    @Override
    public void playSE(){
        gp.playSE(11); // Play the tree chopping sound effect (SE index 11)
    }

    /**
     * Returns the interactive tile that replaces the tree when it is destroyed (a stump).
     * @return A new IT_Trunk instance created at the tree's tile position.
     */
    @Override
    public interactiveTile getDestroyedForm(){
        // Create the stump, passing the tile grid coordinates (worldX/tilesSize, worldY/tilesSize)
        interactiveTile tile = new IT_Trunk(gp, worldX/gp.tilesSize, worldY/gp.tilesSize);
        return tile;
    }

    // --- Particle Effects Properties (Used for generating wood chips/debris when hit) ---

    @Override
    public Color getPaticleColor(){
        // Earthy brown color (RGB: 65, 50, 30) for wood/bark debris
        Color color = new Color(65,50,30);
        return color;
    }

    @Override
    public int getParticleSize(){
        int size = 6; // Particles are 6 pixels in size
        return size;
    }

    @Override
    public int getParticleSpeed(){
        int speed = 1; // Slow particle movement speed
        return speed;
    }

    @Override
    public int getParticleMaxLife(){
        int maxLife = 20; // Particles disappear after 20 frames
        return maxLife;
    }
}