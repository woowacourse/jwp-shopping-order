package cart.exception;

public class NonExistCouponException extends RuntimeException {

    public NonExistCouponException() {
        super("존재하지 않는 쿠폰입니다.");
    }
}
