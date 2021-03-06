/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clzola.pmnlgame.client.gui;

import clzola.pmnlgame.client.ServerEndpoint;
import clzola.pmnlgame.client.ServerEndpointManager;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.DefaultListModel;

/**
 *
 * @author Lazar
 */
public class ServersListDialog extends javax.swing.JDialog {

    /**
     * Creates new form ServersListDialog
     */
    public ServersListDialog(java.awt.Frame parent, boolean modal, boolean connectButton) {
        super(parent, modal);
        serverManager = ServerEndpointManager.getInstance();
        initComponents();
        
        this.connectButtonEnabled = connectButton;
        updateServersList();
        
        if( connectButton ) {
            this.ConnectSelectButton.setText("Connect");
        } else {
            this.ConnectSelectButton.setText("Select Server");
        }
        
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Servers List");
        this.setVisible(true);
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        AddServerButton = new javax.swing.JButton();
        EditServerButton = new javax.swing.JButton();
        RemoveServerButton = new javax.swing.JButton();
        CloseButton = new javax.swing.JButton();
        ScrollPane = new javax.swing.JScrollPane();
        ServersList = new javax.swing.JList();
        ConnectSelectButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        AddServerButton.setText("Add Server");
        AddServerButton.setPreferredSize(new java.awt.Dimension(122, 26));
        AddServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddServerButtonActionPerformed(evt);
            }
        });

        EditServerButton.setText("Edit Server");
        EditServerButton.setPreferredSize(new java.awt.Dimension(122, 26));
        EditServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditServerButtonActionPerformed(evt);
            }
        });

        RemoveServerButton.setText("Remove Server");
        RemoveServerButton.setPreferredSize(new java.awt.Dimension(122, 26));
        RemoveServerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveServerButtonActionPerformed(evt);
            }
        });

        CloseButton.setText("Close");
        CloseButton.setPreferredSize(new java.awt.Dimension(122, 26));
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        ServersList.setModel(new DefaultListModel<String>());
        ScrollPane.setViewportView(ServersList);

        ConnectSelectButton.setText("ConnectSelect");
        ConnectSelectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectSelectButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(ScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(ConnectSelectButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(AddServerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(EditServerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(RemoveServerButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(8, 8, 8))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(AddServerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(EditServerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(RemoveServerButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(ConnectSelectButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 230, Short.MAX_VALUE)
                        .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ScrollPane))
                .addGap(8, 8, 8))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        this.dispose();
    }//GEN-LAST:event_CloseButtonActionPerformed

    private void AddServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddServerButtonActionPerformed
        ServerForm form = new ServerForm(this, true, null);
        form.setTitle("Add new server");
        form.setLocationRelativeTo(this);
    }//GEN-LAST:event_AddServerButtonActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        serverManager.saveServersCSVFile();
    }//GEN-LAST:event_formWindowClosed

    private void EditServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditServerButtonActionPerformed
        int selectedIndex = ServersList.getSelectedIndex();
        if( selectedIndex != -1 ) {
            String serverName = (String) ServersList.getModel().getElementAt(selectedIndex);
            ServerEndpoint server = serverManager.getServer(serverName);
           
            ServerForm form = new ServerForm(this, true, server);
            form.setTitle("Edit server");
            form.setLocationRelativeTo(this);
        }
    }//GEN-LAST:event_EditServerButtonActionPerformed

    private void RemoveServerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveServerButtonActionPerformed
        int selectedIndex = ServersList.getSelectedIndex();
        if( selectedIndex != -1 ) {
            String serverName = (String) ServersList.getModel().getElementAt(selectedIndex);
            ServerEndpoint server = serverManager.removeServer(serverName);
            updateServersList();
            
            if( server == serverManager.getSelectedServer() ) {
                serverManager.setSelectedServer(null);
                LoginView view = (LoginView)((ClientWindow) this.getParent()).getView("login");
                 view.SelectedServer.setVisible(false);
            }
        }
    }//GEN-LAST:event_RemoveServerButtonActionPerformed

    private void ConnectSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectSelectButtonActionPerformed
        if( this.connectButtonEnabled ) {
            int selectedIndex = ServersList.getSelectedIndex();
            if( selectedIndex != -1 ) {
                // get server
                String serverName = (String) ServersList.getModel().getElementAt(selectedIndex);
                ServerEndpoint server = serverManager.getServer(serverName);
                serverManager.setSelectedServer(server);
                
                // connect to server via thread
            }
            
            this.dispose();
        } else {
            int selectedIndex = ServersList.getSelectedIndex();
            if( selectedIndex != -1 ) {
                String serverName = (String) ServersList.getModel().getElementAt(selectedIndex);
                ServerEndpoint server = serverManager.getServer(serverName);
                
                serverManager.setSelectedServer(server);
                LoginView view = (LoginView)((ClientWindow) this.getParent()).getView("login");
                view.SelectedServer.setText("Selected server: " + server.serverName);
                view.SelectedServer.setVisible(true);
            }
        }
    }//GEN-LAST:event_ConnectSelectButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddServerButton;
    private javax.swing.JButton CloseButton;
    private javax.swing.JButton ConnectSelectButton;
    private javax.swing.JButton EditServerButton;
    private javax.swing.JButton RemoveServerButton;
    private javax.swing.JScrollPane ScrollPane;
    private javax.swing.JList ServersList;
    // End of variables declaration//GEN-END:variables
    
    private ServerEndpointManager serverManager;
    private boolean connectButtonEnabled;
    
    public void updateServersList() {
        ServerEndpoint[] servers = serverManager.all();
        Arrays.sort(servers, new Comparator<ServerEndpoint>() {
            @Override public int compare(ServerEndpoint s1, ServerEndpoint s2) {
                return s1.serverName.toLowerCase().compareTo(s2.serverName.toLowerCase());
            } 
        });
        
        DefaultListModel<String> model = (DefaultListModel<String>) this.ServersList.getModel();
        model.removeAllElements();
        for(ServerEndpoint s : servers)
            model.addElement(s.serverName);
    }
}
