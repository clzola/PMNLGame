/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame;

import clzola.pmnlgame.client.NetworkListener;
import clzola.pmnlgame.client.ServerEndpointManager;
import clzola.pmnlgame.client.gui.ClientWindow;
import clzola.pmnlgame.client.gui.GameView;
import clzola.pmnlgame.client.gui.LobbyView;
import clzola.pmnlgame.client.gui.LoginView;
import clzola.pmnlgame.message.AuthServerMessages;
import clzola.pmnlgame.message.RoomMessages;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.share.RoomInfo;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.skin.WebLafSkin;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Lazar
 */
public class PmnlGameClient {
    public static void main(String[] args) throws IOException {
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) { javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ClientWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        Log.set(Log.LEVEL_DEBUG);
        // Intialize LookAndFeel
        WebLookAndFeel.install();
        
        // Create ServerEndpointManager object
        ServerEndpointManager.getInstance();
        
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ClientWindow window = new ClientWindow();
                Client client = new Client();  
                
                client.addListener(new NetworkListener(client, window));
                client.start();

                // Register messages.
                Kryo kryo = client.getKryo();
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
        
                window.attachClient(client);
                window.addView(new LoginView(window));
                window.addView(new LobbyView(window));
                window.addView(new GameView(window));
                window.setStartView("login");
                window.initialize();
                
            }
        });
    }
}
