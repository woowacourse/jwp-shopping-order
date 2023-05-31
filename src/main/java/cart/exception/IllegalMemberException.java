package cart.exception;

import org.springframework.http.HttpStatus;

public class IllegalMemberException extends CustomException {

    public IllegalMemberException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}
