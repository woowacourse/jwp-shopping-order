package cart.exception;

import cart.domain.Product;

public class OrderException extends RuntimeException {

    private final int errorCode;

    public OrderException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static class NotEnoughPointException extends OrderException {
        public NotEnoughPointException() {
            super(ErrorCode.NOT_ENOUGH_POINT.getErrorCode(), "포인트가 부족합니다.");
        }
    }

    public static class NotEnoughStockException extends OrderException {
        public NotEnoughStockException(Product product) {
            super(ErrorCode.NOT_ENOUGH_STOCK.getErrorCode(), product.getName() + "의 재고가 부족합니다.");
        }
    }

    public int getErrorCode() {
        return errorCode;
    }
}
