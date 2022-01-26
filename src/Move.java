public class Move {
    private Coordinate from;
    private Coordinate to;

    Move(Coordinate from, Coordinate to) {
        this.from = from;
        this.to = to;
    }

    public Coordinate getFrom() {
        return from;
    }

    public void setFrom(Coordinate from) {
        this.from = from;
    }

    public Coordinate getTo() {
        return to;
    }

    public void setTo(Coordinate to) {
        this.to = to;
    }

    public Integer getDiffX() {
        return to.getX() - from.getX();
    }

    public Integer getDiffY() {
        return to.getY() - from.getY();
    }

    public Integer getDeltaX() {
        return Math.abs(getDiffX());
    }

    public Integer getDeltaY() {
        return Math.abs(getDiffY());
    }
}
