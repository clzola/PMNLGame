/**    
 * PMNL Game, A board game similar to TicTacToe played on 15x15 grid
 * by 4 players.
 * Copyright (C) 2014  Lazar Radinović <lazar.radinovic@gmail.com>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package clzola.pmnlgame.server;

import clzola.pmnlgame.server.player.Player;
import clzola.pmnlgame.server.player.PlayerManager;
import clzola.pmnlgame.server.room.Room;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @class ServerDispatcher
 * 
 * @author Lazar Radinović <lazar.radinovic@gmail.com>
 * @version 1.0.0
 */
public class ServerDispatcher extends Thread {
    private final HashMap<String, MessageHandler> messageHandlers;
    private final LinkedList<MessagePair> messageQueue;
    private static ServerDispatcher instance = null;
    
    public static ServerDispatcher getInstance() {
        if( ServerDispatcher.instance == null )
            ServerDispatcher.instance = new ServerDispatcher();
        return ServerDispatcher.instance;
    }
    
    private ServerDispatcher() {
        this.messageHandlers = new HashMap<>();
        this.messageQueue = new LinkedList<>();
        this.setName("ServerDispatcher");
        Log.info("Created ServerDispatcher object.");
    }
    
    public synchronized void dispatchMessage(Connection connection, Object message) {
        this.messageQueue.add( new MessagePair(connection, message) );
        notify();
    }
    
    private synchronized MessagePair getNextMessageFromQueue() 
    throws InterruptedException {
        while(messageQueue.size() == 0)
            wait();
        return messageQueue.poll();
    }
    
    public synchronized void addMessageHandler(String messageType, MessageHandler handler) {
        if( this.messageHandlers.containsKey(messageType) ) 
            Log.info("Error while adding messageHandler for " + messageType + ". Message handler for this type of message already exists.");
        else {
            this.messageHandlers.put(messageType, handler);
            Log.info("Added message handler for " + messageType + ".");
        }
    }
    
    @Override public void run() {
        try {
            while(true) {
                MessagePair messagePair = getNextMessageFromQueue();
                MessageHandler handler = this.messageHandlers.get(messagePair.message.getClass().getSimpleName());

                if( handler == null ) {
                    System.out.println("No meessage hanlder for " + messagePair.message.getClass().getSimpleName());
                    Player player = PlayerManager.getInstance().getPlayer(messagePair.connection.getID());
                    Room room = player.getCurrentRoom();
                    room.gotMessage(messagePair.message, player);
                } else handler.onMessage(messagePair.connection, messagePair.message);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerDispatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
