package cart.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public final class LoggingRequestUrlInterceptor implements HandlerInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoggingRequestUrlInterceptor.class);

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        final String url = request.getRequestURL().toString();
        logger.info("Http Request: {}", url);

        return true;
    }
}
