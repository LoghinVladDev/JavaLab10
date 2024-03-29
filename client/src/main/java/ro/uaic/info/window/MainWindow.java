package ro.uaic.info.window;

import com.mifmif.common.regex.Main;
import ro.uaic.info.game.Player;
import ro.uaic.info.net.Connection;
import ro.uaic.info.net.state.ClientState;
import ro.uaic.info.panel.GamePanel;
import ro.uaic.info.panel.MatchmakingPanel;
import ro.uaic.info.window.state.ConnectionWindowStates;
import ro.uaic.info.window.state.MessageWindowStates;
import ro.uaic.info.window.type.MessageWindowType;
import ro.uaic.info.net.handler.EventHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Queue;

public class MainWindow extends JFrame {
    public static final int APP_WINDOW_DEFAULT_WIDTH = 1366;
    public static final int APP_WINDOW_DEFAULT_HEIGHT = 768;
    public static final int APP_WINDOW_DEFAULT_X_OFFSET = 20;
    public static final int APP_WINDOW_DEFAULT_Y_OFFSET = 20;

    private int windowWidth = APP_WINDOW_DEFAULT_WIDTH;
    private int windowHeight = APP_WINDOW_DEFAULT_HEIGHT;
    private int windowOffsetX = APP_WINDOW_DEFAULT_X_OFFSET;
    private int windowOffsetY = APP_WINDOW_DEFAULT_Y_OFFSET;

    private String username;
    private Connection connection;
    private String connectionStatus;
    private String ping = "0";

    private Player player;

    private MatchmakingPanel matchmakingPanel;
    private GamePanel gamePanel;

    private JLabel connectionLabel;
    private JLabel usernameLabel;
    private JLabel pingLabel;

    private boolean lobbyCreated = false;

    private JPanel mainPanel;

    private static Object lock = new Object();

    public static Object getLock() {
        return lock;
    }

    private EventHandler messageHandler;

    public MainWindow setLobbyCreated(boolean status){
        this.lobbyCreated = status;
        return this;
    }

    public void triggerVictory(String playerColour){
        this.getGamePanel().getGameStatusPanel().getTurnLabel().setText(playerColour + " wins!");
        try {
            this.messageHandler.join();
        }
        catch (InterruptedException ignored){

        }
    }


    public boolean isLobbyCreated() {
        return this.lobbyCreated;
    }

    public MainWindow(){

    }

