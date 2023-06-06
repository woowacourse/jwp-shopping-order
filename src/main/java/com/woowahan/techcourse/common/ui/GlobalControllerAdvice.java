package com.woowahan.techcourse.common.ui;

import java.util.stream.Collectors;
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

        String message = generateFieldErrorMessages(bindingResult);

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(message));
    }

    private String generateFieldErrorMessages(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(this::generateFieldErrorMessage)
                .collect(Collectors.joining(System.lineSeparator()));
    }

    private String generateFieldErrorMessage(FieldError fieldError) {
        return String.format("[%s](은)는 %s 입력된 값: [%s]",
                fieldError.getField(),
                fieldError.getDefaultMessage(),
                fieldError.getRejectedValue());
    }
}
