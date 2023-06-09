package cart.exception.internal;

public class CannotFindCouponException extends InternalException {

    @Override
    public int getErrorCode() {
        return 5001;
    }

    @Override
    public String getErrorMessage() {
        return "쿠폰을 찾을 수 없습니다.";
    }
}
