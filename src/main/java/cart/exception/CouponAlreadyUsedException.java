package cart.exception;

public class CouponAlreadyUsedException extends CartException {
    public CouponAlreadyUsedException() {
        super("이미 사용된 쿠폰입니다.");
    }
}
