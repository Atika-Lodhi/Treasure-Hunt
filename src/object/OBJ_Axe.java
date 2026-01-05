package object;

import entity.Entity;
import Main.GamePanel;

// Represents the Woodcutter's Axe item, extending the base Entity class for use as a weapon.
public class OBJ_Axe extends Entity {

    public OBJ_Axe (GamePanel gp){
        super(gp);

        type = type_axe; // Set the entity type to weapon/tool (axe)
        name = "Woodcutter's axe";

        // Load the axe sprite using the standard setup utility
        down1 = setup("/object/axe", gp.tilesSize, gp.tilesSize);

        attackValue = 2; // The attack power this item provides
        price = 75;      // The selling/buying price
        stackable = true;

        // Define the weapon's physical attack area (hitbox size during swing)
        attackArea.width = 30;
        attackArea.height = 30;

        // Description displayed in the inventory screen. \n creates a new line.
        description = "[" + name + "]: \nA bit rusty but still\ncan cut some trees.";
    }
}