package cart.exception.network;

public class IllegalCartItemMemberException extends NetworkException {

    @Override
    public int getErrorCode() {
        return 4004;
    }

    @Override
    public String getErrorMessage() {
        return "이 장바구니에 권한이 없는 사용자입니다.";
    }
}
