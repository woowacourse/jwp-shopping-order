package cart.controller.exception;

import cart.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice(annotations = Controller.class)
public class GlobalUiExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(BaseException e) {
        log.warn(e.getMessage(), e);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", e.getMessage());
        modelAndView.setViewName("error");
        return modelAndView;
    }
}
