package cart.exception.notfound;

public class ProductNotFoundException extends NotFoundException {

    private static final String MESSAGE = "해당 상품을 찾을 수 없습니다. 입력한 상품 id: %d";

    public ProductNotFoundException() {
        super("해당 상품을 찾을 수 없습니다.");
    }

    public ProductNotFoundException(final Long productId) {
        super(String.format(MESSAGE, productId));
    }
}
