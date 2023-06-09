package cart.exception;

public class NoSuchCouponException extends IllegalArgumentException {
    public NoSuchCouponException() {
        super("존재하지 않는 쿠폰입니다.");
    }
}
