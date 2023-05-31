package cart.step2.coupontype.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class CouponTypeNotFoundException extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new CouponTypeNotFoundException();

    public CouponTypeNotFoundException() {
        super(new ErrorCode(404, "존재하지 않는 쿠폰 종류입니다. 쿠폰 타입 ID가 일치하는지 확인해주세요."));
    }

}
