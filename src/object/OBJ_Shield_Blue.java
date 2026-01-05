package object;

import entity.Entity;
import Main.GamePanel;

// Represents a Blue Shield item, extending the base Entity class for use as equipment.
public class OBJ_Shield_Blue extends Entity{

    public OBJ_Shield_Blue (GamePanel gp){
        super(gp);

        type = type_shield; // Set the entity type to shield/defense equipment
        name = "Blue Shield";

        // Load the shield sprite
        down1 = setup("/object/shield_blue", gp.tilesSize, gp.tilesSize);

        // value assigns to attackValue.
        attackValue = 2;
        stackable = true;

        price = 200; // The selling/buying price

        // Description displayed in the inventory screen.
        description = "[" + name + "]: \nA Shiny blue Shield for \ndefence. ";
    }
}