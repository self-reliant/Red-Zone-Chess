public class Rook extends Piece {

    Rook(Color color) {
        super(color);
    }

    @Override
    public boolean movesAs(Move move) {
        return (move.getDiffX() == 0 || move.getDiffY() == 0);
    }

    @Override
    public Type getType() {
        return Type.ROOK;
    }

    @Override
    public char getSignature() {
        return (color == Color.BLACK ? Signature.BLACK_ROOK : Signature.WHITE_ROOK);
    }
}
