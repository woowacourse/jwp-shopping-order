package cart.exception;

public class PolicyException extends RuntimeException {

    public PolicyException(String message) {
        super(message);
    }

    public static class NoShippingFee extends PolicyException {
        public NoShippingFee() {
            super("배송비를 가져올 수 없습니다");
        }
    }

    public static class NoShippingDiscountThreshold extends PolicyException {
        public NoShippingDiscountThreshold() {
            super("배송비 할인 기준 금액을 가져올 수 없습니다");
        }
    }
}
