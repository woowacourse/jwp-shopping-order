package cart.exception;

public class AlreadyUsedCouponException extends IllegalArgumentException {

    public AlreadyUsedCouponException() {
        super("사용된 쿠폰을 다시 사용할 수 없습니다.");
    }
}
