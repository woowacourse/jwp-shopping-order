package cart.exception;

public class CannotDeleteCouponException extends IllegalArgumentException {

    public CannotDeleteCouponException() {
        super("사용하지 않은 쿠폰을 삭제할 수 없습니다.");
    }
}
