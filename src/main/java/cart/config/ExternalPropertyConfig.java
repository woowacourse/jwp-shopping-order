package cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Profile(value = "application")
@Configuration
@PropertySource(
        value = {
                "file:/home/ubuntu/application-prod.yml"
        },
        factory = YamlPropertySourceFactory.class
)
public class ExternalPropertyConfig {
}
