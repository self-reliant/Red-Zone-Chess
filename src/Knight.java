public class Knight extends Piece {
    Knight(Color color) {
        super(color);
    }

    @Override
    public boolean movesAs(Move move) {
        int dx = move.getDeltaX();
        int dy = move.getDeltaY();
        return (Math.min(dx, dy) == 1 && Math.max(dx, dy) == 2);
    }

    @Override
    public Type getType() {
        return Type.KNIGHT;
    }

    @Override
    public char getSignature() {
        return (color == Color.BLACK ? Signature.BLACK_KNIGHT : Signature.WHITE_KNIGHT);
    }
}
