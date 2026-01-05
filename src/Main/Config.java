package Main;

import java.io.*;

// This class manages saving and loading game configuration settings to a file.
public class Config {

    GamePanel gp; // Reference to the main GamePanel to access/modify settings

    // Constructor: Initializes the Config class with a reference to GamePanel
    Config(GamePanel gp) {
        this.gp = gp;
    }

    // ## Save Configuration
    // Writes the current game settings (full screen status, music/SE volume) to "config.txt".
    public void saveConfig() throws IOException {
        // Use BufferedWriter for efficient writing to the configuration file
        BufferedWriter bw = new BufferedWriter(new FileWriter("config.txt"));

        // 1. Save Full Screen Status (Line 1)
        if(gp.fullScreenOn){
            bw.write("Full Screen On");
        }
        if(gp.fullScreenOn == false){
            bw.write("Full Screen Off");
        }

        bw.newLine(); // Move to the next line

        // 2. Save Music Volume Scale (Line 2)
        bw.write(String.valueOf(gp.music.volumeScale));

        bw.newLine(); // Move to the next line

        // 3. Save Sound Effect Volume Scale (Line 3)
        bw.write(String.valueOf(gp.se.volumeScale));

        bw.newLine(); // Move to the next line (optional, but consistent)

        bw.close(); // Close the writer, ensuring data is flushed and saved
    }

    // ## Load Configuration
    // Reads game settings from "config.txt" and applies them to the GamePanel.
    public void loadConfig() throws IOException {
        // Use BufferedReader for efficient reading from the configuration file
        BufferedReader br = new BufferedReader(new FileReader("config.txt"));

        // 1. Load Full Screen Status (Line 1)
        String s = br.readLine();

        if(s.equals("Full Screen On")){
            gp.fullScreenOn = true;
        }
        if(s.equals("Full Screen Off")){
            gp.fullScreenOn = false;
        }

        // 2. Load Music Volume Scale (Line 2)
        s = br.readLine();
        gp.music.volumeScale = Integer.parseInt(s); // Convert string to integer and set volume

        // 3. Load Sound Effect Volume Scale (Line 3)
        s = br.readLine();
        gp.se.volumeScale = Integer.parseInt(s); // Convert string to integer and set volume

        br.close(); // Close the reader
    }
}