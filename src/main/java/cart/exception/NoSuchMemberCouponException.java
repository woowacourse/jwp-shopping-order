package cart.exception;

public class NoSuchMemberCouponException extends IllegalArgumentException {
    public NoSuchMemberCouponException() {
        super("존재하지 않는 멤버 쿠폰입니다.");
    }
}
