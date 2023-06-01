package cart.ui;

import cart.exception.ShoppingOrderException;
import cart.exception.ShoppingOrderResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ShoppingOrderException.class)
    public ResponseEntity<ShoppingOrderResponse> handleShoppingOrderException(ShoppingOrderException e) {
        logger.warn(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ShoppingOrderResponse(e.getMessage(), e.getCode()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException e,
                                                                  final HttpHeaders headers, final HttpStatus status,
                                                                  final WebRequest request) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append("[");
            stringBuilder.append(fieldError.getField());
            stringBuilder.append("] ");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(" 입력된 값 : [");
            stringBuilder.append(fieldError.getRejectedValue());
            stringBuilder.append("]");
        }
        logger.warn(stringBuilder.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ShoppingOrderResponse(stringBuilder.toString(), -1000));
    }
}
