package cart.exception.product;

import cart.exception.BadRequestException;

public class ProductException {

    public static class NotExistProduct extends BadRequestException {
        public NotExistProduct() {
            super("존재하지 않는 상품입니다");
        }
    }

    public static class DuplicatedProduct extends BadRequestException {
        public DuplicatedProduct() {
            super("이미 존재하는 상품입니다");
        }
    }
}
