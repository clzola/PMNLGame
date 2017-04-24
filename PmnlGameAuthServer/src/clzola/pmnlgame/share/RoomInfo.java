package clzola.pmnlgame.share;

public class RoomInfo {
    public String  roomId;
    public int     playersCount;
    public boolean hasPassword;
    
    public RoomInfo() {
        this(null, 0, false);
    }
    
    public RoomInfo(String roomId, int playersCount, boolean hasPassword) {
        this.roomId = roomId;
        this.playersCount = playersCount;
        this.hasPassword = hasPassword;
    }  
    
    @Override public String toString() {
        return "RoomInfo.class\n" +
               "  roomId = " + this.roomId + "\n" + 
               "  playersCount = " + this.playersCount + "\n" + 
               "  hasPassword = " + (this.hasPassword ? "true" : "false") + "\n";
    }
}
