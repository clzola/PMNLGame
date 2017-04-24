/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client;

import clzola.pmnlgame.share.RoomInfo;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Lazar
 */
public class RoomInfoManager {
    private ArrayList<RoomInfo> rooms;
    private static RoomInfoManager instance = null;
    private RoomComparator roomComparator;
    
    private RoomInfoManager() {
        this.rooms = new ArrayList<>();
        this.roomComparator = new RoomComparator();
    }
    
    public static RoomInfoManager getInstance() {
        if( RoomInfoManager.instance == null )
            RoomInfoManager.instance = new RoomInfoManager();
        return RoomInfoManager.instance;
    }
    
    public void addRoomInfo(RoomInfo room) {
        this.rooms.add(room);
        rooms.sort(roomComparator);
    }
    
    public ArrayList<RoomInfo> all( String like ) {
        ArrayList<RoomInfo> rooms = new ArrayList<>();
        for(RoomInfo room : this.rooms) {
            if( like == null ) 
                rooms.add(room);
            else if ( room.roomId.toLowerCase().startsWith(like) )
                rooms.add(room);
        }
        
        return rooms;
    }
    
    public void clear() {
        this.rooms.clear();
    }
    
    private class RoomComparator implements Comparator<RoomInfo> {
        @Override public int compare(RoomInfo r1, RoomInfo r2) {
            return r1.roomId.toLowerCase().compareTo(r2.roomId.toLowerCase());
        } 
    }
}
