package entity;

import Main.GamePanel;
import object.*;

import java.awt.*;

// NPC class representing a merchant that the player can trade with
public class NPC_Merchant extends Entity {

    // Constructor
    public NPC_Merchant(GamePanel gp){
        super(gp);

        // Initial facing direction and movement speed
        direction = "Down";
        speed = 1;

        // Collision area for the NPC
        solidArea = new Rectangle(8, 16, 28, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Load NPC images, set dialogues and inventory
        getImage();
        setDialogue();
        setItems();
    }

    // Load NPC sprites for all directions (currently all use down sprite)
    public void getImage () {
        up1 = setup("/npc/merchant_down_1",gp.tilesSize,gp.tilesSize);
        up2 = setup("/npc/merchant_down_2",gp.tilesSize,gp.tilesSize);
        down1 = setup("/npc/merchant_down_1",gp.tilesSize,gp.tilesSize);
        down2 = setup("/npc/merchant_down_2",gp.tilesSize,gp.tilesSize);
        left1 = setup("/npc/merchant_down_1",gp.tilesSize,gp.tilesSize);
        left2 = setup("/npc/merchant_down_2",gp.tilesSize,gp.tilesSize);
        right1 = setup("/npc/merchant_down_1",gp.tilesSize,gp.tilesSize);
        right2 = setup("/npc/merchant_down_2",gp.tilesSize,gp.tilesSize);
    }

    // Set NPC dialogue lines
    public void setDialogue(){
        dialogues[0] = "Hey, so you found me. I have some \ngood stuff! Want to trade?";
    }

    // Set items available in the merchant's inventory
    public void setItems() {
        inventory.add(new OBJ_Potion_Red(gp));
        inventory.add(new OBJ_key(gp));
        inventory.add(new OBJ_Sword_Normal(gp));
        inventory.add(new OBJ_Axe(gp));
        inventory.add(new OBJ_Shield_Wood(gp));
        inventory.add(new OBJ_Shield_Blue(gp));
        inventory.add(new OBJ_ManaCrystal(gp));
    }

    // Override speak to open trading interface when player interacts
    @Override
    public void speak() {
        super.speak();         // Call base speak method (NPC faces player, dialogue shows)
        gp.gameState = gp.tradeState;  // Switch to trade state
        gp.ui.npc = this;      // Set current NPC in UI
    }
}
