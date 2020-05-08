package ro.uaic.info.game;

import ro.uaic.info.panel.GameBoardPanel;

public class Board {
    private int[][] board;

    public static final int EMPTY = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    private GameBoardPanel parent;

    public Board(int width, int height, GameBoardPanel parent) {
        this.board = new int[height][width];
        this.parent = parent;
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int getPieceOn(int x, int y){
        return this.board[y][x];
    }

    public int getWidth(){
        if(this.board != null && this.board[0] != null)
            return this.board[0].length;
        return 0;
    }

    public int getHeight(){
        if(this.board != null)
            return this.board.length;
        return 0;
    }

    public void setEncodedBoard(String JSONBoard){
        String[] properties = JSONBoard.split(";");
        int height = Integer.parseInt(properties[0].split(",")[0].replace("{", ""));
        int width = Integer.parseInt(properties[0].split(",")[1]);

        String[] matArray = properties[1].split(",");

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                this.board[i][j] = Integer.parseInt(
                        matArray[i * width + j]
                                .replace("[","")
                                .replace("[","")
                                .replace("]", "")
                                .replace("]", "")
                                .replace("}", "")
                );
            }
        }

        this.parent.getParent().repaint();
    }
}
