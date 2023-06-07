package cart.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("해당 상품은 존재하지 않습니다.");
    }
}
