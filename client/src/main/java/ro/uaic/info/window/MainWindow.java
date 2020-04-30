package ro.uaic.info.window;

import ro.uaic.info.net.Connection;
import ro.uaic.info.window.state.ConnectionWindowStates;
import ro.uaic.info.window.state.MessageWindowStates;
import ro.uaic.info.window.type.MessageWindowType;

import javax.swing.*;
import java.awt.*;

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

    public MainWindow(){
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
        boolean showReconnect = false;

        this.buildWindow();

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

        this.buildListeners();

        this.setVisible(true);
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
}