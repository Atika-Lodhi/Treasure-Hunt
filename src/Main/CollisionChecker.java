package Main;

import entity.Entity;
import java.awt.Rectangle;

// This class handles collision detection for entities (player, NPCs, monsters, projectiles)
public class CollisionChecker {
    GamePanel gp; // Reference to main game panel

    // Constructor
    public CollisionChecker(GamePanel gp) {
        this.gp = gp;
    }

    // ---

    // ## Check Tile Collision
    // Checks if an entity will collide with a solid tile in its next movement step.
    public void checkTile(Entity entity) {
        entity.collisionOn = false; // Assume no collision initially

        // 1. Calculate the entity's current boundary in world coordinates
        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        // 2. Determine the current tile columns and rows the entity occupies
        int entityLeftCol = entityLeftWorldX / gp.tilesSize;
        int entityRightCol = entityRightWorldX / gp.tilesSize;
        int entityTopRow = entityTopWorldY / gp.tilesSize;
        int entityBottomRow = entityBottomWorldY / gp.tilesSize;

        int tileNum1, tileNum2; // Variables to hold the two tiles checked (e.g., top-left and top-right corners)

        // 3. Predict collision based on movement direction
        switch (entity.direction) {
            case "Up":
                // Calculate the row the entity will enter next
                entityTopRow = (entityTopWorldY - entity.speed) / gp.tilesSize;
                if (entityTopRow < 0) entityTopRow = 0; // safety check for map boundary

                // Get the tile numbers for the two tiles above the entity's left and right sides
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityTopRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityTopRow][entityRightCol];

                // Check if either tile has collision set to true
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;

            case "Down":
                // Calculate the row the entity will enter next
                entityBottomRow = (entityBottomWorldY + entity.speed) / gp.tilesSize;
                if (entityBottomRow >= gp.MaxWorldRow) entityBottomRow = gp.MaxWorldRow - 1; // safety check

                // Get the tile numbers for the two tiles below the entity's left and right sides
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityBottomRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityBottomRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;

            case "Left":
                // Calculate the column the entity will enter next
                entityLeftCol = (entityLeftWorldX - entity.speed) / gp.tilesSize;
                if (entityLeftCol < 0) entityLeftCol = 0;

                // Get the tile numbers for the two tiles to the left of the entity's top and bottom sides
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityTopRow][entityLeftCol];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityBottomRow][entityLeftCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;

            case "Right":
                // Calculate the column the entity will enter next
                entityRightCol = (entityRightWorldX + entity.speed) / gp.tilesSize;
                if (entityRightCol >= gp.maxWorldCol) entityRightCol = gp.maxWorldCol - 1;

                // Get the tile numbers for the two tiles to the right of the entity's top and bottom sides
                tileNum1 = gp.tileM.mapTileNum[gp.currentMap][entityTopRow][entityRightCol];
                tileNum2 = gp.tileM.mapTileNum[gp.currentMap][entityBottomRow][entityRightCol];
                if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    entity.collisionOn = true;
                break;
        }
    }

    // ---

    // ## Check Object Collision
    // Checks collision between an entity and objects in the world.
    // 'player' boolean indicates if the entity is the player (affects return value).
    public int checkObject(Entity entity, boolean player) {
        int index = 999; // Default index if no collision (used for interaction/pickup)

        // Iterate through all objects on the current map
        for (int i = 0; i < gp.obj[1].length; i++) {
            if (gp.obj[gp.currentMap][i] != null) {

                // 1. Create a collision rectangle representing the entity's solid area
                Rectangle entityArea = new Rectangle(
                        entity.worldX + entity.solidArea.x,
                        entity.worldY + entity.solidArea.y,
                        entity.solidArea.width,
                        entity.solidArea.height
                );

                // 2. Create a collision rectangle representing the object's solid area
                Rectangle objectArea = new Rectangle(
                        gp.obj[gp.currentMap][i].worldX + gp.obj[gp.currentMap][i].solidArea.x,
                        gp.obj[gp.currentMap][i].worldY + gp.obj[gp.currentMap][i].solidArea.y,
                        gp.obj[gp.currentMap][i].solidArea.width,
                        gp.obj[gp.currentMap][i].solidArea.height
                );

                // 3. Predict the entity's next position
                switch (entity.direction) {
                    case "Up": entityArea.y -= entity.speed; break;
                    case "Down": entityArea.y += entity.speed; break;
                    case "Left": entityArea.x -= entity.speed; break;
                    case "Right": entityArea.x += entity.speed; break;
                }

                // 4. Check for intersection at the predicted position
                if (entityArea.intersects(objectArea)) {
                    if (gp.obj[gp.currentMap][i].collision) entity.collisionOn = true; // Stop movement if object is solid
                    if (player) index = i; // Return the object index if the entity is the player
                }
            }
        }

        return index; // returns collided object index or 999 if none
    }

    // ---

    // ## Check Entity Collision (NPCs, Monsters, Interactive Tiles)
    // Checks collision between a moving entity and a dynamic array of target entities (e.g., entity vs. monsters).
    public int checkEntity(Entity entity, Entity[][] target) {

        int index = 999; // Default index if no collision

        // Iterate through all target entities on the current map
        for (int i = 0; i < target[1].length; i++) {

            if (target[gp.currentMap][i] != null) {

                // 1. Store default solid area offsets (to restore later)
                int entityDefaultX = entity.solidArea.x;
                int entityDefaultY = entity.solidArea.y;
                int targetDefaultX = target[gp.currentMap][i].solidArea.x;
                int targetDefaultY = target[gp.currentMap][i].solidArea.y;

                // 2. Convert solid area offsets to actual World Collision Boxes for checking
                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;
                target[gp.currentMap][i].solidArea.x = target[gp.currentMap][i].worldX + target[gp.currentMap][i].solidArea.x;
                target[gp.currentMap][i].solidArea.y = target[gp.currentMap][i].worldY + target[gp.currentMap][i].solidArea.y;

                // 3. Predict the moving entity's next position (temporarily move its collision box)
                switch (entity.direction) {
                    case "Up": entity.solidArea.y -= entity.speed; break;
                    case "Down": entity.solidArea.y += entity.speed; break;
                    case "Left": entity.solidArea.x -= entity.speed; break;
                    case "Right": entity.solidArea.x += entity.speed; break;
                }

                // 4. Check for intersection
                if (entity.solidArea.intersects(target[gp.currentMap][i].solidArea)) {
                    // Prevent an entity from colliding with itself (e.g., if checking an NPC array that contains the moving NPC)
                    if(target[gp.currentMap][i] != entity){
                        entity.collisionOn = true; // Stop the moving entity
                        index = i; // Return the index of the entity collided with
                    }
                }

                // 5. Restore collision area offsets to their default state
                entity.solidArea.x = entityDefaultX;
                entity.solidArea.y = entityDefaultY;
                target[gp.currentMap][i].solidArea.x = targetDefaultX;
                target[gp.currentMap][i].solidArea.y = targetDefaultY;
            }
        }

        return index;
    }

    // ---

    // ## Check Player Collision (For non-player entities checking against the player)
    // Checks collision between a moving entity (e.g., a monster or projectile) and the player.
    public boolean checkPlayer(Entity entity) {
        boolean contactPlayer = false;

        // 1. Store default solid area offsets
        int entityDefaultX = entity.solidArea.x;
        int entityDefaultY = entity.solidArea.y;
        int playerDefaultX = gp.player.solidArea.x;
        int playerDefaultY = gp.player.solidArea.y;

        // 2. Convert solid area offsets to actual World Collision Boxes
        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        // 3. Predict the entity's next position
        switch (entity.direction) {
            case "Up": entity.solidArea.y -= entity.speed; break;
            case "Down": entity.solidArea.y += entity.speed; break;
            case "Left": entity.solidArea.x -= entity.speed; break;
            case "Right": entity.solidArea.x += entity.speed; break;
        }

        // 4. Check for intersection
        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true; // Stop the non-player entity (optional, depending on entity type)
            contactPlayer = true; // Flag contact
        }

        // 5. Restore collision area offsets
        entity.solidArea.x = entityDefaultX;
        entity.solidArea.y = entityDefaultY;
        gp.player.solidArea.x = playerDefaultX;
        gp.player.solidArea.y = playerDefaultY;

        return contactPlayer;
    }
}