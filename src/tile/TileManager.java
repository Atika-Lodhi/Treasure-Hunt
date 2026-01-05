package tile;

import Main.GamePanel;
import Main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

// Manages all game tiles: loading images, handling collision flags, and loading map layouts.
public class TileManager {

    GamePanel gp;
    public Tiles[] tile;       // Array to hold unique tile types (image, collision data)
    // 3D array: [map index][world row][world column] stores the tile ID number
    public int[][][] mapTileNum;

    public TileManager(GamePanel gp) {
        this.gp = gp;

        // Initialize tile storage (setting size to 70 as per the original setup)
        tile = new Tiles[70];

        // Initialize the 3D map array using constants from GamePanel
        mapTileNum = new int[gp.maxMap][gp.MaxWorldRow][gp.maxWorldCol];

        getTileImage();           // Load tile images and set properties

        // Load the world map and the interior map
        loadMap("/maps/worldV3.txt",0);  // Load map data into index 0 (World Map)
        loadMap("/maps/interior01.txt",1);// Load map data into index 1 (Interior Map)
        loadMap("/maps/dungeon01.txt",2);
    }

    /**
     * Helper function to safely load an image from the resources folder.
     * @param path The resource path of the image.
     * @return The loaded BufferedImage, or null if loading fails.
     */
    private BufferedImage loadImage(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.out.println("Image not found: " + path);
                return null;
            }
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Initializes all tile images and their collision flags by calling the setup method.
     */
    public void getTileImage() {
        // --- PLACEHOLDER TILES (IDs 0-9) ---
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);

        // --- STANDARD TILES ---
        setup(10, "grass00", false);
        setup(11, "grass01", false);

        // Water tiles (collidable)
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);

        // Road tiles (non-collidable)
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road00", false); // Duplicates
        setup(38, "road00", false); // Duplicates

        // Environmental/Structure tiles
        setup(39, "earth", false);  // Walkable earth/dirt
        setup(40, "wall", true);    // Solid wall
        setup(41, "round_tree", true); // Collidable round tree
        setup(42, "orange_tree", true); // Collidable orange tree
        setup(43, "tree", true);    // Collidable tree
        setup(44, "bush", true);    // Collidable bush
        setup(45, "rock", true);    // Collidable rock
        setup(46, "trunk", true);   // Collidable trunk (stump)
        setup(47, "hut", false);    // Walkable base of a hut
        setup(48, "floor01", false); // Interior floor
        setup(49, "table01", true); // Collidable table
        setup(50, "037", false);
        setup(51, "036", false);
        setup(52, "000", true);
    }

    /**
     * Loads the tile image, scales it, and sets the collision flag for a specific tile ID.
     * @param index The tile ID number.
     * @param imageName The filename of the sprite (e.g., "grass01").
     * @param collision The collision flag (true if the player cannot walk on it).
     */
    public  void setup(int index,String imageName,boolean collision){
        UtilityTool utool=new UtilityTool();
        try{
            tile[index]=new Tiles();
            // Load the image resource
            tile[index].image = loadImage("/tiles/"+imageName+".png");
            // Scale the image to the game's tile size (e.g., 48x48)
            tile[index].image= utool.scaleImage(tile[index].image,gp.tilesSize,gp.tilesSize);
            tile[index].collision=collision;
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    /**
     * Loads the map layout data from a text file into the mapTileNum array.
     * @param fileName The path to the map text file.
     * @param mapIndex The index of the map array to load the data into (e.g., 0 for world map).
     */
    public void loadMap(String fileName, int mapIndex) {
        try (InputStream is = getClass().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            int row = 0;
            // Loop through all possible rows in the world map
            while (row < gp.MaxWorldRow) {
                String line = br.readLine();
                if (line == null) break; // Stop reading if end of file reached

                // Split the line by spaces to get individual tile numbers
                String[] numbers = line.split(" ");

                // Loop through all possible columns in the row
                for (int col = 0; col < gp.maxWorldCol; col++) {
                    int num = Integer.parseInt(numbers[col]);
                    // Assign the tile ID number to the map grid
                    mapTileNum[mapIndex][row][col] = num;
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Draws the tiles visible on the screen, optimizing rendering by skipping off-screen tiles.
     * @param g2 The Graphics2D context used for drawing.
     */
    public void draw(Graphics2D g2) {
        int worldCol = 0;
        int worldRow = 0;

        // Iterate through all tiles in the current map
        while (worldRow < gp.MaxWorldRow) {

            // Get the tile ID for the current position
            int tileNum = mapTileNum[gp.currentMap][worldRow][worldCol];

            // Calculate tile's world position
            int worldX = worldCol * gp.tilesSize;
            int worldY = worldRow * gp.tilesSize;

            // Calculate tile's screen position relative to the player's center
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            // Frustum Culling: Draw only tiles visible on the screen (plus a one-tile buffer)
            if (worldX + gp.tilesSize > gp.player.worldX - gp.player.screenX &&
                    worldX - gp.tilesSize < gp.player.worldX + gp.player.screenX &&
                    worldY + gp.tilesSize > gp.player.worldY - gp.player.screenY &&
                    worldY - gp.tilesSize < gp.player.worldY + gp.player.screenY) {

                // Draw the scaled tile image
                g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tilesSize, gp.tilesSize, null);
            }

            // Move to the next column/row
            worldCol++;
            if (worldCol == gp.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}