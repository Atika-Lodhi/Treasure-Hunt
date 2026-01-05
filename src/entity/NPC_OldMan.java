package entity;

import java.awt.*;
import java.util.Random;
import Main.GamePanel;

// NPC class representing the Old Man character
public class NPC_OldMan extends Entity {

    // Constructor
    public NPC_OldMan(GamePanel gp){
        super(gp);

        // Initial direction and speed
        direction = "Down";
        speed = 1;

        // Collision area setup
        solidArea = new Rectangle(8, 16, 28, 28);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // Load NPC images and dialogues
        getImage();
        setDialogue();
    }

    // Load NPC sprites for all directions
    public void getImage () {
        up1 = setup("/npc/oldman_up_1",gp.tilesSize,gp.tilesSize);
        up2 = setup("/npc/oldman_up_2",gp.tilesSize,gp.tilesSize);
        down1 = setup("/npc/oldman_down_1",gp.tilesSize,gp.tilesSize);
        down2 = setup("/npc/oldman_down_2",gp.tilesSize,gp.tilesSize);
        left1 = setup("/npc/oldman_left_1",gp.tilesSize,gp.tilesSize);
        left2 = setup("/npc/oldman_left_2",gp.tilesSize,gp.tilesSize);
        right1 = setup("/npc/oldman_right_1",gp.tilesSize,gp.tilesSize);
        right2 = setup("/npc/oldman_right_2",gp.tilesSize,gp.tilesSize);
    }

    // Set dialogue lines for the Old Man
    public void setDialogue(){
        dialogues[0] = "Hello, lad.";
        dialogues[1] = "So you've come to this island to \nfind treasure?";
        dialogues[2] = "I used to be a great wizard but \nnow... I'm too old for adventure.";
        dialogues[3] = "Well, good luck to you.\nHope you can find Dungeon.";
        dialogues[4] = "Ha ha ha!";
    }

    // NPC action logic: randomly look around every 120 frames
    public void setAction() {
        actionLookCounter++;

        if(actionLookCounter == 120){
            Random random = new Random();
            int i = random.nextInt(100) + 1;

            if (i <= 25) direction = "Up";
            else if (i <= 50) direction = "Down";
            else if (i <= 75) direction = "Left";
            else direction = "Right";

            actionLookCounter = 0;
        }
    }

    // Override speak to use base NPC speaking behavior
    @Override
    public void speak(){
        super.speak(); // Handles dialogue display and facing player
    }
}
