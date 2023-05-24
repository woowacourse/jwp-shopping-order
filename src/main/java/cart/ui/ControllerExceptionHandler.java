package cart.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public String handleException(final Exception e) {
        log.error("------------------------This is Error------------------");
        log.error(e.getMessage());
        log.error("------------------------This is Error------------------");

        return e.getMessage();
    }
}
