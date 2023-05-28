package cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
        value = {"file:/home/ubuntu/application-prod.yml"},
        factory = YamlPropertySourceFactory.class
)
@Profile("prod")
public class ExternalPropertyConfig {

}
