import java.util.Scanner;

public class Game {
    private Board board;
    private Scanner input;

    Game() {
        board = new Board();
        input = new Scanner(System.in);
    }

    void conclude() {
        board = null;
        input.close();
        input = null;
    }

    Verdict playMove(Move move) {
        return board.playMove(move);
    }

    void display() {
        board.display();
    }

    Move nextMove() {
        int x1, y1;
        int x2, y2;
        x1 = input.nextInt();
        y1 = input.nextInt();
        x2 = input.nextInt();
        y2 = input.nextInt();
        return new Move(new Coordinate(x1, y1), new Coordinate(x2, y2));
    }

    Verdict getVerdict() {
        return board.getVerdict();
    }

    public static void main(String[] args) {

        Game game = new Game();
        game.display();

        while (true) {
            Move move = game.nextMove();
            Verdict verdict = game.playMove(move);
            if (verdict == Verdict.SUCCESSFUL_MOVE) {
                System.out.println("Successful move!");
            }
            if (verdict == Verdict.ILLEGAL_MOVE) {
                System.out.println("Illegal move.");
            }
            game.display();
            verdict = game.getVerdict();
            if (verdict.isConclusive()) {
                System.out.print("Game over: ");
                if (verdict == Verdict.DRAW)
                    System.out.println("Draw");
                else {
                    System.out.printf("%s won", (verdict == Verdict.WHITE_WON ? "White" : "Black"));
                }
                break;
            }
        }
        game.conclude();
    }
}
