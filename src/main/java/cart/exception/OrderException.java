package cart.exception;

import cart.domain.Product;

public class OrderException extends RuntimeException {

    private static final int NOT_ENOUGH_STOCK_ERROR_CODE = 1;
    private static final int NOT_ENOUGH_POINT_ERROR_CODE = 2;

    private final int errorCode;

    public OrderException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public static class NotEnoughPointException extends OrderException {
        public NotEnoughPointException() {
            super(NOT_ENOUGH_POINT_ERROR_CODE, "포인트가 부족합니다.");
        }
    }

    public static class NotEnoughStockException extends OrderException {
        public NotEnoughStockException(Product product) {
            super(NOT_ENOUGH_STOCK_ERROR_CODE, product.getName() + "의 재고가 부족합니다.");
        }
    }

    public int getErrorCode() {
        return errorCode;
    }
}
