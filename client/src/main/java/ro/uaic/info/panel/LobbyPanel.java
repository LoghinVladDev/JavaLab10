package ro.uaic.info.panel;

import ro.uaic.info.net.state.ClientState;
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

    private Lobby lobby;

    private boolean isShown = false;

    public Lobby getLobby(){
        return this.lobby;
    }

    public void buildListeners(){
        if(!isShown){
            this.leaveLobbyButton.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent e) { }
                    public void mousePressed(MouseEvent e) { }
                    public void mouseReleased(MouseEvent e) {
                        leaveLobby(lobby.getCreator());
                    }
                    public void mouseEntered(MouseEvent e) { }
                    public void mouseExited(MouseEvent e) { }
                }
            );

            this.startGameButton.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent e) { }
                    public void mousePressed(MouseEvent e) { }
                    public void mouseReleased(MouseEvent e) {
                        sendStartGame();
                    }
                    public void mouseEntered(MouseEvent e) { }
                    public void mouseExited(MouseEvent e) { }
                }
            );
        }
    }

    private void sendStartGame(){
        System.out.println("Pressed Start Game");

        System.out.println(this.getLobby().getOtherPlayer());

        if(this.getLobby().getOtherPlayer() == null)
            return;

        this.parent.getParent().getMessageHandler().addToMessageQueue(ClientState.START_GAME);
    }

    public void leaveLobby(String lobbyCreator){
        if(this.lobby.getOtherPlayer() != null)
            if(!this.lobby.getOtherPlayer().equals(this.parent.getParent().getUsername()))
                return;

        this.parent.getParent().getMessageHandler().addToMessageQueue(ClientState.LEAVE_LOBBY);
        this.parent.getParent().getMessageHandler().addToMessageQueue(lobbyCreator);
        this.parent.getParent().setLobbyCreated(false);
    }

    public void leaveLobby(){
        this.lobbyTitle.setText("");
        this.creatorLabel.setText("");
        this.opponentLabel.setText("");
        this.lobby.setOtherPlayer(null);
        this.lobby.setCreator(null);
        this.leaveLobbyButton.setEnabled(false);
        this.startGameButton.setEnabled(false);
    }

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
                    .addGroup(
                        layout.createSequentialGroup()
                            .addComponent(this.lobbyTitle, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    )
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
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(this.lobbyTitle)
                    )
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

    public JButton getStartGameButton(){
        return this.startGameButton;
    }

    public void setLobby(Lobby lobby){
        this.buildComponents();
        this.buildLayout();
        this.buildListeners();
        this.isShown = true;

        this.lobby = lobby;

        this.leaveLobbyButton.setEnabled(true);
        this.startGameButton.setEnabled(true);

        this.lobbyTitle.setText(lobby.getCreator() + "'s lobby");
        this.creatorLabel.setText("White : " + lobby.getCreator());
        this.opponentLabel.setText("Black : " + (lobby.getOtherPlayer() == null ? "N/A" : lobby.getOtherPlayer()));

        System.out.println(this);
    }

    public LobbyPanel setLobbyCreator(String creatorUsername){
        this.lobbyTitle.setText(creatorUsername + "'s lobby");
        this.creatorLabel.setText("White : " + creatorUsername);
        return this;
    }

    public LobbyPanel setLobbyOtherPlayer(String otherPlayerUsername){
        this.opponentLabel.setText("Black : " + otherPlayerUsername);
        return this;
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
