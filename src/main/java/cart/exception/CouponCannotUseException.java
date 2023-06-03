package cart.exception;

public class CouponCannotUseException extends RuntimeException {

    public CouponCannotUseException() {
        super("쿠폰 사용이 불가능합니다.");
    }

    public CouponCannotUseException(String message) {
        super(message);
    }
}
