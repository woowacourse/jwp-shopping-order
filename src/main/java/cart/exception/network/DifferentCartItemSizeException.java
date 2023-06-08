package cart.exception.network;

public class DifferentCartItemSizeException extends NetworkException {

    @Override
    public int getErrorCode() {
        return 4005;
    }

    @Override
    public String getErrorMessage() {
        return "존재하지 않는 상품이 포함되어 있습니다.";
    }
}
