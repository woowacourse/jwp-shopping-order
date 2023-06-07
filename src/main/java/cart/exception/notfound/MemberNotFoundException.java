package cart.exception.notfound;

public class MemberNotFoundException extends NotFoundException {
    public MemberNotFoundException(final String message) {
        super("찾을 수 없는 회원입니다.");
    }
}
