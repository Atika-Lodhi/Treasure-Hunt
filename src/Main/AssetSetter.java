package Main;

// Import required entity and tile classes
import entity.NPC_Merchant;
import entity.NPC_OldMan;
import monster.MON_greenSlime;
import monster.MON_redSlime;
import object.*;
import tile.tile_interactive.IT_DryTree;

// This class places objects (keys, doors, chest, boots), NPCs, and Monsters in the game world
public class AssetSetter {
    GamePanel gp; // Reference to main game panel

    // Constructor to get the GamePanel reference
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    // ## Set Objects (Items, Equipment, Containers)
    public void setObject(){
        int mapNum = 0; // The map index to place objects on (Map 0 is generally the starting map)
        int i = 0; // Index counter for the object array gp.obj[mapNum][i]

        // OBJECT 0: AXE
        gp.obj[mapNum][i] = new OBJ_Axe(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*33; // World X position (tile column 33)
        gp.obj[mapNum][i].worldY = gp.tilesSize*21; // World Y position (tile row 21)
        i++;

        // OBJECT 1: DOOR (Normal)
        gp.obj[mapNum][i] = new OBJ_door(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*14;
        gp.obj[mapNum][i].worldY = gp.tilesSize*28;
        i++;
//
//        // OBJECT 2: DOOR (Normal)
        gp.obj[mapNum][i] = new OBJ_door(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*12;
        gp.obj[mapNum][i].worldY = gp.tilesSize*23;
        i++;
//
//        // OBJECT 3: SPECIAL DOOR
        gp.obj[mapNum][i] = new Special_door(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*10;
        gp.obj[mapNum][i].worldY = gp.tilesSize*12;
        i++;

        // OBJECT 4: CHEST containing a KEY
        gp.obj[mapNum][i] = new OBJ_chest(gp, new OBJ_key(gp));
        gp.obj[mapNum][i].worldX = gp.tilesSize*30;
        gp.obj[mapNum][i].worldY = gp.tilesSize*28;
        i++;

        // OBJECT 6: CHEST containing a MANA CRYSTAL
        gp.obj[mapNum][i] = new OBJ_chest(gp, new OBJ_Lantern(gp));
        gp.obj[mapNum][i].worldX = gp.tilesSize*18;
        gp.obj[mapNum][i].worldY = gp.tilesSize*13;
        i++;

        // OBJECT 8: BRONZE COIN
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*14;
        gp.obj[mapNum][i].worldY = gp.tilesSize*32;
        i++;

        // OBJECT 9: BRONZE COIN
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*10;
        gp.obj[mapNum][i].worldY = gp.tilesSize*33;
        i++;

        // OBJECT 10: BRONZE COIN
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*37;
        gp.obj[mapNum][i].worldY = gp.tilesSize*10;
        i++;

        // OBJECT 11: BRONZE COIN
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*25;
        gp.obj[mapNum][i].worldY = gp.tilesSize*23;
        i++;

        // OBJECT 12: BRONZE COIN
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*21;
        gp.obj[mapNum][i].worldY = gp.tilesSize*19;
        i++;

        // OBJECT 13: RED PORTION
        gp.obj[mapNum][i] = new OBJ_Potion_Red(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*34;
        gp.obj[mapNum][i].worldY = gp.tilesSize*8;
        i++;

        // OBJECT 14: RED PORTION
        gp.obj[mapNum][i] = new OBJ_Potion_Red(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*23;
        gp.obj[mapNum][i].worldY = gp.tilesSize*24;
        i++;

        // OBJECT 15: BRONZE COIN
        gp.obj[mapNum][i] = new OBJ_Coin_Bronze(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*23;
        gp.obj[mapNum][i].worldY = gp.tilesSize*7;
        i++;

        // OBJECT 16: BLUE SHIELD
        gp.obj[mapNum][i] = new OBJ_Shield_Blue(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*38;
        gp.obj[mapNum][i].worldY = gp.tilesSize*40;
        i++;

        mapNum = 2;
        // OBJECT 7: SPECIAL CHEST containing a KEY
        gp.obj[mapNum][i] = new Special_chest(gp, new OBJ_key(gp));
        gp.obj[mapNum][i].worldX = gp.tilesSize*27;
        gp.obj[mapNum][i].worldY = gp.tilesSize*15;
        i++;

        gp.obj[mapNum][i] = new OBJ_door(gp);
        gp.obj[mapNum][i].worldX = gp.tilesSize*30;
        gp.obj[mapNum][i].worldY = gp.tilesSize*19;
        i++;
    }

    // ---

    // ## Set NPCs (Non-Player Characters)
    public void setNPC() {
        // NPC for Map 0 (Starting Map)
        int mapNum = 0;
        int i = 0;
        gp.npc[mapNum][i] = new NPC_OldMan(gp);
        gp.npc[mapNum][i].worldX = gp.tilesSize*18;
        gp.npc[mapNum][i].worldY = gp.tilesSize*21;// Placed near the player's starting area
        i++;

        // NPC for Map 1 (e.g., a dedicated shop area)
        mapNum = 1;
        i = 0;
        gp.npc[mapNum][i] = new NPC_Merchant(gp);
        gp.npc[mapNum][i].worldX = gp.tilesSize*12;
        gp.npc[mapNum][i].worldY = gp.tilesSize*7;
    }

    // ---

    // ## Set Monsters
    public void setMonster() {
        int mapNum = 0;
        int i = 0;

        // Place multiple Green Slime monsters on Map 0
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*23;
        gp.monster[mapNum][i].worldY = gp.tilesSize*36;
        i++;

        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*23;
        gp.monster[mapNum][i].worldY = gp.tilesSize*42;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*24;
        gp.monster[mapNum][i].worldY = gp.tilesSize*37;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*34;
        gp.monster[mapNum][i].worldY = gp.tilesSize*42;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*33;
        gp.monster[mapNum][i].worldY = gp.tilesSize*22;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*35;
        gp.monster[mapNum][i].worldY = gp.tilesSize*20;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*37;
        gp.monster[mapNum][i].worldY = gp.tilesSize*10;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*12;
        gp.monster[mapNum][i].worldY = gp.tilesSize*32;
        i++;
        gp.monster[mapNum][i] = new MON_greenSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*13;
        gp.monster[mapNum][i].worldY = gp.tilesSize*30;
        i++;

        mapNum = 2;
        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*15;
        gp.monster[mapNum][i].worldY = gp.tilesSize*35;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*10;
        gp.monster[mapNum][i].worldY = gp.tilesSize*25;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*15;
        gp.monster[mapNum][i].worldY = gp.tilesSize*15;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*35;
        gp.monster[mapNum][i].worldY = gp.tilesSize*25;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*25;
        gp.monster[mapNum][i].worldY = gp.tilesSize*17;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*29;
        gp.monster[mapNum][i].worldY = gp.tilesSize*10;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*34;
        gp.monster[mapNum][i].worldY = gp.tilesSize*40;
        i++;

        gp.monster[mapNum][i] = new MON_redSlime(gp);
        gp.monster[mapNum][i].worldX = gp.tilesSize*16;
        gp.monster[mapNum][i].worldY = gp.tilesSize*25;
        i++;


    }

    // ---

    // ## Set Interactive Tiles (Destructible/Usable Tiles)
    public void setInteractiveTile(){
        int mapNum = 0;
        int i = 0;

        // Place rows of destructible Dry Trees (IT_DryTree) on Map 0

        // Row 1: Trees from column 27 to 33, row 12
        gp.iTile[mapNum][i] = new IT_DryTree(gp,27,12);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,28,12);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,29,12);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,30,12);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,31,12);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,32,12);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,33,12);i++;

        // Row 2: Cluster of trees around row 40/41
        gp.iTile[mapNum][i] = new IT_DryTree(gp,11,41);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,10,41);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,10,40);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,12,41);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,13,41);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,13,40);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,14,40);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,15,40);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,16,40);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,17,40);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,18,40);i++;

        // Row 3: Trees around row 29/30
        gp.iTile[mapNum][i] = new IT_DryTree(gp,30,29);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,30,30);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,29,30);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,28,30);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,27,30);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,26,30);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,25,30);i++;

        // Row 4: Trees around row 14/15
        gp.iTile[mapNum][i] = new IT_DryTree(gp,21,15);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,20,15);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,19,15);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,19,14);i++;
        gp.iTile[mapNum][i] = new IT_DryTree(gp,18,14);i++;

    }

}