/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server.room;

import clzola.pmnlgame.message.RoomMessages;
import clzola.pmnlgame.message.ServerMessages;
import clzola.pmnlgame.server.player.Player;
import clzola.pmnlgame.server.player.PlayerLetter;

/**
 *
 * @author Lazar
 */
public class Room {
    protected String roomId;
    protected int playersLimit;
    protected int playersCount;
    protected String password;
    protected boolean visible;
    
    private Player[] players;
    private int lastSlotIndex;
    private boolean gamePaused;
    private boolean gameStarted;
    private int adminIndex;
    private int nextPlayerIndex;
    private PlayerLetter[][] gameTable;
    private int numberOfPlacedLetters;
    
    public Room(String roomId, String password, int playerLimit, boolean isVisible) {
        this.roomId = roomId;
        this.password = password;
        this.playersLimit = playerLimit;
        this.playersCount = 0;
        this.visible = isVisible;
        
        this.players = new Player[playerLimit];
        this.gamePaused = false;
        this.gameStarted = false;
        this.adminIndex = -1;
        this.nextPlayerIndex = 0;
        this.gameTable = new PlayerLetter[15][15];
        this.numberOfPlacedLetters = 0;
    }
    
    public synchronized void startGame() {
        nextPlayerIndex = 0;
        gameStarted = true;
        gamePaused = false;
        numberOfPlacedLetters = 0;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                gameTable[i][j] = PlayerLetter.NONE;
            }
        }
        
        broadcast(new RoomMessages.GameStartedMessage());
        players[nextPlayerIndex].sendMessage(new RoomMessages.YouAreNextMessage());
        nextPlayerIndex = (nextPlayerIndex + 1 == 4 ? 0 : nextPlayerIndex+1);
    }
    
    public synchronized void restartGame() {
        nextPlayerIndex = 0;
        gameStarted = true;
        gamePaused = false;
        numberOfPlacedLetters = 0;

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                gameTable[i][j] = PlayerLetter.NONE;
            }
        }
        
        players[nextPlayerIndex].sendMessage(new RoomMessages.YouAreNextMessage());
        nextPlayerIndex = (nextPlayerIndex + 1 == 4 ? 0 : nextPlayerIndex+1);
    }
    
    public synchronized void closeGame() {
        
    }
    
    public synchronized boolean allowPlayerJoin(Player player, String password) { 
        if (this.playersCount == this.playersLimit) {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "Room is full!";
            player.sendMessage(error);
            return false;
        }

        if (this.password == null) {
            return true;
        }

        if (!password.equals(this.password)) {
            ServerMessages.ErrorMessage error = new ServerMessages.ErrorMessage();
            error.message = "Wrong password!";
            player.sendMessage(error);
            return false;
        }

        return true;
    }
    
    protected synchronized void playerJoined(Player player) { 
        int k = lastSlotIndex;
        if (adminIndex == -1) {
            adminIndex = k;
            player.sendMessage(new RoomMessages.YouAreAdminMessage());
        }

        player.resetPlayerStats();
        player.assignLetter(integerToLetter(k));

        RoomMessages.PlayerJoinedMessage playerJoined = new RoomMessages.PlayerJoinedMessage();
        playerJoined.playerId = player.getPlayerId();
        playerJoined.index = k;
        broadcast(playerJoined);

        if (playersCount == playersLimit) {
            players[adminIndex].sendMessage(new RoomMessages.StartGameMessage());
        }
        
        RoomMessages.PlayersListMessage playersList = new RoomMessages.PlayersListMessage();
        playersList.players = new String[4];
        playersList.indexes = new int[4];
        int iiNextPlayerInList = 0;
        for(Player p : this.players) {
            if( p != null ) {
                System.out.println(p.getPlayerId());
                playersList.players[iiNextPlayerInList] = p.getPlayerId();
                playersList.indexes[iiNextPlayerInList] = letterToInteger(p.getLetter());
                iiNextPlayerInList++;
            }
        }
        
        player.sendMessage(playersList);

        if (gameStarted) {
            RoomMessages.LettersListMessage letters = new RoomMessages.LettersListMessage();
            letters.x = new int[numberOfPlacedLetters];
            letters.y = new int[numberOfPlacedLetters];
            letters.idx = new int[numberOfPlacedLetters];
            letters.nextPlayer = nextPlayerIndex;
            
            int iNextLetter = 0;
            for (int i = 0; i < 15; i++) {
                for (int j = 0; j < 15; j++) {
                    if (gameTable[i][j] != PlayerLetter.NONE) {
                        letters.x[iNextLetter] = j;
                        letters.y[iNextLetter] = i;
                        letters.idx[iNextLetter] = letterToInteger(gameTable[i][j]);
                        iNextLetter++;
                    }
                }
            }

            player.sendMessage(letters);
        }

        if (gamePaused && playersCount == playersLimit) {
            // unpause the game
            gamePaused = false;
            players[nextPlayerIndex].sendMessage(new RoomMessages.YouAreNextMessage());
            nextPlayerIndex = ++nextPlayerIndex % playersLimit;
        }
    }
    
    protected synchronized void playerLeft(Player player) {
        // reassign admin if needed
        // reset player
        // set game pause to true if games has started

        // if this is last player there is no point doing anything
        // because room will be destroyed
        if (playersCount == 0) {
            return;
        }
        
        RoomMessages.PlayerLeftMessage playerLeft = new RoomMessages.PlayerLeftMessage();
        playerLeft.playerId = player.getPlayerId();
        playerLeft.index = letterToInteger(player.getLetter());
        broadcast(playerLeft);
        player.resetPlayerStats();

        if (playerLeft.index == adminIndex) {
            // find new admin
            int jPlayer = 0;
            for (Player player1 : players) {
                if (player1 != null) {
                    adminIndex = jPlayer;
                    break;
                }
                
                jPlayer++;
            }

            players[adminIndex].sendMessage(new RoomMessages.YouAreAdminMessage());
        }

        // pause the game if
        if (gameStarted) {
            gamePaused = true;
            broadcast(new RoomMessages.GamePausedMessage());

            // reset next player.
            nextPlayerIndex--;
            if (nextPlayerIndex < 0) {
                nextPlayerIndex = 3;
            }
        }
    }
    
    public synchronized void gotMessage(Object message, Player player) {
        switch(message.getClass().getSimpleName()) {
            case "ChatMessage":
                {
                    RoomMessages.ChatMessage m1 = (RoomMessages.ChatMessage) message;
                    m1.playerId = player.getPlayerId();
                    broadcast(m1);
                }
                break;
            case "StartGameMessage":
                startGame();
                break;
            case "PlayMessage":
                {
                    RoomMessages.PlayMessage m1 = (RoomMessages.PlayMessage) message; 
                    m1.index = ( nextPlayerIndex - 1 == -1  ? 3 : nextPlayerIndex - 1);
                    gameTable[m1.y][m1.x] = integerToLetter(m1.index);
                    broadcast(m1);
                    
                    if( checkForWinner() ) {
                        int winnerIndex = m1.index;
                        RoomMessages.WinnerMessage m2 = new RoomMessages.WinnerMessage();
                        m2.index = winnerIndex;
                        m2.playerId = players[winnerIndex].getPlayerId();
                        broadcast(m2);
                    } else {
                        players[nextPlayerIndex].sendMessage(new RoomMessages.YouAreNextMessage());
                        nextPlayerIndex = ( nextPlayerIndex + 1 == 4 ? 0 : nextPlayerIndex + 1 );
                    }
                }
                break;
            case "RestartGameMessage":
                broadcast(new RoomMessages.GameRestartedMessage());
                restartGame();
                break;
            case "ChangePasswordMessage":
                {
                    RoomMessages.ChangePasswordMessage m1 = (RoomMessages.ChangePasswordMessage) message;
                    this.password = m1.newPassword;
                }
                break;
            default:
                break;
        }
    }
    
    protected synchronized void broadcast(Object message) {
        for (Player player : players) {
            if( player != null ) {
                player.sendMessage(message);
            }
        }
    }
    
    private synchronized boolean checkForWinner() {
        for(int j=0; j<15; j++) {
            for(int i=0; i<15; i++)	{

                // NorthEast
                if( i >= 3 && j <= 11 && gameTable[i][j] != PlayerLetter.NONE) {
                    if( gameTable[i][j] == gameTable[i-1][j+1] && 
                            gameTable[i][j] == gameTable[i-2][j+2] && 
                            gameTable[i][j] == gameTable[i-3][j+3]
                    ) {
                            return true;
                    }
                }

                // East
                if( j <= 11 && gameTable[i][j] != PlayerLetter.NONE ) {
                    if( gameTable[i][j] == gameTable[i][j+1] && 
                            gameTable[i][j] == gameTable[i][j+2] && 
                            gameTable[i][j] == gameTable[i][j+3]
                    ) {
                            return true;
                    }
                }

                //SouthEast
                if( i <= 11 && j <= 11 && gameTable[i][j] != PlayerLetter.NONE ) {
                    if( gameTable[i][j] == gameTable[i+1][j+1] && 
                            gameTable[i][j] == gameTable[i+2][j+2] && 
                            gameTable[i][j] == gameTable[i+3][j+3]
                    ) {
                            return true;
                    }
                }

                //South
                if( i <= 11 && gameTable[i][j] != PlayerLetter.NONE ) {
                    if( gameTable[i][j] == gameTable[i+1][j] && 
                            gameTable[i][j] == gameTable[i+2][j] && 
                            gameTable[i][j] == gameTable[i+3][j]
                    ) {
                            return true;
                    }
                }

            }
        }

        return false;
    }
    
    public synchronized void insertPlayer(Player player) {
        player.setCurrentRoom(this.roomId);

        // find empty slot in room
        int index = 0;
        for (int i = 0; i < 4; i++) {
            if (players[i] == null) {
                index = i;
                lastSlotIndex = i;
                break;
            }
        }

        this.players[index] = player;
        this.playersCount++;
        this.playerJoined(player);
    }
    
    public synchronized void dropPlayer(Player player) {
        player.setCurrentRoom(null);
        this.playersCount--;
        this.players[letterToInteger(player.getLetter())] = null;

        this.playerLeft(player);
        if( playersCount == 0 )
            RoomManager.getInstance().removeRoom(this.roomId);
    }
    
    protected synchronized void destroy() {
        // disconnect all players with message "Room destroyed by Server"
    }
    
    private int letterToInteger(PlayerLetter l) {
        switch(l) {
            case NONE:
                return -1;
            case P:
                return 0;
            case M:
                return 1;
            case N:
                return 2;
            case L:
                return 3;
         
            default:
                return -1;
        }
    }
    
    private PlayerLetter integerToLetter(int k) {
        switch(k) {
            case 0: return PlayerLetter.P;
            case 1: return PlayerLetter.M;
            case 2: return PlayerLetter.N;
            case 3: return PlayerLetter.L;
            default: return PlayerLetter.NONE;
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public int getPlayersLimit() {
        return playersLimit;
    }

    public void setPlayersLimit(int playersLimit) {
        this.playersLimit = playersLimit;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean hasPassword() {
        return ( this.password != null );
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
}
