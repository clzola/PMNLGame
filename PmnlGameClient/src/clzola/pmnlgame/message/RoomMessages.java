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
    public static class PlayerJoinedMessage { public String playerId; public int index; }
    public static class PlayerLeftMessage { public String playerId; public int index; }
    public static class YouAreAdminMessage {}
    public static class ChatMessage { public boolean info; public String playerId, chatMessage; }
    public static class YouAreNextMessage {}
    public static class PlayMessage { public int x, y, index; }
    public static class PlayersListMessage { public String[] players; public int[] indexes; }
    public static class LettersListMessage { public int[] x, y, idx; public int nextPlayer; }
    public static class StartGameMessage {}
    public static class PauseGameMessage {}
    public static class UnpauseGameMessage {}
    public static class CloseGameMessage {}
    public static class RestartGameMessage {}
    public static class GameStartedMessage {}
    public static class GamePausedMessage {}
    public static class GameUnpausedMessage {}
    public static class GameRestartedMessage {}
    public static class WinnerMessage { public String playerId; public int index; }
    public static class ChangePasswordMessage { public String newPassword; }
    public static class AddOPMessage { public int index; }
}
