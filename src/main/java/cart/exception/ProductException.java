package cart.exception;

public class ProductException extends RuntimeException {

    public ProductException(String message) {
        super(message);
    }

    public static class IllegalProduct extends ProductException {
        public IllegalProduct() {
            super("존재하지 않는 상품입니다.");
        }
    }

    public static class DuplicatedProduct extends ProductException {
        public DuplicatedProduct() {
            super("이미 등록된 상품입니다.");
        }
    }
}
