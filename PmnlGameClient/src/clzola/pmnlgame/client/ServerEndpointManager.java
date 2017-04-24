/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lazar
 */
public final class ServerEndpointManager {
    private final HashMap<String, ServerEndpoint> servers;
    private ServerEndpoint selected;
    
    public static String serversCSVFile = "servers.csv";
    private static ServerEndpointManager instance = null;
    
    public static ServerEndpointManager getInstance() {
        if( ServerEndpointManager.instance == null )
            ServerEndpointManager.instance = new ServerEndpointManager();
        return ServerEndpointManager.instance;
    }
    
    private ServerEndpointManager() {
        this.servers = new HashMap<>();
        this.selected = null;
        
        readServersCSVFile();
    }
    
    public void addServer(ServerEndpoint server) {
        this.servers.put(server.serverName, server);
    }
    
    public ServerEndpoint getServer(String serverName) {
        return this.servers.get(serverName);
    }
    
    public ServerEndpoint getSelectedServer() {
        return this.selected;
    }
    
    public void setSelectedServer(ServerEndpoint server) {
        this.selected = server;
    } 
    
    public ServerEndpoint removeServer(String serverName) {
        return this.servers.remove(serverName);
    }
    
    public ServerEndpoint[] all() {
        int iServer = 0;
        ServerEndpoint[] servers = new ServerEndpoint[this.servers.size()];
        Iterator<Entry<String, ServerEndpoint>> itServer = this.servers.entrySet().iterator();
        while(itServer.hasNext())
            servers[iServer++] = itServer.next().getValue();
        return servers;
    }
    
    public void readServersCSVFile() {
        try {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ServerEndpointManager.serversCSVFile))) {
                String line = null;
                while((line = bufferedReader.readLine()) != null) {
                    String[] aServer = line.split(",");
                    if( aServer.length < 4 )
                        continue;
                    ServerEndpoint server = new ServerEndpoint(aServer[0].trim(), aServer[1].trim(),
                            Integer.parseInt(aServer[2].trim()), Integer.parseInt(aServer[3].trim()));
                    
                    if( aServer.length == 5 && aServer[4].trim().equals("selected")) {
                        
                        this.selected = server;
                    }
                    
                    addServer(server);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerEndpointManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerEndpointManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public void saveServersCSVFile() {
        try {
            PrintWriter printWriter = new PrintWriter(new FileWriter(ServerEndpointManager.serversCSVFile));
            
            Iterator<Entry<String, ServerEndpoint>> itServer = this.servers.entrySet().iterator();
            while(itServer.hasNext()) {
                ServerEndpoint server = itServer.next().getValue();
                String toWrite = server.toString() + (this.selected == server ? ", selected" : "") + "\n";
                printWriter.write(toWrite);
            }
            
            printWriter.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerEndpointManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerEndpointManager.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
