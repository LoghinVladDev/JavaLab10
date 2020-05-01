package ro.uaic.info.panel;

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

    public LobbyPanel(MatchmakingPanel parent, Rectangle location){
        this.parent = parent;

        this.offsetX        = location.x;
        this.offsetY        = location.y;
        this.panelWidth     = location.width;
        this.panelHeight    = location.height;

        this.buildPanel();

        //System.out.println("Lobby Built...");
    }

    private void buildPanel(){
        super.setBounds(
                this.offsetX,
                this.offsetY,
                this.panelWidth,
                this.panelHeight
        );

        super.setBackground(Color.RED);
    }
}
