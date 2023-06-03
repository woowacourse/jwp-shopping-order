package cart.exception;

public class PagingException extends RuntimeException {

    public PagingException(final String message) {
        super(message);
    }

    public static class NullSize extends PagingException {
        public NullSize() {
            super("size 정보가 입력되지 않았습니다.");
        }
    }

    public static class NullPage extends PagingException {
        public NullPage() {
            super("page 정보가 입력되지 않았습니다.");
        }
    }
}
