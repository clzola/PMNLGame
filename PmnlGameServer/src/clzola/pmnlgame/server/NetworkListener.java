/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server;

import clzola.pmnlgame.PmnlGameServer;
import clzola.pmnlgame.message.AuthServerMessages;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.server.player.Player;
import clzola.pmnlgame.server.player.PlayerManager;
import clzola.pmnlgame.server.room.Room;
import clzola.pmnlgame.share.RoomInfo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

/**
 *
 * @author Lazar Radinović <lazar.radinovic@gmail.com>
 */
public class NetworkListener extends Listener {

    /**
     *
     * @param connection
     */
    @Override public void connected(Connection connection) {
        super.connected(connection);
    }

    /**
     *
     * @param connection
     */
    @Override public void disconnected(Connection connection) {
        super.disconnected(connection);
        Player player = PlayerManager.getInstance().getPlayer(connection.getID());
        if(player != null) {
            Room room = player.getCurrentRoom();
            if( room != null ) {
                room.dropPlayer(player);
            }
            PlayerManager.getInstance().removePlayer(connection.getID());

            /*String sql = "UPDATE users SET logged_in = 0 WHERE username = :username";
            try( org.sql2o.Connection database = PmnlGameServer.database.open() ) {
                database.createQuery(sql).addParameter("username", player.getPlayerId())
                        .executeUpdate();
            }*/
        }

    }

    /**
     *
     * @param connection
     * @param message
     */
    @Override public void received(Connection connection, Object message ) {
        super.received(connection, message);
        if( message instanceof FrameworkMessage.KeepAlive )
            return;
        ServerDispatcher.getInstance().dispatchMessage(connection, message);
    }
}
