package cart.exception;

public class MemberNotFoundException extends CartException {
    public MemberNotFoundException() {
        super("사용자를 찾을 수 없습니다.");
    }
}

