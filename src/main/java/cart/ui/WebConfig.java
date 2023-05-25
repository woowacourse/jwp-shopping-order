package cart.ui;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> MAPPING_URLS = List.of("/products/**", "/cart-items/**");

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        for (String mappingUrl : MAPPING_URLS) {
            registry.addMapping(mappingUrl)
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .exposedHeaders("Location")
                    .allowCredentials(true);
        }
    }
}
