package object;

import Main.GamePanel;
import entity.Entity;

import javax.swing.*;

public class OBJ_Lantern extends Entity {

    public OBJ_Lantern(GamePanel gp) {
        super(gp);

        type = type_light;
        name = "Lantern";
        stackable = true;
        down1 = setup("/object/lantern", gp.tilesSize, gp.tilesSize);
        price = 200;
        lightRadius = 300;
        // Description displayed in the inventory screen. \n creates a new line.
        description = "[" + name + "]: \nIlluminates your\nSurroundings.";
    }
    @Override
    public void use(Entity entity) {
        entity.currentLight = this;
        gp.playSE(10);
    }

}
