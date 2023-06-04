package cart.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(final Long memberId) {
        super("요청하신 유저를 찾을 수 없습니다. / 입력하신 Member id : " + memberId);
    }
}
