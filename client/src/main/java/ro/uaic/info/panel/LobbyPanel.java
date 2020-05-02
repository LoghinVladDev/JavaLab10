package ro.uaic.info.panel;

import ro.uaic.info.resource.Lobby;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class LobbyPanel extends JPanel{
    private MatchmakingPanel parent;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    private JLabel lobbyTitle;
    private JLabel creatorLabel;
    private JLabel opponentLabel;

    private JButton leaveLobbyButton;
    private JButton startGameButton;

    private boolean isShown = false;

    public LobbyPanel(MatchmakingPanel parent, Rectangle location){
        this.parent = parent;

        this.offsetX        = location.x;
        this.offsetY        = location.y;
        this.panelWidth     = location.width;
        this.panelHeight    = location.height;

        this.buildPanel();

        //this.setVisible(true);

        //System.out.println("Lobby Built...");
    }

    private void buildComponents(){
        if(!this.isShown) {
            this.lobbyTitle = new JLabel();
            this.creatorLabel = new JLabel();
            this.opponentLabel = new JLabel();
            this.leaveLobbyButton = new JButton("Leave Lobby");
            this.startGameButton = new JButton("Start Game!");
        }
    }

    private void buildLayout(){
        if(!this.isShown){
            GroupLayout layout = new GroupLayout(this);
            this.setLayout(layout);

            layout.setAutoCreateContainerGaps(true);
            layout.setAutoCreateGaps(true);

            layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(this.lobbyTitle)
                    .addGroup(
                        layout.createSequentialGroup()
                            .addComponent(this.creatorLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(this.opponentLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    )
                    .addGroup(
                        layout.createSequentialGroup()
                            .addComponent(this.leaveLobbyButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(this.startGameButton, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    )
            );

            layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addComponent(this.lobbyTitle)
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(this.creatorLabel)
                            .addComponent(this.opponentLabel)
                    )
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(this.leaveLobbyButton)
                            .addComponent(this.startGameButton)
                    )
            );
        }
    }

    public void setLobby(Lobby lobby){
        this.buildComponents();
        this.buildLayout();
        this.isShown = true;

        this.lobbyTitle.setText(lobby.getCreator() + "'s lobby");
        this.creatorLabel.setText("White : " + lobby.getCreator());
        this.opponentLabel.setText("Black : " + (lobby.getOtherPlayer() == null ? "N/A" : lobby.getOtherPlayer()));

        System.out.println(this);
    }

    private void buildPanel(){
        super.setBounds(
                this.offsetX,
                this.offsetY,
                this.panelWidth,
                this.panelHeight
        );

        super.setBackground(Color.WHITE);
    }
}
