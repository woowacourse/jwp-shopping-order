package common.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("예상치 못한 예외가 발생했습니다", e);
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse("예상치 못한 예외가 발생했습니다"));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApiException(ApiException e) {
        logger.error("API 요청에 실패했습니다", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder message = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            message.append("[");
            message.append(fieldError.getField());
            message.append("](은)는 ");
            message.append(fieldError.getDefaultMessage());
            message.append(" 입력된 값: [");
            message.append(fieldError.getRejectedValue());
            message.append("]");
        }

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message.toString()));
    }

}
