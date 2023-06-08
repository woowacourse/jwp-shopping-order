package cart.coupon.exception;

import cart.common.execption.BaseException;
import cart.common.execption.BaseExceptionType;

public class CouponException extends BaseException {

    private final CouponExceptionType couponExceptionType;

    public CouponException(CouponExceptionType couponExceptionType) {
        this.couponExceptionType = couponExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return couponExceptionType;
    }
}
