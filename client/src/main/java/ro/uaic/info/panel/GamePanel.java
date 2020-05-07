package ro.uaic.info.panel;

import ro.uaic.info.window.MainWindow;

import javax.swing.*;

public class GamePanel extends JPanel {
    private MainWindow parent;

    private GameStatusPanel gameStatusPanel;
    private GameBoardPanel gameBoardPanel;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    public GamePanel(MainWindow parent){
        this.parent = parent;

        this.gameStatusPanel = null;
        this.gameBoardPanel = null;

        this.buildPanel();
        this.buildComponents();
    }

    private void buildPanel(){
        //this.setSize();
    }

    private void buildComponents(){

    }
}
