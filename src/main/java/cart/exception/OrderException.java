package cart.exception;

public class OrderException extends ApiException {

    public OrderException(String message) {
        super(message);
    }

    public static class NotFound extends OrderException {

        public NotFound(Long id) {
            super("해당 주문정보를 찾을 수 없습니다. " +
                    "id : " + id);
        }
    }

    public static class ParseFail extends OrderException {

        public ParseFail(Object order) {
            super("Order 변환과정에서 문제가 발생했습니다. " +
                    "order : " + order);
        }
    }
}
