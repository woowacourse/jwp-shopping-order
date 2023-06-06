package cart.exception.authexception;

public class CartUnauthorizedException extends UnauthorizedException {

    private static final String MESSAGE = "해당 장바구니에 접근 권한이 없습니다. cartItemId = %d, memberId = %d";

    public CartUnauthorizedException(long cartItemId, long memberId) {
        super(String.format(MESSAGE, cartItemId, memberId));
    }
}
