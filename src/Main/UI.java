package Main;

import entity.Entity;
import object.OBJ_Coin_Bronze;
import object.OBJ_Heart;
import object.OBJ_ManaCrystal;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.awt.BasicStroke;
import java.util.ArrayList;

public class UI {
    GamePanel gp;   // Reference to game panel
    Graphics2D g2;
    Font maruMonica;          // Custom font for text

    // UI Graphics Assets
    public BufferedImage titleBackground, playButton, loadButton, quitButton, titleImage, select, coin;
    public BufferedImage Fighter, Thief, back, selectF, selectT, diamond;
    public BufferedImage heart_full, heart_half, heart_blank, crystal_full, crystal_blank;

    // Message System
    ArrayList<String> message = new ArrayList<>(); // List of messages to display
    ArrayList<Integer> messageCounter = new ArrayList<>(); // Timer for message display

    // Game State Flags
    public boolean gameFinished = false;  // Flag for game completion
    public String currentDialogue = ""; // Text currently displayed in dialogue box

    // Menu/Cursor State Variables
    public int commandNum = 0; // Current selection in a menu
    public int slotCol = 0; // Current inventory column selection (Player)
    public int slotRow = 0; // Current inventory row selection (Player)
    public int npcslotCol = 0; // Current inventory column selection (NPC)
    public int npcslotRow = 0; // Current inventory row selection (NPC)
    public int subState = 0; // Sub-state for option/trade menus
    public int counter = 0; // General purpose counter (used for transitions)
    public Entity npc; // Reference to the NPC currently involved in trade/dialogue

