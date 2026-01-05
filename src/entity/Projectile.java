package entity;

// Import the main game panel
import Main.GamePanel;

// Projectile class, inherits properties from Entity
public class Projectile extends Entity{

    public Entity user; // The entity that fired this projectile (e.g., Player or Monster)

    // Constructor: Calls the parent Entity constructor
    public Projectile(GamePanel gp){
        super(gp);
    }

    // Initializes the projectile's state and position
    public void set(int worldX, int worldY, String  direction, boolean alive, Entity user){

        this.worldX = worldX; // Set starting world X coordinate
        this.worldY = worldY; // Set starting world Y coordinate
        this.direction = direction; // Set direction of travel
        this.alive = alive; // Set initial alive status (should be true when fired)
        this.user = user; // Set the entity that created it
        this.life = this.maxLife; // Reset current life/range to maximum
    }

    // Handles the projectile's movement, collision, and life cycle each frame
    public void update(){

        // --- Collision Logic (Player's Projectile) ---
        if (user == gp.player) {
            // Check if the projectile hits a monster
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            if (monsterIndex != 999){ // Monster found
                // Damage the monster using the Player's damage method
                gp.player.damageMonster(monsterIndex, attack);
                // Generate particle effect at the monster's location
                generateParticle(user.projectile, gp.monster[gp.currentMap][monsterIndex]);
                alive = false; // Destroy the projectile upon hit
            }
        }

        // --- Collision Logic (Monster's Projectile) ---
        if (user != gp.player){
            // Check if the projectile hits the player
            boolean contactPlayer = gp.cChecker.checkPlayer(this);
            // Check if player is not invincible and contact occurred
            if (gp.player.invincible == false && contactPlayer == true){
                damagePlayer(attack); // Damage the player (method inherited from Entity)
                // Generate particle effect at the player's location
                generateParticle(user.projectile, gp.player);
                alive = false; // Destroy the projectile upon hit
            }
        }

        // --- Movement ---
        switch (direction) {
            case "Up": worldY -= speed; break;
            case "Down": worldY += speed; break;
            case "Left": worldX -= speed; break;
            case "Right": worldX += speed; break;
        }

        // --- Life and Duration Management ---
        life--; // Decrease projectile life/range counter
        if (life <= 0){
            alive = false; // Projectile dies when life runs out
        }

        // --- Animation ---
        spriteCounter++;
        if (spriteCounter > 12){ // Change sprite every 12 frames
            if (spriteNum == 1){
                spriteNum = 2; // Switch to sprite 2
            }
            else if (spriteNum == 2){
                spriteNum = 1; // Switch back to sprite 1
            }
            spriteCounter = 0; // Reset counter
        }
    }

    // Check if the user has the resources (mana, ammo, etc.) to fire the projectile
    public boolean haveResource(Entity user){
        boolean haveResource = false;
        return haveResource; // Default implementation always returns false (must be overridden)
    }

    // Subtract the required resources from the user after firing
    public void subtractResource(Entity user){} // Default empty implementation (must be overridden)
}