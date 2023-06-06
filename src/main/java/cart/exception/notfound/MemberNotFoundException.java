package cart.exception.notfound;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException(Long id) {
        super("해당 멤버가 존재하지 않습니다. 요청 멤버 ID: " + id);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
