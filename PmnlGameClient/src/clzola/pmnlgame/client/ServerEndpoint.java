/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client;

/**
 *
 * @author Lazar
 */
public class ServerEndpoint {
    public String   serverName;
    public String   serverAddress;
    public int      gameServerPort;
    public int      authServerPort;
 
    public ServerEndpoint() {
        this(null, null, 0, 0);
    }

    public ServerEndpoint(String serverName, String serverAddress, int gameServerPort, int authServerPort) {
        this.serverName = serverName;
        this.serverAddress = serverAddress;
        this.gameServerPort = gameServerPort;
        this.authServerPort = authServerPort;
    }
    
    public String toString() {
        return this.serverName + ", " + this.serverAddress + ", " 
                + this.gameServerPort + ", " + this.authServerPort;
    }
}
