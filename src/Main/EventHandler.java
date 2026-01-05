package Main;

import entity.Entity;
import object.OBJ_key;

import java.awt.*;

// EventHandler manages events (teleportation, healing pools, dialogue triggers)
public class EventHandler {
    GamePanel gp; // Reference to the main game panel

    // Array to hold the specific event rectangles for every tile on every map: [map][row][col]
    EventReact[][][] eventReact;

    // Variables to track the player's position after an event is triggered to prevent immediate re-triggering
    int previousEventX, previousEventY;
    boolean canTouchEvent = true; // Cooldown flag to manage event re-triggering

    // Temporary variables used to store the destination of a transition/teleport event
    int temRow, temCol, temMap;

    // Constructor: Initializes the EventReact array
    public EventHandler(GamePanel gp) {
        this.gp = gp;

        // Initialize the 3D array based on max map, world rows, and world columns
        eventReact = new EventReact[gp.maxMap][gp.MaxWorldRow][gp.maxWorldCol];
        int map = 0;
        int row = 0;
        int col = 0;

        // Loop through all possible tile positions across all maps
        while (map < gp.maxMap && row < gp.MaxWorldRow && col < gp.maxWorldCol) {
            eventReact[map][row][col] = new EventReact();

            // Set the default size and offset of the event hitbox (e.g., center 2x2 area)
            eventReact[map][row][col].x = 23; // X offset within the tile
            eventReact[map][row][col].y = 23; // Y offset within the tile
            eventReact[map][row][col].width = 2; // Width of the hitbox
            eventReact[map][row][col].height = 2; // Height of the hitbox

            // Store the default offsets for reset later
            eventReact[map][row][col].eventRectDefaultX = eventReact[map][row][col].x;
            eventReact[map][row][col].eventRectDefaultY = eventReact[map][row][col].y;

            // Increment loop counters
            col++;
            if (col == gp.maxWorldCol) {
                col = 0;
                row++;
                if (row == gp.MaxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }
    }

    // ## Main Event Check Loop
    public void CheckEvent() {
        // Calculate distance from the last triggered event location
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.min(xDistance, yDistance); // Use the shortest distance (simpler check)

        // Reset the event cooldown flag if the player has moved far enough (more than 1 tile size)
        if (distance > gp.tilesSize) {
            canTouchEvent = true;
        }

        // Only check for events if the cooldown is over
        if (canTouchEvent) {
            // Check 1: Teleport from Map 0 (Tile 39, 10) to Map 1
            if (hit(0, 39, 10, "any")) {
                teleport(1, 12, 13);
            }
            // Check 2: Teleport from Map 1 (Tile 13, 12) back to Map 0
            else if (hit(1, 13, 12, "any")) {
                teleport(0, 39, 10);
            }
            else if (hit(0, 9, 10, "any")) {
                teleport(2, 40, 9);
            }
            else if (hit(2, 40, 9, "any")) {
                teleport(0, 9, 10);
            }
            // Check 3: Healing Pool event (Map 0, Tile 12, 23) - requires standing on tile AND pressing Enter/Action key, and facing Up
            else if (hit(0, 12, 23, "Up") && gp.keyH.enterPressed) {
                healingPool(gp.dialogueState); // Trigger healing action
            }
            // Check 4: NPC dialogue trigger (Map 1, Tile 9, 12) - requires standing on tile AND pressing Enter/Action key, and facing Up
            else if (hit(1, 9, 12, "Up") && gp.keyH.enterPressed) {
                speak(gp.npc[1][0]); // Trigger dialogue with the first NPC on Map 1
            }
        }
    }

    // ## Collision Check for Event Tile
    // Checks if the player's solid area intersects with an event rectangle at a specific world tile.
    public boolean hit(int map, int row, int col, String reqDirection) {
        boolean hit = false;

        // Check only if the player is on the specified map
        if (map == gp.currentMap) {

            // 1. Get the player's current world collision box
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

            // 2. Calculate the event rectangle's actual world position
            EventReact er = eventReact[map][row][col];
            er.x = col * gp.tilesSize + er.x; // World X position of the event rectangle
            er.y = row * gp.tilesSize + er.y; // World Y position of the event rectangle

            // 3. Check for intersection and if the event hasn't been triggered yet
            if (gp.player.solidArea.intersects(er) && !er.eventDone) {
                // Check if the player's direction matches the required direction or "any"
                if (gp.player.direction.contentEquals(reqDirection) || reqDirection.contentEquals("any")) {
                    hit = true;
                    // Store the current player position for event cooldown check
                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            // 4. Reset the player's and event rectangle's collision boxes back to default offsets
            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            er.x = er.eventRectDefaultX;
            er.y = er.eventRectDefaultY;
        }

        return hit; // Returns true if conditions are met
    }

    // ## Event Action: Healing Pool
    public void healingPool(int gameState) {
        gp.gameState = gameState; // Change game state (e.g., to dialogue)
        gp.player.attackCanceled = true; // Prevent player from attacking while dialog is up
        gp.playSE(2); // Play healing sound effect
        gp.ui.currentDialogue = "You drink the water,\nYour life has been recovered!";
        gp.player.life = gp.player.maxLife; // Restore player life
        gp.player.mana = gp.player.maxMana; // Restore player mana
        gp.aSetter.setMonster(); // Reset (respawn/reposition) all monsters after healing (a common game mechanic)
    }

    // ## Event Action: Teleportation
    public void teleport(int map, int row, int col) {
        gp.gameState = gp.transitionState; // Switch to a transition screen (fade out/in)
        gp.currentMap = map;
        temMap = map; // Store target map
        temCol = col; // Store target column
        temRow = row; // Store target row
        canTouchEvent = false; // Immediately disable event check (handled by transition logic)
        gp.player.inventory.add(new OBJ_key(gp));
        gp.playSE(13); // Play teleport sound
        if (map == 2) {
            gp.player.lightUpdated = true;
        }

    }

    // ## Event Action: Dialogue Trigger
    public void speak(Entity entity){
        if(gp.keyH.enterPressed){
            gp.gameState = gp.dialogueState; // Switch to dialogue state
            gp.player.attackCanceled = true; // Prevent player from attacking
            entity.speak(); // Trigger the target entity's dialogue
        }
    }
}