package ro.uaic.info.game;

public class Board {
    private int[][] board;

    public static final int EMPTY = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public Board(int width, int height){
        this.board = new int[height][width];
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
}
