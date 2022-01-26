public enum Verdict {
    WHITE_WON,
    BLACK_WON,
    DRAW,
    CONTINUE,
    ILLEGAL_MOVE,
    SUCCESSFUL_MOVE;

    boolean isConclusive() {
        return (this == Verdict.WHITE_WON || this == Verdict.BLACK_WON || this == Verdict.DRAW);
    }
}
