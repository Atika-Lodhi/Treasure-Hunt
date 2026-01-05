package monster;

import Main.GamePanel;
import entity.Entity;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;
import object.OBJ_Rock;
import object.OBJ_key;

import java.util.Random;

// Represents a Green Slime monster, extending the base Entity class.
public class MON_greenSlime extends Entity {

    public MON_greenSlime(GamePanel gp) {
        super(gp);

        type = type_monster;
        name = "Green Slime";
        speed = 1;         // Movement speed
        maxLife = 4;       // Maximum life points
        life = maxLife;
        attack = 2;
        defense = 0;
        exp = 2;           // Experience points awarded upon defeat
        projectile = new OBJ_Rock(gp); // This monster can shoot rocks

        // Define the collision area (Hitbox) relative to the monster's drawing position
        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x; // Store default hitbox offset
        solidAreaDefaultY = solidArea.y; // Store default hitbox offset

        getImage();
    }

    // Loads the sprites for the slime. The Green Slime uses the same sprite
    // for all directions, as is common for simple, spherical enemies.
    public void getImage(){
        up1 = setup("/monster/greenslime_down_1",gp.tilesSize,gp.tilesSize);
        up2 = setup("/monster/greenslime_down_2",gp.tilesSize,gp.tilesSize);
        down1 = setup("/monster/greenslime_down_1",gp.tilesSize,gp.tilesSize);
        down2 = setup("/monster/greenslime_down_2",gp.tilesSize,gp.tilesSize);
        left1 = setup("/monster/greenslime_down_1",gp.tilesSize,gp.tilesSize);
        left2 = setup("/monster/greenslime_down_2",gp.tilesSize,gp.tilesSize);
        right1 = setup("/monster/greenslime_down_1",gp.tilesSize,gp.tilesSize);
        right2 = setup("/monster/greenslime_down_2",gp.tilesSize,gp.tilesSize);
    }

    // Defines the monster's AI logic (movement and attacks).
    public void setAction(){
        actionLookCounter++;

        // 1. Random Movement: Change direction every 120 frames (approx. 2 seconds)
        if(actionLookCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1; // Generate a number from 1 to 100

            if (i <= 25) direction = "Up";
            else if (i <= 50) direction = "Down";
            else if (i <= 75) direction = "Left";
            else direction = "Right"; // i > 75 (i.e., 76-100)

            actionLookCounter = 0; // Reset counter
        }

        // 2. Projectile Attack: 1% chance to shoot a rock every frame, with cooldown check
        int i = new Random().nextInt(100) + 1;

        // If the random number is high (99 or 100) AND the monster is not already shooting
        // AND the shot cooldown is ready (shotAvailableCounter == 30)
        if (i > 99 && projectile.alive == false && shotAvailableCounter == 30){
            // Instantiate the projectile and add it to the game's projectile list
            projectile.set(worldX, worldY, direction, true, this);
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0; // Reset shot cooldown
        }
    }

    // Defines the monster's immediate reaction upon taking damage.
    public void damageReaction(){
        actionLookCounter = 0;           // Immediately resets the random movement timer
        direction = gp.player.direction; // Turns the monster towards the player who hit it
    }

    // Determines which item the monster drops upon death.
    public void checkDrop(){
        // Cast a die (roll a random number from 1 to 100)
        int i = new Random().nextInt(100)+1;

        // Set the monster drop based on probability ranges
        if (i < 50){
            // ~49% chance
            dropItem(new OBJ_Heart(gp));
        }
        else if (i >= 50 && i < 75){
            // ~25% chance
            dropItem(new OBJ_key(gp));
        }
        else if (i >= 75){
            // ~26% chance (covers 75 to 100)
            dropItem(new OBJ_ManaCrystal(gp));
        }
    }

}