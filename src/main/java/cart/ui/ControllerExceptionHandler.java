package cart.ui;

import cart.application.dto.ErrorResponse;
import cart.exception.ApplicationException;
import cart.exception.auth.UnauthorizedException;
import cart.exception.badrequest.BadRequestException;
import cart.exception.forbidden.ForbiddenException;
import cart.exception.noexist.NoExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({UnauthorizedException.class, ForbiddenException.class, NoExistException.class,
            BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException exception) {
        logger.warn(exception.getClass().getName(), exception);
        return ResponseEntity.status(exception.getHttpStatus())
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> unhandledException(Exception exception) {
        final String ERROR_TYPE = "INTERNAL_SERVER_ERROR";

        logger.error("[Internal Server Error]", exception);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(ERROR_TYPE));
    }
}
