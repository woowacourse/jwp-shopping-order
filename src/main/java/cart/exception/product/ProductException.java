package cart.exception.product;

public class ProductException extends RuntimeException{
    public ProductException(String message) {
        super(message);
    }

    public static class NoProduct extends ProductException {
        public NoProduct() {
            super("존재하지 않는 상품입니다");
        }
    }

    public static class DuplicatedProduct extends ProductException {
        public DuplicatedProduct() {
            super("이미 존재하는 상품입니다");
        }
    }
}
