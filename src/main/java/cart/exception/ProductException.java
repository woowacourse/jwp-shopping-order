package cart.exception;

import static cart.domain.Product.*;

public class ProductException extends RuntimeException {

    public ProductException(String message) {
        super(message);
    }

    public static class NotFound extends ProductException {

        public NotFound(Long id) {
            super("찾는 상품이 없습니다. " +
                    "id : " + id);
        }
    }

    public static class InvalidImageUrl extends ProductException {

        public InvalidImageUrl(String url) {
            super("잘못된 이미지 url입니다. " +
                    "url : " + url);
        }
    }

    public static class InvalidPrice extends ProductException {

        public InvalidPrice(Integer price) {
            super("상품 가격은 " + MINIMUM_PRICE + "원 이상이어야 합니다. " +
                    "price : " + price);
        }
    }

    public static class InvalidNameLength extends ProductException {

        public InvalidNameLength(String name) {
            super("상품 이름은 " + MINIMUM_NAME_LENGTH + "자 이상 " + MAXIMUM_NAME_LENGTH + "자 이하여야합니다. " +
                    "name : " + name);
        }
    }
}
