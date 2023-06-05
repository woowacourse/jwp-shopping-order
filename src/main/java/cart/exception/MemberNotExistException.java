package cart.exception;

public class MemberNotExistException extends NoSuchDataExistException {
    private static final String MEMBER_NAME = "사용자";

    public MemberNotExistException(final String memberEmail) {
        super(MEMBER_NAME, memberEmail);
    }

    public MemberNotExistException(final Long memberId) {
        super(MEMBER_NAME, memberId.toString());
    }

}
