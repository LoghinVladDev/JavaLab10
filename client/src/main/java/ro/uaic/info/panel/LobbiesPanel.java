package ro.uaic.info.panel;

import ro.uaic.info.net.handler.EventHandler;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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

    public LobbiesPanel(MatchmakingPanel parent, Rectangle location){
        this.parent = parent;

        this.offsetX        = location.x;
        this.offsetY        = location.y;
        this.panelWidth     = location.width;
        this.panelHeight    = location.height;

        this.buildPanel();
        this.buildComponents();
        this.buildLayout();
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

        this.createLobbyPushButton.addActionListener(e->this.createLobby());
        System.out.println("wtf");

        this.setVisible(true);
        this.setEnabled(true);

        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        //EventHandler.addToMessageQueue("CRT_LBY");


        EventHandler.addToMessageQueue("CRT_LBY");
    }

    private void createLobby(){
        /*try {
            synchronized (MainWindow.getLock()) {
                MainWindow.getLock().wait(1000);
                System.out.println("Button Pressed!!!!");
                EventHandler.addToMessageQueue("CRT_LBY");
                MainWindow.getLock().notifyAll();
            }
        }
        catch (InterruptedException e){

        }*/

        System.out.println("PRESSED BUTTON FFS");
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
