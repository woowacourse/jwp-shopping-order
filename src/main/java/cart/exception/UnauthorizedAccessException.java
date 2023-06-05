package cart.exception;

public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(final String memberId, final Object resourceId) {
        super("해당 요청에 권한이 없는 사용자 입니다.\n"
                + "[Member Id] : " + memberId + " : "
                + "[Resource Id] : " + resourceId.toString());
    }
}
