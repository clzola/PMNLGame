/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client;

import clzola.pmnlgame.client.gui.ClientWindow;
import clzola.pmnlgame.client.gui.GameView;
import clzola.pmnlgame.client.gui.LobbyView;
import clzola.pmnlgame.client.gui.LoginView;
import clzola.pmnlgame.message.AuthServerMessages;
import clzola.pmnlgame.message.RoomMessages;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.share.RoomInfo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Lazar
 */
public class NetworkListener extends Listener {
    private Client client;
    private ClientWindow window;
    
    public NetworkListener(Client c, ClientWindow w) {
        super();
        this.client = c;
        this.window = w;
    }
    
    @Override public void received(Connection connection, Object object) {
        super.received(connection, object);
        
        if( object instanceof AuthServerMessages.AuthAcceptMessage ) {
            client.close();
            client.stop();
            
            Client client1 = new Client();  
                
            client1.addListener(new NetworkListener(client, window));
            client1.start();
            
            // Register messages.
            Kryo kryo = client1.getKryo();
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
            kryo.register(String[].class);
            kryo.register(String.class);
            kryo.register(int[].class);
            kryo.register(int.class);
            kryo.register(RoomMessages.PlayerJoinedMessage.class);
            kryo.register(RoomMessages.PlayerLeftMessage.class);
            kryo.register(RoomMessages.YouAreAdminMessage.class);
            kryo.register(RoomMessages.ChatMessage.class);
            kryo.register(RoomMessages.YouAreNextMessage.class);
            kryo.register(RoomMessages.PlayMessage.class);
            kryo.register(RoomMessages.PlayersListMessage.class);
            kryo.register(RoomMessages.LettersListMessage.class);
            kryo.register(RoomMessages.StartGameMessage.class);
            kryo.register(RoomMessages.PauseGameMessage.class);
            kryo.register(RoomMessages.UnpauseGameMessage.class);
            kryo.register(RoomMessages.CloseGameMessage.class);
            kryo.register(RoomMessages.RestartGameMessage.class);
            kryo.register(RoomMessages.GameStartedMessage.class);
            kryo.register(RoomMessages.GamePausedMessage.class);
            kryo.register(RoomMessages.GameUnpausedMessage.class); 
            kryo.register(RoomMessages.GameRestartedMessage.class); 
            kryo.register(RoomMessages.WinnerMessage.class);
            kryo.register(RoomMessages.ChangePasswordMessage.class);
            
            window.attachClient(client1);
            window.getView("login").setClient(client1);
            window.getView("lobby").setClient(client1);
            window.getView("game").setClient(client1);
            
            ServerEndpoint server = ServerEndpointManager.getInstance().getSelectedServer();
            try {
                client1.connect(5000, server.serverAddress, server.gameServerPort);                
                ServerMessages.JoinServerMessage joinServerMessage = new ServerMessages.JoinServerMessage();
                joinServerMessage.username = ((LoginView)window.getView("login")).Username.getText().trim();
                client1.sendTCP(joinServerMessage);
            } catch (IOException ex) {
                Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else if ( object instanceof AuthServerMessages.AuthFailedMessage ) {
            client.close();
            AuthServerMessages.AuthFailedMessage message = (AuthServerMessages.AuthFailedMessage) object;
            JOptionPane.showMessageDialog(window, message.error, "Auth Failed", JOptionPane.ERROR_MESSAGE);
        } else if ( object instanceof ServerMessages.JoinRoomMessage ) {
            ServerMessages.JoinRoomMessage message = (ServerMessages.JoinRoomMessage) object;
            if( message.roomId.equals("@ServerLobby") )
                window.changeView("lobby");
            else window.changeView("game");
        } else if ( object instanceof ServerMessages.ListRoomsMessage ) {
            ServerMessages.ListRoomsMessage message = (ServerMessages.ListRoomsMessage) object;
            RoomInfoManager.getInstance().clear();
            for(RoomInfo room : message.rooms) {
                System.out.println(room);
                RoomInfoManager.getInstance().addRoomInfo(room);
            }
            ((LobbyView) window.getView("lobby")).refreshTableModel(RoomInfoManager.getInstance().all(null));
        } else if ( object instanceof ServerMessages.ErrorMessage ) {
            ServerMessages.ErrorMessage error = (ServerMessages.ErrorMessage) object;
            JOptionPane.showMessageDialog(window, error.message, "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            GameView view = (GameView) window.getView("game");
            view.processMessage(object);
        }
    }

    @Override public void disconnected(Connection connection) {
        
    }

    @Override public void connected(Connection connection) {
        
    }
    
}
