/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame;

import clzola.pmnlgame.authserver.NetworkListener;
import clzola.pmnlgame.authserver.ServerConfiguration;
import clzola.pmnlgame.authserver.User;
import clzola.pmnlgame.message.AuthServerMessages;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.share.RoomInfo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.sql2o.Sql2o;

/**
 *
 * @author Lazar
 */
public class PmnlGameAuthServer {
    private Server server;
    private ServerConfiguration configuration;
    
    public static Sql2o database = null;
        
    public PmnlGameAuthServer() throws IOException {
        this.configuration = new ServerConfiguration();
        Log.info(this.configuration.toString());
        
        Log.info("Preparing server for start.");
        server = new Server();
        
        Log.info("Connecting to database.");
        PmnlGameAuthServer.database = new Sql2o(
                "jdbc:mysql://"+configuration.DATABASE_SERVER+":"+configuration.DATABASE_PORT+"/"+configuration.DATABASE_SCHEME, 
                configuration.DATABASE_USER, configuration.DATABASE_PASSWORD);
        
        
        // Register messages.
        Log.info("Registrating messages.");
        Kryo kryo = server.getKryo();
        kryo.register(AuthServerMessages.AuthMessage.class);
        kryo.register(AuthServerMessages.AuthAcceptMessage.class);
        kryo.register(AuthServerMessages.AuthFailedMessage.class);
        kryo.register(ServerMessages.JoinServerMessage.class);
        kryo.register(ServerMessages.JoinRoomMessage.class);
        kryo.register(ServerMessages.CreateRoomMessage.class);
        kryo.register(ServerMessages.CreateJoinRoomMessage.class);
        kryo.register(ServerMessages.LeaveRoomMessage.class);
        kryo.register(RoomInfo[].class);
        kryo.register(RoomInfo.class);
        kryo.register(ServerMessages.ListRoomsMessage.class);
        kryo.register(ServerMessages.ErrorMessage.class);
        
        Log.info("Attaching NetworkListener object.");
        server.addListener(new NetworkListener());
        server.bind( configuration.SERVER_PORT );
        server.start();
    }
    
    public static void main(String[] args) {
        try {
            PmnlGameAuthServer pmnlGameAuthServer = new PmnlGameAuthServer();
            Log.set(Log.LEVEL_DEBUG);
        } catch (IOException ex) {
            Log.error("ERROR!!", ex);
        }
    }
}
