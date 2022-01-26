public class Empty extends Point {
    @Override
    public Status getStatus() {
        return Status.EMPTY;
    }

    @Override
    public char getSignature() {
        return Signature.EMPTY;
    }
}
