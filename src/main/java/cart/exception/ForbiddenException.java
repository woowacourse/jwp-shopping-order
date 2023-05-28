package cart.exception;

public class ForbiddenException extends RuntimeException {

    public ForbiddenException(final String cartItemId, final String memberName) {
        super("Illegal member attempts to cart; cartItemId=" + cartItemId + ", memberName=" + memberName);
    }
}
