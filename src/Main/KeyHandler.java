package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// Handles player keyboard input
public class KeyHandler implements KeyListener {

    GamePanel gp;

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed, shotKeyPressed, defensePressed;
    public boolean checkDrawTime = false;

    public KeyHandler(GamePanel gp){
        this.gp = gp;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        //TITLE STATE
        if(gp.gameState == gp.titleState) {
                titleState(code);
        }

        // PLAY STATE
        else if (gp.gameState == gp.playState) {
            if (code == KeyEvent.VK_W) upPressed = true;
            if (code == KeyEvent.VK_S) downPressed = true;
            if (code == KeyEvent.VK_A) leftPressed = true;
            if (code == KeyEvent.VK_D) rightPressed = true;
            if (code == KeyEvent.VK_C) gp.gameState = gp.characterState;
            if (code == KeyEvent.VK_P) gp.gameState = gp.pauseState;
            if (code == KeyEvent.VK_T) checkDrawTime = !checkDrawTime;
            if (code == KeyEvent.VK_ENTER) enterPressed = true;
            if (code == KeyEvent.VK_F) shotKeyPressed = true;
            if(code == KeyEvent.VK_R) defensePressed = true;
            if (code == KeyEvent.VK_ESCAPE) gp.gameState = gp.optionState;
        }
        // PAUSE STATE
        else if (gp.gameState == gp.pauseState) {
            if (code == KeyEvent.VK_P) gp.gameState = gp.playState;
        }
        // DIALOGUE STATE
        else if (gp.gameState == gp.dialogueState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }
        //Character State
        else if (gp.gameState == gp.characterState){
            if (code == KeyEvent.VK_C){
                gp.gameState = gp.playState;
            }
            playerInventory(code);
            if (code == KeyEvent.VK_ENTER){
                gp.player.selectItem();
            }
        }

        else if (gp.gameState == gp.optionState) {
            optionState(code);
        }

        else if (gp.gameState == gp.gameOverState) {
            gameOverState(code);
        }
        else if (gp.gameState == gp.endState) {
            endState(code);
        }
        else if (gp.gameState == gp.tradeState) {
            if (code == KeyEvent.VK_W) {
                gp.ui.commandNum--;
                gp.playSE(9);
                if (gp.ui.commandNum < 0) {
                    gp.ui.commandNum = 2; // max 2 options: Buy, Sell, Leave
                }
            }
            if (code == KeyEvent.VK_S) {
                gp.ui.commandNum++;
                gp.playSE(9);
                if (gp.ui.commandNum > 2) {
                    gp.ui.commandNum = 0;
                }
            }
            if (code == KeyEvent.VK_ENTER) {
                enterPressed = true;
            }
            if(gp.ui.subState ==1){
                npcInventory(code);
                if(code == KeyEvent.VK_ESCAPE){
                    gp.ui.subState = 0;
                }
            }
            if(gp.ui.subState ==2){
                playerInventory(code);
                if(code == KeyEvent.VK_ESCAPE){
                    gp.ui.subState = 0;
                }
            }
        }
        else if (gp.gameState == gp.requirementState) {
            if (code == KeyEvent.VK_ENTER) {
                gp.gameState = gp.playState;
            }
        }

    }

