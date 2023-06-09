package cart.exception.internal;

public class NoCouponException extends InternalException {

    @Override
    public int getErrorCode() {
        return 5002;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
