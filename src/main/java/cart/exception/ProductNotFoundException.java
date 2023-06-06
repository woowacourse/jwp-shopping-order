package cart.exception;

public class ProductNotFoundException extends ShoppingOrderException {

    private static final String MESSAGE = "id: %s 에 해당하는 상품을 찾을 수 없습니다.";

    public ProductNotFoundException(final Long id) {
        super(String.format(MESSAGE, id));
    }
}
