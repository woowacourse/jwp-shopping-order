package cart.step2.coupon.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class UnusedCouponsCannotDeleted extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new UnusedCouponsCannotDeleted();

    public UnusedCouponsCannotDeleted() {
        super(new ErrorCode(500, "사용하지 않은 쿠폰은 삭제할 수 없습니다. 사용한 후에 삭제해주세요!"));
    }
}
