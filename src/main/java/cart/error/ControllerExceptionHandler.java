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

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(CartItemException.IllegalMember.class)
	public ResponseEntity<Void> handleForbiddenException(CartItemException.IllegalMember e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@ExceptionHandler(OrderException.NotFound.class)
	public ResponseEntity<String> handleNotFoundException(OrderException.NotFound e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	}

	@ExceptionHandler({OrderException.BadRequest.class, MethodArgumentNotValidException.class})
	public ResponseEntity<String> handleBadRequestException(Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

}
