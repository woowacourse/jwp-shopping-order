package cart.exception;

public class ProductNotFoundException extends ProductException {
    public ProductNotFoundException(final Long id) {
        super("해당 상품을 찾을 수 없습니다 : " + id);
    }

}
