package cart.exception;

public class DiscountPolicyException extends RuntimeException {
    public DiscountPolicyException(String message) {
        super(message);
    }

    public static class NotFound extends DiscountPolicyException {
        public NotFound(Long id) {
            super("해당 아이디의 할인 정책을 찾을 수 없습니다. id = " + id);
        }
    }
}
