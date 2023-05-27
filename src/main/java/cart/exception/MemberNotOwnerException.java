package cart.exception;

public class MemberNotOwnerException extends RuntimeException {

    public MemberNotOwnerException() {
        super("요청에 대하여 일치하지 않는 유저입니다. 로그인 정보를 확인해주세요.");
    }
}
