package cart.exception.network;

public class AlreadyUsedCouponException extends NetworkException {

    @Override
    public int getErrorCode() {
        return 4001;
    }

    @Override
    public String getErrorMessage() {
        return "사용된 쿠폰을 다시 사용할 수 없습니다.";
    }
}
