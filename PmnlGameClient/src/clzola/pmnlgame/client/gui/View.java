/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client.gui;

import com.esotericsoftware.kryonet.Client;
import java.awt.Dimension;
import javax.swing.JPanel;

/**
 *
 * @author Lazar
 */
public abstract class View extends JPanel {
    protected String viewId;
    protected ClientWindow window;
    
    public View(String viewId, ClientWindow window) {
        this.viewId = viewId;
        this.window = window;
    }
    
    public abstract void load();
    public abstract void unload();
    
    public String getViewId() {
        return this.viewId;
    }
    
    public ClientWindow getWindow() {
        return this.window;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(640, 520);
    }
    
  ////////////////////////////////////////////////////////////////
  // Client Space
  //
    protected Client client;

    public void setClient(Client client) { 
        this.client = client; 
    }
    
    public Client getClient() { 
        return this.client; 
    }
}
