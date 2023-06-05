package cart.exception;

public class InvalidMemberCouponException extends CartException {
    public InvalidMemberCouponException() {
        super("쿠폰의 소유자가 아닙니다.");
    }
}
