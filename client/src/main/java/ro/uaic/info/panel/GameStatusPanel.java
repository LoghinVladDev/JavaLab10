package ro.uaic.info.panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GameStatusPanel extends JPanel {
    private GamePanel parent;

    private JLabel turnLabel;
    private boolean turnStatus;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    @Override
    public GamePanel getParent() {
        return parent;
    }

    public boolean isTurnStatus() {
        return turnStatus;
    }

    public GameStatusPanel(GamePanel parent){
        this.parent = parent;

        this.turnLabel = null;
        this.turnStatus = false;

        this.offsetX = parent.getX() + ( parent.getWidth() / 4 ) * 3;
        this.offsetY = parent.getY();
        this.panelWidth = parent.getWidth() / 4;
        this.panelHeight = parent.getHeight();

        this.buildPanel();
        this.buildComponents();
        this.buildLayout();
        this.buildListeners();
        System.out.println("Build game status");

        //System.out.println(this.getSize() + ", " + this.getLocation());
        this.setVisible(true);
        this.setEnabled(true);
    }

    private void buildComponents(){
        this.turnLabel = new JLabel("TODO");
    }

    private void buildLayout(){
        this.add(this.turnLabel);
    }

    private void buildListeners(){
        this.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent e) { }
                    public void mousePressed(MouseEvent e) {
                        System.out.println("status " + e.getPoint().x + ", " + e.getPoint().y);
                    }
                    public void mouseReleased(MouseEvent e) { }
                    public void mouseEntered(MouseEvent e) { }
                    public void mouseExited(MouseEvent e) { }
                }
        );
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

        this.setBackground(Color.BLUE);

        this.setVisible(true);
    }
}
