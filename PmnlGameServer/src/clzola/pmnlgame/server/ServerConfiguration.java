/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lazar
 */
public class ServerConfiguration {
    public static String filename = "server.conf";
    
    public String SERVER_NAME;
    public int    SERVER_PORT;
    public int    AUTH_SERVER_PORT;
    
    public String DATABASE_SERVER;
    public String DATABASE_USER;
    public String DATABASE_PASSWORD;
    public String DATABASE_SCHEME;
    public int    DATABASE_PORT;
    
    public String SECRET_KEY;
    
    public ServerConfiguration() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(ServerConfiguration.filename));
            
            String configurationLine = null;
            int lineNumber = 0;
            while( (configurationLine = bufferedReader.readLine()) != null ) {
                lineNumber++;
                
                if( configurationLine.trim().length() == 0 )
                    continue;
                
                if( configurationLine.trim().charAt(0) == '#' )
                    continue;
                
                String[] config = configurationLine.trim().split("=");
                if( config.length != 2 ) 
                    throw new IOException();
                
                if( config[0].contains("#") )
                    throw new IOException();
                
                String value = config[1].split("#")[0].trim();
                switch( config[0].trim() ) {
                    case "server_name":
                        this.SERVER_NAME = value;
                        break;
                    case "server_port":
                        try{
                            this.SERVER_PORT = Integer.parseInt(value);
                        } catch ( NumberFormatException ex ) {
                            Logger.getLogger(ServerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "auth_server_port":
                        try{
                            this.AUTH_SERVER_PORT = Integer.parseInt(value);
                        } catch ( NumberFormatException ex ) {
                            Logger.getLogger(ServerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case "database_server":
                        this.DATABASE_SERVER = value;
                        break;
                    case "database_user":
                        this.DATABASE_USER = value;
                        break;
                    case "database_password":
                        this.DATABASE_PASSWORD = value;
                        break;
                    case "database_scheme":
                        this.DATABASE_SCHEME = value;
                        break;
                    case "database_port":
                        try{
                            this.DATABASE_PORT = Integer.parseInt(value);
                        } catch ( NumberFormatException ex ) {
                            Logger.getLogger(ServerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    break;
                    case "secret_key":
                        this.SECRET_KEY = value;
                        break;
                    default:
                        Logger.getLogger(ServerConfiguration.class.getName()).log(Level.WARNING, 
                                "Unknown option \""+config[0]+"\" in " + ServerConfiguration.filename + " at line " + lineNumber);
                        break;
                }  
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ServerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            setDefaultValues();
        } catch (IOException ex) {
            Logger.getLogger(ServerConfiguration.class.getName()).log(Level.SEVERE, null, ex);
            setDefaultValues();
        } 
    }
    
    private void setDefaultValues() {
        this.SERVER_NAME = "PMNL Game Server";
        this.SERVER_PORT = 27050;
        this.AUTH_SERVER_PORT = 27052;
        this.DATABASE_SERVER = "127.0.0.1";
        this.DATABASE_USER = "pmnlgame";
        this.DATABASE_PASSWORD = "pmnlgame";
        this.DATABASE_SCHEME = "pmnlgame_db";
        this.DATABASE_PORT = 3306;
        this.SECRET_KEY = "cbb239cb4f3a89bc6eee63acca9572f5";
    }
    
    @Override public String toString() {
        String string = "Server Configuration:\n";
        string += "  ServerConfiguration.SERVER_NAME = "        + this.SERVER_NAME          + "\n";
        string += "  ServerConfiguration.SERVER_PORT = "        + this.SERVER_PORT          + "\n";
        string += "  ServerConfiguration.AUTH_SERVER_PORT = "   + this.AUTH_SERVER_PORT     + "\n";
        string += "  ServerConfiguration.DATABASE_SERVER = "    + this.DATABASE_SERVER      + "\n";
        string += "  ServerConfiguration.DATABASE_USER = "      + this.DATABASE_USER        + "\n";
        string += "  ServerConfiguration.DATABASE_PASSWORD = "  + this.DATABASE_PASSWORD    + "\n";
        string += "  ServerConfiguration.DATABASE_SCHEME = "    + this.DATABASE_SCHEME      + "\n";
        string += "  ServerConfiguration.SECRET_KEY = "         + this.SECRET_KEY           + "";
        
        return string;
    }
}
