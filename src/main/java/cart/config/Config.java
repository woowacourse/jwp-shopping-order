package cart.config;

import cart.domain.point.PointPolicy10Percent;
import cart.domain.point.PointPolicyStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public PointPolicyStrategy pointPolicyStrategy() {
        return new PointPolicy10Percent();
    }
}
