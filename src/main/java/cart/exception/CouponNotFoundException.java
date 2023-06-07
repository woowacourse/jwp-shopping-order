package cart.exception;

public class CouponNotFoundException extends RuntimeException {

    public CouponNotFoundException(final long couponId) {
        super("쿠폰을 찾을 수 없습니다. / 입력하신 Id : " + couponId);
    }
}
