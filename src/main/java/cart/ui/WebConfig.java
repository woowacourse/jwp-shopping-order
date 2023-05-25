package cart.ui;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> MAPPING_URLS = List.of("/products/**", "/cart-items/**");
    private static final String[] ALLOWED_ORIGINS = {"localhost:3000", "https://feb-dain.github.io"};

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        for (String mappingUrl : MAPPING_URLS) {
            registry.addMapping(mappingUrl)
                    .allowedOrigins(ALLOWED_ORIGINS)
                    .allowedMethods(
                            HttpMethod.GET.name(),
                            HttpMethod.POST.name(),
                            HttpMethod.PUT.name(),
                            HttpMethod.PATCH.name(),
                            HttpMethod.DELETE.name(),
                            HttpMethod.OPTIONS.name())
                    .allowedHeaders("*")
                    .exposedHeaders("Location")
                    .allowCredentials(true);
        }
    }
}
