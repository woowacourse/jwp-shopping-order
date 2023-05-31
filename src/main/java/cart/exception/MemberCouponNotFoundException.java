package cart.exception;

public class MemberCouponNotFoundException extends CartException {
    public MemberCouponNotFoundException() {
        super("쿠폰을 찾을 수 없습니다.");
    }
}
