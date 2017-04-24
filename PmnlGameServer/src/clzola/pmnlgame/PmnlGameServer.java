/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame;

import clzola.pmnlgame.message.AuthServerMessages;
import clzola.pmnlgame.message.RoomMessages;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.server.MessageHandler;
import clzola.pmnlgame.server.NetworkListener;
import clzola.pmnlgame.server.ServerConfiguration;
import clzola.pmnlgame.server.ServerDispatcher;
import clzola.pmnlgame.server.player.Player;
import clzola.pmnlgame.server.player.PlayerManager;
import clzola.pmnlgame.server.room.Room;
import clzola.pmnlgame.server.room.RoomManager;
import clzola.pmnlgame.share.RoomInfo;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
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
public final class PmnlGameServer {
    private final Server server;
    private final ServerConfiguration configuration;
    private final ServerDispatcher serverDispatcher;
    private final PlayerManager playerManager;
    private final RoomManager roomManager;

    public static Sql2o database = null;

    public PmnlGameServer() throws IOException {
        configuration = new ServerConfiguration();
        Log.info(this.configuration.toString());

        serverDispatcher = ServerDispatcher.getInstance();

        Log.info("Preparing server for start.");
        server = new Server();

        Log.info("Connecting to database.");
        /*
        PmnlGameServer.database = new Sql2o(
                "jdbc:mysql://"+configuration.DATABASE_SERVER+":"+configuration.DATABASE_PORT+"/"+configuration.DATABASE_SCHEME,
                configuration.DATABASE_USER, configuration.DATABASE_PASSWORD);
        */

        // set all logged in to 0

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

        // Register message handlers;
        serverDispatcher.addMessageHandler(ServerMessages.JoinServerMessage.class.getSimpleName(), new MessageHandler() {
            @Override public void onMessage(Connection connection, Object message) {
                onJoinServerMessage(connection, (ServerMessages.JoinServerMessage) message);
            }
        });

        serverDispatcher.addMessageHandler(ServerMessages.JoinRoomMessage.class.getSimpleName(), new MessageHandler() {
            @Override public void onMessage(Connection connection, Object message) {
                onJoinRoomMessage(connection, (ServerMessages.JoinRoomMessage) message);
            }
        });

        serverDispatcher.addMessageHandler(ServerMessages.CreateRoomMessage.class.getSimpleName(), new MessageHandler() {
            @Override public void onMessage(Connection connection, Object message) {
                onCreateRoomMessage(connection, (ServerMessages.CreateRoomMessage) message);
            }
        });

        serverDispatcher.addMessageHandler(ServerMessages.CreateJoinRoomMessage.class.getSimpleName(), new MessageHandler() {
            @Override public void onMessage(Connection connection, Object message) {
                System.out.println("RECV");
                onCreateJoinRoomMessage(connection, (ServerMessages.CreateJoinRoomMessage) message);
            }
        });

        serverDispatcher.addMessageHandler(ServerMessages.LeaveRoomMessage.class.getSimpleName(), new MessageHandler() {
            @Override public void onMessage(Connection connection, Object message) {
                onLeaveRoomMessage(connection, (ServerMessages.LeaveRoomMessage) message);
            }
        });

        serverDispatcher.addMessageHandler(ServerMessages.ListRoomsMessage.class.getSimpleName(), new MessageHandler() {
            @Override public void onMessage(Connection connection, Object message) {
                onListRoomsMessage(connection, (ServerMessages.ListRoomsMessage) message);
            }
        });

        this.playerManager = PlayerManager.getInstance();
        this.roomManager = RoomManager.getInstance();
        RoomManager.createServerLobby();

        Log.info("Attaching NetworkListener object.");
        server.addListener(new NetworkListener());

        server.bind( configuration.SERVER_PORT );
        serverDispatcher.start();
        server.start();
    }

    public static void main(String[] args) {
        try {
            PmnlGameServer pmnlGameServer = new PmnlGameServer();
            Log.set(Log.LEVEL_DEBUG);
        } catch (IOException ex) {
            Logger.getLogger(PmnlGameServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Message handlers
    public void onJoinServerMessage(Connection connection, ServerMessages.JoinServerMessage message) {
        Player player = PlayerManager.getInstance().createPlayer(connection, message.username);
    }

    public void onJoinRoomMessage(Connection connection, ServerMessages.JoinRoomMessage message) {
        Player player = PlayerManager.getInstance().getPlayer(connection.getID());
        PlayerManager.getInstance().movePlayerToRoom(player, message.roomId, message.password);
    }

    public void onCreateRoomMessage(Connection connection, ServerMessages.CreateRoomMessage message){
        // check if room with same name exists
        // if room with same name exists sendMessage @Error "Room with same name exists!" exit
        // create room, error check
        // sendMessage @CreateRoom "success" exit



        Room room = RoomManager.getInstance().getRoom(message.roomId);
        if( room != null ) {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "Room with same name already exists.";
            connection.sendTCP(error);
            return;
        }

        room = RoomManager.getInstance().createRoom(message.roomId, message.password, message.playersLimit, message.isVisible);
        if( room == null ) {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "There was an error while creating your room.";
            connection.sendTCP(error);
            return;
        }

        ServerMessages.CreateRoomMessage createRoomMessage = new ServerMessages.CreateRoomMessage();
        createRoomMessage.roomId = message.roomId;
        createRoomMessage.password = message.password;
        createRoomMessage.playersLimit = message.playersLimit;
        createRoomMessage.isVisible = message.isVisible;
        connection.sendTCP(createRoomMessage);
    }

    public void onCreateJoinRoomMessage(Connection connection, ServerMessages.CreateJoinRoomMessage message) {
        // check if room with same name exists
        // if room with same name exists sendMessage @Error "Room with same name exists!" exit
        // create room, error check
        // join room

        System.out.println("0");

        Room room = RoomManager.getInstance().getRoom(message.roomId);
        if( room != null ) {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "Room with same name already exists.";
            connection.sendTCP(error);
            return;
        }

         System.out.println("1");

        room = RoomManager.getInstance().createRoom(message.roomId, message.password, message.playersLimit, message.isVisible);
        if( room == null ) {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "There was an error while creating your room.";
            connection.sendTCP(error);
            return;
        }

         System.out.println("2");

        Player player = PlayerManager.getInstance().getPlayer(connection.getID());
        System.out.println(player);
        PlayerManager.getInstance().movePlayerToRoom(player, message.roomId, message.password);
    }

    public void onLeaveRoomMessage(Connection connection, ServerMessages.LeaveRoomMessage message) {
        Player player = PlayerManager.getInstance().getPlayer(connection.getID());
        PlayerManager.getInstance().movePlayerToRoom(player, "@ServerLobby", null);
    }

    public void onListRoomsMessage(Connection connection, ServerMessages.ListRoomsMessage messages) {
        Room[] rooms = RoomManager.getInstance().getVisibleRooms();
        RoomInfo[] roomInfo = new RoomInfo[rooms.length];

        int iRoomInfo = 0;
        for(Room room : rooms) {
            roomInfo[iRoomInfo] = new RoomInfo(
                    room.getRoomId(),
                    room.getPlayersCount(),
                    room.hasPassword()
            );
        }

        ServerMessages.ListRoomsMessage listRoomsMessage = new ServerMessages.ListRoomsMessage();
        listRoomsMessage.rooms = roomInfo;
        connection.sendTCP(listRoomsMessage);
    }
}
