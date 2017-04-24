/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client.gui;

import clzola.pmnlgame.message.RoomMessages;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;

/**
 *
 * @author Lazar
 */
public class GamePanel extends javax.swing.JPanel {

    /**
     * Creates new form GamePanel
     */
    public GamePanel(GameView view) {
        initComponents();
        
        setBorder(BorderFactory.createLoweredBevelBorder());
        
        this.enabled = false;
        this.view = view;
        try {
            GameTable = ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/table.jpg"));
            GameLetters = new BufferedImage[] {
                    ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/P.jpg")),
                    ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/M.jpg")),
                    ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/N.jpg")),
                    ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/L.jpg"))
            };
            
            playBack = ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/play.png"));
            waitBack = ImageIO.read(getClass().getResource("/clzola/pmnlgame/resources/wait.png"));
        } catch (IOException ex) {
            Logger.getLogger(GamePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        enabled = false;
        letters = new ArrayList<>();
        visited = new boolean [15][15];
        initTable();
        
        this.addMouseListener(new MouseListener() {
            @Override public void mouseClicked(MouseEvent e) {
                if( enabled ) {
                    int X = (e.getX()-5-12 < 0 ? -23 : e.getX()-5-12) / 23;
                    int Y = (e.getY()-5-12 < 0 ? -23 : e.getY()-5-12) / 23;
                    if( X >= 0 && X < 15 && Y >= 0 && Y < 15 && !visited[Y][X] ) {
                        RoomMessages.PlayMessage play = new RoomMessages.PlayMessage();
                        play.x = X;
                        play.y = Y;
                        view.client.sendTCP(play);
                        waitYourTurn();
                    }
                }
            }

            @Override public void mousePressed(MouseEvent e) {}
            @Override public void mouseReleased(MouseEvent e) {}
            @Override public void mouseEntered(MouseEvent e) {}
            @Override public void mouseExited(MouseEvent e) {}
        });
    }
    
    protected final void initTable() {
        for(int i=0; i<15; i++)
            for(int j=0; j<15; j++)
                visited[i][j] = false;
    }
    
    public void clearTable() {
        initTable();
        letters.clear();
        repaint();
    }
    
    public void play() {
        enabled = true;
        this.repaint();
    }
    
    public void waitYourTurn() {
        enabled = false;
        this.repaint();
    }
    
    public void addLetter(int index, int x, int y) {
        letters.add(new LetterInfo(x*23+12+5+1, y*23+12+5+1, index));
        visited[y][x] = true;
        repaint();
    }
    
    @Override public void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);
        
        if(enabled)
            grphcs.drawImage(playBack, 0, 0, null);
        else grphcs.drawImage(waitBack, 0, 0, null);
        grphcs.drawImage(GameTable, 5, 5, null);
        
        for(LetterInfo li : letters)
            grphcs.drawImage(GameLetters[li.letter], li.x, li.y, null);
        
        grphcs.drawLine(5, 0, 5, 380);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(1, 1, 1));
        setPreferredSize(new java.awt.Dimension(380, 380));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 385, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    /**
     *
     */
    
    protected BufferedImage     GameTable;
    protected BufferedImage[]   GameLetters;
    protected BufferedImage     playBack;
    protected BufferedImage     waitBack;
    protected ArrayList<LetterInfo> letters;
    protected boolean[][] visited;
    protected GameView view;
    protected boolean enabled;
    
    private class LetterInfo {
        public int x;
        public int y;
        public int letter;
        
        public LetterInfo(int x, int y, int l) {
            this.x = x;
            this.y = y;
            this.letter = l;
        }
    }
}
