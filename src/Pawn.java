public class Pawn extends Piece {
    Pawn(Color color) {
        super(color);
    }

    @Override
    public boolean movesAs(Move move) {
        return (move.getDeltaY() <= 1 && (color == Color.WHITE ? move.getDiffX() == -1 : move.getDiffX() == 1));
    }

    public boolean forward(Move move) {
        return (move.getDeltaY() == 0 && (color == Color.WHITE ? move.getDiffX() == -1 : move.getDiffX() == 1));
    }

    public boolean sideways(Move move) {
        return (move.getDeltaY() == 1 && (color == Color.WHITE ? move.getDiffX() == -1 : move.getDiffX() == 1));
    }

    @Override
    public Type getType() {
        return Type.PAWN;
    }

    @Override
    public char getSignature() {
        return (color == Color.BLACK ? Signature.BLACK_PAWN : Signature.WHITE_PAWN);
    }
}
