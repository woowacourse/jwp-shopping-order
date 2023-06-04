package cart.exception.business.order;

import cart.exception.business.BusinessException;

public class InvalidDeliveryFeeException extends BusinessException {

    private static final String MESSAGE = "배송비는 음수값이 될 수 없습니다. 입력한 배송비: %d";

    public InvalidDeliveryFeeException(final int deliveryFee) {
        super(String.format(MESSAGE, deliveryFee));
    }
}
