package cart.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cart.error.exception.AuthenticationException;
import cart.error.exception.CartItemException;
import cart.error.exception.OrderException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleException(final Exception e) {
		return ResponseEntity.internalServerError().build();
	}

	@ExceptionHandler(AuthenticationException.Unauthorized.class)
	public ResponseEntity<String> handlerAuthenticationException(final AuthenticationException.Unauthorized e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler({CartItemException.NotFound.class, OrderException.NotFound.class,
		AuthenticationException.NotFound.class})
	public ResponseEntity<String> handleNotFoundException(final Exception e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler({OrderException.BadRequest.class, AuthenticationException.BadRequest.class,
		MethodArgumentNotValidException.class})
	public ResponseEntity<String> handleBadRequestException(final Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

}
