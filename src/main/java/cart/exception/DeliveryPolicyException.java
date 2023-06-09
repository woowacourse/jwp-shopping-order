package cart.exception;

public class DeliveryPolicyException extends RuntimeException {
    public DeliveryPolicyException(String message) {
        super(message);
    }

    public static class NotFound extends DeliveryPolicyException {
        public NotFound(Long id) {
            super("해당 아이디의 배송 정책을 찾을 수 없습니다. id = " + id);
        }
    }
}