package cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import static cart.env.Profile.PROFILE_ZONE;


@Profile(value = PROFILE_ZONE)
@Configuration
@PropertySource(
        value = {
                "file:/home/ubuntu/application-prod.yml"
        },
        factory = YamlPropertySourceFactory.class
)
public class ExternalPropertyConfig {
}
