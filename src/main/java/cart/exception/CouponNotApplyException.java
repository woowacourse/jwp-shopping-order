package cart.exception;

public class CouponNotApplyException extends CartException {
    public CouponNotApplyException(final long minimumPrice) {
        super("쿠폰 적용이 되지 않습니다. 최소 금액 : " + minimumPrice);
    }
}
