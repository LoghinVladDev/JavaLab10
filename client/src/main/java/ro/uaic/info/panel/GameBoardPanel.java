package ro.uaic.info.panel;

import ro.uaic.info.game.Board;
import ro.uaic.info.net.state.ClientState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;

public class GameBoardPanel extends JPanel {
    private Canvas drawingCanvas;

    public static final int DEFAULT_BOARD_WIDTH = 15;
    public static final int DEFAULT_BOARD_HEIGHT = 15;

    private String tokenColour;
    private boolean turn;

    public String getTokenColour() {
        return tokenColour;
    }

    public void setTokenColour(String tokenColour) {
        this.tokenColour = tokenColour;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    private int horizontalOffset;
    private int squareSize;

    private GamePanel parent;
    private Board gameBoard;

    private int offsetX;
    private int offsetY;
    private int panelWidth;
    private int panelHeight;

    @Override
    public GamePanel getParent() {
        return this.parent;
    }

    public Canvas getDrawingCanvas() {
        return this.drawingCanvas;
    }

    public GameBoardPanel(GamePanel parent){
        this.parent = parent;

        this.gameBoard = null;

        this.drawingCanvas = null;

        this.offsetX = parent.getX();
        this.offsetY = parent.getY();
        this.panelHeight = parent.getHeight();
        this.panelWidth  = (parent.getWidth()/4)*3;

        this.buildPanel();
        this.buildComponents();

        this.squareSize = this.getHeight()/this.gameBoard.getBoard().length - 2;
        this.horizontalOffset = (this.panelWidth - DEFAULT_BOARD_WIDTH * squareSize)/2;

        //this.buildLayout();
        this.buildListeners();
        System.out.println("Build game board");

        //System.out.println(this.getSize() + ", " + this.getLocation());

        this.setVisible(true);
        this.setEnabled(true);
    }

    public void buildListeners(){
        this.addMouseListener(
                new MouseListener() {
                    public void mouseClicked(MouseEvent e) { }
                    public void mousePressed(MouseEvent e) {
                        System.out.println("board " + e.getPoint().x + ", " + e.getPoint().y);
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

        this.setBackground(Color.RED);

        this.setVisible(true);
    }

    private void buildComponents(){
        this.drawingCanvas = new Canvas();
        this.drawingCanvas.setPreferredSize(
                new Dimension(
                        this.panelWidth,
                        this.panelHeight
                )
        );
        this.drawingCanvas.setMaximumSize(
                new Dimension(
                        this.panelWidth,
                        this.panelHeight
                )
        );
        this.drawingCanvas.setMinimumSize(
                new Dimension(
                        this.panelWidth,
                        this.panelHeight
                )
        );

        this.gameBoard = new Board(GameBoardPanel.DEFAULT_BOARD_WIDTH, GameBoardPanel.DEFAULT_BOARD_HEIGHT, this);
    }

    private boolean init = true;

    public void paintComponent(Graphics graphics){
        if(init) {
            graphics.setColor(Color.BLACK);

            //graphics.drawLine(this.panelWidth, 0, this.panelWidth, this.panelHeight); PANEL FINISH

            for (int i = 0; i < this.gameBoard.getHeight(); i++) {
                //System.out.println(i);
                graphics.drawLine(
                        horizontalOffset + squareSize / 2,
                        this.offsetY / 2 + squareSize * i + squareSize / 2,
                        horizontalOffset + squareSize * DEFAULT_BOARD_WIDTH - squareSize / 2,
                        this.offsetY / 2 + squareSize * i + squareSize / 2
                );
            }

            System.out.println((horizontalOffset + squareSize / 2) + ", " + (this.offsetY + squareSize / 2));

        /* upperLeft = new Point(horizontalOffset, this.offsetY);
        Point upperRight = new Point(horizontalOffset + squareSize, this.offsetY);
        Point lowerLeft = new Point(horizontalOffset, this.offsetY + squareSize);
        Point lowerRight = new Point(horizontalOffset + squareSize, this.offsetY + squareSize);


        System.out.println("Corners : " +
                upperLeft + "\n" +
                upperRight + "\n" +
                lowerLeft + "\n" +
                lowerRight + "\n"
        );*/

            for (int j = 0; j < this.gameBoard.getWidth(); j++) {
                graphics.drawLine(
                        horizontalOffset + squareSize * j + squareSize / 2,
                        this.offsetY / 2 + squareSize / 2,
                        horizontalOffset + squareSize * j + squareSize / 2,
                        this.offsetY / 2 + squareSize * DEFAULT_BOARD_HEIGHT - squareSize / 2
                );
            }
        }
        for(int i = 0; i < this.getGameBoard().getHeight(); i++){
            for(int j = 0; j < this.getGameBoard().getWidth(); j++){
                Point squareMiddle = new Point(
                        this.getHorizontalOffset() + this.getSquareSize() * j + this.getSquareSize()/2,
                        this.offsetY/2 + this.getSquareSize() * i + this.getSquareSize()/2
                );

                if(this.getGameBoard().getPieceOn(j, i) > 0){
                    graphics.setColor(this.getGameBoard().getPieceOn(j,i) == Board.WHITE ? Color.GRAY : Color.BLACK);

                    graphics.fillOval(squareMiddle.x-squareSize/2, squareMiddle.y-squareSize/2, squareSize - 3, squareSize - 3);
                }

                //System.out.println(squareMiddle + ", " + i + ", " + j);
            }
        }


    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public int getSquareSize() {
        return squareSize;
    }

    public int getHorizontalOffset() {
        return horizontalOffset;
    }

    public void drawToken(int x, int y){
        System.out.println(x  + ", " + y);

        if(this.isTurn()){
            Point circleMiddle = new Point(
                this.horizontalOffset + this.squareSize * x + this.squareSize/2,
                this.offsetY/2 + this.squareSize * y + this.squareSize/2
            );

            this.parent.getParent().getMessageHandler().addToMessageQueue(ClientState.PUT_PIECE);
            this.parent.getParent().getMessageHandler().addToMessageQueue("{" + x + "," + y + "}");
        }
    }

    public void getJSONEncodedMatrix(String matrixJSON){
        System.out.println("...map changed... " + matrixJSON);
        this.getGameBoard().setEncodedBoard(matrixJSON);
    }

    private void buildLayout(){
        this.add(this.drawingCanvas);
    }

    public void initialiseCanvas(){

    }

}
