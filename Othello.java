import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Othello {
    private int[][] board=new int[8][8]; //should be private
    private int turn =-1;
    public int pcount=0;
    public int rcount=0;
    public int count=0;

    Othello(int[][] board){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j]=board[i][j];
            }
        }
    }
    Othello(){
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.board[i][j]=0;
            }
        }
        this.board[4][4]=1;
        this.board[3][3]=1;
        this.board[3][4]=-1;
        this.board[4][3]=-1;
    }

    private int score(int[][] board, int turn) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                score += board[i][j];
            }
        }
        return (turn > 0) ? score : -1 * score;
    }

    public int currentScore(){
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                score += board[i][j];
            }
        }
        return (turn > 0) ? score : -1 * score;
    }


    public void printBoard(){
        for (int i = 0; i < 8; i++) {
            System.out.println(Arrays.toString(this.board[i]));
        }
    }



    // best moves

    public int search(int[][] board,int y, int x,int i,int j,int enemy){
        while(i<8 && i>=0 && j<8 && j>=0 && board[i][j]==enemy){
            i+=y;
            j+=x;
        }
        if(i<8 && i>=0 && j<8 && j>=0){
            if(board[i][j] == -enemy){
                return 8*i+j;
            }
        }
        return -100;
    }

    public List<List<Integer>> validMove(int[][] board,int turn ,int[][] dir){
        int enemy=-turn;
        ArrayList<List<Integer>> moves=new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if(board[i][j]==enemy){
                    for (int k = 0; k < 8; k++) {
                        if(i+dir[k][0]>7 || i+dir[k][0]<0 ||  j+dir[k][1]<0 || j+dir[k][1]>7 ){
                            continue;
                        }
                        if(board[i+dir[k][0]][j+dir[k][1]]==0){
                            int searchMove=search(board,-dir[k][0],-dir[k][1],i,j,enemy);
                            if(searchMove!= -100){
                                List<Integer> move=new ArrayList<>();
                                move.add(8*(i+dir[k][0])+(j+dir[k][1]));
                                move.add(searchMove);
                                count++;
                                moves.add(move);
                            }
                        }
                    }
                }
            }
        }
        return moves;
    }

    public void performMove(int[][] board, int turn,int move,int pos){
        pcount++;
//        System.out.println(pcount);
        int i=move/8;
        int j=move%8;
        int y=pos/8;
        int x=pos%8;
        int ydir=(i-y==0)?0:(i-y>0)?-1:1;
        int xdir=(j-x==0)?0:(j-x>0)?-1:1;
        while(i!=y || j!=x){
            board[i][j]=turn;
            i+=ydir;
            j+=xdir;
        }
    }
    public void reverseMove(int[][] board, int turn,int move,int pos){
        this.rcount--;
        int i=move/8;
        int j=move%8;
        board[i][j]=0;
        int y=pos/8;
        int x=pos%8;
        int ydir=(i-y==0)?0:(i-y>0)?-1:1;
        int xdir=(j-x==0)?0:(j-x>0)?-1:1;
        i+=ydir;
        j+=xdir;
        while(i!=y || j!=x){
            board[i][j]=-turn;
            i+=ydir;
            j+=xdir;
        }

    }

    public int bestScore(int[][] board,int turn,int depth,int[][] dir){
        List<List<Integer>> validMoves=validMove(board,turn,dir);
        if(depth==0) {
            return score(board,turn);
        }
        if(validMoves.isEmpty()){
            if(validMove(board, -turn, dir).isEmpty()){
                return -99;
            }
            else{
                return -1 * bestScore(board, -turn, depth-1, dir);
            }
        }

        int optimalScore=-65;
        for (int i=0;i<validMoves.size();i++) {
            performMove(board,turn,validMoves.get(i).get(0),validMoves.get(i).get(1));
            int scoreTemp = -1 * bestScore(board, -turn, depth - 1, dir);
            if(scoreTemp==99){
                reverseMove(board, turn, validMoves.get(i).get(0), validMoves.get(i).get(1));
                continue;
            }

            optimalScore = Math.max(optimalScore, scoreTemp);
            reverseMove(board, turn, validMoves.get(i).get(0), validMoves.get(i).get(1));
        }

        return optimalScore;
    }

    public List<Integer> bestMove(int[][] board , int turn,int depth,int[][] dir) {
        List<List<Integer>> validMoves = validMove(board, turn, dir);

        int optimalScore = 65 * -1;
        List<Integer> movett = new ArrayList<>();

        for (int i = 0; i < validMoves.size(); i++) {
            performMove(board, turn, validMoves.get(i).get(0), validMoves.get(i).get(1));
            int scoreTemp = -1 * bestScore(board, -turn, depth - 1, dir);
            if (scoreTemp == 99) {
                scoreTemp=score(board,turn);
            }
            if (scoreTemp > optimalScore) {
                optimalScore = scoreTemp;
                movett = validMoves.get(i);
            }
            reverseMove(board, turn, validMoves.get(i).get(0), validMoves.get(i).get(1));
        }
        return movett;
    }

    public void fullGame(int depth,int[][] dir){
        List<Integer> bmove=bestMove(this.board,this.turn,depth,dir);
        if(bmove.isEmpty()){
            this.turn=-this.turn;
            bmove=bestMove(this.board,this.turn,depth-1,dir);
            if(bmove.isEmpty()){
                return;
            }
            else{
                performMove(this.board,this.turn,bmove.get(0),bmove.get(1));
                this.turn=-this.turn;
                printBoard();
                fullGame(depth,dir);
            }
        }
        performMove(this.board,this.turn,bmove.get(0),bmove.get(1));
        this.turn=-this.turn;
        printBoard();
        System.out.println("=========================");
        System.out.println("=========================");
        fullGame(depth,dir);
    }
}
