package cart.ui;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import javax.servlet.RequestDispatcher;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

@Configuration
public class CustomErrorAttributes extends DefaultErrorAttributes {

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Object status = getStatusCodeOf(webRequest);
                return Map.of(
                        "status", status,
                        "message", "오류가 발생했습니다",
                        "timestamp", LocalDateTime.now()
                );
            }

            private Object getStatusCodeOf(WebRequest webRequest) {
                Integer status = (Integer)webRequest.getAttribute(
                        RequestDispatcher.ERROR_STATUS_CODE,
                        RequestAttributes.SCOPE_REQUEST
                );
                if (Objects.isNull(status)) {
                    return HttpStatus.INTERNAL_SERVER_ERROR.value();
                }
                return status;
            }
        };
    }
} 
