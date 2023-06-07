package cart.ui.api;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, Object> body = new HashMap<>();

        body.put("statusCode", HttpStatus.BAD_REQUEST);
        body.put("timestamp", LocalDateTime.now());

        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        body.put("messages", errors);

        logger.warn("사용자 인증 정보 관련 오류가 발생했습니다 :" + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        logger.error("서버 내부에서 문제가 발생했습니다 : " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("서버 내부 문제가 발생했습니다. 관리자에게 문의해주세요.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(AuthenticationException e) {
        logger.warn("사용자 인증 정보 관련 문제가 발생했습니다 :" + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("아이디와 비밀번호를 정확히 입력해주세요.");
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(CartItemException.InvalidMember e) {
        logger.warn("잘못된 사용자입니다. " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("장바구니 페이지에 접근 권한이 없는 계정입니다. 아이디와 비밀번호를 정확히 입력해주세요.");
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
        logger.warn("잘못된 사용자 입력으로 인한 문제가 발생했습니다." + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
