package cart.exception;

import cart.exception.CartItemException.IllegalMember;
import cart.exception.dto.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDto> handlerAuthenticationException(AuthenticationException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ExceptionDto.from(exception));
    }

    @ExceptionHandler(IllegalMember.class)
    public ResponseEntity<ExceptionDto> handleException(IllegalMember exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionDto.from(exception));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionDto.from(exception));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionDto.from(exception));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleException(Exception exception) { // TODO : 이 부분은 어떻게 수정할지 고민해봐야 할 것 같음, Exception Message 를 그대로 반환한다면? 문제가 있을 것 같음
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionDto.from(exception));
    }

}
