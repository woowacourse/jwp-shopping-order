//package cart.ui;
//
//import cart.exception.AuthenticationException;
//import cart.exception.CartItemException;
//import org.springframework.context.support.DefaultMessageSourceResolvable;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@ControllerAdvice
//public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
//
//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
//        Map<String, Object> body = new HashMap<>();
//
//        body.put("statusCode", HttpStatus.BAD_REQUEST);
//        body.put("timestamp", LocalDateTime.now());
//
//        List<String> errors = ex.getBindingResult().getFieldErrors()
//                .stream()
//                .map(DefaultMessageSourceResolvable::getDefaultMessage)
//                .collect(Collectors.toList());
//        body.put("messages", errors);
//
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .body(body);
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<Void> handleException(Exception e) {
//        logger.error("message:" + e.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .build();
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<Void> handleException(AuthenticationException e) {
//        logger.warn("사용자 인증 정보 관련 오류입니다;" + e.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .build();
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<Void> handleException(CartItemException.InvalidMember e) {
//        logger.warn(e.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.FORBIDDEN)
//                .build();
//    }
//
//    @ExceptionHandler({RuntimeException.class})
//    public ResponseEntity<Void> handleBadRequestException(RuntimeException e) {
//        logger.warn(e.getMessage());
//        return ResponseEntity
//                .status(HttpStatus.BAD_REQUEST)
//                .build();
//    }
//}
