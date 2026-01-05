package entity;
import Main.GamePanel;
import Main.UtilityTool;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

// Base class for all moving or interactable entities (Player, NPCs, monsters)
abstract public class Entity {

    // Position in the world (not screen coordinates)
    public int worldX, worldY;

    // Movement speed of the entity
    public int speed;

    // Sprites for entity animation (2 frames per direction)
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;

    // Sprites for attack animation
    public BufferedImage attackup1, attackup2, attackdown1, attackdown2, attackleft1, attackleft2,
            attackright1, attackright2;

    public BufferedImage defendUp, defendDown, defendLeft, defendRight;


    // Current direction entity is facing
    public String direction = "down";

    // Status flags
    public boolean alive = true;
    public boolean dying = false;
    boolean hpBarOn = false;
    public boolean onPath = false;
    public boolean guarding = false;
    public boolean transparent = false;

    // Inventory system
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int inventorySize = 20;

    // Animation and timing control
    public int spriteCounter = 0; // Counts frames for switching sprite
    public int spriteNum = 1;     // Current sprite frame (1 or 2)
    public int shotAvailableCounter = 0;
    int dyingCounter = 0;
    int hpBarCounter = 0;

    // Collision detection
    public Rectangle solidArea;
    public Rectangle attackArea = new Rectangle(0,0,0,0);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;
    public boolean collision = false; // Collision flag for obstacles/entities
    public int actionLookCounter = 0; // Counter for AI actions

    // Reference to game panel
    public GamePanel gp;

    // Dialogue system
    String[] dialogues = new String[20];
    public int dialogueIndex = 0;

    // Object images
    public BufferedImage image, image2, image3;
    public String name; // Name identifier

    // Character stats
    public int maxLife;
    public int life;
    public int maxMana;
    public int mana;
    public int ammo;
    public int level;
    public int strength;
    public int dexterity;
    public int attack;
    public int defense;
    public int exp;
    public int nextLevelExp;
    public int coin;

    // Equipped items
    public Entity currentWeapon;
    public Entity currentShield;
    public Entity currentLight;
    public Projectile projectile;

    // Item attributes
    public int value;
    public int attackValue;
    public int defenseValue;
    public String description = "";
    public int useCost;
    public int price;
    public boolean stackable = false;
    public int amount = 1;
    public int lightRadius;

    // Status effects
    public boolean invincible;
    public int invincibleCounter;
    boolean attacking = false;

    // Entity type constants
    public int type; // 0=player, 1=npc, 2=monster
    public final int type_player = 0;
    public final int type_npc = 1;
    public final int type_monster = 2;
    public final int type_sword = 3;
    public final int type_axe = 4;
    public final int type_shield = 5;
    public final int type_consumable = 6;
    public final int type_key = 8;
    public final int type_pickupOnly = 7;
    public final int type_interactiveTile = 9;
    public final int type_obstacle = 10;
    public final int type_light = 11;

    // Constructor
    public Entity(GamePanel gp) {
        this.gp = gp;
        this.solidArea = new Rectangle(0, 0, gp.tilesSize, gp.tilesSize); // default collision area
        this.solidAreaDefaultX = solidArea.x;
        this.solidAreaDefaultY = solidArea.y;
    }

    // Collision boundary getters
    public int getLeftX(){ return worldX + solidArea.x; }
    public int getRightX(){ return worldX + solidArea.x + solidArea.width; }
    public int getTopY(){ return worldY + solidArea.y; }
    public int getDownY(){ return worldY + solidArea.y + solidArea.height; }
    public int getCol(){ return (worldX + solidArea.x ) / gp.tilesSize; }
    public int getRow(){ return (worldY + solidArea.y ) / gp.tilesSize; }

    // Placeholder for subclass-specific actions
    public void setAction() {}
    public void damageReaction(){}

