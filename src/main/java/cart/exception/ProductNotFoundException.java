package cart.exception;

public class ProductNotFoundException extends GlobalException {

    private static final String message = "해당 상품을 찾을 수 없습니다. 입력한 상품 id: %d";

    public ProductNotFoundException() {
        super("해당 상품을 찾을 수 없습니다.");
    }

    public ProductNotFoundException(final Long productId) {
        super(String.format(message, productId));
    }
}
