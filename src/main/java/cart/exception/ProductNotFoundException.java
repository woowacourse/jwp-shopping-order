package cart.exception;

public class ProductNotFoundException extends CartException {

    public ProductNotFoundException() {
        super("상품을 찾을 수 없습니다.");
    }
}