    public void titleState(int code) {

        // MOVE UP
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum--;
            gp.playSE(9);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 2; // total = play, load, quit
            }
        }

        // MOVE DOWN
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum++;
            gp.playSE(9);
            if (gp.ui.commandNum > 2) {
                gp.ui.commandNum = 0;
            }
        }

        // SELECT OPTION
        if (code == KeyEvent.VK_ENTER) {

            if (gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;   // PLAY
            }

            if (gp.ui.commandNum == 1) {
                // LOAD GAME (if you want)
            }

            if (gp.ui.commandNum == 2) {
                System.exit(0);                // QUIT
            }
        }
    }


    public void optionState(int code) {
        if (code == KeyEvent.VK_ESCAPE) {
            gp.gameState = gp.playState;
        }
        if (code == KeyEvent.VK_ENTER) enterPressed = true;

        int maxCommandNum = 0;
        switch (gp.ui.subState) {
            case 0:
                maxCommandNum = 4;
                break;
                case 3: maxCommandNum = 1; break;
        }

        if(code == KeyEvent.VK_W){
            gp.ui.commandNum --;
            gp.playSE(9);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = maxCommandNum;
            }
        }
        if(code == KeyEvent.VK_S){
            gp.ui.commandNum ++;
            gp.playSE(9);
            if (gp.ui.commandNum > maxCommandNum) {
                gp.ui.commandNum = 0;
            }
        }
        if(code == KeyEvent.VK_A){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 0 && gp.music.volumeScale > 0){
                    gp.music.volumeScale--;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 1 && gp.se.volumeScale > 0){
                    gp.se.volumeScale--;
                    gp.playSE(9);
                }
            }
        }
        if(code == KeyEvent.VK_D){
            if(gp.ui.subState == 0){
                if(gp.ui.commandNum == 0 && gp.music.volumeScale < 5){
                    gp.music.volumeScale++;
                    gp.music.checkVolume();
                    gp.playSE(9);
                }
                if(gp.ui.commandNum == 1 && gp.se.volumeScale < 5){
                    gp.se.volumeScale++;
                    gp.playSE(9);
                }
            }
        }
    }

    public void gameOverState(int code) {
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum --;
            gp.playSE(9);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum ++;
            gp.playSE(9);
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.retry();
                gp.playMusic(0);
            }
            if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.restart();
                gp.stopMusic();
            }
        }
    }

    public void endState(int code){
        if (code == KeyEvent.VK_W) {
            gp.ui.commandNum --;
            gp.playSE(9);
            if (gp.ui.commandNum < 0) {
                gp.ui.commandNum = 1;
            }
        }
        if (code == KeyEvent.VK_S) {
            gp.ui.commandNum ++;
            gp.playSE(9);
            if (gp.ui.commandNum > 1) {
                gp.ui.commandNum = 0;
            }
        }
        if (code == KeyEvent.VK_ENTER) {
            if(gp.ui.commandNum == 0) {
                gp.gameState = gp.playState;
                gp.restart();
                gp.playMusic(0);
            }
            if (gp.ui.commandNum == 1) {
                gp.gameState = gp.titleState;
                gp.restart();
            }
        }
    }

    public void playerInventory(int code){
        if (code == KeyEvent.VK_W){
            if(gp.ui.slotRow != 0){
                gp.ui.slotRow--;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_S){
            if(gp.ui.slotRow != 3){
                gp.ui.slotRow++;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_A) {
            if(gp.ui.slotCol != 0){
                gp.ui.slotCol--;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_D){
            if(gp.ui.slotCol != 4){
                gp.ui.slotCol++;
                gp.playSE(9);
            }
        }
    }

    public void npcInventory(int code){
        if (code == KeyEvent.VK_W){
            if(gp.ui.npcslotRow != 0){
                gp.ui.npcslotRow--;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_S){
            if(gp.ui.npcslotRow != 3){
                gp.ui.npcslotRow++;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_A) {
            if(gp.ui.npcslotCol != 0){
                gp.ui.npcslotCol--;
                gp.playSE(9);
            }
        }
        if (code == KeyEvent.VK_D){
            if(gp.ui.npcslotCol != 4){
                gp.ui.npcslotCol++;
                gp.playSE(9);
            }
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) upPressed = false;
        if (code == KeyEvent.VK_S) downPressed = false;
        if (code == KeyEvent.VK_A) leftPressed = false;
        if (code == KeyEvent.VK_D) rightPressed = false;
        if (code == KeyEvent.VK_F) shotKeyPressed = false;
        if(code == KeyEvent.VK_R) defensePressed = false;
        if (code == KeyEvent.VK_ENTER) enterPressed = false;

    }
}


