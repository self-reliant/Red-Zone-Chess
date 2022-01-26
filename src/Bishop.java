public class Bishop extends Piece {
    Bishop(Color color) {
        super(color);
    }

    @Override
    public boolean movesAs(Move move) {
        return (move.getDeltaX().equals(move.getDeltaY()));
    }

    @Override
    public Type getType() {
        return Type.BISHOP;
    }

    @Override
    public char getSignature() {
        return (color == Color.BLACK ? Signature.BLACK_BISHOP : Signature.WHITE_BISHOP);
    }
}
