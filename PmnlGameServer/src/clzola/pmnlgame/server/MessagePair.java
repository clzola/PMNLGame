/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server;

import com.esotericsoftware.kryonet.Connection;

/**
 *
 * @author Lazar
 */
public class MessagePair {
    public Connection connection;
    public Object message;
    
    public MessagePair() {
        this(null, null);
    }
    
    public MessagePair(Connection connection, Object message) {
        this.connection = connection;
        this.message = message;
    }
}
