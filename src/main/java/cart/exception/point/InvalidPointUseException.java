package cart.exception.point;

import cart.exception.GlobalException;

public class InvalidPointUseException extends GlobalException {

    private static final String message = "상품 가격보다 더 많은 포인트를 입력하실 수 없습니다. 상품 가격: %d, 입력한 포인트 : %d";

    public InvalidPointUseException(final Integer productPrice, final Integer inputPoint) {
        super(String.format(message, productPrice, inputPoint));
    }
}
