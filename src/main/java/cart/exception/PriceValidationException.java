package cart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PriceValidationException extends RuntimeException {
    // 예외의 내용과 추가적인 로직...
}
