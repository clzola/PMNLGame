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
public interface MessageHandler {
    public void onMessage(Connection connection, Object message );
}
