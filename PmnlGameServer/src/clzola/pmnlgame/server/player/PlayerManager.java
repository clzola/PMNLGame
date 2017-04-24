/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server.player;

import clzola.pmnlgame.PmnlGameServer;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.server.room.Room;
import clzola.pmnlgame.server.room.RoomManager;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import java.util.HashMap;

/**
 *
 * @author Lazar
 */
public class PlayerManager {
    private final HashMap<Integer, Player> players;
    private static PlayerManager instance = null;

    private PlayerManager() {
        this.players = new HashMap<>();
        Log.info("Created PlayerManager object.");

    }

    public static PlayerManager getInstance() {
        if( PlayerManager.instance == null )
            PlayerManager.instance = new PlayerManager();
        return PlayerManager.instance;
    }

    public synchronized Player createPlayer(Connection connection, String playerId) {
        Player player = new Player(connection, playerId);
        this.players.put(connection.getID(), player);
        movePlayerToRoom(player, "@ServerLobby", null);

        /*
        String sql = "UPDATE users SET logged_in = 1 WHERE username = :username";
        try( org.sql2o.Connection database = PmnlGameServer.database.open() ) {
            database.createQuery(sql).addParameter("username", playerId).executeUpdate();
        }
        */

        return player;
    }

    public synchronized void addPlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public synchronized Player getPlayer(int playerId) {
        return this.players.get(playerId);
    }

    public synchronized Player removePlayer(int playerId) {
        Player player = this.players.remove(playerId);
        if( player == null )
            return null;

        removePlayerFromCurrentRoom(player);
        return player;
    }

    public synchronized void removePlayerFromCurrentRoom(Player player) {
        if( player.currentRoomId == null )
            return;

        Room room = RoomManager.getInstance().getRoom(player.currentRoomId);
        room.dropPlayer(player);
    }

    public synchronized void movePlayerToRoom(Player player, String roomId, String password) {
        if( roomId == null )
            return;

        System.out.println("3");

        Room room = RoomManager.getInstance().getRoom(roomId);
        if( room != null ) {
            /**
             * No need for else and error response.
             * Room::allowPlayerJoin does this for you.
             */
            if( room.allowPlayerJoin(player, password) ) {
                removePlayerFromCurrentRoom(player);

                ServerMessages.JoinRoomMessage message = new ServerMessages.JoinRoomMessage();
                message.roomId = roomId;
                message.password = null;

                player.sendMessage(message);
                room.insertPlayer(player);

            }
        } else {
            ServerMessages.ErrorMessage message = new ServerMessages.ErrorMessage();
            message.message = "Room with name does \""+roomId+"\" not exist.";
            player.sendMessage(message);
        }
    }
}
