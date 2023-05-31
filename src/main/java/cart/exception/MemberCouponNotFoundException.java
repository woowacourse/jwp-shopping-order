package cart.exception;

public class MemberCouponNotFoundException extends CartException {
    public MemberCouponNotFoundException() {
        super("해당 사용자의 쿠폰을 찾을 수 없습니다.");
    }
}
