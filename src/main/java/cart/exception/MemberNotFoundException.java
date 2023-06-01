package cart.exception;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException() {
        super("요청하신 유저를 찾을 수 없습니다.");
    }
}
