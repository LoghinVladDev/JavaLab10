package ro.uaic.info.panel;

import ro.uaic.info.net.handler.EventHandler;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.resource.Lobby;
import ro.uaic.info.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class LobbiesPanel extends JPanel {
    private MatchmakingPanel parent;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    private JButton createLobbyPushButton;
    private JButton joinLobbyPushButton;
    private List<Lobby> lobbyList;

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
        this.parent.getParent().getMessageHandler().addToMessageQueue(ClientState.CREATE_LOBBY);
    }

    public LobbiesPanel(MatchmakingPanel parent, Rectangle location){
        this.parent = parent;

        this.lobbyList = new ArrayList<>();

        this.offsetX        = location.x;
        this.offsetY        = location.y;
        this.panelWidth     = location.width;
        this.panelHeight    = location.height;

        this.buildPanel();
        this.buildComponents();
        this.buildLayout();

        this.buildListeners();

        //this.requestFocusInWindow();
        //System.out.println("Lobbies Built...");
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

    public boolean lobbyExists(Lobby lobby){
        for(int i = 0; i < this.lobbiesListModel.size(); i++)
            if(lobby.getCreator().equals(this.lobbiesListModel.get(i)))
                return true;
        return false;
    }

    public void updateLobbies(String JSONEncodedLobbies){
        //this.lobbiesListModel.addAll(this.decodeJSONLobbies(JSONEncodedLobbies));

        //this.lobbiesListModel.clear();

        List<Lobby> list = decodeJSONLobbies(JSONEncodedLobbies);

        int index = 0;
        if(list != null)
        for(Lobby lobby : list){
            System.out.println(lobby);
            if(!this.lobbyExists(lobby)){
                this.lobbiesListModel.add(index++, lobby.getCreator());
                System.out.println(lobby);
            }
        }
        for(int i = 0; i < this.lobbiesListModel.size(); i++)
            if(!list.contains(new Lobby(this.lobbiesListModel.get(i))))
                this.lobbiesListModel.remove(i);
    }

    private List<Lobby> decodeJSONLobbies(String json){
        List<Lobby> list = new ArrayList<>();

        String[] lobbies = json.split(",");

        if(lobbies.length == 1 && lobbies[0].charAt(1) != '{')
            return null;

        for (String lobby : lobbies) {
            String components = lobby.split(":")[1];

            list.add(
                new Lobby(
                    components.split(";")[0].split("=")[1]
                ).setOtherPlayer(
                    components.split(";")[1].split("=")[1].split("}")[0].equals("null")
                        ? null
                        : components.split(";")[1].split("=")[1].split("}")[0]
                )
            );
        }

        return list;
    }

    private void buildComponents(){
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
