package cart.exception;

import cart.domain.Member;

public class CartItemAlreadyExistException extends CartItemException {
    public CartItemAlreadyExistException(final Member member, final Long productId) {
        super(member.getEmail() + "의 장바구니에 이미 " + productId + "상품이 존재합니다.");
    }
}
