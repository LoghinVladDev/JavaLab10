package ro.uaic.info.resource.object;

public class Board {
    private int[][] board;

    public static final int EMPTY = 0;
    public static final int WHITE = 1;
    public static final int BLACK = 2;

    public Board(int width,int height){
        this.board = new int[height][width];
    }

    public int[][] getBoard() {
        return this.board;
    }

    public int getPieceOn(int x, int y){
        return this.board[y][x];
    }
}
