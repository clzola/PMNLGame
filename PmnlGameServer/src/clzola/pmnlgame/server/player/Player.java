/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server.player;

import clzola.pmnlgame.server.room.Room;
import clzola.pmnlgame.server.room.RoomManager;
import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Lazar
 */
public class Player {
    protected Connection connection;
    protected String playerId;
    protected String currentRoomId;
    protected PlayerLetter letter;
    protected int wins;
    protected boolean bot;
    
    protected Player(Connection connection, String playerId) {
        this.connection = connection;
        this.playerId = playerId;
        this.currentRoomId = null;
        this.letter = PlayerLetter.NONE;
        this.wins = 0;
        this.bot = false;
    }
    
    public void sendMessage(Object message) {
        this.connection.sendTCP(message);
    }
    
    public String getPlayerId() {
        return this.playerId;
    }
    
    public Room getCurrentRoom() {
        return RoomManager.getInstance().getRoom(this.currentRoomId);
    }
    
    public void setCurrentRoom(String roomId) {
        this.currentRoomId = roomId;
    }
    
    public PlayerLetter getLetter() {
        return this.letter;
    }
    
    public void assignLetter(PlayerLetter letter) {
        this.letter = letter;
    }
    
    public void addWin() {
        this.wins++;
    }
    
    public int getWins() {
        return this.wins;
    }
    
    public void resetPlayerStats() {
        this.letter = PlayerLetter.NONE;
        this.wins = 0;
    }
    
    public boolean isBot() {
        return this.bot;
    }
}
