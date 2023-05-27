package cart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
        value = {"file:/Users/choeuchang/Desktop/wooteco/course/level2/application-prod.yml"},
        factory = YamlPropertySourceFactory.class
)
public class ExternalPropertyConfig {

}
