package cart.exception.policy;

import cart.exception.BadRequestException;

public class PolicyException {

    public static class NotExistShippingFee extends BadRequestException {
        public NotExistShippingFee() {
            super("배송비를 가져올 수 없습니다");
        }
    }

    public static class NoShippingDiscountThreshold extends BadRequestException {
        public NoShippingDiscountThreshold() {
            super("배송비 할인 기준 금액을 가져올 수 없습니다");
        }
    }
}
