package cart.exception.internal;

public class NoMemberException extends InternalException {

    @Override
    public int getErrorCode() {
        return 5003;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
