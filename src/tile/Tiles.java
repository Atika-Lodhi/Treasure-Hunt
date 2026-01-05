package tile;

import java.awt.image.BufferedImage;

// Represents a single type of tile used on the map, storing its visual and physical properties.
public class Tiles {

    // The visual image data for the tile, loaded as a BufferedImage.
    public BufferedImage image;

    // A flag indicating whether the tile is solid.
    // True means the player (or entities) cannot pass through it (e.g., a wall or water).
    public boolean collision = false;
}