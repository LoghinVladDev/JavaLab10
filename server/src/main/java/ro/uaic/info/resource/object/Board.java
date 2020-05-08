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

    public void putPiece(int x, int y, int player){
        this.board[y][x] = player;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb
                .append("{")
                .append(this.board == null ? 0 : this.board.length)
                .append(",")
                .append(this.board == null ? 0 : (this.board[0] == null ? 0 : this.board[0].length))
                .append(";[");
        for(int i = 0, height = this.board == null ? 0 : this.board.length; i < height; i++){
            sb.append('[');
            for(int j = 0, width = this.board[i] == null ? 0 : this.board[i].length; j < width; j++){
                sb.append(this.board[i][j]);
                if(j < width - 1)
                    sb.append(",");
            }
            sb.append("]");
            if(i < height - 1)
                sb.append(",");
        }
        return sb.append("]}").toString();
    }

    public boolean inMatrix(int i, int j, int n, int m){
        return i >= 0 && i < n && j >= 0 && j < m;
    }

    public int checkForPlayerVictory(int x, int y){
        int n = this.board.length;
        int m = this.board[0].length;

        System.out.println("stuck in ... ");

        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); i--, j--){
            System.out.println("stuck in ... 1 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); i--){
            System.out.println("stuck in ... 2 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); i--, j++){
            System.out.println("stuck in ... 3 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m);  j++){
            System.out.println("stuck in ... 4 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); i++, j++){
            System.out.println("stuck in ... 5 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); i++){
            System.out.println("stuck in ... 6 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); i++, j--){
            System.out.println("stuck in ... 7 ");
            if(this.board[i][j] == token)
                pieceCount++;
            if(pieceCount == 5)
                return token;
        }
        for(int i = y, j = x, pieceCount = 0, token = this.board[y][x]; inMatrix(i,j,n,m); j--) {
            System.out.println("stuck in ... 8 ");
            if (this.board[i][j] == token)
                pieceCount++;
            if (pieceCount == 5)
                return token;
        }
        return 0;
    }
}
