package cart.exception;

public class CannotFindCouponException extends IllegalStateException {

    public CannotFindCouponException() {
        super("쿠폰을 찾을 수 없습니다.");
    }
}
