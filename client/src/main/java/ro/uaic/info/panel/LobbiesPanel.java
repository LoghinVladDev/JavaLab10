package ro.uaic.info.panel;

import ro.uaic.info.net.handler.EventHandler;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class LobbiesPanel extends JPanel {
    private MatchmakingPanel parent;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    private JButton createLobbyPushButton;
    private JButton joinLobbyPushButton;

    private JList<String> lobbiesList;
    private DefaultListModel<String> lobbiesListModel;
    private JScrollPane scrollPane;

    private void buildListeners(){
        this.createLobbyPushButton.addMouseListener(
                new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        createLobby();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {

                    }

                    @Override
                    public void mouseExited(MouseEvent e) {

                    }
                }
        );
    }

    private void createLobby(){
        System.out.println("PRESSED BUTTON FFS");
        this.parent.getParent().getMessageHandler().addToMessageQueue(ClientState.CREATE_LOBBY);
    }

    public LobbiesPanel(MatchmakingPanel parent, Rectangle location){
        this.parent = parent;

        this.offsetX        = location.x;
        this.offsetY        = location.y;
        this.panelWidth     = location.width;
        this.panelHeight    = location.height;

        this.buildPanel();
        this.buildComponents();
        this.buildLayout();

        this.buildListeners();

        //this.requestFocusInWindow();
        System.out.println("Lobbies Built...");
    }

    private void buildLayout(){
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(this.scrollPane)
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(this.createLobbyPushButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.joinLobbyPushButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(this.scrollPane)
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(this.createLobbyPushButton)
                        .addComponent(this.joinLobbyPushButton)
                )
        );
    }

    private  void buildComponents(){
        this.lobbiesListModel = new DefaultListModel<>();
        this.lobbiesList = new JList<>(this.lobbiesListModel);
        this.scrollPane = new JScrollPane(this.lobbiesList);

        this.createLobbyPushButton = new JButton("Create Lobby");
        this.joinLobbyPushButton = new JButton("Join Lobby");

        //System.out.println("wtf");

        //EventHandler.addToMessageQueue("CRT_LBY");

        //EventHandler.addToMessageQueue("CRT_LBY");
    }

    public JButton getCreateLobbyPushButton(){
        return this.createLobbyPushButton;
    }

    private void buildPanel(){
        super.setBounds(
                this.offsetX,
                this.offsetY,
                this.panelWidth,
                this.panelHeight
        );

        //super.setMinimumSize(new Dimension(this.panelWidth, this.panelHeight));

        super.setBackground(Color.BLUE);
    }
}
