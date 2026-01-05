package entity;

// Import core game components
import Main.GamePanel;
import Main.KeyHandler;
// Import different object types the player can interact with/use
import object.*;
import java.awt.*;
import java.awt.image.BufferedImage;

// Player class handles movement, collision, animation, and object interactions
public class Player extends Entity {

    GamePanel gp;
    KeyHandler keyH;
    // Flag to cancel an attack animation/action (e.g., if interacting with an object)
    public boolean attackCanceled = false;
    public boolean lightUpdated = false;
    public int maxInventorySize; // Maximum number of items the player can hold
    public final int screenX; // Player fixed on screen X (center of the window)
    public final int screenY; // Player fixed on screen Y (center of the window)


    // Constructor to initialize the Player
    public Player(GamePanel gp, KeyHandler keyH) {
        super(gp); // Call Entity class constructor
        this.gp = gp;
        this.keyH = keyH;

        // Calculate and set Player's fixed position on the screen (center)
        screenX = gp.screenwidth / 2 - (gp.tilesSize / 2);
        screenY = gp.screenheight / 2 - (gp.tilesSize / 2);

        // Collision rectangle setup
        solidArea = new Rectangle();
        solidArea.x = 8; // X offset within the tile
        solidArea.y = 16; // Y offset within the tile
        solidAreaDefaultX = solidArea.x; // Store default X for reset
        solidAreaDefaultY = solidArea.y; // Store default Y for reset
        solidArea.width = 32;
        solidArea.height = 32;

        setDefaultValues(); // Set initial stats, position, and items
        getPlayerImage();   // Load player movement sprites
        getPlayerAttackImage(); // Load player attack sprites
        getPlayerDefenseImage();
        setItems(); // Initialize inventory with starter items
    }

    // Sets the player's starting world position
    public void setDefaultPosition(){
        worldX = gp.tilesSize * 23;
        worldY = gp.tilesSize * 21;
        direction = "Down";
    }

    // Resets player's life and mana and sets invincibility to false
    public void setLifeAndMan(){
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }

    // Set initial player position, speed, direction, and stats
    public void setDefaultValues() {
        // Initial World Position
        worldX = gp.tilesSize * 23;
        worldY = gp.tilesSize * 21;
        gp.currentMap = 0; // Starting map index
        speed = 4;
        direction = "Down";

        // Player Status (Stats)
        level = 1;
        maxLife = 6;
        life = maxLife;
        maxMana = 4;
        mana = maxMana;
        ammo = 10;
        strength = 1; // Affects damage output
        dexterity = 1; // Affects damage resistance
        exp = 0;
        nextLevelExp = 5;
        coin = 100;
        maxInventorySize = 20; // Explicitly set inventory size (was missing before)

        // Initial Equipment
        currentWeapon = new OBJ_Sword_Normal(gp);
        currentShield = new OBJ_Shield_Wood(gp);
        projectile = new OBJ_Fireball(gp);
        //  projectile = new OBJ_Rock(gp); // Alternate projectile option

        // Calculate initial attack and defense based on stats and equipment
        attack = getAttack(); // The total attack value is decided by strength and weapon
        defense = getDefense(); // The total defense value is decided by dexterity and shields
    }

