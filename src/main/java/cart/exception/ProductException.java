package cart.exception;

public class ProductException extends RuntimeException {

    public ProductException(String message) {
        super(message);
    }

    public static class NameEmpty extends ProductException {

        public NameEmpty() {
            super("상품 이름은 존재해야 합니다.");
        }
    }

    public static class NameLength extends ProductException {

        public NameLength(int currentLength, int maxLength) {
            super("상품 이름은 최대 " + maxLength + "글자까지 가능합니다. 현재 길이: " + currentLength);
        }
    }

    public static class NegativePrice extends ProductException {

        public NegativePrice() {
            super("상품 가격은 음수일 수 없습니다.");
        }
    }

    public static class ImageEmpty extends ProductException {

        public ImageEmpty() {
            super("상품 이미지는 존재해야 합니다.");
        }
    }

    public static class ImageLength extends ProductException {

        public ImageLength(int currentLength, int maxLength) {
            super("상품 이미지 URL은 최대 " + maxLength + "글자까지 가능합니다. 현재 길이: " + currentLength);
        }
    }

    public static class NotFound extends ProductException {

        public NotFound() {
            super("해당 상품이 존재하지 않습니다.");
        }
    }
}
