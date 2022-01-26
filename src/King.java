public class King extends Piece {
    King(Color color) {
        super(color);
    }

    @Override
    public boolean movesAs(Move move) {
        return (Math.max(move.getDeltaX(), move.getDeltaY()) <= 1);
    }

    @Override
    public Type getType() {
        return Type.KING;
    }

    @Override
    public char getSignature() {
        return (color == Color.BLACK ? Signature.BLACK_KING : Signature.WHITE_KING);
    }
}
