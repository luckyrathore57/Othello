import java.util.List;

public class Main {
    public static void main(String[] args) {
        Othello oth=new Othello();
        int[][] dir =new int[8][2];
        int x=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i!=1 || j!=1){
                    dir[x][0]=i-1;
                    dir[x][1]=j-1;
                    x++;
                }

            }
        }

        oth.printBoard();
//        int ttt=oth.bestScore(oth.board,oth.turn,3,dir);
        System.out.println("kkkkk");
        oth.printBoard();
        oth.fullGame(8,dir);

        int t=oth.currentScore();
        System.out.println(t);
//        System.out.println(oth.count);

//        System.out.println(ttt);

    }

}
