package cart.exception.enum_exception;

import org.springframework.http.HttpStatus;

public interface CustomExceptionType {

  HttpStatus httpStatus();

  String errorMessage();
}