    // Dialogue system
    public void speak(){
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gp.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;

        // Face the player when speaking
        switch(gp.player.direction){
            case "Up": direction = "Down"; break;
            case "Down": direction = "Up"; break;
            case "Left": direction = "Right"; break;
            case "Right": direction = "Left"; break;
        }
    }
    public void interact(){}
    public void use(Entity entity){}
    public void checkDrop(){}

    // Drop item into the map
    public void dropItem(Entity droppedItem){
        for (int i = 0; i < gp.obj[1].length; i++){
            if (gp.obj[gp.currentMap][i] == null){
                gp.obj[gp.currentMap][i] = droppedItem;
                gp.obj[gp.currentMap][i].worldX = worldX;
                gp.obj[gp.currentMap][i].worldY = worldY;
                break;
            }
        }
    }

    // Particle effects (for attacks, damage, etc.)
    public Color getPaticleColor(){ return null; }
    public int getParticleSize(){ return 0; }
    public int getParticleSpeed(){ return 0; }
    public int getParticleMaxLife(){ return 0; }

    public void generateParticle(Entity generator, Entity target){
        Color color = generator.getPaticleColor();
        int size = generator.getParticleSize();
        int speed = generator.getParticleSpeed();
        int maxLife = generator.getParticleMaxLife();

        Particles p1 = new Particles(gp, target, color,-2,-1, size, speed, maxLife);
        Particles p2 = new Particles(gp, target, color,2,-1, size, speed, maxLife);
        Particles p3 = new Particles(gp, target, color,-2,1, size, speed, maxLife);
        Particles p4 = new Particles(gp, target, color,2,1, size, speed, maxLife);
        gp.particleList.add(p1);
        gp.particleList.add(p2);
        gp.particleList.add(p3);
        gp.particleList.add(p4);
    }

    // Collision checking with tiles, objects, entities, player
    public void checkCollison(){
        collisionOn = false;
        gp.cChecker.checkTile(this);
        gp.cChecker.checkObject(this,false);
        gp.cChecker.checkEntity(this, gp.npc);
        gp.cChecker.checkEntity(this, gp.monster);
        gp.cChecker.checkEntity(this, gp.iTile);
        boolean contactPLayer = gp.cChecker.checkPlayer(this);

        if(this.type == type_monster && contactPLayer){
            damagePlayer(attack);
        }
    }

    // Update entity each frame
    public void update() {
        setAction();
        checkCollison();

        // Movement if no collision
        if (!collisionOn) {
            switch (direction) {
                case "Up": worldY -= speed; break;
                case "Down": worldY += speed; break;
                case "Left": worldX -= speed; break;
                case "Right": worldX += speed; break;
            }
        }

        // Sprite animation
        spriteCounter++;
        if (spriteCounter > 24) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
        }

        // Invincibility timer
        if (invincible){
            invincibleCounter++;
            if(invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }

        // Projectile cooldown
        if (shotAvailableCounter < 30){
            shotAvailableCounter++;
        }
    }

    // Deal damage to player
    public void damagePlayer(int attack){
        if(!gp.player.invincible){
            gp.playSE(6);
            int damage = attack - gp.player.defense;

            String canGuard = getOppositeDirection(direction);
            if(gp.player.guarding && gp.player.direction.equals(canGuard)){
                damage = 0;
                gp.playSE(14);
            } else {
                gp.playSE(6);
                if(damage < 1){damage = 1;}
            }

            if(damage != 0){
                gp.player.transparent = true;
            }

            gp.player.life -= damage;
            gp.player.invincible = true;
        }
    }

    public String getOppositeDirection(String Direction){
        String OppositeDirection = "";
        switch (Direction) {
            case "Up": OppositeDirection = "Down"; break;
            case "Down": OppositeDirection = "Up"; break;
            case "Left": OppositeDirection = "Right"; break;
            case "Right": OppositeDirection = "Left"; break;
        }
        return OppositeDirection;
    }
    // Draw entity on screen
    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;

