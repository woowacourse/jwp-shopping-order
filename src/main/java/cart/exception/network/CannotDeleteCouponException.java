package cart.exception.network;

public class CannotDeleteCouponException extends NetworkException {

    @Override
    public int getErrorCode() {
        return 4003;
    }

    @Override
    public String getErrorMessage() {
        return "사용하지 않은 쿠폰을 삭제할 수 없습니다.";
    }
}
