public class Queen extends Piece {
    Queen(Color color) {
        super(color);
    }

    @Override
    public boolean movesAs(Move move) {
        return ((move.getDeltaX().equals(move.getDeltaY())) ||
                (move.getDiffX() == 0 || move.getDiffY() == 0));
    }

    @Override
    public Type getType() {
        return Type.QUEEN;
    }

    @Override
    public char getSignature() {
        return (color == Color.BLACK ? Signature.BLACK_QUEEN : Signature.WHITE_QUEEN);
    }
}
