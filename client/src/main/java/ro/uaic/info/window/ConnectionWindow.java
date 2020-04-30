package ro.uaic.info.window;

import com.github.javafaker.Faker;
import ro.uaic.info.net.Connection;
import ro.uaic.info.window.state.ConnectionWindowStates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ConnectionWindow extends JFrame {
    public static final int POP_WINDOW_DEFAULT_WIDTH = 400;
    public static final int POP_WINDOW_DEFAULT_HEIGHT = 200;

    private static final Faker faker = new Faker();

    private int windowWidth = POP_WINDOW_DEFAULT_WIDTH;
    private int windowHeight = POP_WINDOW_DEFAULT_HEIGHT;
    private MainWindow parent;

    private JTextField ipAddressLineEdit;
    private JTextField portLineEdit;
    private JTextField usernameLineEdit;

    private JButton randomUsernamePushButton;
    private JButton confirmPushButton;
    private JButton cancelPushButton;

    private volatile ConnectionWindowStates windowStatus = ConnectionWindowStates.WINDOW_ACTIVE;

    public String getInputIpAddress(){
        return this.ipAddressLineEdit.getText();
    }

    public String getInputPort(){
        return this.portLineEdit.getText();
    }

    public String getInputUsername(){
        return this.usernameLineEdit.getText();
    }

    public ConnectionWindow(MainWindow parent){
        this.parent = parent;
    }

    public ConnectionWindow(MainWindow parent, int width, int height){
        this.parent = parent;
        this.windowWidth = width;
        this.windowHeight = height;
    }

    private void buildWindow(){
        super.setBounds(
                new Rectangle(
                    this.parent.getWindowOffsetX() + this.parent.getWindowWidth()/2 - this.windowWidth/2,
                    this.parent.getWindowOffsetY() + this.parent.getWindowHeight()/2 - this.windowHeight/2,
                    this.windowWidth,
                    this.windowHeight
                )
        );
    }

    public ConnectionWindowStates run(){
        this.buildWindow();
        this.buildGroups();
        this.buildListeners();

        this.setVisible(true);

        //noinspection StatementWithEmptyBody
        while(this.windowStatus == ConnectionWindowStates.WINDOW_ACTIVE);

        this.setVisible(false);

        return this.windowStatus;
    }

    private void buildListeners(){
        this.addWindowListener(
            new WindowAdapter() {
                public void windowClosing(WindowEvent e){
                    cancel();
                }
            }
        );

        this.confirmPushButton.addActionListener(e->this.confirm());
        this.cancelPushButton.addActionListener(e->this.cancel());
        this.randomUsernamePushButton.addActionListener(e->this.generateRandomUsername());
    }

    private void generateRandomUsername(){
        this.usernameLineEdit.setText(ConnectionWindow.faker.name().username());
    }

    private void cancel(){
        this.windowStatus = ConnectionWindowStates.WINDOW_CLOSED_CANCEL;
    }

    private void confirm(){
        this.windowStatus = ConnectionWindowStates.WINDOW_CLOSED_CONFIRM;
    }

    private void buildGroups(){
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel ipAddressLabel           = new JLabel("IP Address : ");
        JLabel portLabel                = new JLabel("Port : ");
        JLabel usernameLabel            = new JLabel("Username : ");

        this.ipAddressLineEdit          = new JTextField(Connection.DEFAULT_ADDRESS);
        this.portLineEdit               = new JTextField(Integer.toString(Connection.DEFAULT_PORT));
        this.usernameLineEdit           = new JTextField();

        this.randomUsernamePushButton   = new JButton("Get Random Username");
        this.confirmPushButton          = new JButton("Connect");
        this.cancelPushButton           = new JButton("Close");

        layout.setHorizontalGroup(
                layout.createSequentialGroup()
                    .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(ipAddressLabel)
                                .addComponent(portLabel)
                                .addComponent(usernameLabel)
                                .addComponent(this.cancelPushButton)
                    )
                    .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(this.ipAddressLineEdit)
                                .addComponent(this.portLineEdit)
                                .addComponent(this.usernameLineEdit)
                                .addComponent(this.randomUsernamePushButton)
                                .addComponent(this.confirmPushButton)
                    )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                    .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(ipAddressLabel)
                                .addComponent(this.ipAddressLineEdit)
                    )
                    .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(portLabel)
                                .addComponent(this.portLineEdit)
                    )
                    .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(usernameLabel)
                                .addComponent(this.usernameLineEdit)
                    )
                    .addComponent(this.randomUsernamePushButton)
                    .addGroup(
                            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(this.cancelPushButton)
                                .addComponent(this.confirmPushButton)
                    )
        );
    }
}