    public Player getPlayer() {
        return this.player;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    private boolean retartedFunctionLock = false;

    public void gameStartCallback(String colour){
        if(!this.retartedFunctionLock){
            this.retartedFunctionLock = true;
            this.matchmakingPanel.setEnabled(false);
            this.matchmakingPanel.setVisible(false);
            this.setPlayer(
                    new Player().setColour(colour)
            );

            this.gamePanel = new GamePanel(this);
            this.gamePanel.getGameBoardPanel().setTokenColour(colour);
            this.gamePanel.getGameBoardPanel().setTurn(colour.equals("WHITE"));

            this.buildLayout(true);
        }
    }

    public void setPlayer(Player player){
        this.player = player;
        this.usernameLabel.setText(this.usernameLabel.getText() + " (" + this.player.getColour() + ")");
    }

    public void setConnectionStatus(String status){
        this.connectionStatus = status;
        this.connectionLabel.setText(this.connectionStatus);

    }

    private String updateConnectionStatus(){
        this.connectionStatus =
                this.connection.isConnected() ?
                "Connected to server (" +
                this.connection.getSocket().getInetAddress() +
                ":" +
                this.connection.getSocket().getPort() +
                ")" :
                "Disconnected";

        return this.connectionStatus;
    }

    public void setPing(int ping){
        this.ping = ping + " ms ping";
        this.pingLabel.setText(this.ping);
    }

    public MainWindow(int width, int height){
        this.windowWidth = width;
        this.windowHeight = height;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public MainWindow(int width, int height, int x, int y){
        this.windowWidth = width;
        this.windowHeight = height;
        this.windowOffsetX = x;
        this.windowOffsetY = y;
    }

    public void run(){

        this.buildWindow();
        this.waitForConnection();

        this.buildComponents();
        this.buildLayout(false);

        this.setVisible(true);

        //System.out.println("Window Built...");

        this.buildListeners();

        this.matchmakingPanel.setFocusable(true);
        this.matchmakingPanel.grabFocus();
        this.matchmakingPanel.getLobbiesPanel().grabFocus();
    }

    public void debugRun(){
        this.buildWindow();
        ///this.connectionLabel    = new JLabel();
        //this.pingLabel          = new JLabel();
        //this.usernameLabel      = new JLabel();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.gamePanel = new GamePanel(this);
        this.setContentPane(this.gamePanel);
        this.gamePanel.getGameBoardPanel().grabFocus();
        //this.buildLayout(true);
        //this.gamePanel.getGameBoardPanel().initialiseCanvas();
        //this.gamePanel.getGameBoardPanel().grabFocus();
        //this.repaint();
    }

    public String getUsername(){
        return this.username;
    }

    private void buildComponents(){
        this.messageHandler = new EventHandler(this);
        this.messageHandler.start();

        this.connectionLabel    = new JLabel();
        this.pingLabel          = new JLabel();
        this.usernameLabel      = new JLabel();

        this.connectionLabel.setText(this.updateConnectionStatus());
        this.usernameLabel.setText(this.username);
        this.pingLabel.setText(this.ping);

        this.matchmakingPanel   = new MatchmakingPanel(this); // TODO: add here
    }

    private void buildLayout(boolean redrawForGame){
        if(this.mainPanel != null)
            this.getContentPane().remove(this.mainPanel);

        if(!redrawForGame){
            this.mainPanel = new JPanel();
        }
        else {
            this.mainPanel = new JPanel() {
                public void paintComponent(Graphics g) {
                    gamePanel.paintComponent(g);
                }
            };
        }

        GroupLayout groupLayout = new GroupLayout(this.mainPanel);
        this.mainPanel.setLayout(groupLayout);

        this.getContentPane().add(this.mainPanel);

        groupLayout.setHorizontalGroup(
            groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                    .addComponent(this.connectionLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.usernameLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(this.pingLabel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
                .addComponent(redrawForGame ? this.gamePanel :  this.matchmakingPanel)
        );

        groupLayout.setVerticalGroup(
            groupLayout.createSequentialGroup()
                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(this.connectionLabel)
                    .addComponent(this.usernameLabel)
                    .addComponent(this.pingLabel)
                )
                .addComponent(redrawForGame ? this.gamePanel : this.matchmakingPanel)
        );
    }

    private void waitForConnection(){
        boolean showReconnect = false;
        do {
            if(showReconnect){
                MessageWindowStates messageWindowStatus = new MessageWindow(this, MessageWindowType.MESSAGE_WINDOW_ACKNOWLEDGE)
                        .setMessageText("Connection Failed!")
                        .setMessageDetailedText("Server might be down")
                        .run();
            }

            ConnectionWindow connectionWindow = new ConnectionWindow(this);
            ConnectionWindowStates connectionWindowReturn = connectionWindow.run();

            if (connectionWindowReturn != ConnectionWindowStates.WINDOW_CLOSED_CONFIRM) {
                this.close();
            }

            this.buildConnection(
                    connectionWindow.getInputIpAddress(),
                    Integer.parseInt(connectionWindow.getInputPort()),
                    connectionWindow.getInputUsername(),
                    true
            );

            showReconnect = true;
        }while(!this.connection.isConnected());
    }

    public MatchmakingPanel getMatchmakingPanel() {
        return matchmakingPanel;
    }

    private void buildConnection(String address, int port, String username, boolean debugMode){
        this.username = username;

        this.connection = new Connection.ConnectionFactory()
                .withIPV4Address(address)
                .withPort(port)
                .withDebugMode(debugMode)
                .build();

        this.connection.connect();
    }

    private void buildListeners(){
        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e){
                        close("Client closed", 0);
                    }
                }
        );
    }

    private void buildWindow(){
        super.setBounds(
                new Rectangle(
                        this.windowOffsetX,
                        this.windowOffsetY,
                        this.windowWidth,
                        this.windowHeight
                )
        );
    }

    public void close(String message, int status){
        this.messageHandler.addToMessageQueue(ClientState.CLIENT_EXIT);
        System.out.println(message);
        System.exit(status);
    }

    public void close(String message){
        this.close(message, 0);
    }

    public void close(){
        this.close("Exiting Application...");
    }

    public int getWindowHeight() {
        return this.windowHeight;
    }

    public int getWindowOffsetX() {
        return this.windowOffsetX;
    }

    public int getWindowOffsetY() {
        return this.windowOffsetY;
    }

    public int getWindowWidth() {
        return this.windowWidth;
    }

    public EventHandler getMessageHandler(){
        return this.messageHandler;
    }
}