    public UI(GamePanel gp){
        this.gp=gp;

        // --- Initialize Custom Font ---
        try{
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (IOException | FontFormatException e) {
            System.err.println("Failed to load custom font.");
            throw new RuntimeException(e);
        }

        // --- Load UI Images ---
        try {
            titleBackground = ImageIO.read(getClass().getResourceAsStream("/title/bg.jpg"));
            titleImage = ImageIO.read(getClass().getResourceAsStream("/title/title.png"));
            playButton = ImageIO.read(getClass().getResourceAsStream("/title/play.png"));
            loadButton = ImageIO.read(getClass().getResourceAsStream("/title/load.png"));
            quitButton = ImageIO.read(getClass().getResourceAsStream("/title/quit.png"));
            select = ImageIO.read(getClass().getResourceAsStream("/title/boy_down_1.png")); // Generic selector icon
            Fighter = ImageIO.read(getClass().getResourceAsStream("/title/fighter.png"));
            Thief = ImageIO.read(getClass().getResourceAsStream("/title/thief.png"));
            back = ImageIO.read(getClass().getResourceAsStream("/title/back.png"));
            selectT = ImageIO.read(getClass().getResourceAsStream("/title/theif.png"));
            selectF = ImageIO.read(getClass().getResourceAsStream("/title/fight.png"));
            diamond = ImageIO.read(getClass().getResourceAsStream("/object/blueheart.png")); // Used for end screen treasure

        } catch (Exception e) {
            System.err.println("Failed to load title screen assets.");
            e.printStackTrace();
        }

        // --- Create HUD Elements from Entity/Object classes ---

        // Heart icons (Life)
        Entity heart = new OBJ_Heart(gp);
        heart_full = heart.image;
        heart_blank = heart.image3;
        heart_half = heart.image2;

        // Mana crystal icons (Mana)
        Entity crystal = new OBJ_ManaCrystal(gp);
        crystal_full = crystal.image;
        crystal_blank = crystal.image2;

        // Coin icon
        Entity bronze_coin = new OBJ_Coin_Bronze(gp);
        coin = bronze_coin.down1;
    }

    // ## Message System

    // Adds a new message to the queue to be displayed on screen.
    public void addMessage(String text){
        message.add(text);
        messageCounter.add(0);
    }


    // ## Main Draw Loop

    // Draws the appropriate screen based on the current game state.
    public void draw(Graphics2D g2) throws IOException {
        this.g2=g2;
        g2.setFont(maruMonica);
        g2.setColor(Color.white);

        // TITLE STATE
        if(gp.gameState==gp.titleState){
            drawtitleScreen();
        }
        // PLAY STATE (HUD, Messages)
        else if(gp.gameState==gp.playState){
            drawPlayerLife();
            drawMessage();
        }
        // PAUSE STATE
        else if(gp.gameState==gp.pauseState){
            drawPauseScreen();
            drawPlayerLife();
        }
        // DIALOGUE STATE
        else if(gp.gameState==gp.dialogueState){
            drawPlayerLife();
            drawDialogueScreen();
        }
        // CHARACTER/STATUS SCREEN
        else if (gp.gameState == gp.characterState){
            drawCharacterScreen(); // Draws character stats
            drawInventory(gp.player, true); // Draws player inventory
        }
        // OPTION SCREEN
        else if (gp.gameState == gp.optionState){
            drawOptionScreen();
        }
        // GAME OVER SCREEN
        else if (gp.gameState == gp.gameOverState){
            drawGameOverScreen();
        }
        // TRANSITION SCREEN (Map fade)
        else if (gp.gameState == gp.transitionState){
            drawtransitionScreen();
        }
        // TRADE SCREEN
        else if (gp.gameState == gp.tradeState){
            drawtradeScreen();
        }
        // END GAME SCREEN
        else if (gp.gameState == gp.endState){
            drawEndScreen();
        }
        // REQUIREMENT SCREEN (Locked door/gate message)
        else if (gp.gameState == gp.requirementState){
            drawRequirement();
        }
    }

    // --- HUD Drawing ---

    public void drawPlayerLife(){
        int x = gp.tilesSize/2;
        int y = gp.tilesSize/2;
        int i = 0;

        // Draw Max Life (Blank Hearts)
        int maxLife = gp.player.maxLife / 2;
        while(i < maxLife){
            g2.drawImage(heart_blank, x, y, null);
            x += gp.tilesSize;
            i++;
        }

        // Reset positions
        x = gp.tilesSize/2;
        y = gp.tilesSize/2;
        i = 0;

        // Draw Current Life (Full/Half Hearts)
        while(i < gp.player.life){
            if(gp.player.life - i >= 2){ // Full heart (2 points of life)
                g2.drawImage(heart_full, x, y, null);
            } else if (gp.player.life - i == 1){ // Half heart (1 point of life)
                g2.drawImage(heart_half, x, y, null);
            }
            x += gp.tilesSize;
            i += 2; // Check 2 points of life at a time
        }

        // Reset positions for Mana Crystal
        x = gp.tilesSize/2;
        y = (int)(gp.tilesSize * 1.5);
        i = 0;

        // Draw Max Mana (Blank Crystals)
        int maxMana = gp.player.maxMana;
        while(i < maxMana){
            g2.drawImage(crystal_blank, x, y, null);
            x += 35; // Crystal size is slightly smaller than full tile
            i++;
        }

        // Reset positions
        x = gp.tilesSize/2;
        y = (int)(gp.tilesSize * 1.5);
        i = 0;

        // Draw Current Mana (Full Crystals)
        int currentMana = gp.player.mana;
        while(i < currentMana){
            g2.drawImage(crystal_full, x, y, null);
            x += 35;
            i++;
        }
    }

    // --- End Screen Drawing ---

    public void drawEndScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenwidth,gp.screenheight);
        int x;
        int y;
        String text;

