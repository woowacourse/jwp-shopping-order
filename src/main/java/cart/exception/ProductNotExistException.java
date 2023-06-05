package cart.exception;

public class ProductNotExistException extends NoSuchDataExistException {
    private static final String PRODUCT_NAME = "상품";

    public ProductNotExistException(final Long productId) {
        super(PRODUCT_NAME, productId.toString());
    }
}
