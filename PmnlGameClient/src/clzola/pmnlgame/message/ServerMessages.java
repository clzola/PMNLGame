package clzola.pmnlgame.message;

import clzola.pmnlgame.share.RoomInfo;

public class ServerMessages {
    public static class JoinServerMessage { public String username; }
    public static class JoinRoomMessage { public String roomId, password; }
    public static class CreateRoomMessage { public String roomId, password; public int playersLimit; public boolean isVisible; }
    public static class CreateJoinRoomMessage { public String roomId, password; public int playersLimit; public boolean isVisible; }
    public static class LeaveRoomMessage { }
    public static class ListRoomsMessage { public RoomInfo[] rooms; }
    public static class ErrorMessage { public String message; }
}
