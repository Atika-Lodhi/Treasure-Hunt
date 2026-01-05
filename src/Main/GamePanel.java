package Main;

import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import environment.Lighting;
import tile.TileManager;
import tile.tile_interactive.interactiveTile;

import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// GamePanel serves as the main game surface and runs the game loop
public class GamePanel extends JPanel implements Runnable {

    // --- SCREEN & TILE SETTINGS ---
    final int originalTilesSize = 16;   // Base 16x16 tile size
    final int scale = 3;
    public final int tilesSize = originalTilesSize * scale; // Final tile size (48x48 pixels)

    // SCREEN RESOLUTION
    public final int maxScreenCol = 16;
    public final int MaxScreenRow = 12;
    public final int screenwidth = tilesSize * maxScreenCol;   // 768 pixels
    public final int screenheight = tilesSize * MaxScreenRow;  // 576 pixels

    // WORLD MAP SETTINGS
    public final int maxWorldCol = 50;
    public final int MaxWorldRow = 50;
    public final int maxMap = 10; // Maximum number of maps/areas the game can handle
    public int currentMap = 0; // The map currently being played
    public Lighting lighting;

    // FPS (Frame Rate)
    int FPS = 60;

    // --- CORE GAME COMPONENTS ---
    public TileManager tileM = new TileManager(this); // Handles tile loading and drawing
    public KeyHandler keyH = new KeyHandler(this);    // Handles keyboard input
    Sound music = new Sound();                // Handles background music
    Sound se = new Sound();                   // Handles sound effects

    public CollisionChecker cChecker = new CollisionChecker(this); // Handles entity/tile collision
    public UI ui = new UI(this);           // Manages user interface drawing
    public EventHandler eHandler = new EventHandler(this); // Manages event triggers (teleports, healing)
    EnvironmentManager eManager = new EnvironmentManager(this);
    Config config = new Config(this); // Manages saving/loading game settings
    Thread gameThread; // The thread that runs the game loop
    public boolean fullScreenOn = false;

    // --- ENTITY MANAGEMENT ---
    public AssetSetter aSetter = new AssetSetter(this); // Handles placing objects, NPCs, monsters
    public Player player = new Player(this, keyH);      // The main player entity

    // Entity arrays: [map index][entity index]
    public Entity[][] obj = new Entity[maxMap][20]; // Objects (keys, chests, weapons)
    public Entity[][] npc= new Entity[maxMap][10]; // NPCs (dialogue characters)
    public Entity[][] monster= new Entity[maxMap][20]; // Monsters
    public interactiveTile[][] iTile = new interactiveTile[maxMap][50]; // Interactive tiles (trees, rocks)

    public ArrayList<Entity> projectileList = new ArrayList<>(); // Active projectiles
    public ArrayList<Entity> particleList = new ArrayList<>(); // Active particles (effects)
    ArrayList<Entity> entityList = new ArrayList<>(); // List used for drawing order sorting (only)

    // PLAYER DEFAULT VALUES (redundant if set in Player class, but kept for context)
    // Note: The Player class likely sets its own defaults using the Player constructor/methods
    int playerX = 100;
    int playerY = 100;
    int playerSpeed = 4;

    // --- GAME STATE MANAGEMENT ---
    public int gameState;
    public final int titleState = 0;
    public final int playState=1;
    public final int pauseState=2;
    public final int dialogueState=3;
    public final int characterState = 4; // Character status screen
    public final int optionState=5; // Game settings menu
    public final int gameOverState=6;
    public final int transitionState = 7; // Screen fading effect for map change
    public final int tradeState = 8; // Merchant trading menu
    public final int endState = 9; // End game screen
    public final int requirementState = 10; // State for showing a requirement message

