package object;

import entity.Entity;
import Main.GamePanel;

// Represents a Wood Shield item, extending the base Entity class for use as equipment.
public class OBJ_Shield_Wood extends Entity {

    public OBJ_Shield_Wood (GamePanel gp){
        super(gp);

        type = type_shield; // Set the entity type to shield/defense equipment
        name = "Wood Shield";

        // Load the shield sprite
        down1 = setup("/object/shield_wood", gp.tilesSize, gp.tilesSize);

        // value assigns to attackValue.
        attackValue = 1;

        price = 100; // The selling/buying price

        // Description displayed in the inventory screen.
        description = "[" + name + "]: \nA wooden Shield for \ndefence";
    }
}