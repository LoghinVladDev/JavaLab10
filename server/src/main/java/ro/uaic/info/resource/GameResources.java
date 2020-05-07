package ro.uaic.info.resource;

import ro.uaic.info.resource.object.Board;

public class GameResources {

    public static final int DEFAULT_BOARD_HEIGHT = 15;
    public static final int DEFAULT_BOARD_WIDTH = 15;

    private Board board;

    public GameResources(){
        this.board = new Board(DEFAULT_BOARD_WIDTH, DEFAULT_BOARD_HEIGHT);
    }

    public GameResources withBoardSize(int width, int height){
        this.board = new Board(width, height);
        return this;
    }

    public Board getBoard(){
        return this.board;
    }
}
