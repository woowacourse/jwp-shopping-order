package cart.exception;

public class IllegalCouponException extends RuntimeException {

    public IllegalCouponException() {
        super("잘못된 쿠폰입니다.");
    }
}
