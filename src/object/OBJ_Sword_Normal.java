package object;

import entity.Entity;
import Main.GamePanel;

// Represents a Normal Sword item, extending the base Entity class for use as a weapon.
public class OBJ_Sword_Normal extends Entity {

    public OBJ_Sword_Normal (GamePanel gp){
        super(gp);

        type = type_sword; // Set the entity type to weapon (sword)
        name = "Normal Sword";

        // Load the sword sprite
        down1 = setup("/object/sword_normal", gp.tilesSize, gp.tilesSize);

        attackValue = 1; // The base attack power this item provides

        // Define the weapon's physical attack area (hitbox size during swing)
        attackArea.width = 40;
        attackArea.height = 40;

        price = 100; // The selling/buying price

        // Description displayed in the inventory screen.
        description = "[" + name + "]: \nA normal wooden \nSword";

    }
}