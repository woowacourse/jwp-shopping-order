package cart.step2.coupon.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class CouponNotFoundException extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new CouponNotFoundException();

    public CouponNotFoundException() {
        super(new ErrorCode(404, "쿠폰을 찾을 수 없습니다. 쿠폰 ID가 일치하는지 확인해주세요."));
    }

}
