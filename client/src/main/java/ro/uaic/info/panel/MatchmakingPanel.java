package ro.uaic.info.panel;

import ro.uaic.info.window.MainWindow;

import javax.swing.*;
import java.awt.*;

public class MatchmakingPanel extends JPanel {
    public static final int DEFAULT_COMPONENT_MARGIN = 10;

    private MainWindow parent;

    private LobbyPanel lobbyPanel;
    private LobbiesPanel lobbiesPanel;
    private LobbySettingsPanel lobbySettingsPanel;

    public MatchmakingPanel(MainWindow parent){
        this.parent = parent;

        this.buildPanel();
        this.buildComponents();
        this.buildLayout();
    }

    private void buildComponents(){
        this.setBackground(Color.BLACK);

        this.lobbyPanel = new LobbyPanel(
                this,
                new Rectangle(
                this.getX() + DEFAULT_COMPONENT_MARGIN,
                this.getY() + DEFAULT_COMPONENT_MARGIN,
                this.getWidth() / 3 - DEFAULT_COMPONENT_MARGIN,
                this.getHeight() / 2 - DEFAULT_COMPONENT_MARGIN
                )
        );

        this.lobbiesPanel = new LobbiesPanel(
                this,
                new Rectangle(
                        this.getX() + this.getWidth() / 3 + 2 * DEFAULT_COMPONENT_MARGIN,
                        this.getY() + DEFAULT_COMPONENT_MARGIN,
                        this.getWidth() / 3 * 2 - DEFAULT_COMPONENT_MARGIN,
                        this.getHeight() - DEFAULT_COMPONENT_MARGIN
                )
        );

        this.lobbySettingsPanel = new LobbySettingsPanel(
                this,
                new Rectangle(
                        this.getX() + DEFAULT_COMPONENT_MARGIN,
                        this.getY() + this.getHeight() / 2 + 2 * DEFAULT_COMPONENT_MARGIN,
                        this.getWidth() / 3 - DEFAULT_COMPONENT_MARGIN,
                        this.getHeight() / 2 - DEFAULT_COMPONENT_MARGIN
                )
        );
    }

    private void buildLayout(){
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(this.lobbyPanel)
                        .addComponent(this.lobbySettingsPanel)
                )
                .addComponent(this.lobbiesPanel)
        );

        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(this.lobbyPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.lobbySettingsPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
                .addComponent(this.lobbiesPanel)
        );
    }

    private void buildPanel(){
        this.setBounds(0,
                new JLabel().getHeight(),
                this.parent.getWidth(),
                this.parent.getHeight() - new JLabel().getHeight());
    }
}