    // CONSTRUCTOR: set panel properties
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenwidth, screenheight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);   // Improves rendering performance
        this.addKeyListener(keyH);     // Register keyboard input listener
        this.setFocusable(true);       // Allows the panel to receive keyboard focus
    }

    // --- GAME SETUP & MANAGEMENT METHODS ---

    // Used when the player dies and needs to restart a section (keeps inventory/progress but resets location/enemies)
    public void retry(){
        player.setDefaultPosition(); // Move player back to start
        player.setLifeAndMan(); // Restore HP/MP
        aSetter.setNPC(); // Reset NPC positions
        aSetter.setMonster(); // Respawn monsters
        currentMap = 0;
        player.transparent = false;
    }

    // Used for a full game restart (resets player status and re-initializes all assets)
    public void restart (){
        player.setDefaultValues(); // Reset player stats/inventory
        player.setDefaultPosition();
        aSetter.setObject(); // Re-initialize objects
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        player.setItems();
        stopMusic();
    }

    // SETUP GAME: place objects and set initial state
    public void setupGame() {
        aSetter.setObject(); // Initialize objects
        aSetter.setNPC();
        aSetter.setMonster();
        aSetter.setInteractiveTile();
        gameState=titleState; // Start on the title screen
        eManager.setup();
        playMusic(0);

        // Try to load saved configuration
        try {
            config.loadConfig();
        } catch (IOException e) {
            System.out.println("No config file found or error loading.");
            // Handle error or use default settings
        }
    }

    // START GAME THREAD: creates and starts the game loop thread
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    // --- GAME LOOP (The Runnable implementation) ---
    public void run() {
        // High-precision timing loop (Delta method)
        double drawInterval = 1000000000.0 / FPS; // Time needed to draw one frame (e.g., 0.0166 seconds for 60 FPS)
        double delta = 0; // Accumulates time passed
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0; // Used for measuring 1 second intervals (for FPS count)
        int drawCount = 0; // Frame counter

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval; // How many "frames" worth of time have passed
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) { // If at least one frame interval has passed
                update();    // 1. Update game logic
                repaint();   // 2. Redraw screen (calls paintComponent)
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) { // Check if 1 second (1 billion nanoseconds) has passed
                // FPS is drawCount (often 60 if running smoothly)
                drawCount = 0;
                timer = 0;
            }
        }
    }

    // --- UPDATE GAME LOGIC ---
    public void update() {

        if(gameState==playState){
            // PLAYER
            player.update();
            eHandler.CheckEvent(); // Check for map events

            // NPC
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }
            // MONSTER
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) {
                    if (monster[currentMap][i].alive == true && monster[currentMap][i].dying == false){
                        monster[currentMap][i].update(); // Update alive, non-dying monsters
                    }
                    if (monster[currentMap][i].alive == false){
                        monster[currentMap][i].checkDrop(); // Drop item when dead
                        monster[currentMap][i] = null; // Remove monster from array
                    }
                }
            }
            // PROJECTILES
            // Use standard list iteration for removal safety
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) {
                    if (projectileList.get(i).alive == true){
                        projectileList.get(i).update();
                    }
                    if (projectileList.get(i).alive == false){
                        projectileList.remove(i); // Remove dead projectile
                    }
                }
            }
            // INTERACTIVE TILES
            for (int i = 0; i < iTile[1].length; i++){
                if (iTile[currentMap][i] != null){
                    iTile[currentMap][i].update();
                }
            }
            // PARTICLES
            // Use standard list iteration for removal safety
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) {
                    if (particleList.get(i).alive == true){
                        particleList.get(i).update();
                    }
                    if (particleList.get(i).alive == false){
                        particleList.remove(i); // Remove dead particle
                    }
                }
            }
        }
        if(gameState==pauseState){
            // No update logic runs when paused
        }
        eManager.update();

    }

    // --- DRAW EVERYTHING (Called by repaint()) ---
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // DEBUG: Draw time measurement start
        long drawStart=0;
        if(keyH.checkDrawTime){
            drawStart =System.nanoTime();
        }

        if(gameState == titleState){
            // Draw only the title screen UI
            try {
                ui.draw(g2);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            // 1. DRAW TILES
            tileM.draw(g2); // Draw background map tiles

            // 2. DRAW INTERACTIVE TILES (drawn before entities)
            for(int i = 0; i<iTile[1].length; i++){
                if(iTile[currentMap][i] != null){
                    iTile[currentMap][i].draw(g2);
                }
            }

            // 3. ASSEMBLE ENTITY LIST FOR SORTING
            entityList.add(player); // Player is always drawn

            // Add all active NPCs, Objects, Monsters, Projectiles, and Particles to the list
            for (int i = 0; i < npc[1].length; i++) {
                if (npc[currentMap][i] != null) entityList.add(npc[currentMap][i]);
            }
            for (int i = 0; i < obj[1].length; i++) {
                if (obj[currentMap][i] != null) entityList.add(obj[currentMap][i]);
            }
            for (int i = 0; i < monster[1].length; i++) {
                if (monster[currentMap][i] != null) entityList.add(monster[currentMap][i]);
            }
            for (int i = 0; i < projectileList.size(); i++) {
                if (projectileList.get(i) != null) entityList.add(projectileList.get(i));
            }
            for (int i = 0; i < particleList.size(); i++) {
                if (particleList.get(i) != null) entityList.add(particleList.get(i));
            }

            // 4. SORT ENTITIES (Painter's Algorithm for 2.5D perspective)
            // Entities with a higher worldY (closer to bottom of screen) are drawn last (on top)
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            // 5. DRAW ENTITIES (in sorted order)
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }

            // 6. CLEAR LIST (Must be cleared after drawing)
            entityList.clear();

            // 7. DRAW UI
            try {
                //Environment
                eManager.draw(g2);
                ui.draw(g2); // Draw UI elements (health, inventory, menu, dialogue)
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // DEBUG: Draw time measurement end
            if(keyH.checkDrawTime){
                long drawEnd=System.nanoTime();
                long passed=drawEnd -drawStart;
                g2.setColor(Color.white);
                g2.drawString("Draw Time:"+passed,10,400); // Display draw time on screen
            }

            g2.dispose(); // Release graphics context
        }
    }

    // --- MUSIC & SOUND EFFECTS ---
    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}