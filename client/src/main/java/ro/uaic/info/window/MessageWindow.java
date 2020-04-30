package ro.uaic.info.window;

import ro.uaic.info.window.state.MessageWindowStates;
import ro.uaic.info.window.type.MessageWindowType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MessageWindow extends JFrame {
    public static final int MSG_WINDOW_DEFAULT_WIDTH = 300;
    public static final int MSG_WINDOW_DEFAULT_HEIGHT = 200;

    private int windowWidth = MSG_WINDOW_DEFAULT_WIDTH;
    private int windowHeight = MSG_WINDOW_DEFAULT_HEIGHT;
    private int offsetX;
    private int offsetY;

    private final JFrame parent;

    private JButton positiveConfirmButton;
    private JButton negativeConfirmButton;

    private String messageText = "Warning";
    private String messageDetailedText = "";

    private final MessageWindowType type;

    private volatile MessageWindowStates state = MessageWindowStates.MESSAGE_WINDOW_ACTIVE;

    public MessageWindow(JFrame parent, MessageWindowType type){
        this.parent = parent;
        this.type = type;

        this.offsetX = this.parent.getX() + this.parent.getWidth()/2 - this.windowWidth/2;
        this.offsetY = this.parent.getY() + this.parent.getHeight()/2 - this.windowHeight/2;
    }

    public MessageWindow(int x, int y, MessageWindowType type){
        this.parent = null;
        this.type = type;

        this.offsetX = x;
        this.offsetY = y;
    }

    private void buildWindow(){
        super.setTitle(messageText);
        super.setBounds(
                new Rectangle(
                        this.offsetX,
                        this.offsetY,
                        this.windowWidth,
                        this.windowHeight
                )
        );
    }

    public MessageWindow setMessageText(String messageText){
        this.messageText = messageText;
        return this;
    }

    public MessageWindow setMessageDetailedText(String messageDetailedText) {
        this.messageDetailedText = messageDetailedText;
        return this;
    }

    private void buildButtons(){
        switch(this.type){
            case MESSAGE_WINDOW_ACKNOWLEDGE:
                this.positiveConfirmButton = new JButton("Ok");
                break;
            case MESSAGE_WINDOW_QUESTION:
                this.positiveConfirmButton = new JButton("Yes");
                this.negativeConfirmButton = new JButton("No");
        }
    }

    private void buttonOkPressed(){
        switch(this.type){
            case MESSAGE_WINDOW_ACKNOWLEDGE:
                this.state = MessageWindowStates.MESSAGE_WINDOW_CLOSED_OK;
                break;
            case MESSAGE_WINDOW_QUESTION:
                this.state = MessageWindowStates.MESSAGE_WINDOW_CLOSED_YES;
                break;
        }
    }

    private void buttonNoPressed(){
        switch(this.type){
            case MESSAGE_WINDOW_ACKNOWLEDGE:
                this.state = MessageWindowStates.MESSAGE_WINDOW_CLOSED_OK;
                break;
            case MESSAGE_WINDOW_QUESTION:
                this.state = MessageWindowStates.MESSAGE_WINDOW_CLOSED_NO;
                break;
        }
    }

    private void buildGroups(){
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JTextPane messageDetailedLabel = new JTextPane();
        messageDetailedLabel.setText(this.messageDetailedText);
        messageDetailedLabel.setEditable(false);

        GroupLayout.Group horizontalGroup =
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(messageDetailedLabel);

        switch(this.type){
            case MESSAGE_WINDOW_ACKNOWLEDGE:
                horizontalGroup
                    .addComponent(this.positiveConfirmButton);
                break;
            case MESSAGE_WINDOW_QUESTION:
                horizontalGroup
                    .addGroup(
                        layout.createSequentialGroup()
                            .addComponent(
                                this.negativeConfirmButton,
                                0,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE
                            )
                            .addComponent(
                                this.positiveConfirmButton,
                                0,
                                GroupLayout.DEFAULT_SIZE,
                                Short.MAX_VALUE
                            )
                    );
                break;
        }

        GroupLayout.Group verticalGroup = layout.createSequentialGroup()
            .addComponent(messageDetailedLabel);

        switch(this.type){
            case MESSAGE_WINDOW_ACKNOWLEDGE:
                verticalGroup
                    .addComponent(this.positiveConfirmButton);
                break;
            case MESSAGE_WINDOW_QUESTION:
                verticalGroup
                    .addGroup(
                        layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(this.negativeConfirmButton)
                            .addComponent(this.positiveConfirmButton)
                    );
                break;
        }

        layout.setHorizontalGroup(horizontalGroup);
        layout.setVerticalGroup(verticalGroup);
    }

    private void buildListeners(){
        switch(this.type){
            case MESSAGE_WINDOW_ACKNOWLEDGE:
                this.positiveConfirmButton.addActionListener(
                        e->this.buttonOkPressed()
                );
                break;
            case MESSAGE_WINDOW_QUESTION:
                this.positiveConfirmButton.addActionListener(
                        e->this.buttonOkPressed()
                );
                this.negativeConfirmButton.addActionListener(
                        e->this.buttonNoPressed()
                );
        }

        this.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent event){
                        buttonNoPressed();
                    }
                }
        );
    }

    public MessageWindowStates run(){
        if(this.parent != null)
            this.parent.setEnabled(false);

        this.buildWindow();
        this.buildButtons();
        this.buildGroups();
        this.buildListeners();
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        this.setVisible(true);

        //noinspection StatementWithEmptyBody
        while(this.state == MessageWindowStates.MESSAGE_WINDOW_ACTIVE);

        this.setVisible(false);

        if(this.parent != null){
            this.parent.setEnabled(true);
        }

        return this.state;
    }
}
