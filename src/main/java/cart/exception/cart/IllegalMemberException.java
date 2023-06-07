package cart.exception.cart;

public class IllegalMemberException extends CartItemException {

    private static final String ILLEGAL_MEMBER_ACCEPT_EXCEPTION = "올바르지 않은 사용자가 장바구니 상품에 접근했습니다.";

    public IllegalMemberException() {
        super(ILLEGAL_MEMBER_ACCEPT_EXCEPTION);
    }
}
