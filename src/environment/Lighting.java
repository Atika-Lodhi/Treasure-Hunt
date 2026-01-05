package environment;

import Main.GamePanel;
import object.OBJ_key;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Lighting {
    GamePanel gp;
    BufferedImage darknessFilter;


    public Lighting(GamePanel gp) {
        this.gp = gp;
    }

    public void setLightSource(){
        // Create empty buffer ONCE
        darknessFilter = new BufferedImage(
                gp.screenwidth,
                gp.screenheight,
                BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g2 = (Graphics2D) darknessFilter.getGraphics();
            if(gp.player.currentLight == null){
                g2.setColor(new Color(0, 0, 0, 0.98f));
            } else {
                int centerX = gp.player.screenX + gp.tilesSize / 2;
                int centerY = gp.player.screenY + gp.tilesSize / 2;

                Color[] colors = {
                        new Color(0, 0, 0, 0.1f),
                        new Color(0, 0, 0, 0.42f),
                        new Color(0, 0, 0, 0.52f),
                        new Color(0, 0, 0, 0.61f),
                        new Color(0, 0, 0, 0.69f),
                        new Color(0, 0, 0, 0.76f),
                        new Color(0, 0, 0, 0.82f),
                        new Color(0, 0, 0, 0.87f),
                        new Color(0, 0, 0, 0.91f),
                        new Color(0, 0, 0, 0.94f),
                        new Color(0, 0, 0, 0.96f),
                        new Color(0, 0, 0, 0.98f)
                };

                float[] fractions = {0.1f,0.42f,0.52f,0.61f,0.69f,0.76f,0.82f,0.87f,0.91f,0.94f,0.96f,0.98f};

                RadialGradientPaint paint = new RadialGradientPaint(
                        centerX, centerY, (float) gp.player.currentLight.lightRadius, fractions, colors
                );

                g2.setPaint(paint);
            }

            g2.fillRect(0,0, gp.screenwidth,  gp.screenheight);

            g2.dispose();
        }

    public void update() {

        // ONLY update lighting if player is on MAP 2
        if (gp.currentMap == 2) {

            if (gp.player.lightUpdated) {
                setLightSource();
                gp.player.lightUpdated = false;
            }

        } else {
            // Disable darkness on other maps
            darknessFilter = null;
        }
    }


    public void draw(Graphics2D g2) {

        // Only draw darkness on map 2
        if (gp.currentMap == 2 && darknessFilter != null) {
            g2.drawImage(darknessFilter, 0, 0, null);
        }
    }

}
