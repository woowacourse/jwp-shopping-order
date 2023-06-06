package cart.exception;

public class PriceNotMatchException extends ShoppingOrderException {

    private static final String MESSAGE = "예상 총 가격 : %s, 실제 총 가격 : %s 이 일치하지 않습니다.";

    public PriceNotMatchException(final int expectedPrice, final int realPrice) {
        super(String.format(MESSAGE, expectedPrice, realPrice));
    }
}
