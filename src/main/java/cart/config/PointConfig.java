package cart.config;

import cart.domain.DefaultPointManager;
import cart.domain.PointManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PointConfig {

    @Bean
    public PointManager pointManager() {
        return new DefaultPointManager();
    }
}
