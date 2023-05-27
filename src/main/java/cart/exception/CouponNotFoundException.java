package cart.exception;

public class CouponNotFoundException extends CartException {
    public CouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다.");
    }
}

