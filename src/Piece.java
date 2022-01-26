public abstract class Piece extends Point {
    protected Color color;

    Piece(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Status getStatus() {
        return Status.PIECE;
    }

    public abstract boolean movesAs(Move move);

    public abstract Type getType();
}
