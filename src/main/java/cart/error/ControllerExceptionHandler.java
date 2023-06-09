package cart.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import cart.error.exception.BadRequestException;
import cart.error.exception.ConflictException;
import cart.error.exception.ForbiddenException;
import cart.error.exception.UnauthorizedException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Void> handleException(final Exception e) {
		return ResponseEntity.internalServerError().build();
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<String> handlerAuthenticationException(final UnauthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<String> handleForbiddenException(final ForbiddenException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
	}

	@ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class})
	public ResponseEntity<String> handleBadRequestException(final Exception e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
	}

	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<String> handleConflictException(final ConflictException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
	}

}