        // Draw only if object is within visible screen area
        if (worldX + gp.tilesSize > gp.player.worldX - gp.player.screenX &&
                worldX - gp.tilesSize < gp.player.worldX + gp.player.screenX &&
                worldY + gp.tilesSize > gp.player.worldY - gp.player.screenY &&
                worldY - gp.tilesSize < gp.player.worldY + gp.player.screenY) {

            // Objects like keys, axes, shields, etc.
            if (type == type_consumable
                    || type == type_axe
                    || type == type_shield
                    || type == type_key
                    || type == type_pickupOnly
                    || type == type_interactiveTile
                    || type == type_obstacle
                    || type == type_light) {
                image = down1;
            }

            // Player/NPC/Monster animation
            else {
                switch (direction) {
                    case "Up": image = (spriteNum == 1) ? up1 : up2; break;
                    case "Down": image = (spriteNum == 1) ? down1 : down2; break;
                    case "Left": image = (spriteNum == 1) ? left1 : left2; break;
                    case "Right": image = (spriteNum == 1) ? right1 : right2; break;
                }
            }

            // Monster HP bar
            if (type == 2 && hpBarOn) {
                double oneScale = (double)gp.tilesSize/maxLife;
                double hpBarValue = oneScale*life;

                g2.setColor(new Color(35, 35, 35));
                g2.fillRect(screenX-1, screenY - 16, gp.tilesSize+2, 12);

                g2.setColor(new Color(255, 0, 30));
                g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);

                hpBarCounter++;
                if (hpBarCounter > 600){
                    hpBarCounter = 0;
                    hpBarOn = false;
                }
            }

            // Invincibility visual effect
            if(invincible){
                hpBarOn = true;
                hpBarCounter = 0;
                changeAlpha(g2, 0.4f);
            }

            // Dying animation
            if (dying){
                dyingAnimation(g2);
            }

            g2.drawImage(image, screenX, screenY, null);
            changeAlpha(g2, 1f); // Reset alpha
        }
    }

    // Dying animation effect (blinking)
    public void dyingAnimation(Graphics2D g2){
        dyingCounter++;
        int i = 5;
        if (dyingCounter <= i) {changeAlpha(g2, 0f);}
        if (dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2, 0f);}
        if (dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2, 0f);}
        if (dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2, 0f);}
        if (dyingCounter > i*7 && dyingCounter <= i*8) {changeAlpha(g2, 1f);}
        if (dyingCounter > i*8) {
            alive = false;
        }
    }

    // Set transparency for drawing
    public void changeAlpha(Graphics2D g2, float alphaValue){
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    // Load and scale images
    public BufferedImage setup(String imagePath, int width, int height) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            var stream = getClass().getResourceAsStream(imagePath + ".png");
            if (stream == null) {
                System.out.println("Image not found: " + imagePath + ".png");
                return null;
            }
            image = ImageIO.read(stream);
            image = uTool.scaleImage(image, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    // Detect target entity in front of the user
    public int getDetected(Entity user, Entity[][] target, String target_name){
        int index = 999;
        int nextWorldX = user.getLeftX();
        int nextWorldY = user.getTopY();

        switch (user.direction) {
            case "Up": nextWorldY = user.getTopY() - gp.tilesSize; break;
            case "Down": nextWorldY = user.getDownY(); break;
            case "Left": nextWorldX = user.getLeftX() - gp.tilesSize; break;
            case "Right": nextWorldX = user.getRightX(); break;
        }

        int col = nextWorldX / gp.tilesSize;
        int row = nextWorldY / gp.tilesSize;

        for(int i = 0; i < target[gp.currentMap].length; i++){
            if(target[gp.currentMap][i] != null){
                if(target[gp.currentMap][i].getCol() == col &&
                        target[gp.currentMap][i].getRow() == row &&
                        Objects.equals(target[gp.currentMap][i].name, target_name)){
                    index = i;
                    break;
                }
            }
        }
        return index;
    }
}
