package cart.exception.comon;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String domain) {
        super("해당 " + domain + "이 존재하지 않습니다.");
    }
}