    // Populates the player's initial inventory
    public void setItems() {
        inventory.clear();
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new OBJ_key(gp)); // Add a key to the inventory
    }

    // Calculates and returns the total attack value
    public int getAttack(){
        attackArea = currentWeapon.attackArea; // Set the attack hit-box size
        // Calculate attack: Strength * Weapon's attack value
        return attack = strength * currentWeapon.attackValue;
    }

    // Calculates and returns the total defense value
    public int getDefense(){
        // Calculate defense: Strength * Shield's defense value (Note: Uses strength, not dexterity in this implementation)
        return defense = strength * currentShield.defenseValue;
    }

    // Load all player sprites for movement animations
    public void getPlayerImage() {
        // Load walking sprites for each direction
        up1 = setup("/play_pic/boy_up_1",gp.tilesSize,gp.tilesSize);
        up2 = setup("/play_pic/boy_up_2",gp.tilesSize,gp.tilesSize);
        down1 = setup("/play_pic/boy_down_1",gp.tilesSize,gp.tilesSize);
        down2 = setup("/play_pic/boy_down_2",gp.tilesSize,gp.tilesSize);
        right1 = setup("/play_pic/boy_right_1",gp.tilesSize,gp.tilesSize);
        right2 = setup("/play_pic/boy_right_2",gp.tilesSize,gp.tilesSize);
        left1 = setup("/play_pic/boy_left_1",gp.tilesSize,gp.tilesSize);
        left2 = setup("/play_pic/boy_left_2",gp.tilesSize,gp.tilesSize);
    }

    // Load attack sprites based on the currently equipped weapon type
    public void getPlayerAttackImage() {

        // Load sword attack sprites
        if (currentWeapon.type == type_sword){
            // Note: Vertical attacks are twice the tile size in height
            attackup1 = setup("/play_pic/boy_attack_up_1",gp.tilesSize,gp.tilesSize*2);
            attackup2 = setup("/play_pic/boy_attack_up_2",gp.tilesSize,gp.tilesSize*2);
            attackdown1 = setup("/play_pic/boy_attack_down_1",gp.tilesSize,gp.tilesSize*2);
            attackdown2 = setup("/play_pic/boy_attack_down_2",gp.tilesSize,gp.tilesSize*2);
            // Horizontal attacks are normal tile size
            attackright1 = setup("/play_pic/boy_attack_right_1",gp.tilesSize,gp.tilesSize);
            attackright2 = setup("/play_pic/boy_attack_right_2",gp.tilesSize,gp.tilesSize);
            attackleft1 = setup("/play_pic/boy_attack_left_1",gp.tilesSize,gp.tilesSize);
            attackleft2 = setup("/play_pic/boy_attack_left_2",gp.tilesSize,gp.tilesSize);
        }
        // Load axe attack sprites
        if (currentWeapon.type == type_axe){
            // Note: Vertical attacks are twice the tile size in height
            attackup1 = setup("/play_pic/boy_axe_up_1",gp.tilesSize,gp.tilesSize*2);
            attackup2 = setup("/play_pic/boy_axe_up_2",gp.tilesSize,gp.tilesSize*2);
            attackdown1 = setup("/play_pic/boy_axe_down_1",gp.tilesSize,gp.tilesSize*2);
            attackdown2 = setup("/play_pic/boy_axe_down_2",gp.tilesSize,gp.tilesSize*2);
            // Horizontal attacks are normal tile size
            attackright1 = setup("/play_pic/boy_axe_right_1",gp.tilesSize,gp.tilesSize);
            attackright2 = setup("/play_pic/boy_axe_right_2",gp.tilesSize,gp.tilesSize);
            attackleft1 = setup("/play_pic/boy_axe_left_1",gp.tilesSize,gp.tilesSize);
            attackleft2 = setup("/play_pic/boy_axe_left_2",gp.tilesSize,gp.tilesSize);
        }

    }

    public void getPlayerDefenseImage() {

        // Load sword attack sprites
        if (currentShield.type == type_shield){
            // Note: Vertical attacks are twice the tile size in height
            defendUp = setup("/play_pic/boy_guard_up",gp.tilesSize,gp.tilesSize);
            defendDown = setup("/play_pic/boy_guard_down",gp.tilesSize,gp.tilesSize);
            // Horizontal attacks are normal tile size
            defendRight = setup("/play_pic/boy_guard_right",gp.tilesSize,gp.tilesSize);
            defendLeft= setup("/play_pic/boy_guard_left",gp.tilesSize,gp.tilesSize);
        }
    }
    // Overloaded setup method for simple tile-sized images
    public BufferedImage setup(String imageName) {
        // Use the common setup method with standard tile size
        return setup(imageName, gp.tilesSize, gp.tilesSize);
    }

    // Update player position, collision, animation, and state
    public void update() {

        // Handle the attack sequence if the player is currently attacking
        if(attacking){
            attacking(); // Progress the attack animation/hitbox
            // Check for collision with interactive tiles during an attack
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile);
            damageinteractivetile(iTileIndex); // Damage the tile if found
        } else if (keyH.defensePressed) {
            guarding = true;
        }
        // Handle movement or interaction if not attacking
        else if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {

            // Set direction based on pressed key
            if (keyH.upPressed) direction = "Up";
            else if (keyH.downPressed) direction = "Down";
            else if (keyH.leftPressed) direction = "Left";
            else if (keyH.rightPressed) direction = "Right";

            // Check collision with solid tiles
            collisionOn = false;
            gp.cChecker.checkTile(this);

            // Check collision with objects and pick up if applicable
            int objIndex = gp.cChecker.checkObject(this, true); // Player is the caller, check pick-up
            pickUpObject(objIndex);


            // CHECK NPC COLLISION and interaction
            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            // Check monster collision (for contact damage)
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            // Check interactive tile collision (for interaction, e.g., using an axe on a tree)
            int iTileIndex = gp.cChecker.checkEntity(this, gp.iTile); // Not used for movement, only for events/interaction logic

            // Check for map events (e.g., transitions, traps)
            gp.eHandler.CheckEvent();


            // Move player if no collision AND not initiating an interaction/attack
            if (!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "Up":
                        worldY -= speed;
                        break;
                    case "Down":
                        worldY += speed;
                        break;
                    case "Left":
                        worldX -= speed;
                        break;
                    case "Right":
                        worldX += speed;
                        break;
                }
            }

            // Start an attack sequence if Enter is pressed and not canceled
            if (keyH.enterPressed && !attackCanceled){
                gp.playSE(7); // Play attack sound effect
                attacking = true;
                spriteCounter = 0; // Reset counter for attack animation
            }

            // Reset flags after handling input
            attackCanceled = false;
            gp.keyH.enterPressed = false;
            guarding = false;

            // Handle sprite animation for movement
            spriteCounter++;
            if (spriteCounter > 12) { // Change sprite every 12 frames
                if (spriteNum == 1) {
                    spriteNum = 2;
                }else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }
        } else {
            spriteCounter++;
            if (spriteCounter == 20) {
                spriteNum = 1;
                spriteCounter = 0;
            }
            guarding = false;
        }

        // Handle projectile firing (e.g., fireball)
        if (gp.keyH.shotKeyPressed && !projectile.alive // Key pressed and previous projectile is gone
                && shotAvailableCounter == 30 && projectile.haveResource(this)){ // Rate limit and resource check
            // Set up the projectile's initial state
            projectile.set(worldX, worldY, direction, true, this);

            // Subtract the cost (mana, ammo, etc.)
            projectile.subtractResource(this);
            // Add the new projectile to the game's list
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0; // Reset shot timer
            gp.playSE(10); // Play shot sound effect
        }

        // Invincibility frame management (needs to be outside the movement/interaction block)
        if (invincible){
            invincibleCounter++;
            if(invincibleCounter > 60) { // 60 frames = 1 second of invincibility
                invincible = false;
                transparent = false;
                invincibleCounter = 0;
            }
        }

        // Projectile cooldown timer
        if (shotAvailableCounter < 30){
            shotAvailableCounter++;
        }

        // Cap life and mana at their maximum values
        if (life > maxLife){
            life = maxLife;
        }
        if (mana > maxMana){
            mana = maxMana;
        }

        // Handle player death
        if(life <= 0){
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(12); // Play game over sound
        }
    }

    // Handle picking up or interacting with objects
    public void pickUpObject(int i) {
        if (i != 999) { // 999 means no object found

            // Handle pickup-only items (e.g., hearts, coins)
            if (gp.obj[gp.currentMap][i].type == type_pickupOnly){
                gp.obj[gp.currentMap][i].use(this); // Apply effect (e.g., heal, gain coin)
                gp.obj[gp.currentMap][i] = null; // Remove object from map
            }
            // Handle obstacle objects (e.g., doors, chests that need interaction)
            else if (gp.obj[gp.currentMap][i].type == type_obstacle) {
                if(gp.keyH.enterPressed){
                    attackCanceled = true; // Prevent attack when interacting with obstacle
                    gp.obj[gp.currentMap][i].interact(); // Call obstacle's interaction logic
                }

            }
            // Handle regular inventory items (e.g., weapons, shields, keys)
            else {
                String text;
                // Check if inventory has space
                if (canObtainItem(gp.obj[gp.currentMap][i])){
                    gp.playSE(1); // Play pickup sound
                    text = "Got a " + gp.obj[gp.currentMap][i].name + "!";
                }
                // Inventory is full
                else {
                    text = "You cannot carry any more!";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null; // Remove object from map after interaction/pickup
            }
        }
    }

    // Handle interaction with Non-Player Characters (NPCs)
    public void interactNPC(int i) {
        if (gp.keyH.enterPressed){ // Interaction only happens on Enter press
            if (i != 999) { // 999 means no NPC found
                attackCanceled = true; // Prevent attack during dialogue
                gp.gameState = gp.dialogueState; // Switch to dialogue state
                gp.npc[gp.currentMap][i].speak(); // Start the NPC's dialogue
            }
        }
    }

    // Draw player on screen based on current direction and sprite
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;

        // Determine the correct image based on direction and state (moving/attacking)
        switch (direction) {
            case "Up":
                image = (!attacking)
                        ? ((spriteNum == 1) ? up1 : up2) // Movement sprites
                        : ((spriteNum == 1) ? attackup1 : attackup2); // Attack sprites
                if(guarding){
                    image = defendUp;
                }
                break;

            case "Down":
                image = (!attacking)
                        ? ((spriteNum == 1) ? down1 : down2)
                        : ((spriteNum == 1) ? attackdown1 : attackdown2);
                if(guarding){
                    image = defendDown;
                }
                break;
            case "Left":
                image = (!attacking)
                        ? ((spriteNum == 1) ? left1 : left2)
                        : ((spriteNum == 1) ? attackleft1 : attackleft2);
                if(guarding){
                    image = defendLeft;
                }
                break;
            case "Right":
                image = (!attacking)
                        ? ((spriteNum == 1) ? right1 : right2)
                        : ((spriteNum == 1) ? attackright1 : attackright2);
                if(guarding){
                    image = defendRight;
                }
                break;
        }

        // Apply a translucent effect if the player is invincible (blinking)
        if(transparent){
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        // Draw the player image
        g2.drawImage(image, tempScreenX, tempScreenY,null);

        // Reset the alpha composite back to opaque
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }

    // Apply damage to player when colliding with a monster
    public void contactMonster(int i) {
        if (i != 999) { // Monster detected
            // Only take damage if not invincible and monster is not dying
            if (!invincible && !gp.monster[gp.currentMap][i].dying) {
                gp.playSE(6); // Play hit sound

                // Calculate damage taken (Monster Attack - Player Defense)
                int damage = gp.monster[gp.currentMap][i].attack - defense;
                if(damage < 1){
                    damage = 1; // Minimum damage is 0
                }
                life -= damage;
                invincible = true; // Start invincibility frames
                transparent = true;
            }
        }
    }

    // Apply damage to a monster from the player's attack
    public void damageMonster(int i, int attack) {
        if (i != 999) { // Monster detected
            if(!gp.monster[gp.currentMap][i].invincible){ // Check if monster is not in i-frames
                gp.playSE(5); // Play monster hit sound

                // Calculate damage dealt (Player Attack - Monster Defense)
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage < 0){damage = 0;} // Minimum damage is 0

                gp.monster[gp.currentMap][i].life -= damage;
                gp.ui.addMessage(damage + " damage!"); // Display damage message

                // Start displaying monster HP bar
                gp.monster[gp.currentMap][i].hpBarOn = true;
                gp.monster[gp.currentMap][i].hpBarCounter = 0;

                gp.monster[gp.currentMap][i].invincible = true; // Start monster invincibility
                gp.monster[gp.currentMap][i].damageReaction(); // Trigger monster's reaction to damage

                // Handle monster death
                if(gp.monster[gp.currentMap][i].life <=0){
                    gp.monster[gp.currentMap][i].dying = true;
                    gp.ui.addMessage("Killed " + gp.monster[gp.currentMap][i].name + "!");
                    gp.ui.addMessage("Gain Exp + " + gp.monster[gp.currentMap][i].exp);
                    exp += gp.monster[gp.currentMap][i].exp; // Gain experience
                    checkLevelUp(); // Check for level up
                }
            }
        }
    }

    // Apply damage to an interactive tile from the player's attack
    public void damageinteractivetile(int i){
        if (i != 999 // Tile exists
                && gp.iTile[gp.currentMap][i].destructible // Tile can be destroyed
                && gp.iTile[gp.currentMap][i].isCorrectItem(this) // Player has the correct tool (e.g., axe)
                && !gp.iTile[gp.currentMap][i].invincible ){ // Tile is not in invincibility frames

            gp.iTile[gp.currentMap][i].playSE(); // Play tile interaction sound
            gp.iTile[gp.currentMap][i].life--; // Decrease tile life
            gp.iTile[gp.currentMap][i].invincible = true; // Start tile invincibility

            // Generate particle effects on the tile
            generateParticle(gp.iTile[gp.currentMap][i], gp.iTile[gp.currentMap][i]);

            // Handle tile destruction (life reaches 0)
            if (gp.iTile[gp.currentMap][i].life == 0){
                // Replace the destroyed tile with its "destroyed form" (e.g., a stump)
                gp.iTile[gp.currentMap][i] = gp.iTile[gp.currentMap][i].getDestroyedForm();
            }
        }
    }

    // Checks if the player has enough experience to level up
    public void checkLevelUp(){
        if(exp >= nextLevelExp){
            level ++; // Increase level
            nextLevelExp = nextLevelExp*2; // Double the experience required for the next level
            maxLife += 2; // Increase max health
            strength++; // Increase strength
            dexterity++; // Increase dexterity
            attack = getAttack(); // Recalculate attack power
            defense = getDefense(); // Recalculate defense power

            gp.playSE(8); // Play level up sound
            gp.gameState = gp.dialogueState; // Switch to dialogue state
            gp.ui.currentDialogue = "Congrats! You are now on level "+ level +"!\n" +
                    "Feeling stronger, right?";
        }
    }

    // Handles item selection logic in the inventory screen
    public void selectItem(){
        // Get the index of the item in the inventory list based on the UI slot position
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.slotCol,gp.ui.slotRow);

        // Check if the slot contains a valid item
        if (itemIndex < inventory.size()){
            Entity selectedItem = inventory.get(itemIndex);

            // Equip weapon
            if (selectedItem.type == type_sword || selectedItem.type == type_axe){
                currentWeapon = selectedItem;
                attack = getAttack(); // Recalculate attack
                getPlayerAttackImage(); // Load new weapon sprites
            }

            // Equip shield
            if (selectedItem.type == type_shield){
                currentShield = selectedItem;
                defense = getDefense(); // Recalculate defense
            }

            if(selectedItem.type == type_light){
                if(currentLight == selectedItem){
                    currentLight = null;
                } else {
                    currentLight = selectedItem;
                }
                lightUpdated = true;
            }

            // Consume item (e.g., potion)
            if (selectedItem.type == type_consumable){
                selectedItem.use(this); // Apply consumable effect
                if(selectedItem.amount > 1){
                    selectedItem.amount--; // Remove consumed item from inventory
                } else {
                    inventory.remove(itemIndex);
                }
            }
        }
    }

    // Manages the player's attack animation and hit detection
    public void attacking(){
        spriteCounter++; // Advance attack animation frame

        // First part of the attack animation (wind-up)
        if (spriteCounter <= 5) { // Frame 1
            spriteNum = 1;
        }

        // Second part of the attack animation (swing/active frames)
        if (spriteCounter > 5 && spriteCounter <=25) { // Frames 2-25
            spriteNum = 2;

            // Store original position/solid area for reset
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;

            // Adjust player's worldX/Y and solidArea to match the attack hit-box position/size
            switch (direction) {
                case "Up": worldY -= attackArea.height;break;
                case "Down": worldY += attackArea.height;break;
                case "Left": worldX -= attackArea.width;break;
                case "Right": worldX += attackArea.width;break;
            }

            // Set the solidArea to the weapon's attack area for collision check
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;

            // Check for monster hit
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack); // Damage the monster if found

            // Check for interactive tile hit
            int iTileIndex = gp.cChecker.checkEntity(this,gp.iTile);
            // Note: damageinteractivetile(iTileIndex) is called in the main update loop

            // Restore player's original world position and solid area
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }

        // Third part of the attack animation (finish/reset)
        if (spriteCounter > 25) { // Frame 26+
            spriteNum = 1; // Reset sprite frame
            spriteCounter = 0; // Reset counter
            attacking = false; // End attack sequence
        }

    }

    // Counts how many items of a specific name the player has
    public int inventoryCount(String itemName){
        int count = 0;
        for(Entity e : inventory){
            if(e != null && e.name.equals(itemName)){
                count++;
            }
        }
        return count;
    }

    // Removes a specified amount of an item from the inventory
    public void removeItem(String itemName, int amount){
        int removed = 0;
        // Iterate through inventory to find and remove items
        for(int i = 0; i < inventory.size(); i++){
            if(removed == amount) break; // Stop when required amount is removed
            if(inventory.get(i).name.equals(itemName)){
                inventory.remove(i);
                removed++;
                i--; // Decrement index to re-check the current position after removal
            }
        }
    }

    public int searchItemInventory(String itemName){
        int itemIndex = 999;
        for(int i = 0; i < inventory.size(); i++){
            if(inventory.get(i).name.equals(itemName)){
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }

    public boolean canObtainItem(Entity item){
        boolean canObtain = false;

        if(item.stackable == true){
            int index =  searchItemInventory(item.name);
            if(index != 999){
                inventory.get(index).amount++;
                canObtain = true;
            } else {
                if(inventory.size() != maxInventorySize){
                    inventory.add(item);
                    canObtain = true;
                }
            }
        }
        return canObtain;
    }
}