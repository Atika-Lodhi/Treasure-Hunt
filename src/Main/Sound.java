package Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

// This class handles playing sound effects and background music
public class Sound {
    Clip clip; // Holds the audio clip currently loaded or playing
    URL soundURL[] = new URL[30]; // Array to store URLs/paths of sound files (up to 30)
    FloatControl fc; // Used to control the master volume (gain) of the clip
    int volumeScale = 3; // Current volume setting (1-5 scale)
    float volume; // Actual decibel (dB) value corresponding to volumeScale

    // Constructor: Maps sound indices to file paths
    public Sound() {
        // Load the resource URLs for all sound files from the project's resource folder
        soundURL[0] = getClass().getResource("/sound/BlueBoyAdventure.wav"); // Background music
        soundURL[1] = getClass().getResource("/sound/coin.wav");              // Coin pickup sound
        soundURL[2] = getClass().getResource("/sound/powerup.wav");           // Power-up gained
        soundURL[3] = getClass().getResource("/sound/unlock.wav");            // Door unlock sound
        soundURL[4] = getClass().getResource("/sound/fanfare.wav");           // Game finish/Fanfare
        soundURL[5] = getClass().getResource("/sound/hitmonster.wav");        // Monster taking damage
        soundURL[6] = getClass().getResource("/sound/receivedamage.wav");     // Player taking damage
        soundURL[7] = getClass().getResource("/sound/swingweapon.wav");       // Weapon swing sound
        soundURL[8] = getClass().getResource("/sound/levelup.wav");           // Level up
        soundURL[9] = getClass().getResource("/sound/cursor.wav");            // UI cursor movement
        soundURL[10] = getClass().getResource("/sound/burning.wav");          // Burning effect
        soundURL[11] = getClass().getResource("/sound/cuttree.wav");          // Cutting wood/tree
        soundURL[12] = getClass().getResource("/sound/gameover.wav");         // Game over music/effect
        soundURL[13] = getClass().getResource("/sound/stairs.wav");           // Stairs/teleport sound
        soundURL[14] = getClass().getResource("/sound/blocked.wav");
        soundURL[15] = getClass().getResource("/sound/parry.wav");
    }

    // ## setFile
    // Loads a specific sound file into the 'clip' variable.
    public void setFile(int i){
        try{
            if(soundURL[i] == null) { // Check if a path exists for the index
                System.out.println("Sound file not found at index: " + i);
                clip = null;
                return;
            }

            // 1. Get audio input stream from the URL
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);

            // 2. Obtain a Clip object
            clip = AudioSystem.getClip();

            // 3. Open the audio stream with the clip
            clip.open(ais);

            // 4. Get the Master Gain control for volume adjustment
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);

            // 5. Apply the current volume setting
            checkVolume();

        }catch (Exception e){
            e.printStackTrace();
            clip = null; // Reset clip on error to prevent NullPointerExceptions later
        }
    }

    // ## play
    // Starts playing the loaded clip from the beginning.
    public void play(){
        if(clip != null){
            clip.setFramePosition(0); // Rewind clip to the start
            clip.start();// Start playback
            checkVolume();
        }
    }

    // ## loop
    // Plays the loaded clip continuously.
    public void loop(){
        if(clip != null){
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // ## stop
    // Stops the clip immediately.
    public void stop(){
        if(clip != null){
            clip.stop();
        }
    }

    // ## checkVolume
    // Converts the integer volumeScale (1-5) into a decibel (dB) value and applies it.
    public void checkVolume(){
        // Note: Decibels are logarithmic. Lower negative numbers mean quieter.
        switch (volumeScale){
            case 1: volume = -80f; break; // Muted/Extremely low
            case 2: volume = -20f; break; // Quiet
            case 3: volume = -12f; break; // Default (Medium)
            case 4: volume = 1f; break;  // Loud
            case 5: volume = 6f; break;  // Max Volume
        }

        // Apply the calculated decibel value to the clip's master gain control
        if (fc != null) {
            fc.setValue(volume);
        }
    }
}