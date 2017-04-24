/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server.room;

import clzola.pmnlgame.server.player.Player;
import com.esotericsoftware.minlog.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 *
 * @author Lazar
 */
public class RoomManager {
    private final HashMap<String, Room> rooms;
    private int visibleRoomCount;
    
    private static RoomManager instance = null;
    
    public static RoomManager getInstance() {
        if( RoomManager.instance == null )
            RoomManager.instance = new RoomManager();
        return RoomManager.instance;
    }
    
    public static void createServerLobby() {
        RoomManager.getInstance().rooms.put("@ServerLobby", new ServerLobby());
        Log.info("[RoomManager] Created ServerLobby room.");
    }
    
    private RoomManager() {
        this.rooms = new HashMap<>();
        this.visibleRoomCount = 0;
        Log.info("[RoomManager] Created RoomManager object.");
    }
    
    public synchronized Room createRoom(String roomId, String password, int playerCount, boolean isVisible) {        
        Room room = new Room(roomId, password, playerCount, isVisible);
        this.rooms.put(roomId, room);
        if(isVisible)
            this.visibleRoomCount++;
        Log.info("[RoomManager] Created new room with ID: " + roomId);
        return room;
    }
    
    public synchronized Room getRoom(String roomId) {
        return this.rooms.get(roomId);
    }
    
    public synchronized Room removeRoom(String roomId) {
        Room room = this.rooms.remove(roomId);
        if(room == null)
            return null;
        
        room.destroy();
        if( room.visible )
            this.visibleRoomCount--;
        
        Log.info("[RoomManager] Deleted room with ID: " + roomId);
        return room;
    }
    
    public synchronized int getVisibleRoomCount() {
        return this.visibleRoomCount;
    }
    
    public synchronized Room[] getVisibleRooms() {
        int iRoom = 0;
        Room[] rooms = new Room[this.visibleRoomCount];
        
        Iterator<Entry<String, Room>> itRoom = this.rooms.entrySet().iterator();
        while(itRoom.hasNext()) {
            Room room = itRoom.next().getValue();
            System.out.println(room.roomId);
            if( room.visible )
                rooms[iRoom++] = room;
        }
        
        return rooms;
    }
    
    public synchronized void dispatchMessageToRoom(String roomId, Object message, Player player) {
        Room room = this.rooms.get(roomId);
        if( room != null ) 
            room.gotMessage(message, player);
    }
}
