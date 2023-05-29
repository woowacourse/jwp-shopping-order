package cart.exception;

public class MemberCouponNotFoundException extends CartException {
    public MemberCouponNotFoundException() {
        super("멤버는 해당 쿠폰을 가지고 있지 않습니다.");
    }
}
