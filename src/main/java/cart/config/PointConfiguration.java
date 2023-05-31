package cart.config;

import cart.domain.BasicPointPolicy;
import cart.domain.PointPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointConfiguration {

    @Bean
    public PointPolicy pointPolicy() {
        return new BasicPointPolicy();
    }
}
