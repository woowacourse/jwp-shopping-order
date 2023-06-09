package cart.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptorHandler implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptorHandler.class);

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) {
        final String url = request.getRequestURL().toString();
        logger.info("Http Request: {}", url);

        return true;
    }
}