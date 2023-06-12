package cart.exception;

import static cart.domain.vo.DeliveryFee.MINIMUM_AMOUNT;

public class DeliveryFeeException extends RuntimeException {

    public DeliveryFeeException(String message) {
        super(message);
    }

    public static class SmallerThanMinimum extends DeliveryFeeException {

        public SmallerThanMinimum() {
            super("입력 가능한 배달비는 최소 " + MINIMUM_AMOUNT + "보다 커야합니다.");
        }
    }

}
