/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server.room;

import clzola.pmnlgame.server.player.Player;
import java.util.HashMap;

/**
 *
 * @author Lazar
 */
public class ServerLobby extends Room {
    private final HashMap<String, Player> players;
    
    public ServerLobby() {
        super("@ServerLobby", null, 0, false);
        this.players = new HashMap<>();
    }
    
    @Override public synchronized boolean allowPlayerJoin(Player player, String password) { return true; }
    @Override protected synchronized void playerJoined(Player player) { }
    @Override protected synchronized void playerLeft(Player player) { }
    @Override public synchronized void gotMessage(Object message, Player player) { }
    
    @Override public synchronized void insertPlayer(Player player) {
        player.setCurrentRoom(this.roomId);
        this.players.put(player.getPlayerId(), player);
        this.playersCount++;
    }
    
    @Override public synchronized void dropPlayer(Player player) {
        player.setCurrentRoom(null);
        this.players.remove(player.getPlayerId());
        this.playersCount--;
    }
}