        // Title: Congratulations!
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,100f));
        text = "Congratulations!";
        g2.setColor(Color.black); // Shadow
        x = getXForCenteredText(text);
        y = gp.tilesSize*3;
        g2.drawString(text,x,y);
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        // Subtitle: You found the hidden treasure!
        g2.setFont(g2.getFont().deriveFont(40f));
        text = "You found the hidden treasure!";
        g2.setColor(Color.black); // Shadow
        x = getXForCenteredText(text);
        y += (int)(gp.tilesSize*1.5);
        g2.drawString(text,x,y);
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        // Draw Treasure Icon (Diamond)
        int newWidth = 150;
        int newHeight = (diamond.getHeight() * newWidth) / diamond.getWidth();
        x = (gp.screenwidth - newWidth) / 2;
        y += gp.tilesSize-10;
        g2.drawImage(diamond, x, y, newWidth, newHeight, null);

        // Menu Option: Play Again
        g2.setFont(g2.getFont().deriveFont(40f));
        text = "Play Again";
        x = getXForCenteredText(text);
        y += gp.tilesSize*4;
        g2.drawString(text,x,y);
        if(commandNum==0){
            g2.drawString(">",x-40,y);
        }

        // Menu Option: Quit
        text = "Quit";
        x = getXForCenteredText(text);
        y += 65;
        g2.drawString(text,x,y);
        if(commandNum==1){
            g2.drawString(">",x-40,y);
        }
    }

    // --- Requirement Screen Drawing ---

    public void drawRequirement(){
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // Dialogue box header
        currentDialogue = "This door is locked. \nYou need Following to open it!";
        drawDialogueScreen();

        // Sub-window for requirements list
        int frameX = gp.tilesSize*2;
        int frameY = gp.tilesSize*4;
        int frameWidth=gp.screenwidth - (gp.tilesSize*6);
        int frameHeight=gp.tilesSize*5;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Requirement list content
        String text = "1 --> Golden Keys";
        int textX = frameX + gp.tilesSize;
        int textY = frameY +gp.tilesSize;
        g2.drawString(text,textX,textY);

        text = "300 --> Coins";
        textY += gp.tilesSize;
        g2.drawString(text,textX,textY);

        text = "3 --> Level 03";
        textY += gp.tilesSize;
        g2.drawString(text,textX,textY);
    }

    // --- Trade Screen Drawing ---

    public void drawtradeScreen(){
        // Handles the three sub-states of trading
        switch (subState){
            case 0: tradeSelect();break; // Select Buy/Sell/Leave
            case 1: tradeBuy();break;    // Buying menu
            case 2: tradeSell();break;   // Selling menu
        }
        gp.keyH.enterPressed = false;

    }

    public void tradeSelect(){
        // Dialogue box for the NPC's greeting
        drawDialogueScreen();

        // Trade Selection Menu Window
        int x = gp.tilesSize*10;
        int y = gp.tilesSize*4;
        int width = gp.tilesSize*5;
        int height = (int)(gp.tilesSize*3.5);
        drawSubWindow(x,y,width,height);

        x += gp.tilesSize;
        y += gp.tilesSize;

        // Buy Option
        g2.drawString("Buy", x,y);
        if(commandNum == 0){
            g2.drawString(">", x-24,y);
            if(gp.keyH.enterPressed){
                subState = 1; // Switch to Buy state
            }
        }

        // Sell Option
        y += gp.tilesSize;
        g2.drawString("Sell", x,y);
        if(commandNum == 1){
            g2.drawString(">", x-24,y);
            if(gp.keyH.enterPressed){
                subState = 2; // Switch to Sell state
            }
        }

        // Leave Option
        y += gp.tilesSize;
        g2.drawString("Leave", x,y);
        if(commandNum == 2){
            g2.drawString(">", x-24,y);
            if(gp.keyH.enterPressed){
                commandNum = 0;
                gp.gameState = gp.dialogueState; // Exit trade, go back to dialogue
                currentDialogue = "Come Again!";
            }
        }
    }

    public void tradeSell(){
        drawInventory(gp.player, true); // Player's inventory (selectable)

        // Back/Escape button hint
        int x = gp.tilesSize*2;
        int y = (int)(gp.tilesSize*9.5);
        int width = gp.tilesSize*6;
        int height = gp.tilesSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Back [ESC]", x+24,y+60);

        // Player's coin count display
        x = gp.tilesSize*9;
        y = (int)(gp.tilesSize*9.5);
        width = gp.tilesSize*6;
        height = gp.tilesSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Your Coin: "+gp.player.coin, x+24,y+60);

        // Price display and selling logic
        int itemIndex = getItemIndexOnSlot(slotCol, slotRow);

        // Ensure index is valid before trying to access inventory
        if(itemIndex < gp.player.inventory.size()){

            // Sub-window for price
            x = (int)(gp.tilesSize*12);
            y= (int)(gp.tilesSize*5.5);
            width = (int)(gp.tilesSize*2.5);
            height = gp.tilesSize;

            drawSubWindow(x,y,width,height);
            g2.drawImage(coin, x+10, y+8,32,32,null); // Coin icon

            int price = gp.player.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXForAlignToRightText(text, gp.tilesSize*14-20);
            g2.drawString(text, x, y+32); // Price text

            // Selling action triggered by Enter
            if(gp.keyH.enterPressed){
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWeapon ||
                        gp.player.inventory.get(itemIndex) == gp.player.currentShield){

                    // Cannot sell equipped items
                    subState = 0;
                    commandNum = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You cannot Sale Equipped Item!";
                    // Note: drawDialogueScreen() must be called in draw() loop, not here
                } else {
                    // Successful sale
                    if(gp.player.inventory.get(itemIndex).amount>1){
                        gp.player.inventory.get(itemIndex).amount--;
                    } else {
                        gp.player.inventory.remove(itemIndex);
                    }
                    gp.player.coin += price;
                }
            }
        }
    }

    public void tradeBuy(){
        drawInventory(npc, true);   // NPC's inventory (selectable)
        drawInventory(gp.player, false); // Player's inventory (non-selectable, for reference)

        // Back/Escape button hint
        int x = gp.tilesSize*2;
        int y = (int)(gp.tilesSize*9.5);
        int width = gp.tilesSize*6;
        int height = gp.tilesSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Back [ESC]", x+24,y+60);

        // Player's coin count display
        x = gp.tilesSize*9;
        y = (int)(gp.tilesSize*9.5);
        width = gp.tilesSize*6;
        height = gp.tilesSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Your Coin: "+gp.player.coin, x+24,y+60);

        // Price display and buying logic
        int itemIndex = getItemIndexOnSlot(npcslotCol, npcslotRow);

        // Ensure index is valid before trying to access inventory
        if(itemIndex < npc.inventory.size()){

            // Sub-window for price
            x = (int)(gp.tilesSize*5.5);
            y= (int)(gp.tilesSize*5.5);
            width = (int)(gp.tilesSize*2.5);
            height = gp.tilesSize;

            drawSubWindow(x,y,width,height);
            g2.drawImage(coin, x+10, y+8,32,32,null);

            int price = npc.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXForAlignToRightText(text, gp.tilesSize*8-20);
            g2.drawString(text, x, y+32);

            // Buying action triggered by Enter
            if(gp.keyH.enterPressed){
                if(npc.inventory.get(itemIndex).price > gp.player.coin){
                    // Not enough money
                    subState = 0;
                    gp.gameState = gp.dialogueState;
                    currentDialogue = "You need more coin to buy that!";
                } else {
                    if(gp.player.canObtainItem(npc.inventory.get(itemIndex))== true){
                        gp.player.coin -= npc.inventory.get(itemIndex).price;
                    } else {
                        // Inventory full
                        subState = 0;
                        gp.gameState = gp.dialogueState;
                        currentDialogue = "You cannot carry anymore!";
                    }
                }
            }
        }
    }

    // --- Transition Screen Drawing ---

    public void drawtransitionScreen(){
        counter++; // Increment fade counter

        // Draw black rectangle with increasing opacity (counter * 5 = 255/50, so fade takes 50 frames)
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0,0,gp.screenwidth,gp.screenheight);

        if(counter == 50){
            // Transition is complete, reset counter and move player
            counter=0;
            gp.gameState=gp.playState; // Resume gameplay

            // Set new location and map
            gp.currentMap = gp.eHandler.temMap;
            gp.player.worldX = gp.tilesSize * gp.eHandler.temCol;
            gp.player.worldY = gp.tilesSize * gp.eHandler.temRow;

            // Reset event handler's previous location to enable new events immediately
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }

    // --- Game Over Screen Drawing ---

    public void drawGameOverScreen(){
        g2.setColor(new Color(0,0,0,150));
        g2.fillRect(0,0,gp.screenwidth,gp.screenheight);
        int x;
        int y;
        String text;

        // Title: Game Over
        g2.setFont(g2.getFont().deriveFont(Font.BOLD,110f));
        text = "Game Over";
        g2.setColor(Color.black);
        x = getXForCenteredText(text);
        y = gp.tilesSize*4;
        g2.drawString(text,x,y);
        g2.setColor(Color.white);
        g2.drawString(text,x-4,y-4);

        // Option: Retry
        g2.setFont(g2.getFont().deriveFont(60f));
        text = "Retry";
        x = getXForCenteredText(text);
        y += gp.tilesSize*4;
        g2.drawString(text,x,y);
        if(commandNum==0){
            g2.drawString(">",x-40,y);
        }

        // Option: Quit
        text = "Quit";
        x = getXForCenteredText(text);
        y += 65;
        g2.drawString(text,x,y);
        if(commandNum==1){
            g2.drawString(">",x-40,y);
        }
    }

    // --- Options Screen Drawing ---

    public void drawOptionScreen() throws IOException {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));

        // Options frame dimensions
        int frameX = gp.tilesSize*4;
        int frameY = gp.tilesSize;
        int frameWidth = gp.tilesSize*8;
        int frameHeight = gp.tilesSize*10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Delegate drawing to sub-state methods
        switch (subState){
            case 0: option_top(frameX, frameY);break; // Main options (Volume, Control, End)
            case 1: break; // Unused sub-state
            case 2: optionControl(frameX,frameY);break; // Control mapping display
            case 3: optionEndGame(frameX, frameY);break; // Confirm exit to title
        }

        gp.keyH.enterPressed = false; // Consume enter press

    }

    public void option_top(int frameX, int frameY) throws IOException {
        int textX;
        int textY;

        // Title: Options Menu
        String text = "Options Menu";
        textX = getXForCenteredText(text);
        textY = frameY +gp.tilesSize;
        g2.drawString(text,textX,textY);

        // === Menu Options ===

        // Music Volume
        textX = frameX+gp.tilesSize;
        textY += gp.tilesSize*2;
        g2.drawString("Music",textX,textY);
        if(commandNum == 0){
            g2.drawString(">",textX-25,textY);
        }

        // Sound Effects Volume (SE)
        textY += gp.tilesSize;
        g2.drawString("Sound Effects",textX,textY);
        if(commandNum == 1){
            g2.drawString(">",textX-25,textY);
        }

        // Control (Sub-menu)
        textY += gp.tilesSize;
        g2.drawString("Control",textX,textY);
        if(commandNum == 2){
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 2; // Switch to control menu
                commandNum = 0;
            }
        }

        // End Game (Sub-menu)
        textY += gp.tilesSize;
        g2.drawString("End Game",textX,textY);
        if(commandNum == 3){
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 3; // Switch to end game confirmation
                commandNum = 0;
            }
        }

        // Back to Play
        textY += gp.tilesSize*2;
        g2.drawString("Back",textX,textY);
        if(commandNum == 4){
            g2.drawString(">",textX-25,textY);
            if (gp.keyH.enterPressed == true){
                gp.gameState = gp.playState; // Return to play
                commandNum = 0;
            }
        }

        // === Volume Sliders ===

        int barX = frameX + (int) (gp.tilesSize*4.8);

        // Music Slider
        textY = frameY + gp.tilesSize*2 + 24;
        g2.drawRect(barX,textY,120,24);
        int volumeWidth = 24*gp.music.volumeScale;
        g2.fillRect(barX,textY,volumeWidth,24);

        // Sound Effects Slider
        textY += gp.tilesSize;
        g2.drawRect(barX,textY,120,24);
        volumeWidth = 24*gp.se.volumeScale;
        g2.fillRect(barX,textY,volumeWidth,24);

        // Save settings after drawing
        gp.config.saveConfig();

    }

    public void optionControl(int frameX, int frameY){
        int textX;
        int textY;

        // Title: Controls
        String text = "Controls";
        textX = getXForCenteredText(text);
        textY = frameY +gp.tilesSize;
        g2.drawString(text,textX,textY);

        // Display control actions (Left side)
        textX = frameX+gp.tilesSize-1;
        textY += gp.tilesSize;

        g2.drawString("Move",textX,textY); textY += gp.tilesSize;
        g2.drawString("Confirm/Attack",textX,textY); textY += gp.tilesSize;
        g2.drawString("Shoot/Cast",textX,textY); textY += gp.tilesSize;
        g2.drawString("Character Screen",textX,textY); textY += gp.tilesSize;
        g2.drawString("Pause",textX,textY); textY += gp.tilesSize;
        g2.drawString("Options",textX,textY); textY += gp.tilesSize;

        // Display keys (Right side)
        textX = frameX+(int)(gp.tilesSize*5.5);
        textY = frameY + gp.tilesSize*2;

        g2.drawString("WASD",textX,textY); textY += gp.tilesSize;
        g2.drawString("ENTER",textX,textY); textY += gp.tilesSize;
        g2.drawString("F",textX,textY); textY += gp.tilesSize;
        g2.drawString("C",textX,textY); textY += gp.tilesSize;
        g2.drawString("P",textX,textY); textY += gp.tilesSize;
        g2.drawString("ESC",textX,textY); textY += gp.tilesSize;

        // Back button
        textX = frameX+gp.tilesSize-1;
        textY = gp.tilesSize*10;
        g2.drawString("Back",textX,textY);
        if(commandNum == 0){
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 0; // Return to main options
                commandNum = 2; // Set cursor back to 'Control' option
            }
        }
    }

    public void optionEndGame(int frameX, int frameY){
        int textX = frameX +gp.tilesSize;
        int textY = frameY + gp.tilesSize*3;

        // Confirmation dialogue
        currentDialogue = "Quit the game and \nReturn to title Screen?";
        for(String line: currentDialogue.split("\n")){
            g2.drawString(line,textX,textY);
            textY += 40;
        }

        // Option: Yes
        String text = "Yes";
        textX = getXForCenteredText(text);
        textY += gp.tilesSize;
        g2.drawString(text,textX,textY);
        if(commandNum == 0){
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                gp.gameState = gp.titleState; // Go to title screen
                gp.restart();
            }
        }

        // Option: No
        text = "No";
        textX = getXForCenteredText(text);
        textY += gp.tilesSize;
        g2.drawString(text,textX,textY);
        if(commandNum == 1){
            g2.drawString(">",textX-25,textY);
            if(gp.keyH.enterPressed == true){
                subState = 0;
                commandNum = 4; // Return to main options, cursor on 'Back'
            }
        }
    }

    // --- Inventory Drawing ---

    public void drawInventory(Entity entity, boolean cursor){
        // Determine frame position based on entity (Player on Right, NPC on Left)
        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int fslotCol = 0;
        int fslotRow = 0; // fslot is the slot currently selected by the cursor

        if (entity == gp.player){
            frameX = gp.tilesSize*9;
            frameY= gp.tilesSize;
            frameWidth = gp.tilesSize*6;
            frameHeight = gp.tilesSize*5;
            fslotCol = slotCol;
            fslotRow = slotRow;
        } else { // NPC/Merchant Inventory
            frameX = gp.tilesSize*2;
            frameY= gp.tilesSize;
            frameWidth = gp.tilesSize*6;
            frameHeight = gp.tilesSize*5;
            fslotCol = npcslotCol;
            fslotRow = npcslotRow;
        }

        // Frame background
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Slot positioning
        final int slotXstart = frameX +20;
        final int slotYstart = frameY +20;
        int slotX =  slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tilesSize+3;

        // Draw items and equipped indicators
        for (int i = 0; i < entity.inventory.size(); i++) {

            Entity item = entity.inventory.get(i);

            // Highlight currently equipped item slot
            if(item == entity.currentWeapon || item == entity.currentShield || item == entity.currentLight){
                g2.setColor(new Color(240, 190, 90)); // Gold/Orange color
                g2.fillRoundRect(slotX, slotY, gp.tilesSize, gp.tilesSize, 10, 10);
            }

            // Draw item icon
            g2.drawImage(item.down1, slotX, slotY, null);

            // Draw a yellow border around equipped items for extra visual clarity
            if (item == entity.currentWeapon || item == entity.currentShield || item == entity.currentLight) {
                g2.setColor(Color.yellow);
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(slotX, slotY, gp.tilesSize, gp.tilesSize, 10, 10);
            }
            //Display Amount
            if(entity == gp.player && entity.inventory.get(i).amount > 1){
                g2.setFont(g2.getFont().deriveFont(32f));
                int amountX;
                int amountY;

                String s = "" + entity.inventory.get(i).amount;
                amountX = getXForAlignToRightText(s, slotX+44);
                amountY = slotY +gp.tilesSize;

                //shadow
                g2.setColor(new Color(60, 60, 60));
                g2.drawString(s,amountX,amountY);

                //number
                g2.setColor(Color.white);
                g2.drawString(s,amountX-3,amountY-3);

            }

            // Move to the next slot
            slotX += slotSize;

            if ( (i+1) % 5 == 0 ) {  // 5 items per row
                slotX = slotXstart;
                slotY += slotSize;
            }
        }

        // --- Cursor and Description ---
        if(cursor == true) {

            // Cursor position calculation
            int cursorX = slotXstart + (slotSize * fslotCol);
            int cursorY = slotYstart + (slotSize * fslotRow);
            int cursorWidth = gp.tilesSize;
            int cursorHeight = gp.tilesSize;

            // Draw the cursor border
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);

            // Description frame positioning
            int dframeX = frameX;
            int dframeY= frameY + frameHeight + 10;
            int dframeWidth = frameWidth;
            int dframeHeight = gp.tilesSize*3;

            // Description text positioning
            int textX = dframeX +20;
            int textY = dframeY + gp.tilesSize;
            g2.setFont(g2.getFont().deriveFont(28f));

            int itemIndex = getItemIndexOnSlot(fslotCol, fslotRow);

            // Draw description only if a valid item is selected
            if(itemIndex < entity.inventory.size()){
                drawSubWindow(dframeX, dframeY, dframeWidth, dframeHeight);
                for(String line:entity.inventory.get(itemIndex).description.split("\n") ){
                    g2.drawString(line,textX,textY);
                    textY += 32;
                }
            }
        }
    }

    // Utility to convert column/row coordinates into a linear index.
    public int getItemIndexOnSlot(int islotCol, int islotRow){
        int itemIndex = islotCol + (islotRow * 5); // 5 is the max number of columns
        return itemIndex;
    }

    // --- Message Display ---

    public void drawMessage() {
        int messageX = gp.tilesSize;
        int messageY = gp.tilesSize * 4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));

        // Iterate through all messages
        for (int i = 0; i < message.size(); i++) {
            if (message.get(i) != null) {

                String text = message.get(i);

                // --- Background box settings ---
                int padding = 10;
                int arc = 10; // corner radius
                int textWidth = g2.getFontMetrics().stringWidth(text);
                int textHeight = g2.getFontMetrics().getHeight();

                // Draw semi-transparent black background
                g2.setColor(new Color(0, 0, 0, 148));
                g2.fillRoundRect(
                        messageX - padding,
                        messageY - textHeight + 3,
                        textWidth + padding * 2,
                        textHeight+10,
                        arc,
                        arc
                );

                // Draw text with a slight black shadow/offset
                g2.setColor(Color.black);
                g2.drawString(text, messageX+1, messageY+1);
                g2.setColor(Color.white);
                g2.drawString(text, messageX, messageY);

                // --- Counter logic ---
                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50; // Move down for the next message

                if (messageCounter.get(i) > 180) { // Message displayed for 180 frames (3 seconds at 60 FPS)
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }

    // --- Title Screen Drawing ---

    public void drawtitleScreen() {

            // Draw background
            g2.drawImage(titleBackground, 0, 0, gp.screenwidth, gp.screenheight, null);

            // Scale and center title image
            int newWidth = 600;
            int newHeight = (titleImage.getHeight() * newWidth) / titleImage.getWidth();
            int x = (gp.screenwidth - newWidth) / 2;
            int y = 0;
            g2.drawImage(titleImage, x, y, newWidth, newHeight, null);

            // ==== BUTTONS ====
            int btnWidth = 230;
            int btnHeightPlay  = (playButton.getHeight()  * btnWidth) / playButton.getWidth();
            int btnHeightLoad  = (loadButton.getHeight()  * btnWidth) / loadButton.getWidth();
            int btnHeightQuit  = (quitButton.getHeight()  * btnWidth) / quitButton.getWidth();

            int centerX = (gp.screenwidth - btnWidth) / 2;
            int spacing = 10;

            // Button positions
            int btnPlayY = newHeight-3;         // Play (starts under title logo)
            int btnLoadY = btnPlayY + btnHeightPlay + spacing; // Load
            int btnQuitY = btnLoadY + btnHeightLoad + spacing; // Quit

            // Draw Play button and cursor
            g2.drawImage(playButton, centerX, btnPlayY, btnWidth, btnHeightPlay, null);
            if(commandNum==0){
                g2.drawImage(select, centerX-60, btnPlayY+8, 60, 60, null);
            }

            // Draw Load button and cursor
            g2.drawImage(loadButton, centerX, btnLoadY, btnWidth, btnHeightLoad, null);
            if(commandNum==1){
                g2.drawImage(select, centerX-60, btnLoadY+8, 60, 60, null);
            }

            // Draw Quit button and cursor
            g2.drawImage(quitButton, centerX, btnQuitY, btnWidth, btnHeightQuit, null);
            if(commandNum==2){
                g2.drawImage(select, centerX-60, btnQuitY+8, 60, 60, null);
            }
    }

    // --- Pause Screen Drawing ---

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 80F));
        String text = "PAUSED";
        int x = getXForCenteredText(text);
        int y = gp.screenheight / 2; // Use getter for height
        g2.drawString(text, x, y);
    }

    // --- Dialogue Screen Drawing ---

    public void drawDialogueScreen(){
        // Dialogue Window dimensions
        int x=gp.tilesSize*2;
        int y=gp.tilesSize/3;
        int width=gp.screenwidth - (gp.tilesSize*6);
        int height=gp.tilesSize*3;

        drawSubWindow(x,y,width,height);

        // Dialogue text
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN,32F));
        x +=gp.tilesSize;
        y +=gp.tilesSize;
        for (String line : currentDialogue.split("\n")){
            g2.drawString(line,x,y);
            y +=40;
        }
    }

    // --- Character Status Screen Drawing ---

    public void drawCharacterScreen(){
        // Create Status frame
        final int frameX  = gp.tilesSize;
        final int frameY = gp.tilesSize-5;
        final int frameWidth = gp.tilesSize*5;
        final int frameHeight = (int)(gp.tilesSize*10.5);
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        // Text formatting
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));

        int textX = frameX + 20;
        int textY = frameY + gp.tilesSize;
        final int lineHeight = 35; // Vertical spacing

        // --- Left Column: Stat Names ---
        g2.drawString("Level", textX, textY); textY += lineHeight;
        g2.drawString("Life", textX, textY); textY += lineHeight;
        g2.drawString("Mana", textX, textY); textY += lineHeight;
        g2.drawString("Strength", textX, textY); textY += lineHeight;
        g2.drawString("Dexterity", textX, textY); textY += lineHeight;
        g2.drawString("Attack", textX, textY); textY += lineHeight;
        g2.drawString("Defense", textX, textY); textY += lineHeight;
        g2.drawString("Exp", textX, textY); textY += lineHeight;
        g2.drawString("Next Level", textX, textY); textY += lineHeight;
        g2.drawString("Coin", textX, textY); textY += lineHeight +25;
        g2.drawString("Weapon", textX, textY); textY += lineHeight;
        g2.drawString("Shield", textX, textY); textY += lineHeight;

        //VALUES
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tilesSize;
        String value;

        // Level
        value = String.valueOf(gp.player.level);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Life
        value = gp.player.life + "/" + gp.player.maxLife;
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Mana
        value = gp.player.mana + "/" + gp.player.maxMana;
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Strength
        value = String.valueOf(gp.player.strength);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Dexterity
        value = String.valueOf(gp.player.dexterity);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Attack
        value = String.valueOf(gp.player.attack);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Defense
        value = String.valueOf(gp.player.defense);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Exp
        value = String.valueOf(gp.player.exp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Next Level Exp
        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Coin
        value = String.valueOf(gp.player.coin);
        textX = getXForAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;

        // Weapon (Display current weapon/shield icons)
        g2.drawImage(gp.player.currentWeapon.down1, tailX - gp.tilesSize, textY-16, null);
        textY += gp.tilesSize;
        g2.drawImage(gp.player.currentShield.down1, tailX - gp.tilesSize, textY-14, null);
    }

    // --- Helper Methods (Inferred but necessary for the class to compile) ---

    /**
     * Draws a semi-transparent rounded rectangle used for menu and dialogue frames.
     */
    public void drawSubWindow(int x, int y, int width, int height) {
        // Dark transparent background
        g2.setColor(new Color(0, 0, 0, 210));
        g2.fillRoundRect(x, y, width, height, 25, 25);

        // White border
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    /**
     * Calculates the X coordinate to center a string horizontally.
     */
    public int getXForCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenwidth / 2 - length / 2;
        return x;
    }

    /**
     * Calculates the X coordinate to align a string to the right edge.
     */
    public int getXForAlignToRightText(String text, int tailX) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}