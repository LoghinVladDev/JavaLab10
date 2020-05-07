package ro.uaic.info.panel;

import ro.uaic.info.window.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GamePanel extends JPanel {
    private MainWindow parent;

    private GameStatusPanel gameStatusPanel;
    private GameBoardPanel gameBoardPanel;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    @Override
    public MainWindow getParent() {
        return this.parent;
    }

    public GameBoardPanel getGameBoardPanel() {
        return this.gameBoardPanel;
    }

    public GameStatusPanel getGameStatusPanel() {
        return this.gameStatusPanel;
    }

    public GamePanel(MainWindow parent){
        this.parent = parent;

        this.gameStatusPanel = null;
        this.gameBoardPanel = null;

        this.panelWidth = this.parent.getWidth() - 50;
        this.panelHeight = this.parent.getHeight() - 75;

        this.offsetX = 25;
        this.offsetY = 50;

        this.buildPanel();
        this.buildComponents();
        this.buildLayout();
        this.buildListeners();
        //this.setVisible(true);
        //this.setEnabled(true);
        //System.out.println(this.getSize() + ", " + this.getLocation());
    }

    private void buildPanel(){
        this.setBounds(
                new Rectangle(
                        this.offsetX,
                        this.offsetY,
                        this.panelWidth,
                        this.panelHeight
                )
        );
    }

    public void paintComponent(Graphics g){
        this.gameBoardPanel.paintComponent(g);
    }

    private void buildComponents(){
        this.gameBoardPanel = new GameBoardPanel(this);
        this.gameStatusPanel = new GameStatusPanel(this);
    }

    private void buildListeners(){
        for(int i = 0; i < this.gameBoardPanel.getGameBoard().getHeight(); i++){
            for(int j = 0; j < this.gameBoardPanel.getGameBoard().getWidth(); j++){
                Point squareMiddle = new Point(
                        this.gameBoardPanel.getHorizontalOffset() + this.gameBoardPanel.getSquareSize() * j + this.gameBoardPanel.getSquareSize()/2,
                        this.offsetY/2 + this.gameBoardPanel.getSquareSize() * i + this.gameBoardPanel.getSquareSize()/2
                );

                int squareLeft = squareMiddle.x - this.gameBoardPanel.getSquareSize()/2;
                int squareRight = squareMiddle.x + this.gameBoardPanel.getSquareSize()/2;
                int squareTop = squareMiddle.y - this.gameBoardPanel.getSquareSize()/2;
                int squareBottom = squareMiddle.y + this.gameBoardPanel.getSquareSize()/2;

                int finalJ = j;
                int finalI = i;
                this.addMouseListener(new MouseListener() {
                    public void mouseClicked(MouseEvent e) { }
                    public void mousePressed(MouseEvent e) { }
                    public void mouseReleased(MouseEvent e) {
                        if(
                            e.getPoint().x - 8 >= squareLeft &&
                            e.getPoint().x - 8 <= squareRight &&
                            e.getPoint().y - 30 >= squareTop &&
                            e.getPoint().y - 30 <= squareBottom
                        )
                            gameBoardPanel.drawToken(finalJ, finalI);
                    }
                    public void mouseEntered(MouseEvent e) { }
                    public void mouseExited(MouseEvent e) { }
                });
            }
        }
    }

    private void buildLayout(){
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(
                    layout.createSequentialGroup()
                        .addComponent(this.gameBoardPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(this.gameStatusPanel, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(
                    layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(this.gameBoardPanel)
                        .addComponent(this.gameStatusPanel)
                )
        );
    }
}
