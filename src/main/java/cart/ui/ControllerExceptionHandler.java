package cart.ui;

import cart.exception.AuthenticationException;
import cart.exception.CartItemException;
import cart.exception.IllegalPointUsageException;
import cart.exception.InvalidOrderCheckedException;
import cart.exception.InvalidOrderProductException;
import cart.exception.InvalidOrderQuantityException;
import cart.exception.notfound.NotFoundException;
import cart.exception.notfound.NotFoundResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<Void> handlerAuthenticationException(AuthenticationException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@ExceptionHandler(CartItemException.IllegalMember.class)
	public ResponseEntity<Void> handleException(CartItemException.IllegalMember e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException exception) {
		return ResponseEntity.badRequest().body(exception.getMessage());
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<NotFoundResponse> handleProductNotFoundException(NotFoundException exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(exception.getId(), exception.getName()));
	}

	@ExceptionHandler({
		InvalidOrderCheckedException.class,
		InvalidOrderQuantityException.class,
		InvalidOrderProductException.class,
		IllegalPointUsageException.class
	})

	public ResponseEntity<String> handleCustomApiException(RuntimeException exception) {
		logger.error(exception.getMessage());
		return ResponseEntity.badRequest().body(exception.getClass().getSimpleName());
	}
}
