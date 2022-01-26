public class Board {
    Point[][] points;
    Color turn;

    private Verdict inRedZone() {
        for (int i = 0; i < 8; i++) {
            if (points[0][i].getStatus() == Status.PIECE) {
                Piece piece = (Piece) points[0][i];
                if (piece.getColor() == Color.WHITE)
                    return Verdict.WHITE_WON;
            }
            if (points[7][i].getStatus() == Status.PIECE) {
                Piece piece = (Piece) points[7][i];
                if (piece.getColor() == Color.BLACK)
                    return Verdict.BLACK_WON;
            }
        }
        return Verdict.CONTINUE;
    }

    boolean cannotMove(Color color) {
        Board newBoard = new Board(this);
        boolean cannotMove = true;
        for (int x1 = 0; x1 < 8 && cannotMove; x1++) {
            for (int y1 = 0; y1 < 8 && cannotMove; y1++) {
                for (int x2 = 0; x2 < 8 && cannotMove; x2++) {
                    for (int y2 = 0; y2 < 8 && cannotMove; y2++) {
                        if (points[x1][y1].getStatus() == Status.PIECE) {
                            Piece piece = (Piece) points[x1][y1];
                            if (piece.getColor() == color)
                                cannotMove = (newBoard.playMove(new Move(
                                        new Coordinate(x1, y1), new Coordinate(x2, y2))) == Verdict.ILLEGAL_MOVE);
                        }
                    }
                }
            }
        }
        return cannotMove;
    }

    Verdict getVerdict() {
        Verdict inRedZone = inRedZone();
        if (inRedZone.isConclusive())
            return inRedZone;
        boolean whiteCannotMove = cannotMove(Color.WHITE);
        boolean blackCannotMove = cannotMove(Color.BLACK);
        boolean stalemate = (whiteCannotMove && blackCannotMove);
        if (stalemate)
            return Verdict.DRAW;
        if (whiteCannotMove && isChecked(Color.WHITE))
            return Verdict.BLACK_WON;
        if (blackCannotMove && isChecked(Color.BLACK))
            return Verdict.WHITE_WON;
        return Verdict.CONTINUE;
    }

    public void display() {
        System.out.println("Board:");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.print(points[i][j].getSignature());
            }
            System.out.println();
        }
        System.out.println();
    }

    private void setKings() {
        points[6][4] = new King(Color.WHITE);
        points[1][4] = new King(Color.BLACK);
    }

    private void setQueens() {
        points[6][3] = new Queen(Color.WHITE);
        points[1][3] = new Queen(Color.BLACK);
    }

    private void setRooks() {
        points[6][0] = new Rook(Color.WHITE);
        points[6][7] = new Rook(Color.WHITE);

        points[1][0] = new Rook(Color.BLACK);
        points[1][7] = new Rook(Color.BLACK);
    }

    private void setBishops() {
        points[6][2] = new Bishop(Color.WHITE);
        points[6][5] = new Bishop(Color.WHITE);

        points[1][2] = new Bishop(Color.BLACK);
        points[1][5] = new Bishop(Color.BLACK);
    }

    private void setKnights() {
        points[6][1] = new Knight(Color.WHITE);
        points[6][6] = new Knight(Color.WHITE);

        points[1][1] = new Knight(Color.BLACK);
        points[1][6] = new Knight(Color.BLACK);
    }

    private void setPawns() {
        for (int i = 0; i < 8; i++) {
            points[5][i] = new Pawn(Color.WHITE);
            points[2][i] = new Pawn(Color.BLACK);
        }
    }

    private void setAll() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                points[i][j] = new Empty();
            }
        }
        setKings();
        setQueens();
        setRooks();
        setBishops();
        setKnights();
        setPawns();
    }

    public Board() {
        points = new Point[8][8];
        turn = Color.WHITE;
        setAll();
    }

    public Board(Board board) {
        points = new Point[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.points[i][j].getStatus() == Status.EMPTY) {
                    points[i][j] = new Empty();
                } else {
                    Piece piece = (Piece) board.points[i][j];
                    points[i][j] = getInstanceType(piece);
                }
            }
        }
        turn = board.turn;
    }

    Piece getInstanceType(Piece piece) {
        Type type = piece.getType();
        Color color = piece.getColor();
        if (type == Type.KING)
            return new King(color);
        if (type == Type.QUEEN)
            return new Queen(color);
        if (type == Type.ROOK)
            return new Rook(color);
        if (type == Type.BISHOP)
            return new Bishop(color);
        if (type == Type.KNIGHT)
            return new Knight(color);
        return new Pawn(color);
    }

    public Verdict playMove(Move move) {
        Point from = points[move.getFrom().getX()][move.getFrom().getY()];
        if (from.getStatus() == Status.EMPTY) { // no piece to move
            return Verdict.ILLEGAL_MOVE;
        }
        Piece piece = (Piece) from;
        if (piece.getColor() != turn) // not color's turn to move
            return Verdict.ILLEGAL_MOVE;
        if (isChecked(switchColor(piece.getColor()))) { // the checked side must move
            return Verdict.ILLEGAL_MOVE;
        }
        Point to = points[move.getTo().getX()][move.getTo().getY()];
        if (to.getStatus() == Status.PIECE && ((Piece) to).getColor() == piece.getColor()) { // blocked by same color piece
            return Verdict.ILLEGAL_MOVE;
        }
        if (!piece.movesAs(move)) { // piece doesn't move as 'move'
            return Verdict.ILLEGAL_MOVE;
        }
        if (piece.getType() != Type.KNIGHT && blockedPath(move)) { // path is not blocked
            return Verdict.ILLEGAL_MOVE;
        }
        if (piece.getType() == Type.PAWN) { // corner case
            Pawn pawn = (Pawn) piece;
            if (pawn.forward(move)) {
                if (to.getStatus() == Status.PIECE) {
                    return Verdict.ILLEGAL_MOVE;
                }
            }
            if (pawn.sideways(move)) {
                if (to.getStatus() == Status.EMPTY) {
                    return Verdict.ILLEGAL_MOVE;
                }
            }
        }
        // create new board to check its validity resultant after the move
        Board newBoard = new Board(this);
        newBoard.points[move.getTo().getX()][move.getTo().getY()] = getInstanceType(piece);
        newBoard.points[move.getFrom().getX()][move.getFrom().getY()] = new Empty();
        if (newBoard.isChecked(piece.getColor())) {
            return Verdict.ILLEGAL_MOVE;
        }
        this.points = newBoard.points;
        turn = switchColor(turn);
        return Verdict.SUCCESSFUL_MOVE;
    }

    private boolean blockedPath(Move move) {
        int dx = move.getDiffX();
        int dy = move.getDiffY();
        int rx = Utilities.sign(dx);
        int ry = Utilities.sign(dy);
        int i = 1;
        Coordinate start = move.getFrom();
        while (i * rx != dx || i * ry != dy) {
            Coordinate to = new Coordinate(start.getX() + i * rx, start.getY() + i * ry);
            if (withinBounds(to)) {
                if (points[to.getX()][to.getY()].getStatus() == Status.PIECE) {
                    return true;
                }
            }
            i++;
        }
        return false;
    }

    private boolean isChecked(int[] dx, int[] dy, Color color, Coordinate king, Type[] threats) {
        assert(dx.length == dy.length);
        for (int i = 0; i < dx.length; i++) {
            int rx = dx[i];
            int ry = dy[i];
            int j = 1;
            int threatX, threatY;
            while (withinBounds(threatX = king.getX() + rx * j, threatY = king.getY() + ry * j)) {
                if (points[threatX][threatY].getStatus() == Status.PIECE) {
                    if (((Piece) points[threatX][threatY]).getColor() == switchColor(color))
                        break;
                    if (isThreat(threatX, threatY, color, threats)) {
                        return true;
                    } else {
                        break;
                    }
                }
                j++;
            }
        }
        return false;
    }

    private boolean isThreat(int threatX, int threatY, Color color, Type[] threats) {
        boolean isThreat = false;
        for (Type threat : threats) {
            if (points[threatX][threatY].getStatus() == Status.PIECE) {
                Piece piece = (Piece) points[threatX][threatY];
                isThreat |= (piece.getType().equals(threat) && piece.getColor() == color);
            }
        }
        return isThreat;
    }

    private boolean checkedDiagonally(Color color, Coordinate king) {
        int[] dx = {-1, +1, -1, +1};
        int[] dy = {+1, -1, -1, +1};
        return isChecked(dx, dy, color, king, new Type[]{Type.BISHOP, Type.QUEEN});
    }

    private boolean checkedLinearly(Color color, Coordinate king) {
        int[] dx = {+1, -1, +0, +0};
        int[] dy = {+0, +0, +1, -1};
        return isChecked(dx, dy, color, king, new Type[]{Type.ROOK, Type.QUEEN});
    }

    private boolean checkedByPawn(Color color, Coordinate king) {
        int[] dx = {-1, +1, -1, +1};
        int[] dy = {+1, -1, -1, +1};
        for (int i = 0; i < 4; i++) {
            int x = king.getX() + dx[i];
            int y = king.getY() + dy[i];
            if (withinBounds(x, y) && points[x][y].getStatus() == Status.PIECE) {
                Piece piece = (Piece) points[x][y];
                if (piece.getType() == Type.PAWN && piece.getColor() == color)
                    return true;
            }
        }
        return false;
    }

    private boolean checkedByKnight(Color color, Coordinate king) {
        int[] dx = {-2, -2, -1, -1, +1, +1, +2, +2};
        int[] dy = {-1, +1, -2, +2, -2, +2, -1, +1};
        for (int i = 0; i < 8; i++) {
            int x = king.getX() + dx[i];
            int y = king.getY() + dy[i];
            if (withinBounds(x, y) && points[x][y].getStatus() == Status.PIECE) {
                Piece piece = (Piece) points[x][y];
                if (piece.getType() == Type.KNIGHT && piece.getColor() == color)
                    return true;
            }
        }
        return false;
    }

    private boolean checkedByKing(Color color, Coordinate king) {
        int[] dx = {-1, -1, -1, +0, +0, +1, +1, +1};
        int[] dy = {-1, +0, +1, -1, +1, -1, +0, +1};
        for (int i = 0; i < 8; i++) {
            int x = king.getX() + dx[i];
            int y = king.getY() + dy[i];
            if (withinBounds(x, y) && points[x][y].getStatus() == Status.PIECE) {
                Piece piece = (Piece) points[x][y];
                if (piece.getType() == Type.KING && piece.getColor() == color)
                    return true;
            }
        }
        return false;
    }

    private boolean isChecked(Color color) {
        Coordinate king = findKing(color);
        // System.out.printf("%s %d %d\n", color, king.getX(), king.getY());
        if (!withinBounds(king))
            return false;
        color = switchColor(color);
        return checkedDiagonally(color, king) || checkedLinearly(color, king) ||
                checkedByPawn(color, king) || checkedByKnight(color, king) || checkedByKing(color, king);
    }

    Coordinate findKing(Color color) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (points[i][j].getStatus() == Status.PIECE) {
                    Piece piece = (Piece) points[i][j];
                    if (piece.getType() == Type.KING && piece.getColor() == color) {
                        return new Coordinate(i, j);
                    }
                }
            }
        }
        return new Coordinate(-1, -1);
    }

    private boolean withinBounds(Coordinate coordinate) {
        return (0 <= Math.min(coordinate.getX(), coordinate.getY()) &&
                Math.max(coordinate.getX(), coordinate.getY()) < 8);
    }

    private boolean withinBounds(int x, int y) {
        return withinBounds(new Coordinate(x, y));
    }

    private Color switchColor(Color color) {
        return (color == Color.WHITE ? Color.BLACK : Color.WHITE);
    }
}
