package cart.domain;

import cart.exception.InvalidPointException;

public class CreditCard {

    private final String cardNumber;
    private final int cvc;

    public CreditCard(final String cardNumber, final int cvc) {
        validate(cardNumber, cvc);
        this.cardNumber = cardNumber;
        this.cvc = cvc;
    }

    private void validate(final String cardNumber, final int cvc) {
        // 검증 api 호출
    }

    public void payWithPoint(final int totalPrice, final Point usingPoint) {
        if (usingPoint.getValue() > totalPrice) {
            throw new InvalidPointException("결제 금액보다 많은 포인트를 사용할 수 없습니다.");
        }
        // 결제 api 호출
    }
}
