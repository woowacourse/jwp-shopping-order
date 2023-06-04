package cart.exception;

public class NotEnoughStockException extends ShoppingOrderException {

    private static final String MESSAGE = "재고 : %s 보다 주문량 : %s 이 더 많습니다.";

    public NotEnoughStockException(final int stock, final int orderQuantity) {
        super(String.format(MESSAGE, stock, orderQuantity));
    }
}
