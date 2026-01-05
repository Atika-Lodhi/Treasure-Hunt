package Main;

import java.awt.*;
import java.awt.image.BufferedImage;

// UtilityTool class provides common helper functions for image processing and other utilities.
public class UtilityTool {

    /**
     * Resizes a BufferedImage to a specified width and height.
     * * @param original The original BufferedImage to scale.
     * @param width The target width.
     * @param height The target height.
     * @return The newly scaled BufferedImage.
     */
    public BufferedImage scaleImage(BufferedImage original, int width, int height) {

        // Create a new BufferedImage with the target size.
        // TYPE_INT_ARGB is crucial as it includes an Alpha channel (transparency),
        // preventing black backgrounds/borders on transparent images.
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // Get a Graphics2D context for drawing onto the new image.
        Graphics2D g2 = scaledImage.createGraphics();

        // Draw the original image onto the new canvas, scaled to the target dimensions.
        g2.drawImage(original, 0, 0, width, height, null);

        // Release resources used by the Graphics2D object.
        g2.dispose();

        return scaledImage;
    }
}