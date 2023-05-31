package cart.exception;

public final class PointOverTotalPriceException extends RuntimeException {

    public PointOverTotalPriceException() {
        super("주문 금액 이상으로 포인트를 사용할 수 없습니다.");
    }
}
