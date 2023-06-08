package cart.exception.internal;

public class CannotChangeCouponStatusException extends InternalException {

    @Override
    public int getErrorCode() {
        return 5000;
    }

    @Override
    public String getErrorMessage() {
        return "쿠폰 상태를 변경할 수 없습니다.";
    }
}
