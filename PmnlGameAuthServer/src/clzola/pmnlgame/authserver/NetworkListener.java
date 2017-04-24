/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.authserver;

import clzola.pmnlgame.PmnlGameAuthServer;
import clzola.pmnlgame.message.AuthServerMessages;
import clzola.pmnlgame.message.ServerMessages;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.FrameworkMessage;
import com.esotericsoftware.kryonet.Listener;

/**
 *
 * @author Lazar
 */
public class NetworkListener extends Listener {
    @Override public void received(Connection connection, Object message) {
        super.received(connection, message);

        if( message instanceof FrameworkMessage.KeepAlive )
            return;

        if( message instanceof AuthServerMessages.AuthMessage ) {
            AuthServerMessages.AuthMessage auth = (AuthServerMessages.AuthMessage) message;

            /*User user = null;
            String userSql = "SELECT * FROM users WHERE (username = :username OR email = :email) AND password = :password";
            try( org.sql2o.Connection database = PmnlGameAuthServer.database.open() ) {
                user = database.createQuery(userSql)
                            .addParameter("username", auth.username)
                            .addParameter("email", auth.username)
                            .addParameter("password", auth.password)
                            .executeAndFetchFirst(User.class);
            }

            if( user == null ) {
                AuthServerMessages.AuthFailedMessage failed = new AuthServerMessages.AuthFailedMessage();
                failed.error = "Wrong username (or emial) or password.";
                connection.sendTCP(failed);
                return;
            }

            if( user.logged_in ) {
                AuthServerMessages.AuthFailedMessage failed = new AuthServerMessages.AuthFailedMessage();
                failed.error = "You are already logged in from different computer.";
                connection.sendTCP(failed);
                return;
            }*/

            AuthServerMessages.AuthAcceptMessage accept = new AuthServerMessages.AuthAcceptMessage();
            connection.sendTCP(accept);
        } else {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "Unknown request to AuthServer.";
            connection.sendTCP(error);
            //connection.close();
        }
    }

    @Override public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override public void connected(Connection connection) {
        super.connected(connection);
    }

}
