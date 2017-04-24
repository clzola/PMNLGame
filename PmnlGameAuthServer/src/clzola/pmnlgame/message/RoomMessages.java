/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.message;

/**
 *
 * @author Lazar
 */
public class RoomMessages {
    public static class PlayerJoinedMessage { String playerId; }
    public static class PlayerLeftMessage { String playerId; }
    public static class YouAreAdminMessage {}
    public static class YouCanStartGameMessage {}
    public static class ChatMessage { boolean info; String playerId, chatMessage; }
    public static class YouAreNextMessage {}
    public static class PlayersListMessage { String[] players; }
    public static class StartGameMessage {}
    public static class PauseGameMessage {}
    public static class UnpauseGameMessage {}
    public static class CloseGameMessage {}
    public static class RestartGameMessage {}
    public static class GameStartedMessage {}
    public static class GamePausedMessage {}
    public static class GameUnpausedMessage {}
    public static class GameRestartedMessage {}
}
